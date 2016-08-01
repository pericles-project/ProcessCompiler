package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.ng.CompiledProcess;
import eu.pericles.processcompiler.ng.PCAggregatedProcess;
import eu.pericles.processcompiler.ng.PCDataConnection;
import eu.pericles.processcompiler.ng.PCDataObject;
import eu.pericles.processcompiler.ng.PCSubprocess;
import eu.pericles.processcompiler.ng.ProcessCompiler;
import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ng.ermr.ERMRCommunications;

/**
 * BPMN Files are stored in the Object Store under the Folder:
 * NoaCollection/Compilation/
 */
public class PCCompilationTests {

	private String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/IngestSBA/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ng/Ecosystem_Compilation.ttl";
	static String triplesMediaType = "text/turtle";

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
		} catch (ERMRClientException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}

	// -- TESTS --//
	/*
	 * @Test
	 * public void parseBPMNfilesTest() {
	 * try {
	 * BPMNParser parser = new BPMNParser();
	 * parser.parse("src/test/resources/ng/VirusCheck.bpmn");
	 * parser.parse("src/test/resources/ng/ExtractMD.bpmn");
	 * parser.parse("src/test/resources/ng/EncapsulateDOMD.bpmn");
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * fail("Exception in parseBPMNfilesTest()");
	 * }
	 * }
	 */
	/*
	@Test
	public void compileCompiledProcessTest() {
		try {
			CompiledProcess compiledProcess = new CompiledProcess();
			compiledProcess.setId("agpIngestAWSW");
			compiledProcess.setName("Ingest Artwork Software");
			compiledProcess.setType("Public");
			compiledProcess.getDataObjects().add(new PCDataObject("isIngestAWSWAW", "InputSlot Artwork Software", "ArtworkSoftware"));
			compiledProcess.getDataObjects().add(new PCDataObject("isIngestAWSWPF", "InputSlot Package Format", "PackageFormat"));
			compiledProcess.getDataObjects().add(new PCDataObject("osExtractMDMD_1", "OutputSlot Metadata", "Metadata"));
			compiledProcess.getDataObjects().add(new PCDataObject("osIngestAWSWP", "OutputSlot Package", "Package"));
			PCSubprocess subprocess = new PCSubprocess();
			subprocess.setId("assembler.atpVirusCheck");
			subprocess.getDataInputMap().put("isIngestAWSWAW", "isVirusCheckDO");
			compiledProcess.getSubprocesses().add(subprocess);
			PCSubprocess subprocess2 = new PCSubprocess();
			subprocess2.setId("assembler.atpExtractMD");
			subprocess2.getDataInputMap().put("isIngestAWSWAW", "isExtractMDDO");
			subprocess2.getDataOutputMap().put("osExtractMDMD_1", "osExtractMDMD");
			compiledProcess.getSubprocesses().add(subprocess2);
			PCSubprocess subprocess3 = new PCSubprocess();
			subprocess3.setId("assembler.atpEncapsulateDOMD");
			subprocess3.getDataInputMap().put("isIngestAWSWAW", "isEncapsulateDOMDDO");
			subprocess3.getDataInputMap().put("osExtractMDMD_1", "isEncapsulateDOMDMD");
			subprocess3.getDataInputMap().put("isIngestAWSWPF", "isEncapsulateDOMDPF");
			subprocess3.getDataOutputMap().put("osIngestAWSWP", "osEncapsulateDOMDP");
			compiledProcess.getSubprocesses().add(subprocess3);
			ProcessCompiler compiler = new ProcessCompiler(service);
			compiler.compile(compiledProcess, "src/test/resources/ng/CompiledProcess.bpmn");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in compileCompiledProcessTest()");
		}
	}*/

	@Test
	public void compileTest() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			ProcessCompiler compiler = new ProcessCompiler(service);
			System.out.println("createPCAggregatedProcess()");
			PCAggregatedProcess pcAggregatedProcess = compiler.createPCAggregatedProcess(repository, aggregatedProcess);
			System.out.println("createCompiledProcess()");
			CompiledProcess compiledProcess = compiler.createCompiledProcess(pcAggregatedProcess);
			printCompiledProcess(compiledProcess);
			compiler.compile(compiledProcess, "src/test/resources/ng/CompiledProcess.bpmn");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in compileTest()");
		}
	}

	private void printCompiledProcess(CompiledProcess compiledProcess) {
		System.out.println("\nCOMPILED PROCESS: ");
		System.out.println(compiledProcess.getId());
		System.out.println(compiledProcess.getName());
		System.out.println(compiledProcess.getType());
		for (PCDataObject dataObject : compiledProcess.getDataObjects())
			System.out.println("\tDO: " + dataObject.getId() + ", " + dataObject.getName() + " , " + dataObject.getType());
		for (PCSubprocess subprocess : compiledProcess.getSubprocesses()) {
			System.out.println("\tSUB: " + subprocess.getId());
			Iterator<Entry<String, String>> it = subprocess.getDataInputMap().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> connection = (Map.Entry<String, String>) it.next();
				System.out.println("\t  Input: " + connection.getKey() + " " + connection.getValue());
			}
			it = subprocess.getDataOutputMap().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> connection = (Map.Entry<String, String>) it.next();
				System.out.println("\t  Output: " + connection.getKey() + " " + connection.getValue());
			}
		}
		System.out.println();
	}
}
