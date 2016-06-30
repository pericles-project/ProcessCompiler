package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

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
		CompileRequest c = new CompileRequest();
		c.ermr = "https://pericles1:PASSWORD@141.5.100.67/api";
		c.store = "NoaCLITest";
		c.id = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
				
		Response r = target("compile").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(c));
		assertEquals(200, r.getStatus());
		CompileResult cr = r.readEntity(CompileResult.class);
		assertNotNull(cr.bpmn2);
	}
}
