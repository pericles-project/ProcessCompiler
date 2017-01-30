package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class GetDigitalObjectTest {
	static String digitalObjectPath;
	static String digitalObject;

	@Test
	public void getDigitalObject() throws IOException, ERMRClientException {
		String service = System.getenv("ERMR_URL");
		Response response = new ERMRClientAPI(service).getDigitalObject(digitalObjectPath);
		System.out.println("Get Digital Object: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(200, response.getStatus());
		assertEquals(FileUtils.readFileToString(new File(digitalObject)), response.readEntity(String.class));
	}
	
	public static void setVariables(String digitalObjectPath2,String digitalObject2) {
		digitalObjectPath = digitalObjectPath2;
		digitalObject = digitalObject2;
	}
}
