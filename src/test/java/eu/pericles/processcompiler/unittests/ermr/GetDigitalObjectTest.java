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

public class GetDigitalObjectTest {
	static String digitalObjectPath;
	static String digitalObject;

	@Test
	public void getDigitalObject() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Response response = new ERMRClientAPI().getDigitalObject(digitalObjectPath);
		System.out.println("Get Digital Object: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(digitalObject)), response.readEntity(String.class));
	}
	
	public static void setVariables(String digitalObjectPath2,String digitalObject2) {
		digitalObjectPath = digitalObjectPath2;
		digitalObject = digitalObject2;
	}
}
