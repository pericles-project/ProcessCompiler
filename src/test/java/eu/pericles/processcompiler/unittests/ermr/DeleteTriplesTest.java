package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class DeleteTriplesTest {
	static String repository;

	@Test
	public void deleteTriples() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().deleteTriples(repository);
		System.out.println("Delete Triples: " + response.getStatus() + " " + response.getStatusInfo());
		//TODO Error in the ERMR design: this should be 204 NO CONTENT
		assertEquals(200, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
