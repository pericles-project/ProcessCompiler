package eu.pericles.processcompiler.unittests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.testutils.Utils;

public class JBPMRunnableTests extends JbpmJUnitBaseTestCase {
	
	static String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	static String ecosystem = "src/test/resources/jbpm_runnable/Ecosystem.txt";
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	
	@Before
	public void setRepository() {
		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		try {
			ERMRClientAPI client = new ERMRClientAPI(service);
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
		} catch (ERMRClientException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI(service);
			Response response = client.deleteTriples(repository);
			//TODO Error in the ERMR design: this should be 204 NO CONTENT
			assertEquals(204, response.getStatus());
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
		System.setOut(defaultOutputStream);
	}
	
	@Test
	public void testVirusCheckProcess() {
		RuntimeManager manager = createRuntimeManager("jbpm_runnable/VirusCheckProcess.bpmn2");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("_Digital_Material", "myDigitalMaterial");
		
		ProcessInstance processInstance = ksession.startProcess("_bf96549a-7b98-45af-af77-0f715fe66566", params);
		// check whether the process instance has completed successfully
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Virus Check");
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
		
		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Virus Check Process to: myDigitalMaterial"));
	}
	
	@Test
	public void testExtractMDProcess() {
		RuntimeManager manager = createRuntimeManager("jbpm_runnable/ExtractMetadataProcess.bpmn2");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("_Digital_Object", "myDigitalObject");
		
		ProcessInstance processInstance = ksession.startProcess("_3b8e18d2-d35e-4d6d-898d-6a3f41e227e0", params);
		// check whether the process instance has completed successfully
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Extract MD");
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
		
		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Extract Metadata Process to: myDigitalObject"));
		assertTrue(output.contains("Creating Metadata: myMetadata"));
	}
	
	@Test
	public void testEncapsulateDOandMDProcess() {
		RuntimeManager manager = createRuntimeManager("jbpm_runnable/EncapsulateDOMDProcess.bpmn2");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("_Digital_Object", "myDigitalObject");
		params.put("_Metadata", "myMetadata");
		params.put("_Package_Format", "myPackageFormat");
		
		ProcessInstance processInstance = ksession.startProcess("_99113fc4-116c-4351-a90d-168ee4f038b8", params);
		// check whether the process instance has completed successfully
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Encapsulate DO and MD");
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
		
		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing the encapsulate DO and MD process to: myDigitalObject and myMetadata"));
		assertTrue(output.contains("Using the package format: myPackageFormat"));
	}
	
	@Test
	public void testCompiledProcess() {
		
		try {
			ProcessCompiler compiler = new ProcessCompiler();
			AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository, "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
			BPMNProcess bpmnProcess = compiler.compileAggregatedProcess(repository, aggregatedProcess);
			String outputFileName = "src/test/resources/jbpm_runnable/CompiledAggregatedProcess.bpmn2";
			String testFileName = "src/test/resources/jbpm_runnable/CompiledAggregatedProcessTest.bpmn2";
			new BPMNWriter().write(bpmnProcess, outputFileName);
			Utils.fileContentEquals(outputFileName, testFileName);
			
			RuntimeManager manager = createRuntimeManager("jbpm_runnable/CompiledAggregatedProcess.bpmn2");
			RuntimeEngine engine = getRuntimeEngine(null);
			KieSession ksession = engine.getKieSession();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("_Digital_Material", "myArtwork.mov");
			params.put("_Package_Format", "BagIt");
			
			ProcessInstance processInstance = ksession.startProcess("_bf96549a-7b98-45af-af77-0f715fe66566", params);
			// check whether the process instance has completed successfully
			assertProcessInstanceCompleted(processInstance.getId(), ksession);
			assertNodeTriggered(processInstance.getId(), "Virus Check");
			assertNodeTriggered(processInstance.getId(), "Extract MD");
			assertNodeTriggered(processInstance.getId(), "Encapsulate DO and MD");
			
			manager.disposeRuntimeEngine(engine);
			manager.close();
			
			String output = outputStream.toString();
			defaultOutputStream.println(output);
			assertTrue(output.contains("Executing Virus Check Process to: myArtwork.mov"));
			assertTrue(output.contains("Executing Extract Metadata Process to: myArtwork.mov"));
			assertTrue(output.contains("Creating Metadata: myMetadata"));
			assertTrue(output.contains("Executing the encapsulate DO and MD process to: myArtwork.mov and myMetadata"));
			assertTrue(output.contains("Using the package format: BagIt"));
			
		} catch (Exception e) {
			fail("compileAggregatedProcess(): " + e.getMessage());
		}
	}

}
