package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.ecosystem.Sequence;
import eu.pericles.processcompiler.ecosystem.SlotPair;

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
	
	
	
	/*
	@Test 
	public void getSequence() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		try {
			Sequence sequence = new ERMRCommunications().getSequenceEntity(repository, uri);
			assertEquals("<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", outputSlotURIList.get(0));
		} catch (Exception e) {
			fail("getOutputSlotURIs(): " + e.getMessage());
		}
	}
	
	
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
		expectedProcess.setImplementation(createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined",
				"sha256", "8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedProcess.setInputs(new ArrayList<InputSlot>());
		expectedProcess.getInputs().add(createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", 
				"InputSlot Artwork Software",
				"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), 
				false));
		expectedProcess.getInputs().add(createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", 
				"InputSlot Package Format",
				"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), 
				false));
		expectedProcess.setOutputs(new ArrayList<OutputSlot>());
		expectedProcess.getOutputs().add(createOutputSlot(new ArrayList<String>(Arrays.asList(
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
		expectedAggregatedProcess.setImplementation(createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impIngestAWSW>", "1", "BPMN",
				"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/nodefined",
				"sha256", "8c30fb10c930edc21ad11d0c6d1484430813cfd75375451bced7f3cbcd98c9e8"))));
		expectedAggregatedProcess.setInputs(new ArrayList<InputSlot>());
		expectedAggregatedProcess.getInputs().add(createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>", 
				"InputSlot Artwork Software",
				"Input slot corresponding to the Artwork Software entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#ArtworkSoftware>")), 
				false));
		expectedAggregatedProcess.getInputs().add(createInputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>", 
				"InputSlot Package Format",
				"Input slot corresponding to the Package Format entity for the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), 
				false));
		expectedAggregatedProcess.setOutputs(new ArrayList<OutputSlot>());
		expectedAggregatedProcess.getOutputs().add(createOutputSlot(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>", 
				"OutputSlot Package",
				"Output slot corresponding to the Package entity created as the result of the aggregated process Ingest Artwork Software",
				"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		expectedAggregatedProcess.setSequence(createSequence("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck>, "
				+ "<http://www.pericles-project.eu/ns/ecosystem#atpExtractMD>, <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>", 
				"{[<http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]} "
				+ " {[<http://www.pericles-project.eu/ns/ecosystem#isExtracMDDM> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>] "
				+ " [<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD> <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>]}"
				+ " {[<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD> <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>]"
				+ " [<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP> <http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>]}"));
	}

	private Implementation createImplementation(List<String> values) {
		Implementation implementation = new Implementation();
		implementation.setId(values.get(0));
		implementation.setVersion(values.get(1));
		implementation.setType(values.get(2));
		implementation.setLocation(values.get(3));
		implementation.setFixity(new Fixity());
		implementation.getFixity().setAlgorithm(values.get(4));
		implementation.getFixity().setChecksum(values.get(5));
		
		return implementation;
	}
	
	private InputSlot createInputSlot(List<String> values, boolean optional) {
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId(values.get(0));
		inputSlot.setName(values.get(1));
		inputSlot.setDescription(values.get(2));
		inputSlot.setType(values.get(3));
		inputSlot.setOptional(optional);
		
		return inputSlot;
	}
	
	private OutputSlot createOutputSlot(List<String> values) {
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId(values.get(0));
		outputSlot.setName(values.get(1));
		outputSlot.setDescription(values.get(2));
		outputSlot.setType(values.get(3));
		
		return outputSlot;
	}
	
	private Sequence createSequence(String processFlow, String dataFlow) {
		Sequence sequence = new Sequence();
		String[] processes = processFlow.split("\\s\\s*");
		sequence.setProcessFlow(new ArrayList<String>(Arrays.asList(processes)));
		sequence.setDataFlow(createDataFlow(dataFlow));
		
		return sequence;
	}
	
	private ArrayList<ArrayList<SlotPair<String,String>>> createDataFlow(String dataFlowString) {
		ArrayList<ArrayList<SlotPair<String,String>>> dataFlow = new ArrayList<ArrayList<SlotPair<String,String>>>();		
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(dataFlowString);
		while (matcher.find()) {
			dataFlow.add(createSlotPairs(matcher.group()));
		}
		return dataFlow;
	}
	
	private ArrayList<SlotPair<String,String>> createSlotPairs(String slotPairsString) {
		ArrayList<SlotPair<String,String>> slotPairs = new ArrayList<SlotPair<String, String>>();
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(slotPairsString);
		while (matcher.find()) {
			slotPairs.add(createSlotPair(matcher.group()));
		}
		return slotPairs;
	}
	
	private SlotPair<String, String> createSlotPair(String slotPairString) {
		String[] pairValues = slotPairString.substring(1, slotPairString.length()-1).split("\\s\\s*");
		return new SlotPair<String, String>(pairValues[0], pairValues[1]);
	}
}
