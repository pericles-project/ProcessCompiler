package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.ecosystem.Sequence;
import eu.pericles.processcompiler.testutils.CreateEntities;

public class GetEntitiesTests {

	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ermr/communications/Ecosystem.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;	
	private AggregatedProcess expectedAggregatedProcess; 
	private Process expectedProcess;
	
	@Before
	public void prepareTests() {
		setRepository();
		setExpectedResults();
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
			assertEquals(expectedAggregatedProcess.getInputs().get(0), inputSlot);
		} catch (Exception e) {
			fail("getInputSlot(): " + e.getMessage());
		}
	}
	
	@Test
	public void getOutputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>";
		try {
			OutputSlot outputSlot = new ERMRCommunications().getOutputSlotEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getOutputs().get(0), outputSlot);
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
			Process process = new ERMRCommunications().getProcessAttributes(repository, uri);
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
			Process process = new ERMRCommunications().getProcessEntity(repository, uri);
			assertEquals(expectedProcess, process);
		} catch (Exception e) {
			fail("getProcessEntity(): " + e.getMessage());
		}
	}
	
	@Test 
	public void getSequenceEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#seqIngestAWSW>";
		try {
			Sequence sequence = new ERMRCommunications().getSequenceEntity(repository, uri);
			assertEquals(expectedAggregatedProcess.getSequence(), sequence);
		} catch (Exception e) {
			fail("getSequenceEntity(): " + e.getMessage());
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
	public void getProcessType() {
		String uriAGP = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		//String uriATP = "<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck>";
		try {
			String type = new ERMRCommunications().getProcessType(repository, uriAGP);
			assertEquals(expectedAggregatedProcess.getClass().getSimpleName(), type);
		} catch (Exception e) {
			fail("getProcessType(): " + e.getMessage());
		}
	}
	
	@Test
	public void getImplementationFile() {
		String uri = "cdmi_objectid/0000A4EF00186738BE125FC57E4E4BF5AED5B4845BB62AF7";//"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/0000A4EF00186738BE125FC57E4E4BF5AED5B4845BB62AF7";
		try {
			InputStream inputStream = new ERMRCommunications().getImplementationFile(repository, uri);
			InputStream expectedStream = new FileInputStream(new File("src/test/resources/ermr/communications/VirusCheckProcess.bpmn2"));
			//Utils.writeInputStream(expectedStream, "/home/noa/expectedStream.txt");
			//Utils.writeInputStream(inputStream, "/home/noa/inputStream.txt");
			assertTrue(IOUtils.contentEquals(expectedStream, inputStream));
		} catch (Exception e) {
			fail("getImplementationFile(): " + e.getMessage());
		}
	}

	/*
	@Test
	public void getImplementationLocation() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck>";
		String expectedLocation = "https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/0000A4EF00186738BE125FC57E4E4BF5AED5B4845BB62AF7";
		try {
			String location = new ERMRCommunications().getImplementationLocation(repository, uri);
			assertEquals(expectedLocation, location);
			//URL url = new URL(location);			
			//Utils.fileContentEquals(url.getFile(), "src/test/resources/ermr/communications/DigitalObject.bpmn2");
		} catch (Exception e) {
			fail("getImplementationLocation(): " + e.getMessage());
		}
	}*/
	
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
	
	public void setExpectedResults() {
		setExpectedProcess();
		setExpectedAggregatedProcess();
	}
	
	private void setExpectedProcess() {
		expectedProcess = new Process();
		expectedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		expectedProcess.setName("Ingest Artwork Software");
		expectedProcess.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedProcess.setVersion("1");
		expectedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined",
				"sha256", "8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedProcess.setInputs(new ArrayList<InputSlot>());
		expectedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", 
				"InputSlot Artwork Software",
				"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), 
				false));
		expectedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", 
				"InputSlot Package Format",
				"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), 
				false));
		expectedProcess.setOutputs(new ArrayList<OutputSlot>());
		expectedProcess.getOutputs().add(CreateEntities.createOutputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", 
				"OutputSlot Package",
				"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
	}

	private void setExpectedAggregatedProcess() {
		expectedAggregatedProcess = new AggregatedProcess();
		expectedAggregatedProcess.setId("<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
		expectedAggregatedProcess.setName("Ingest Artwork Software");
		expectedAggregatedProcess.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedAggregatedProcess.setVersion("1");
		expectedAggregatedProcess.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined",
				"sha256", "8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedAggregatedProcess.setInputs(new ArrayList<InputSlot>());
		expectedAggregatedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", 
				"InputSlot Artwork Software",
				"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), 
				false));
		expectedAggregatedProcess.getInputs().add(CreateEntities.createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", 
				"InputSlot Package Format",
				"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), 
				false));
		expectedAggregatedProcess.setOutputs(new ArrayList<OutputSlot>());
		expectedAggregatedProcess.getOutputs().add(CreateEntities.createOutputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", 
				"OutputSlot Package",
				"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		expectedAggregatedProcess.setSequence(CreateEntities.createSequence("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
				"{[1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[2 <http://www.pericles-project.eu/ns/ecosystem#isExtracMDDM>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>] [0 <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]}"
				+ " {[3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>] [2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]}"
				+ " {[0 <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>] [3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>]}"));
				
		/*
		 expectedAggregatedProcess.setSequence(CreateEntities.createSequence("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>",
				"{[<http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]}"
				+ " {[<http://www.pericles-project.eu/ns/ecosystem#isExtracMDDM> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>] "
				+ " [<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD> <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>]}"
				+ " {[<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD> <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP> <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>]}"));
				*/
	}
}
