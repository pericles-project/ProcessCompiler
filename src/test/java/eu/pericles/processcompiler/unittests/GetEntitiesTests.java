package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.AtomicProcess;
import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;

public class GetEntitiesTests {

	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ermr/communications/Ecosystem.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	
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
	public void getImplementationEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#impVirusCheck>";
		Implementation expectedImplementation = new Implementation();
		expectedImplementation.setId(uri);
		expectedImplementation.setVersion("1");
		expectedImplementation.setType("BPMN");
		expectedImplementation.setLocation("https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/0000A4EF00186738BE125FC57E4E4BF5AED5B4845BB62AF7");
		expectedImplementation.setFixity(new Fixity());
		expectedImplementation.getFixity().setAlgorithm("sha256");
		expectedImplementation.getFixity().setChecksum("d04bfc93b0df23bb79179a1616df6fc8bd857ca8a863b487a20fc46bc5129c46");
		try {
			Implementation implementation = new ERMRCommunications().getImplementationEntity(repository, uri);
			assertEquals(expectedImplementation, implementation);
		} catch (Exception e) {
			fail("getImplementation(): " + e.getMessage());
		}
	}
	
	@Test
	public void getInputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>";
		InputSlot expectedInputSlot = new InputSlot();
		expectedInputSlot.setId(uri);
		expectedInputSlot.setName("InputSlot Artwork Software");
		expectedInputSlot.setDescription("Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software");
		expectedInputSlot.setType("<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>");
		expectedInputSlot.setOptional(false);
		try {
			InputSlot inputSlot = new ERMRCommunications().getInputSlotEntity(repository, uri);
			assertEquals(expectedInputSlot, inputSlot);
		} catch (Exception e) {
			fail("getInputSlot(): " + e.getMessage());
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
	public void getOutputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>";
		OutputSlot expectedOutputSlot = new OutputSlot();
		expectedOutputSlot.setId(uri);
		expectedOutputSlot.setName("OutputSlot Package");
		expectedOutputSlot.setDescription("Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software");
		expectedOutputSlot.setType("<http://www.pericles-project.eu/ns/ecosystem#Package>");
		try {
			OutputSlot outputSlot = new ERMRCommunications().getOutputSlotEntity(repository, uri);
			assertEquals(expectedOutputSlot, outputSlot);
		} catch (Exception e) {
			fail("getOutputSlot(): " + e.getMessage());
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
	public void getProcessAttributes() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		Process expectedProcess = new Process();
		expectedProcess.setId(uri);
		expectedProcess.setName("Ingest Artwork Software");
		expectedProcess.setDescription("Aggregated process that ingest an artwork software by doing the following: check for viruses, extract the metadata and encapsulate the artwork together with it");
		expectedProcess.setVersion("1");
		try {
			Process process = new ERMRCommunications().getProcessAttributes(repository, uri);
			assertEquals(expectedProcess.getId(), process.getId());
			assertEquals(expectedProcess.getName(), process.getName());
			assertEquals(expectedProcess.getDescription(), process.getDescription());
			assertEquals(expectedProcess.getVersion(), process.getVersion());
		} catch (Exception e) {
			fail("getOutputSlotURIs(): " + e.getMessage());
		}
	}
	
	/*
	@Test
	public void getAggregatedProcess() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		AggregatedProcess expectedProcess = new AggregatedProcess();
		try {
			AggregatedProcess process = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			assertEquals(expectedProcess, process);
		} catch (Exception e) {
			fail("getAggregatedProcess(): " + e.getMessage());
		}
	}
	*/
	
	/*
	
	@Test
	public void getAtomicProcess() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck>";
		AtomicProcess expectedProcess = new AtomicProcess();
		try {
			AtomicProcess process = new ERMRCommunications().getAtomicProcessEntity(repository, uri);
			assertEquals(expectedProcess, process);
		} catch (Exception e) {
			fail("getAtomicProcess(): " + e.getMessage());
		}
	}
	

	*/
	

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
}
