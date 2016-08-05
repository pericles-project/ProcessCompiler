package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class UpdateDigitalObjectTest {

	static String digitalObjectPath;
	static String digitalObject;
	static String mediaType;

	@Test
	public void updateDigitalObject() throws ERMRClientException  {
		Response response = new ERMRClientAPI().updateDigitalObject(digitalObjectPath, digitalObject, mediaType);
		System.out.println("Update Digital Object: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String digitalObjectPath2, String digitalObject2, String mediaType2) {
		digitalObjectPath = digitalObjectPath2;
		digitalObject = digitalObject2;
		mediaType = mediaType2;
	}
}
