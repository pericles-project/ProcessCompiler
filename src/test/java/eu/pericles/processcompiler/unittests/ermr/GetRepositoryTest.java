package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class GetRepositoryTest {
	static String repository;

	@Test
	public void getRepository() throws ERMRClientException {
		String service = System.getenv("ERMR_URL");
		Response response = new ERMRClientAPI(service).getRepository(repository);
		System.out.println("Get Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		if (response.getStatus() != 200)
			System.out.println(response);
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
