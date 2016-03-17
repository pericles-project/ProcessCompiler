package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class AddTriplesTest {
	static String repository;
	static String triples;
	static String mediaType;

	@Test
	public void addTriples() throws ERMRClientException   {
		Response response = new ERMRClientAPI().addTriples(repository, triples, mediaType);
		System.out.println("Add Triples: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
		if (response.getStatus() != 201)
			System.out.println(response.readEntity(String.class));
	}
	
	public static void setVariables(String repository2, String triples2, String mediaType2) {
		repository = repository2;
		triples = triples2;
		mediaType = mediaType2;
	}
}
