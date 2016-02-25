package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class AddTriplesTest {
	static String repository;
	static String triples;
	static String mediaType;

	@Test
	public void addTriples() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().addTriples(repository, triples, mediaType);
		System.out.println("Add Triples: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setVariables(String repository2, String triples2, String mediaType2) {
		repository = repository2;
		triples = triples2;
		mediaType = mediaType2;
	}
}
