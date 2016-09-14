package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.web.ApiApplication;
import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;
import eu.pericles.processcompiler.web.resources.CompilerResource.CompileRequest;
import eu.pericles.processcompiler.web.resources.CompilerResource.CompileResult;
import eu.pericles.processcompiler.web.resources.ValidateAggregationResource.ValidateAggregationRequest;
import eu.pericles.processcompiler.web.resources.ValidateAggregationResource.ValidateAggregationResult;
import eu.pericles.processcompiler.web.resources.ValidateImplementationResource.ValidateImplementationRequest;
import eu.pericles.processcompiler.web.resources.ValidateImplementationResource.ValidateImplementationResult;

public class WebComponentTests extends JerseyTest {
	
	static String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";
	static String testPath = "src/test/resources/cli/";
	
	private String[] digObjects = {"VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn"};

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI(service);
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
			ERMRClientAPI client = new ERMRClientAPI(service);
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

	@Override
	protected Application configure() {
		set(TestProperties.LOG_TRAFFIC, true);
		set(TestProperties.DUMP_ENTITY, true);
		return new ApiApplication(new ERMRConfig(service, repository));
	}
	
	@Test
	public void validateImplementationByIDTest() {
		ValidateImplementationRequest vImplementationRequest = new ValidateImplementationRequest();
		vImplementationRequest.ermr = service;
		vImplementationRequest.store = repository;
		vImplementationRequest.id = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
		vImplementationRequest.implementation = "<http://www.pericles-project.eu/ns/ecosystem#impEncapsulateDOMD>";
		
		Response response = target("validate_implementation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vImplementationRequest));
		assertEquals(200, response.getStatus());
		ValidateImplementationResult vImplementationResult = response.readEntity(ValidateImplementationResult.class);
		assertTrue(vImplementationResult.valid);
		assertEquals("OK\nVALID IMPLEMENTATION", vImplementationResult.message);
	}
	
	@Test
	public void validateImplementationByFileTest() {
		ValidateImplementationRequest vImplementationRequest = new ValidateImplementationRequest();
		vImplementationRequest.ermr = service;
		vImplementationRequest.store = repository;
		vImplementationRequest.id = testPath + "inputValidImplementation.json";
		vImplementationRequest.implementation = doPath + "EncapsulateDOMD.bpmn";
		
		Response response = target("validate_implementation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vImplementationRequest));
		assertEquals(200, response.getStatus());
		ValidateImplementationResult vImplementationResult = response.readEntity(ValidateImplementationResult.class);
		assertTrue(vImplementationResult.valid);
		assertEquals("OK\nVALID IMPLEMENTATION", vImplementationResult.message);
	}
	
	@Test
	public void invalidImplementationTest() {
		ValidateImplementationRequest vImplementationRequest = new ValidateImplementationRequest();
		vImplementationRequest.ermr = service;
		vImplementationRequest.store = repository;
		vImplementationRequest.id = testPath + "inputInvalidImplementation.json";
		vImplementationRequest.implementation = doPath + "EncapsulateDOMD.bpmn";
		
		Response response = target("validate_implementation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vImplementationRequest));
		assertEquals(200, response.getStatus());
		ValidateImplementationResult vImplementationResult = response.readEntity(ValidateImplementationResult.class);
		assertFalse(vImplementationResult.valid);
		assertEquals("OK\nINVALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> is wrong or missing in the BPMN file", vImplementationResult.message);
	}
	
	@Test
	public void validateAggregationByIDTest() {
		ValidateAggregationRequest vAggregationRequest = new ValidateAggregationRequest();
		vAggregationRequest.ermr = service;
		vAggregationRequest.store = repository;
		vAggregationRequest.id = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		
		Response response = target("validate_aggregation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vAggregationRequest));
		assertEquals(200, response.getStatus());
		ValidateAggregationResult vAggregationResult = response.readEntity(ValidateAggregationResult.class);
		assertTrue(vAggregationResult.valid);
		assertEquals("OK\nVALID AGGREGATION", vAggregationResult.message);
	}
	
	@Test
	public void validateAggregationByFileTest() {
		ValidateAggregationRequest vAggregationRequest = new ValidateAggregationRequest();
		vAggregationRequest.ermr = service;
		vAggregationRequest.store = repository;
		vAggregationRequest.id = testPath + "inputValidAggregation.json";
		
		Response response = target("validate_aggregation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vAggregationRequest));
		assertEquals(200, response.getStatus());
		ValidateAggregationResult vAggregationResult = response.readEntity(ValidateAggregationResult.class);
		assertTrue(vAggregationResult.valid);
		assertEquals("OK\nVALID AGGREGATION", vAggregationResult.message);
	}
	
	@Test
	public void invalidAggregationTest() {
		ValidateAggregationRequest vAggregationRequest = new ValidateAggregationRequest();
		vAggregationRequest.ermr = service;
		vAggregationRequest.store = repository;
		vAggregationRequest.id = testPath + "inputInvalidAggregation.json";
		
		Response response = target("validate_aggregation").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(vAggregationRequest));
		assertEquals(200, response.getStatus());
		ValidateAggregationResult vAggregationResult = response.readEntity(ValidateAggregationResult.class);
		assertFalse(vAggregationResult.valid);
		assertEquals("OK\nINVALID AGGREGATION: Invalid data type in connection (<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWPF>,<http://www.pericles-project.eu/ns/ecosystem#isExtractMDDO>)", vAggregationResult.message);
	}

	@Test
	public void compileByIDTest() {
		CompileRequest compileRequest = new CompileRequest();
		compileRequest.ermr = service;
		compileRequest.store = repository;
		compileRequest.id = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";

		Response response = target("compile").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(compileRequest));
		assertEquals(200, response.getStatus());
		CompileResult result = response.readEntity(CompileResult.class);
		assertNotNull(result.bpmn);
		try {
			String expected = FileUtils.readFileToString(new File(doPath + "IngestAWSW.bpmn"));
			assertEquals(expected, result.bpmn);
		} catch (IOException e) {
			System.out.println("Error when reading file: " + e.getMessage());
		}
	}

	@Test
	public void compileByFileTest() {
		CompileRequest compileRequest = new CompileRequest();
		compileRequest.ermr = service;
		compileRequest.store = repository;
		compileRequest.id = testPath + "inputValidAggregation.json";

		Response response = target("compile").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(compileRequest));
		assertEquals(200, response.getStatus());
		CompileResult result = response.readEntity(CompileResult.class);
		assertNotNull(result.bpmn);
		try {
			String expected = FileUtils.readFileToString(new File(doPath + "IngestAWSW.bpmn"));
			assertEquals(expected, result.bpmn);
		} catch (IOException e) {
			System.out.println("Error when reading file: " + e.getMessage());
		}
	}
	
	@Test
	public void invalidCompilationTest() {
		CompileRequest compileRequest = new CompileRequest();
		compileRequest.ermr = service;
		compileRequest.store = repository;
		compileRequest.id = testPath + "inputInvalidAggregation.json";

		Response response = target("compile").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(compileRequest));
		assertEquals(400, response.getStatus());
	}
}
