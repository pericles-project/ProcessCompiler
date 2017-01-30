package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class DeleteTriplesTest {
	static String repository;

	@Test
	public void deleteTriples() throws ERMRClientException  {
		String service = System.getenv("ERMR_URL");
		Response response = new ERMRClientAPI(service).deleteTriples(repository);
		System.out.println("Delete Triples: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
