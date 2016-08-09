package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ermr.ERMRCommunications;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class ProcessCompilerTests {

	static String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";

	private String[] digObjects = { "VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn" };

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI(service);
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
			for (int i = 0; i < digObjects.length; i++) {
				response = new ERMRClientAPI().createDigitalObject(collection + digObjects[i], doPath + digObjects[i], doMediaType);
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
				response = new ERMRClientAPI().deleteDigitalObject(collection + digObjects[i]);
				assertEquals(204, response.getStatus());
			}
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}

	// ----------------------------- TESTS ----------------------------------

	@Test
	public void vimp_valid() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications(service).getProcessEntity(repository, uri);
			BPMNProcess bpmnProcess = new BPMNParser().parse(new ERMRCommunications(service).getImplementationFile(process
					.getImplementation().getLocation()));
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertTrue(validationResult.isValid());
			assertEquals("VALID IMPLEMENTATION", validationResult.getMessage());
		} catch (Exception e) {
			fail("vimp_valid(): " + e.getMessage());
		}
	}

	@Test
	public void vimp_slotMissing() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			BPMNProcess bpmnProcess = new BPMNParser().parse(new ERMRCommunications(service).getImplementationFile(process
					.getImplementation().getLocation()));
			process.getInputSlots().get(0).setId("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDXX>");
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals(
					"INVALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDXX> is wrong or missing in the BPMN file",
					validationResult.getMessage());
		} catch (Exception e) {
			fail("vimp_inputSlotMissing(): " + e.getMessage());
		}
	}

	@Test
	public void vimp_slotDifferentType() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			BPMNProcess bpmnProcess = new BPMNParser().parse(new ERMRCommunications(service).getImplementationFile(process
					.getImplementation().getLocation()));
			process.getInputSlots().get(1).setType("<http://www.pericles-project.eu/ns/ecosystem#File>");
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals(
					"INVALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD> is wrong or missing in the BPMN file",
					validationResult.getMessage());
		} catch (Exception e) {
			fail("vimp_inputSlotDifferentType(): " + e.getMessage());
		}
	}
	
	@Test
	public void vagg_valid() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			ValidationResult validationResult = new ProcessCompiler(service).validateAggregation(repository, aggregatedProcess);
			assertTrue(validationResult.isValid());
			assertEquals("VALID AGGREGATION", validationResult.getMessage());
		} catch (Exception e) {
			fail("vagg_valid()");
		}
	}
	
	@Test
	public void vagg_subprocessMissing() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			aggregatedProcess.setProcessFlow("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpStoreFile>");
			ValidationResult validationResult = new ProcessCompiler(service).validateAggregation(repository, aggregatedProcess);
			assertFalse(validationResult.isValid());
			assertEquals("INVALID AGGREGATION: Process <http://www.pericles-project.eu/ns/ecosystem#atpStoreFile> is bad defined or missing", validationResult.getMessage());
		} catch (Exception e) {
			fail("vagg_subprocessMissing(): " + e.getMessage());
		}
	}
	
	@Test
	public void vagg_slotMissing() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			aggregatedProcess.setDataFlow("[{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 0, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isVirusCheckFile>\"} , {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isExtractMDDO>\"}, {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>\"} ,{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>\"} , {\"sourceProcess\": 1, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>\"} , {\"sourceProcess\": 2, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>\", \"targetProcess\": 3, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>\"}]");
			ValidationResult validationResult = new ProcessCompiler(service).validateAggregation(repository, aggregatedProcess);
			assertFalse(validationResult.isValid());
			assertEquals("INVALID AGGREGATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckFile> doesn't exist", validationResult.getMessage());
		} catch (Exception e) {
			fail("vagg_slotMissing(): " + e.getMessage());
		}
	}
	
	@Test
	public void vagg_resourceNotAvailable() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			aggregatedProcess.setProcessFlow("<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD>");
			aggregatedProcess.setDataFlow("[{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 0, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDO>\"} , {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isExtractMDDO>\"}, {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>\"} ,{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>\"} , {\"sourceProcess\": 2, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>\"} , {\"sourceProcess\": 1, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>\", \"targetProcess\": 3, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>\"}]");
			ValidationResult validationResult = new ProcessCompiler(service).validateAggregation(repository, aggregatedProcess);
			assertFalse(validationResult.isValid());
			assertEquals("INVALID AGGREGATION: Not available source (2,<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>)", validationResult.getMessage());
		} catch (Exception e) {
			fail("vagg_resourceNotAvailable(): " + e.getMessage());
		}
	}
	
	@Test
	public void vagg_incompatibleConnection() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			aggregatedProcess.setDataFlow("[{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 0, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDO>\"} , {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 1, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isExtractMDDO>\"}, {\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>\"} ,{\"sourceProcess\": 3, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>\"} , {\"sourceProcess\": 1, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD>\", \"targetProcess\": 2, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>\"} , {\"sourceProcess\": 2, \"sourceSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>\", \"targetProcess\": 3, \"targetSlot\": \"<http://www.pericles-project.eu/ns/ecosystem#osIngestAWSWP>\"}]");
			ValidationResult validationResult = new ProcessCompiler(service).validateAggregation(repository, aggregatedProcess);
			assertFalse(validationResult.isValid());
			assertEquals("INVALID AGGREGATION: Invalid data type in connection (<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>,<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>)", validationResult.getMessage());
		} catch (Exception e) {
			fail("vagg_incompatibleConnection(): " + e.getMessage());
		}
	}

	@Test
	public void compile_valid() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			String expected = FileUtils.readFileToString(new File("src/test/resources/ingest_sba/IngestAWSW.bpmn"));
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			String result = new ProcessCompiler(service).compile(repository, aggregatedProcess);
			assertEquals(expected, result);
		} catch (Exception e) {
			fail("compile_valid()");
		}
	}
}
