package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class QueryRepositoryTest {
	static String repository;
	static String query;
	static String expectedResult;

	@Test
	public void query() throws ERMRClientException, IOException  {
		Response response = new ERMRClientAPI().query(repository, query);
		System.out.println("Query: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(expectedResult)), response.readEntity(String.class));
		if (response.getStatus() != 200)
			System.out.println(response);
	}
	
	public static void setVariables(String repository2, String query2, String expectedResult2) {
		repository = repository2;
		query = query2;
		expectedResult = expectedResult2;
	}
}
