package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class UpdateDigitalObjectTest {

	static String digitalObjectPath;
	static String digitalObject;
	static String mediaType;

	@Test
	public void updateDigitalObject() throws KeyManagementException, NoSuchAlgorithmException, IOException {
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
