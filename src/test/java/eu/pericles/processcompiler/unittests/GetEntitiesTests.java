package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.thoughtworks.xstream.io.json.JsonWriter;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.SPARQLQuery;

public class GetEntitiesTests {
	
	static String collection = "NoaCollectionTest";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/helloworld/Ecosystem.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;

	@Test
	public void encodeQuery() {
		String decodedQuery = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"6d4c7d23-d1cc-4ebd-915e-4b465b494ab3\" . }";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";
		try {
			String encodedQuery = new SPARQLQuery().encode(decodedQuery);
			assertEquals(expectedQuery, encodedQuery);
		} catch (UnsupportedEncodingException e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void createQueryGetEntityByID() {
		String id = "6d4c7d23-d1cc-4ebd-915e-4b465b494ab3";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";
		
		try {
			String query = new SPARQLQuery().createQueryGetEntityByID(id);
			assertEquals(expectedQuery, query);
		} catch (UnsupportedEncodingException e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void getImplementation() {
		String id = "6d4c7d23-d1cc-4ebd-915e-4b465b494ab3";
		
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			//TODO Error in the ERMR design: this should be 201 Created 
			assertEquals(204, response.getStatus());
			response = client.query(repository, new SPARQLQuery().createQueryGetImplementationLocation(id));
			assertEquals(200, response.getStatus());
			//Create a function to parse the results of the requests
			//System.out.println("Response to query:\n" + response.readEntity(String.class));
			JsonObject jsonObject = Json.createReader(response.readEntity(InputStream.class)).readObject();
			System.out.println("JSON OBJECT:\n" + jsonObject.toString());
			} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
