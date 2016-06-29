package eu.pericles.processcompiler.unittests;

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
	
	@Before
	public void setRepository() {
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
	}
	
	@Test
	public void testVirusCheckProcess() {
		RuntimeManager manager = createRuntimeManager("jbpm_runnable/VirusCheckProcess.bpmn2");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		
		ProcessInstance processInstance = ksession.startProcess("_bf96549a-7b98-45af-af77-0f715fe66566");
		// check whether the process instance has completed successfully
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Virus Check");
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
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
			
			ProcessInstance processInstance = ksession.startProcess("_bf96549a-7b98-45af-af77-0f715fe66566");
			// check whether the process instance has completed successfully
			assertProcessInstanceCompleted(processInstance.getId(), ksession);
			assertNodeTriggered(processInstance.getId(), "Virus Check");
			assertNodeTriggered(processInstance.getId(), "Extract MD");
			assertNodeTriggered(processInstance.getId(), "Encapsulate DO and MD");
			
			manager.disposeRuntimeEngine(engine);
			manager.close();
			
		} catch (Exception e) {
			fail("compileAggregatedProcess(): " + e.getMessage());
		}
	}

}
