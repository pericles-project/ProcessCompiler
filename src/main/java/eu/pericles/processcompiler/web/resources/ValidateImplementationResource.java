package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.BaseException;
import eu.pericles.processcompiler.web.ApiException;

@Path("/validate_implementation")
public class ValidateImplementationResource extends BaseResource {

	public static class ValidateImplementationRequest {
		public String id;
		public String implementation;
		public String ermr;
		public String store;
		public ProcessBase process;
		public BPMNProcess bpmnProcess;
	}

	public static class ValidateImplementationResult {
		public boolean valid;
		public String message;
	}

	@PUT
	public ValidateImplementationResult validateImplementation(ValidateImplementationRequest request) throws ApiException {
		assertEntity(request);
		assertTrue(request.ermr != null, "The 'ermr' field is required.");
		assertTrue(request.store != null, "The 'store' field is required.");
		assertTrue(request.id != null, "The 'id' field is required.");
		assertTrue(request.implementation != null, "The 'implementation' field is required.");

		try {
			ProcessCompiler compiler = new ProcessCompiler(request.ermr);
			if (new File(request.id).exists()) {
				ConfigBean config = parseConfig(new File(request.id));
				request.process = config.process;
			} else
				request.process = compiler.getProcess(request.store, request.id);
			if (new File(request.implementation).exists()) {
				request.bpmnProcess = new BPMNParser().parse(request.implementation);
			} else
				request.bpmnProcess = compiler.getBPMNProcess(request.store, request.implementation);
			
			ValidateImplementationResult result = new ValidateImplementationResult();
			ValidationResult vResult = compiler.validateImplementation(request.process, request.bpmnProcess);
			result.valid = vResult.isValid();
			result.message = "OK\n" + vResult.getMessage();
			return result;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (BaseException e) {
			throw new ApiException(400, e);
		} catch (IOException e) {
			throw new ApiException(400, e);
		}
	}
}
