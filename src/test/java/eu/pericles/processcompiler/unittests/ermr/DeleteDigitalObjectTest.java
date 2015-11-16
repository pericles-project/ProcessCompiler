package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;

public class DeleteDigitalObjectTest {
	static String digitalObjectPath;

	@Test
	public void deleteDigitalObject() throws KeyManagementException, NoSuchAlgorithmException {
		Response response = new ERMRClientAPI().deleteDigitalObject(digitalObjectPath);
		System.out.println("Delete Digital Object: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String digitalObjectPath2) {
		digitalObjectPath = digitalObjectPath2;
	}
}
