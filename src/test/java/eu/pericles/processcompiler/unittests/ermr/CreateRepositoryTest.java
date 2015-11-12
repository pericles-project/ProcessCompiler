package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class CreateRepositoryTest {
	static String repository;

	@Test
	public void createRepository() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().createCollection(repository);
		System.out.println("Create Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setRepository(String repository2) {
		repository = repository2;
	}
}
