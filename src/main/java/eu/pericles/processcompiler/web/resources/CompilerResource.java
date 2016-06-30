package eu.pericles.processcompiler.web.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.cli.CommandlineInterface.ConfigBean;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.BPMNWriteException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;
import eu.pericles.processcompiler.exceptions.ProcessProcessFlowException;
import eu.pericles.processcompiler.exceptions.ValidationException;
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
		public String bpmn2;
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
			if(request.process == null)
				request.process = compiler.getAggregatedProcess(request.store, request.id);
			BPMNProcess bpmnProcess = compiler.compileAggregatedProcess(request.store, request.process);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			new BPMNWriter().write(bpmnProcess, buffer);
			CompileResult r = new CompileResult();
			r.bpmn2 = buffer.toString();
			return r;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (ValidationException e) {
			throw new ApiException(400, e);
		} catch (BPMNParseException e) {
			throw new ApiException(400, e);
		} catch (ProcessDataFlowException e) {
			throw new ApiException(400, e);
		} catch (ProcessProcessFlowException e) {
			throw new ApiException(400, e);
		} catch (BPMNWriteException e) {
			throw new ApiException(500, e);
		}
	}
}
