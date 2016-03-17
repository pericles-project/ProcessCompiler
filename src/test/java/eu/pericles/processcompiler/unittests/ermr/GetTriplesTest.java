package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class GetTriplesTest {
	static String repository;
	static String triples;

	@Test
	public void getTriples() throws  IOException, ERMRClientException {
		Response response = new ERMRClientAPI().getTriples(repository);
		System.out.println("Get Triples: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(triples)), response.readEntity(String.class));
		if (response.getStatus() != 200)
			System.out.println(response);
	}
	
	public static void setVariables(String repository2, String triples2) {
		repository = repository2;
		triples = triples2;
	}
}
