package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class GetCollectionTest {
	static String collection;

	@Test
	public void getCollection() throws ERMRClientException  {
		String service = System.getenv("ERMR_URL");
		Response response = new ERMRClientAPI(service).getCollection(collection);
		System.out.println("Get Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
	}
	
	public static void setVariables(String collection2) {
		collection = collection2;
	}

}
