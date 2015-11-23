package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.testutils.Utils;

public class GetEntitiesTests {

	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ermr/communications/Ecosystem.txt";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	
	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			// TODO Error in the ERMR design: this should be 201 Created
			assertEquals(204, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getImplementationLocation() {
		String id = "6d4c7d23-d1cc-4ebd-915e-4b465b494ab3";
		String expectedLocation = "file:///home/noa/Pericles/ModelCompiler/Software/model-compiler/src/test/resources/ermr/communications/DigitalObject.bpmn2";//"https://c102-086.cloud.gwdg.de/api/cdmi/cdmi_objectid/0000A4EF00180DE4AF2F3AEB5FD949E68617FE5EB6A569E0";

		try {
			String location = new ERMRCommunications().getImplementationLocation(repository, id);
			assertEquals(expectedLocation, location);
			URL url = new URL(location);			
			Utils.fileContentEquals(url.getFile(), "src/test/resources/ermr/communications/DigitalObject.bpmn2");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void getImplementation() {
		
	}
	
	@Test
	public void getAtomicProcess() {
		
	}
	
	@Test
	public void getAggregatedProcess() {
		
	}
}
