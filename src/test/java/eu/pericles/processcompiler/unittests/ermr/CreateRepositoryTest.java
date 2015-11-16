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
		Response response = new ERMRClientAPI().createRepository(repository);
		System.out.println("Create Repository: " + response.getStatus() + " " + response.getStatusInfo());
		//Error in the ERMR design: this should be 201 Created 
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String repository2) {
		repository = repository2;
	}
}
