package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;

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
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.testutils.Utils;

@RunWith(Parameterized.class)
public class ProcessCompilationTests {

	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	static String[] folders = {"basicprocessaggregation", "processaggregationwithdata"};
	private String ecosystem;
	private String path;
	
	public ProcessCompilationTests(String folder) {
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
		public void compileAggregatedProcess() {
			try {
				AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
				BPMNProcess bpmnProcess = new ProcessCompiler().compileAggregatedProcess(repository, aggregatedProcess);
				String outputFileName = path + "CompiledAggregatedProcess.bpmn2";
				String testFileName = path + "CompiledAggregatedProcessTest.bpmn2";
				new BPMNWriter().write(bpmnProcess, outputFileName);
				Utils.fileContentEquals(outputFileName, testFileName);
			} catch (Exception e) {
				fail("compileAggregatedProcess(): " + e.getMessage());
			}
		}
}
