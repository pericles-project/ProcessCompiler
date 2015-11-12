package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class DeleteRepositoryTest {
	static String repository;

	@Test
	public void deleteRepository() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().deleteCollection(repository);
		System.out.println("Delete Repository: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setRepository(String repository2) {
		repository = repository2;
	}
}
