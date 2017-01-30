package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class CreateRepositoryTest {
	static String repository;

	@Test
	public void createRepository() throws ERMRClientException  {
		String service = System.getenv("ERMR_URL");
		Response response = new ERMRClientAPI(service).createRepository(repository);
		System.out.println("Create Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
