package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.JSONParser;
import eu.pericles.processcompiler.communications.ermr.SPARQLQuery;
import eu.pericles.processcompiler.testutils.Utils;

public class GetEntitiesTests {

	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ermr/communications/Ecosystem.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;

	@Test
	public void encodeQuery() {
		String decodedQuery = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"6d4c7d23-d1cc-4ebd-915e-4b465b494ab3\" . }";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";
		try {
			String encodedQuery = SPARQLQuery.encode(decodedQuery);
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
			String query = SPARQLQuery.createQueryGetEntityByID(id);
			assertEquals(expectedQuery, query);
		} catch (UnsupportedEncodingException e) {
			fail(e.getStackTrace().toString());
		}
	}

	@Test
	public void getImplementation() {
		String id = "6d4c7d23-d1cc-4ebd-915e-4b465b494ab3";
		String expectedLocation = "file:///home/noa/Pericles/ModelCompiler/Software/model-compiler/src/test/resources/ermr/communications/DigitalObject.bpmn2";//"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/0000A4EF00180DE4AF2F3AEB5FD949E68617FE5EB6A569E0";

		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			// TODO Error in the ERMR design: this should be 201 Created
			assertEquals(204, response.getStatus());
			response = client.query(repository, SPARQLQuery.createQueryGetImplementationLocation(id));
			assertEquals(200, response.getStatus());
			String location = JSONParser.parseGetImplementationLocationResponse(response);
			assertEquals(expectedLocation, location);
			URL url = new URL(location);			
			Utils.fileContentEquals(url.getFile(), "src/test/resources/ermr/communications/DigitalObject.bpmn2");
			//String content = new Scanner(new File(url.getFile())).useDelimiter("\\Z").next();
			//System.out.println(content);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
