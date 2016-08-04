package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.ng.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ng.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ecosystem.Implementation;
import eu.pericles.processcompiler.ng.ecosystem.InputSlot;
import eu.pericles.processcompiler.ng.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ng.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.ng.testutils.CreateEntities;

/**
 * BPMN Files are stored in the Object Store under the Folder:
 * NoaCollection/ERMRCommunications/
 */
public class ERMRCommunicationsTests {

	static String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/ERMRCommunications/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ng/Ecosystem_ERMRCommunications.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	private AggregatedProcess expectedAggregatedProcess;
	private ProcessBase expectedProcess;

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
			assertEquals(204, response.getStatus());
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}
	
	@Before
	public void createExpectedEntities() {
		setExpectedProcess();
		setExpectedAggregatedProcess();
	}

	// -- TESTS --//

	@Test
	public void getImplementationEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>";
		try {
			Implementation implementation = new ERMRCommunications().getImplementationEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getImplementation(), implementation);
		} catch (Exception e) {
			fail("getImplementation(): " + e.getMessage());
		}
	}

	@Test
	public void getInputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>";
		try {
			InputSlot inputSlot = new ERMRCommunications().getInputSlotEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getInputSlots().get(0), inputSlot);
		} catch (Exception e) {
			fail("getInputSlot(): " + e.getMessage());
		}
	}

	@Test
	public void getOutputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>";
		try {
			OutputSlot outputSlot = new ERMRCommunications().getOutputSlotEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getOutputSlots().get(0), outputSlot);
		} catch (Exception e) {
			fail("getOutputSlot(): " + e.getMessage());
		}
	}

	@Test
	public void getInputSlotURIList() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			List<String> inputSlotURIList = new ERMRCommunications().getInputSlotURIList(repository, uri);
			assertEquals("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", inputSlotURIList.get(0));
			assertEquals("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", inputSlotURIList.get(1));
		} catch (Exception e) {
			fail("getInputSlotURIs(): " + e.getMessage());
		}
	}

	@Test
	public void getOutputSlotURIList() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			List<String> outputSlotURIList = new ERMRCommunications().getOutputSlotURIList(repository, uri);
			assertEquals("<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", outputSlotURIList.get(0));
		} catch (Exception e) {
			fail("getOutputSlotURIs(): " + e.getMessage());
		}
	}

	@Test
	public void getImplementationURI() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			String implementationURI = new ERMRCommunications().getImplementationURI(repository, uri);
			assertEquals(expectedAggregatedProcess.getImplementation().getId(), implementationURI);
		} catch (Exception e) {
			fail("getImplementationURI(): " + e.getMessage());
		}
	}

	@Test
	public void getProcessAttributes() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			ProcessBase process = new ERMRCommunications().getProcessAttributes(repository, uri);
			assertEquals(expectedAggregatedProcess.getId(), process.getId());
			assertEquals(expectedAggregatedProcess.getName(), process.getName());
			assertEquals(expectedAggregatedProcess.getDescription(), process.getDescription());
			assertEquals(expectedAggregatedProcess.getVersion(), process.getVersion());
		} catch (Exception e) {
			fail("getProcessAttributes(): " + e.getMessage());
		}
	}

	@Test
	public void getProcessEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			assertEquals(expectedProcess, process);
		} catch (Exception e) {
			fail("getProcessEntity(): " + e.getMessage());
		}
	}

	@Test
	public void getProcessFlow() {
		String uriAGP = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			String type = new ERMRCommunications().getProcessFlow(repository, uriAGP);
			assertEquals(expectedAggregatedProcess.getProcessFlow(), type);
		} catch (Exception e) {
			fail("getProcessType(): " + e.getMessage());
		}
	}

	@Test
	public void getDataFlow() {
		String uriAGP = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			String type = new ERMRCommunications().getDataFlow(repository, uriAGP);
			assertEquals(expectedAggregatedProcess.getDataFlow(), type);
		} catch (Exception e) {
			fail("getProcessType(): " + e.getMessage());
		}
	}

	@Test
	public void getAggregatedProcessEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			AggregatedProcess process = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			assertEquals(expectedAggregatedProcess, process);
		} catch (Exception e) {
			fail("getAggregatedProcessEntity(): " + e.getMessage());
		}
	}

	@Test
	public void getImplementationFile() {
		String uri = collection + "VirusCheckProcess.bpmn2";
		try {
			InputStream inputStream = new ERMRCommunications().getImplementationFile(uri);
			InputStream expectedStream = new FileInputStream(new File("src/test/resources/ermr/communications/VirusCheckProcess.bpmn2"));
			// Utils.writeInputStream(expectedStream,
			// "/home/noa/expectedStream.txt");
			// Utils.writeInputStream(inputStream, "/home/noa/inputStream.txt");
			assertTrue(IOUtils.contentEquals(expectedStream, inputStream));
		} catch (Exception e) {
			fail("getImplementationFile(): " + e.getMessage());
		}
	}

	// -- HELP FUNCTIONS: create expected entities and results --//

	private void setExpectedProcess() {
		expectedProcess = new ProcessBase();
		expectedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		expectedProcess.setName("Ingest Artwork Software");
		expectedProcess
				.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedProcess.setVersion("1");
		expectedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/NoaCollection/ERMRCommunications/IngestArtworkSoftware.bpmn2", "sha256",
				"8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedProcess.setInputSlots(new ArrayList<InputSlot>());
		expectedProcess
				.getInputSlots()
				.add(CreateEntities.createInputSlot(
						new ArrayList<String>(
								Arrays.asList(
										"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>",
										"InputSlot Artwork Software",
										"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
										"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), false));
		expectedProcess.getInputSlots().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>",
								"InputSlot Package Format",
								"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
		expectedProcess.setOutputSlots(new ArrayList<OutputSlot>());
		expectedProcess
				.getOutputSlots()
				.add(CreateEntities.createOutputSlot(new ArrayList<String>(
						Arrays.asList(
								"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>",
								"OutputSlot Package",
								"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
	}

	private void setExpectedAggregatedProcess() {
		expectedAggregatedProcess = new AggregatedProcess();
		expectedAggregatedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		expectedAggregatedProcess.setName("Ingest Artwork Software");
		expectedAggregatedProcess
				.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedAggregatedProcess.setVersion("1");
		expectedAggregatedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/NoaCollection/ERMRCommunications/IngestArtworkSoftware.bpmn2", "sha256",
				"8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedAggregatedProcess.setInputSlots(new ArrayList<InputSlot>());
		expectedAggregatedProcess
				.getInputSlots()
				.add(CreateEntities.createInputSlot(
						new ArrayList<String>(
								Arrays.asList(
										"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>",
										"InputSlot Artwork Software",
										"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
										"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), false));
		expectedAggregatedProcess.getInputSlots().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>",
								"InputSlot Package Format",
								"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
		expectedAggregatedProcess.setOutputSlots(new ArrayList<OutputSlot>());
		expectedAggregatedProcess
				.getOutputSlots()
				.add(CreateEntities.createOutputSlot(new ArrayList<String>(
						Arrays.asList(
								"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>",
								"OutputSlot Package",
								"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		expectedAggregatedProcess
				.setProcessFlow("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>");
		expectedAggregatedProcess
				.setDataFlow("{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
						+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtracMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
						+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
						+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
						+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
						+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}");

	}
}
