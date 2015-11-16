package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class GetCollectionTest {
	static String collection;

	@Test
	public void getCollection() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().getCollection(collection);
		System.out.println("Get Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
	}
	
	public static void setVariables(String collection2) {
		collection = collection2;
	}

}
