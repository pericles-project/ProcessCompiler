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
			// TODO Error in the ERMR design: this should be 201 Created
			assertEquals(204, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
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
				List<BPMNProcess> processes = new ProcessCompiler().getBPMNProcessesOfAggregatedProcess(repository, createAggregatedProcess());
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
				String outputFileName = path + "CompiledAggregatedProcess.bpmn2";
				String testFileName = path + "CompiledAggregatedProcessTest.bpmn2";
				BPMNProcess bpmnProcess = new ProcessCompiler().compileAggregatedProcess(repository, createAggregatedProcess());
				new BPMNWriter().write(bpmnProcess, outputFileName);
				Utils.fileContentEquals(outputFileName, testFileName);
			} catch (Exception e) {
				fail("compileAggregatedProcess(): " + e.getMessage());
			}
		}
		// ----------------------- HELP FUNCTIONS ----------------------------------//

		private AggregatedProcess createAggregatedProcess() {
			AggregatedProcess aggregatedProcess = new AggregatedProcess();
			aggregatedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
			aggregatedProcess.setName("Ingest Artwork Software");
			aggregatedProcess
					.setDescription("Aggregated process that ingest an artwork software by doing the following: "
							+ "check for viruses, extract the metadata and encapsulate the artwork together with it");
			aggregatedProcess.setVersion("1");
			aggregatedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
					"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
					"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined", "sha256",
					"8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
			aggregatedProcess.setInputs(new ArrayList<InputSlot>());
			aggregatedProcess
					.getInputs()
					.add(CreateEntities.createInputSlot(
							new ArrayList<String>(
									Arrays.asList(
											"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>",
											"InputSlot Artwork Software",
											"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
											"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), false));
			aggregatedProcess.getInputs().add(
					CreateEntities.createInputSlot(
							new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>",
									"InputSlot Package Format",
									"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
									"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
			aggregatedProcess.setOutputs(new ArrayList<OutputSlot>());
			aggregatedProcess
					.getOutputs()
					.add(CreateEntities.createOutputSlot(new ArrayList<String>(
							Arrays.asList(
									"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>",
									"OutputSlot Package",
									"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
									"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
			aggregatedProcess
					.setSequence(CreateEntities
							.createSequence(
									"<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
									"{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
											+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtractMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
											+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
											+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
											+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
											+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}"));
			return aggregatedProcess;
		}
}

/*
public class ProcessAggregationTests {
	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	private String ecosystem = "src/test/resources/core/basicprocessaggregation/Ecosystem.txt";

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			// TODO Error in the ERMR design: this should be 201 Created
			assertEquals(204, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
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
			List<BPMNProcess> processes = new ProcessCompiler().getBPMNProcessesOfAggregatedProcess(repository, createAggregatedProcess());
			assertEquals("_bf96549a-7b98-45af-af77-0f715fe66566", processes.get(0).getProcess().getId());
			assertEquals("_3b8e18d2-d35e-4d6d-898d-6a3f41e227e0", processes.get(1).getProcess().getId());
			assertEquals("_99113fc4-116c-4351-a90d-168ee4f038b8", processes.get(2).getProcess().getId());
		} catch (Exception e) {
			fail("getBPMNSubprocesses(): " + e.getMessage());
		}
	}
	
	@Test
	public void compileBasicAggregatedProcess() {
		try {
			String outputFileName = "src/test/resources/core/basicprocessaggregation/CompiledAggregatedProcess.bpmn2";
			String testFileName = "src/test/resources/core/basicprocessaggregation/CompiledAggregatedProcessTest.bpmn2";
			BPMNProcess bpmnProcess = new ProcessCompiler().compileAggregatedProcess(repository, createAggregatedProcess());
			new BPMNWriter().write(bpmnProcess, outputFileName);
			Utils.fileContentEquals(outputFileName, testFileName);
		} catch (Exception e) {
			fail("compileBasicAggregatedProcess(): " + e.getMessage());
		}
	}
	
	@Test
	public void compileAggregatedProcessWithData() {
		try {
			ecosystem = "src/test/resources/core/processaggregationwithdata/Ecosystem.txt";
			String outputFileName = "src/test/resources/core/processaggregationwithdata/CompiledAggregatedProcess.bpmn2";
			String testFileName = "src/test/resources/core/processaggregationwithdata/CompiledAggregatedProcessTest.bpmn2";
			BPMNProcess bpmnProcess = new ProcessCompiler().compileAggregatedProcess(repository, createAggregatedProcess());
			new BPMNWriter().write(bpmnProcess, outputFileName);
			Utils.fileContentEquals(outputFileName, testFileName);
		} catch (Exception e) {
			fail("compileAggregatedProcessWithData(): " + e.getMessage());
		}
	}

	// ----------------------- HELP FUNCTIONS ----------------------------------//

	private AggregatedProcess createAggregatedProcess() {
		AggregatedProcess aggregatedProcess = new AggregatedProcess();
		aggregatedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		aggregatedProcess.setName("Ingest Artwork Software");
		aggregatedProcess
				.setDescription("Aggregated process that ingest an artwork software by doing the following: "
						+ "check for viruses, extract the metadata and encapsulate the artwork together with it");
		aggregatedProcess.setVersion("1");
		aggregatedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined", "sha256",
				"8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		aggregatedProcess.setInputs(new ArrayList<InputSlot>());
		aggregatedProcess
				.getInputs()
				.add(CreateEntities.createInputSlot(
						new ArrayList<String>(
								Arrays.asList(
										"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>",
										"InputSlot Artwork Software",
										"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
										"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), false));
		aggregatedProcess.getInputs().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>",
								"InputSlot Package Format",
								"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
		aggregatedProcess.setOutputs(new ArrayList<OutputSlot>());
		aggregatedProcess
				.getOutputs()
				.add(CreateEntities.createOutputSlot(new ArrayList<String>(
						Arrays.asList(
								"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>",
								"OutputSlot Package",
								"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		aggregatedProcess
				.setSequence(CreateEntities
						.createSequence(
								"<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
								"{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
										+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtractMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
										+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
										+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
										+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
										+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}"));
		return aggregatedProcess;
	}

}
*/