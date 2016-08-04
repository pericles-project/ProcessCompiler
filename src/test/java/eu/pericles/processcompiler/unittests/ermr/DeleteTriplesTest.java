package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class DeleteTriplesTest {
	static String repository;

	@Test
	public void deleteTriples() throws ERMRClientException  {
		Response response = new ERMRClientAPI().deleteTriples(repository);
		System.out.println("Delete Triples: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
