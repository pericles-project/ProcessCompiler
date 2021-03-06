package eu.pericles.processcompiler.unittests;

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

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ermr.ERMRCommunications;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.testutils.Utils;

public class ERMRCommunicationsTests {

	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";

	private String[] digObjects = { "VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn" };

	private String service;
	private AggregatedProcess expectedAggregatedProcess;
	private ProcessBase expectedProcess;

	@Before
	public void setRepository() {
		try {
			service = System.getenv("ERMR_URL");
			ERMRClientAPI client = new ERMRClientAPI(service);
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
			for (int i = 0; i < digObjects.length; i++) {
				response = new ERMRClientAPI(service).createDigitalObject(collection + digObjects[i], doPath + digObjects[i], doMediaType);
				assertEquals(201, response.getStatus());
			}
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
			for (int i = 0; i < digObjects.length; i++) {
				response = new ERMRClientAPI(service).deleteDigitalObject(collection + digObjects[i]);
				assertEquals(204, response.getStatus());
			}
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
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#impExtractMD>";
		try {
			Implementation implementation = new ERMRCommunications(service).getImplementationEntity(repository, uri);
			assertEquals(expectedProcess.getImplementation(), implementation);
		} catch (Exception e) {
			fail("getImplementation(): " + e.getMessage());
		}
	}

	@Test
	public void getInputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>";
		try {
			InputSlot inputSlot = new ERMRCommunications(service).getInputSlotEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getInputSlots().get(0), inputSlot);
		} catch (Exception e) {
			fail("getInputSlot(): " + e.getMessage());
		}
	}

	@Test
	public void getOutputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#osIngestAWSWP>";
		try {
			OutputSlot outputSlot = new ERMRCommunications(service).getOutputSlotEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getOutputSlots().get(0), outputSlot);
		} catch (Exception e) {
			fail("getOutputSlot(): " + e.getMessage());
		}
	}

	@Test
	public void getInputSlotURIList() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			List<String> inputSlotURIList = new ERMRCommunications(service).getInputSlotURIList(repository, uri);
			assertEquals("<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>", inputSlotURIList.get(0));
			assertEquals("<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWPF>", inputSlotURIList.get(1));
		} catch (Exception e) {
			fail("getInputSlotURIs(): " + e.getMessage());
		}
	}

	@Test
	public void getOutputSlotURIList() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			List<String> outputSlotURIList = new ERMRCommunications(service).getOutputSlotURIList(repository, uri);
			assertEquals("<http://www.pericles-project.eu/ns/ingest-scenario#osIngestAWSWP>", outputSlotURIList.get(0));
		} catch (Exception e) {
			fail("getOutputSlotURIs(): " + e.getMessage());
		}
	}

	@Test
	public void getImplementationURI() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#atpExtractMD>";
		try {
			String implementationURI = new ERMRCommunications(service).getImplementationURI(repository, uri);
			assertEquals(expectedProcess.getImplementation().getId(), implementationURI);
		} catch (Exception e) {
			fail("getImplementationURI(): " + e.getMessage());
		}
	}

	@Test
	public void getProcessAttributes() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			ProcessBase process = new ERMRCommunications(service).getProcessAttributes(repository, uri);
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
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#atpExtractMD>";
		try {
			ProcessBase process = new ERMRCommunications(service).getProcessEntity(repository, uri);
			assertEquals(expectedProcess, process);
		} catch (Exception e) {
			fail("getProcessEntity(): " + e.getMessage());
		}
	}

	@Test
	public void getProcessFlow() {
		String uriAGP = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			String type = new ERMRCommunications(service).getProcessFlow(repository, uriAGP);
			assertEquals(expectedAggregatedProcess.getProcessFlow(), type);
		} catch (Exception e) {
			fail("getProcessType(): " + e.getMessage());
		}
	}

	@Test
	public void getDataFlow() {
		String uriAGP = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			String type = new ERMRCommunications(service).getDataFlow(repository, uriAGP);
			assertEquals(expectedAggregatedProcess.getDataFlow(), type);
		} catch (Exception e) {
			fail("getProcessType(): " + e.getMessage());
		}
	}

	@Test
	public void getAggregatedProcessEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		try {
			AggregatedProcess process = new ERMRCommunications(service).getAggregatedProcessEntity(repository, uri);
			assertEquals(expectedAggregatedProcess, process);
		} catch (Exception e) {
			fail("getAggregatedProcessEntity(): " + e.getMessage());
		}
	}

	@Test
	public void getImplementationFile() {
		String uri = collection + "VirusCheck.bpmn";
		try {
			InputStream inputStream = new ERMRCommunications(service).getImplementationFile(uri);
			InputStream expectedStream = new FileInputStream(new File("src/test/resources/ingest_sba/VirusCheck.bpmn"));
			assertTrue(IOUtils.contentEquals(expectedStream, inputStream));
		} catch (Exception e) {
			fail("getImplementationFile(): " + e.getMessage());
		}
	}

	// -- HELP FUNCTIONS: create expected entities and results --//

	private void setExpectedProcess() {
		expectedProcess = new ProcessBase();
		expectedProcess.setId("<http://www.pericles-project.eu/ns/ingest-scenario#atpExtractMD>");
		expectedProcess.setName("Extract Metadata");
		expectedProcess.setDescription("Atomic process that extract the metadata of a digital object");
		expectedProcess.setVersion("1");
		expectedProcess.setImplementation(Utils.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ingest-scenario#impExtractMD>", "1", "BPMN", "NoaCollection/Test/ExtractMD.bpmn",
				"SHA256-7c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e3"))));
		expectedProcess.setInputSlots(new ArrayList<InputSlot>());
		expectedProcess.getInputSlots().add(
				Utils.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ingest-scenario#isExtractMDDO>",
								"InputSlot Digital Material",
								"Input slot corresponding to the digital material from which metadata is extracted",
								"<http://www.pericles-project.eu/ns/ingest-scenario#DigitalObject>")), false));
		expectedProcess.setOutputSlots(new ArrayList<OutputSlot>());
		expectedProcess.getOutputSlots().add(
				Utils.createOutputSlot(new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ingest-scenario#osExtractMDMD>",
						"OutputSlot Metadata", "Output slot corresponding to the extracted metadata",
						"<http://www.pericles-project.eu/ns/ingest-scenario#Metadata>"))));
	}

	private void setExpectedAggregatedProcess() {
		expectedAggregatedProcess = new AggregatedProcess();
		expectedAggregatedProcess.setId("<http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>");
		expectedAggregatedProcess.setName("Ingest Artwork Software");
		expectedAggregatedProcess
				.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedAggregatedProcess.setVersion("1");
		expectedAggregatedProcess.setInputSlots(new ArrayList<InputSlot>());
		expectedAggregatedProcess
				.getInputSlots()
				.add(Utils.createInputSlot(
						new ArrayList<String>(
								Arrays.asList(
										"<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>",
										"InputSlot Artwork Software",
										"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
										"<http://www.pericles-project.eu/ns/ingest-scenario#ArtworkSoftware>")), false));
		expectedAggregatedProcess.getInputSlots().add(
				Utils.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWPF>",
								"InputSlot Package Format",
								"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ingest-scenario#PackageFormat>")), false));
		expectedAggregatedProcess.setOutputSlots(new ArrayList<OutputSlot>());
		expectedAggregatedProcess
				.getOutputSlots()
				.add(Utils.createOutputSlot(new ArrayList<String>(
						Arrays.asList(
								"<http://www.pericles-project.eu/ns/ingest-scenario#osIngestAWSWP>",
								"OutputSlot Package",
								"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
								"<http://www.pericles-project.eu/ns/ingest-scenario#Package>"))));
		expectedAggregatedProcess
				.setProcessFlow("<http://www.pericles-project.eu/ns/ingest-scenario#atpVirusCheck> <http://www.pericles-project.eu/ns/ingest-scenario#atpExtractMD> <http://www.pericles-project.eu/ns/ingest-scenario#atpEncapsulateDOMD>");
		expectedAggregatedProcess
				.setDataFlow("[{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>\", \"targetProcess\": 0, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isVirusCheckDO>\"} , {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isExtractMDDO>\"}, {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWAW>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isEncapsulateDOMDDO>\"} ,{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWPF>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isEncapsulateDOMDPF>\"} , {\"sourceProcess\": 1, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#osExtractMDMD>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#isEncapsulateDOMDMD>\"} , {\"sourceProcess\": 2, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#osEncapsulateDOMDP>\", \"targetProcess\": 3, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ingest-scenario#osIngestAWSWP>\"}]");
	}

}
