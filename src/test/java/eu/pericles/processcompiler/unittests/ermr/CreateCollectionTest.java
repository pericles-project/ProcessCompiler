package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class CreateCollectionTest {
	static String collection;

	@Test
	public void createCollection() throws ERMRClientException {
		Response response = new ERMRClientAPI().createCollection(collection);
		System.out.println("Create Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setVariables(String collection2) {
		collection = collection2;
	}

}
