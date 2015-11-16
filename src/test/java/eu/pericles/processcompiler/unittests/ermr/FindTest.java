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

public class FindTest {
	static String term;
	static String expectedResult;

	@Test
	public void find() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Response response = new ERMRClientAPI().find(term);
		System.out.println("Find: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(expectedResult)), response.readEntity(String.class));
	}
	
	public static void setVaribles(String term2, String expectedResult2) {
		term = term2;
		expectedResult = expectedResult2;
	}

}
