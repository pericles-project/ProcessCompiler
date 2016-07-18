package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.PrintWriter;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import eu.pericles.processcompiler.testutils.Utils;
import eu.pericles.processcompiler.web.ApiApplication;
import eu.pericles.processcompiler.web.resources.CompilerResource.CompileRequest;
import eu.pericles.processcompiler.web.resources.CompilerResource.CompileResult;

public class WebComponentTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ApiApplication();
	}

	@Test
	public void testTest() {
		CompileRequest compileRequest = new CompileRequest();
		compileRequest.ermr = "https://pericles1:PASSWORD@141.5.100.67/api";
		compileRequest.store = "NoaCLITest";
		compileRequest.id = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";

		Response response = target("compile").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(compileRequest));
		assertEquals(200, response.getStatus());
		CompileResult compileResult = response.readEntity(CompileResult.class);
		assertNotNull(compileResult.bpmn2);
		String outFile = "src/test/resources/web/out.bpmn2";
		String testFile = "src/test/resources/web/test.bpmn2";
		try (PrintWriter out = new PrintWriter(outFile)) {
			out.println(compileResult.bpmn2);
			out.close();
			Utils.fileContentEquals(outFile, testFile);
		} catch (Exception e) {
			fail("Error when writing");
		}

	}
}
