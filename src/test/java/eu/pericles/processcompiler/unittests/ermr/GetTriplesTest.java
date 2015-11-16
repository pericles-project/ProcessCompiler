package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class GetTriplesTest {
	static String repository;
	static String triples;

	@Test
	public void getTriples() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Response response = new ERMRClientAPI().getTriples(repository);
		System.out.println("Get Triples: " + response.getStatus() + " " + response.getStatusInfo());
		//Error in the ERMR design: this should be 201 Created 
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(triples)), response.readEntity(String.class));
	}
	
	public static void setVariables(String repository2, String triples2) {
		repository = repository2;
		triples = triples2;
	}
}
