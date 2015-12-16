package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.testutils.CreateEntities;

public class ValidationTests {

	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;	
	private String ecosystem = "src/test/resources/core/validation/Ecosystem.txt";
	
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

	@Test
	public void validDataFlow() {
		try {
		ProcessCompiler processCompiler = new ProcessCompiler();
		assertTrue(processCompiler.validateDataFlow(repository, createAggregatedProcess()));
		} catch (Exception e) {
			fail("validDataFlow(): " + e.getMessage());
		}
	}
	
	@Test
	public void invalidDataFlowWithWrongConnection() {
		try {
			ProcessCompiler processCompiler = new ProcessCompiler();
			AggregatedProcess invalidProcess = createAggregatedProcess();
			invalidProcess.setSequence(CreateEntities.createSequence("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
					"{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
							+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtracMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
							+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
							+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
							+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
							+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}"));
			assertFalse(processCompiler.validateDataFlow(repository,invalidProcess));
			} catch (Exception e) {
				fail("invalidDataFlow(): " + e.getMessage());
			}
	}
	
	private AggregatedProcess createAggregatedProcess() {
		AggregatedProcess aggregatedProcess = new AggregatedProcess();
		aggregatedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		aggregatedProcess.setName("Ingest Artwork Software");
		aggregatedProcess.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		aggregatedProcess.setVersion("1");
		aggregatedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined",
				"sha256", "8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		aggregatedProcess.setInputs(new ArrayList<InputSlot>());
		aggregatedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", 
				"InputSlot Artwork Software",
				"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), 
				false));
		aggregatedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", 
				"InputSlot Package Format",
				"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), 
				false));
		aggregatedProcess.setOutputs(new ArrayList<OutputSlot>());
		aggregatedProcess.getOutputs().add(CreateEntities.createOutputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", 
				"OutputSlot Package",
				"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		aggregatedProcess.setSequence(CreateEntities.createSequence("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
				"{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtractMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
				+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}"));
		return aggregatedProcess;
	}

}
