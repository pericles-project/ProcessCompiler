package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class CreateRepositoryTest {
	static String repository;

	@Test
	public void createRepository() throws ERMRClientException  {
		Response response = new ERMRClientAPI().createRepository(repository);
		System.out.println("Create Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
