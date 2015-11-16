package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class GetRepositoryTest {
	static String repository;

	@Test
	public void getRepository() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().getRepository(repository);
		System.out.println("Get Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
