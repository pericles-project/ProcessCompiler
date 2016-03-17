package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class FindTest {
	static String term;
	static String expectedResult;

	@Test
	public void find() throws  IOException, ERMRClientException {
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
