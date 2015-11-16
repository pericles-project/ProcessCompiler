package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class QueryRepositoryTest {
	static String collection;

	@Test
	public void createCollection() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().createCollection(collection);
		System.out.println("Create Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setCollection(String collection2) {
		collection = collection2;
	}

}
