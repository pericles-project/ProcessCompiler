package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.cli.CommandlineInterface;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.ng.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.testutils.Utils;

public class CLITests {
	
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	private String baseArgs = "-s https://pericles1:PASSWORD@141.5.100.67/api -r NoaRepositoryTest ";
	
	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem_Compilation.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";
	static String testPath = "src/test/resources/cli/";
	
	private String[] digObjects = {"VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn"};

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
			for (int i=0; i<digObjects.length; i++) {
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
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
			for (int i=0; i<digObjects.length; i++) {
			response = new ERMRClientAPI().deleteDigitalObject(collection + digObjects[i]);
			assertEquals(204, response.getStatus());
			}
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}
	
	@Before
	public void changeOutputStream() {
		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}
	
	@After
	public void restoreDefaultOutputStream() {
		System.setOut(defaultOutputStream);
		defaultOutputStream.println(outputStream);
	}

	@Test
	public void validateImplementationByFileTest() {
		String command = baseArgs + "validate_implementation " + testPath + "inputValidateImplementation.json " + doPath + "EncapsulateDOMD.bpmn";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid implementation"));
	}
	
	
	@Test
	public void validateImplementationByIDTest() {
		String command = baseArgs + "validate_implementation <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD> <http://www.pericles-project.eu/ns/ecosystem#impEncapsulateDOMD>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid implementation"));
	}
	
	@Test
	public void validateAggregationByFileTest() {
		String command = baseArgs + "validate_aggregation " + testPath + "inputValidateAggregation.json";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	}
	
	@Test
	public void validateAggregationByIDTest() {
		String command = baseArgs + "validate_aggregation <http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	} 

	@Test
	public void compileAggregatedProcessByFileTest() {
		String command = baseArgs + "compile -o " + testPath + "output.bpmn " + testPath + "inputCompile.json";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("201 Created"));
		
		Utils.fileContentEquals(testPath + "output.bpmn", doPath + "IngestAWSW.bpmn");
	}
	
	@Test
	public void compileAggregatedProcessByIDTest() {
		String command = baseArgs + "compile -o " + testPath + "output.bpmn <http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("201 Created"));
		
		Utils.fileContentEquals(testPath + "output.bpmn", doPath + "IngestAWSW.bpmn");
	}
	
	private void assertCall(String[] args, int code) {
		int status = new CommandlineInterface().call(args);
		if(status != code)
			System.out.println(outputStream.toString());
		assertEquals(code,  status);
	}

}