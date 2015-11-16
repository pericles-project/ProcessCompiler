package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class DeleteCollectionTest {
	static String collection;

	@Test
	public void deleteCollection() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().deleteCollection(collection);
		System.out.println("Delete Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String collection2) {
		collection = collection2;
	}

}
