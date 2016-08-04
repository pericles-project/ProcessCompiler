package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jbpm.test.JBPMHelper;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;

import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.ng.ProcessCompiler;
import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ng.ermr.ERMRCommunications;

/**
 * Location of process files is specified in src/test/resources/META-INF/kmodule.xml
 *
 */
public class ProcessExecutionTests extends JbpmJUnitBaseTestCase {
	
	private static boolean dataSourceExists = false;
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	private KieSession ksession;
	private RuntimeEngine engine;
	private RuntimeManager manager;	
	
	private String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/IngestSBA/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem_Compilation.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
			response = new ERMRClientAPI().createDigitalObject(collection + "VirusCheck.bpmn", doPath + "VirusCheck.bpmn", doMediaType);
			assertEquals(201, response.getStatus());
			response = new ERMRClientAPI().createDigitalObject(collection + "ExtractMD.bpmn", doPath + "ExtractMD.bpmn", doMediaType);
			assertEquals(201, response.getStatus());
			response = new ERMRClientAPI().createDigitalObject(collection + "EncapsulateDOMD.bpmn", doPath + "EncapsulateDOMD.bpmn", doMediaType);
			assertEquals(201, response.getStatus());
		} catch (ERMRClientException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
			response = new ERMRClientAPI().deleteDigitalObject(collection + "VirusCheck.bpmn");
			assertEquals(204, response.getStatus());
			response = new ERMRClientAPI().deleteDigitalObject(collection + "ExtractMD.bpmn");
			assertEquals(204, response.getStatus());
			response = new ERMRClientAPI().deleteDigitalObject(collection + "EncapsulateDOMD.bpmn");
			assertEquals(204, response.getStatus());
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}

	@Before
	public void prepareEngine() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase("kbase");
		manager = createRuntimeManager(kbase);
		engine = manager.getRuntimeEngine(null);
		ksession = engine.getKieSession();
		
		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}

	@After
	public void closeEngine() {
		manager.disposeRuntimeEngine(engine);
		manager.close();
		System.setOut(defaultOutputStream);
	}
	
	// ----------------------------- TESTS ----------------------------------

	@Test
	public void testBPMNFile() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isExtractMDDO", "myDO");

		ProcessInstance processInstance = ksession.startProcess("jbpm.atpExtractMD", params);
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Extract Metadata process to: myDO"));
		assertTrue(output.contains("Metadata created: newMD"));
	}
	
	@Test
	public void testCompiledBPMNFile() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			String outputFile = "src/test/resources/ingest_sba/CompiledProcess.bpmn";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			ProcessCompiler compiler = new ProcessCompiler(service);
			compiler.compile(repository, aggregatedProcess, outputFile);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isIngestAWSWAW", "myDO");
			params.put("isIngestAWSWPF", "myPF");

			ProcessInstance processInstance = ksession.startProcess("agpIngestAWSW", params);
			assertProcessInstanceCompleted(processInstance.getId(), ksession);

			String output = outputStream.toString();
			defaultOutputStream.println(output);
			assertTrue(output.contains("Executing Virus Check process to: myDO"));
			assertTrue(output.contains("Virus Check Result: SUCCESS "));
			assertTrue(output.contains("Executing Extract Metadata process to: myDO"));
			assertTrue(output.contains("Metadata created: newMD"));
			assertTrue(output.contains("Executing Encapsulate process to DO: myDO and MD: newMD"));
			assertTrue(output.contains("Package with format myPF created: newPackage"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in compileTest()");
		}
	}

	private static RuntimeManager createRuntimeManager(KieBase kbase) {
		JBPMHelper.startH2Server();
		if (!dataSourceExists) {
			JBPMHelper.setupDataSource();
			dataSourceExists = true;
		}
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf)
				.knowledgeBase(kbase);
		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(builder.get(), "com.sample:example:1.0");
	}

}
