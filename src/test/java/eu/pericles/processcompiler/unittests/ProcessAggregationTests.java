package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.testutils.CreateEntities;
import eu.pericles.processcompiler.testutils.Utils;

@RunWith(Parameterized.class)
public class ProcessAggregationTests {

	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	static String[] folders = {"basicprocessaggregation", "processaggregationwithdata"};
	private String ecosystem;
	private String path;
	
	public ProcessAggregationTests(String folder) {
        this.ecosystem = "src/test/resources/core/" + folder + "/Ecosystem.txt";
        this.path = "src/test/resources/core/" + folder + "/";
    }
	
    @Parameters(name="{0}")
    public static Collection<String> folders() {
        return Arrays.asList(folders);
    }
	
	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			//TODO Error in the ERMR design: this should be 204 NO CONTENT
			assertEquals(200, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}
	// ----------------------- TESTS ----------------------------------//
	
		@Test
		public void getBPMNSubprocess() {
			try {
				BPMNProcess process = new ProcessCompiler().getBPMNProcess(repository, "<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck>");
				assertEquals("_223e152f-97f6-4f9f-864b-cc6af31253ff", process.getId());
				assertEquals("_bf96549a-7b98-45af-af77-0f715fe66566", process.getProcess().getId());
			} catch (Exception e) {
				fail("getBPMNSubprocess(): " + e.getMessage());
			}
		}
		
		@Test
		public void getBPMNSubprocesses() {
			try {
				ProcessCompiler compiler = new ProcessCompiler();
				AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository, "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
				List<BPMNProcess> processes = compiler.getBPMNProcessesOfAggregatedProcess(repository, aggregatedProcess);
				assertEquals("_bf96549a-7b98-45af-af77-0f715fe66566", processes.get(0).getProcess().getId());
				assertEquals("_3b8e18d2-d35e-4d6d-898d-6a3f41e227e0", processes.get(1).getProcess().getId());
				assertEquals("_99113fc4-116c-4351-a90d-168ee4f038b8", processes.get(2).getProcess().getId());
			} catch (Exception e) {
				fail("getBPMNSubprocesses(): " + e.getMessage());
			}
		}
		
		@Test
		public void compileAggregatedProcess() {
			try {
				ProcessCompiler compiler = new ProcessCompiler();
				AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository, "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
				String outputFileName = path + "CompiledAggregatedProcess.bpmn2";
				String testFileName = path + "CompiledAggregatedProcessTest.bpmn2";
				BPMNProcess bpmnProcess = compiler.compileAggregatedProcess(repository, aggregatedProcess);
				new BPMNWriter().write(bpmnProcess, outputFileName);
				Utils.fileContentEquals(outputFileName, testFileName);
			} catch (Exception e) {
				fail("compileAggregatedProcess(): " + e.getMessage());
			}
		}
}
