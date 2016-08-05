package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class CreateDigitalObjectTest {
	static String digitalObjectPath;
	static String digitalObject;
	static String mediaType;

	@Test
	public void createDigitalObject() throws IOException, ERMRClientException {
		Response response = new ERMRClientAPI().createDigitalObject(digitalObjectPath, digitalObject, mediaType);
		System.out.println("Create Digital Object: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(201, response.getStatus());
	}
	
	public static void setVariables(String digitalObjectPath2, String digitalObject2, String mediaType2) {
		digitalObjectPath = digitalObjectPath2;
		digitalObject = digitalObject2;
		mediaType = mediaType2;
	}
}
