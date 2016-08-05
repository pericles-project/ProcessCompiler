package eu.pericles.processcompiler.web.resources;

import java.io.IOException;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.web.ApiException;

@Path("/compile")
public class CompilerResource extends BaseResource {

	public static class CompileRequest {
		public String id;
		public String ermr;
		public String store;
		public AggregatedProcess process;
	}

	public static class CompileResult {
		public String bpmn;
	}

	@PUT
	public CompileResult compile(CompileRequest request) throws ApiException {
		assertEntity(request);
		assertTrue(request.ermr != null, "The 'ermr' field is required.");
		assertTrue(request.store != null, "The 'store' field is required.");
		assertTrue(request.id != null, "The 'id' field is required.");

		ProcessCompiler compiler;
		try {
			compiler = new ProcessCompiler(request.ermr);
			if (request.process == null)
				request.process = compiler.getAggregatedProcess(request.store, request.id);
			CompileResult result = new CompileResult();
			result.bpmn = compiler.compile(request.store, request.process);
			return result;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (IOException e) {
			throw new ApiException(400, e);
		} catch (PCException e) {
			throw new ApiException(400, e);
		}
	}
}
