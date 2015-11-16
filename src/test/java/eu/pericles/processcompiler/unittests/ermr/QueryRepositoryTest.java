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

public class QueryRepositoryTest {
	static String repository;
	static String query;
	static String expectedResult;

	@Test
	public void query() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Response response = new ERMRClientAPI().query(repository, query);
		System.out.println("Query: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(expectedResult)), response.readEntity(String.class));
	}
	
	public static void setVariables(String repository2, String query2, String expectedResult2) {
		repository = repository2;
		query = query2;
		expectedResult = expectedResult2;
	}
}
