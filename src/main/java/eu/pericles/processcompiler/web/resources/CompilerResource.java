package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.google.inject.Inject;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.BaseException;
import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;
import eu.pericles.processcompiler.web.ApiException;

@Path("/compile")
public class CompilerResource extends BaseResource {
	 


	public static class CompileRequest extends BaseRequestBean {
		public String id;
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

		try {
			ProcessCompiler compiler = new ProcessCompiler(request.ermr);
			if (new File(request.id).exists()) {
				ConfigBean config = parseConfig(new File(request.id));
				request.process = config.aggregatedProcess;
			} else
				request.process = compiler.getAggregatedProcess(request.store, request.id);
			CompileResult result = new CompileResult();
			result.bpmn = compiler.compile(request.store, request.process);
			return result;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (IOException e) {
			throw new ApiException(400, e);
		} catch (BaseException e) {
			throw new ApiException(400, e);
		}
	}
}
