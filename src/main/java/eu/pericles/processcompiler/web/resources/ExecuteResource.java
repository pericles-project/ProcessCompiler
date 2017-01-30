package eu.pericles.processcompiler.web.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.exceptions.BPMNParserException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.web.ApiException;

@Path("/execute")
@Consumes("application/json")
@Produces("application/json")
public class ExecuteResource extends BaseResource {

	public static class ExecuteRequest extends BaseRequestBean {
		public String id;
		public Map<String, String> parameters = new HashMap<>();
	}

	public static class ExecuteResult {
		public Map<String, String> scripts;
		public String results;
	}

	@POST
	public byte[] execute(ExecuteRequest request) throws ApiException {
		assertEntity(request);
		assertTrue(request.ermr != null, "The 'ermr' field is required.");
		assertTrue(request.store != null, "The 'store' field is required.");
		assertTrue(request.id != null, "The 'id' field is required.");

		try {
			ProcessCompiler compiler = new ProcessCompiler(request.ermr);
			KieHelper kHelper = new KieHelper();
			
			AggregatedProcess ap = compiler.getAggregatedProcess(request.store, request.id);
			Map<String, Object> params = new HashMap<>();
			for(InputSlot slot: ap.getInputSlots()) {
				String dst = compiler.getLocalName(slot.getId());
				if(!request.parameters.containsKey(dst))
					throw new ApiException(400, "Missing paramerer:" + dst);
				String value = request.parameters.remove(dst);
				// TODO: Convert some known types
				params.put(dst, value);
			}

			for (Map.Entry<String, String> entry : compiler.compileRecursively(request.store, request.id).entrySet()) {
				kHelper.addContent(entry.getValue(), ResourceType.BPMN2);
			}

			KieBase kBase = kHelper.build();
			KieSession kieSession = kBase.newKieSession();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PrintStream printer = new PrintStream(buffer);
			kieSession.setGlobal("log", printer);
			kieSession.startProcess(compiler.getLocalName(request.id), params);
			printer.flush();
			return buffer.toByteArray();

		} catch (ERMRClientException e) {
			log.error("/execute", e);			
			throw new ApiException(500, e);
		} catch (JSONParserException e) {
			log.error("/execute", e);			
			throw new ApiException(500, e);
		} catch (PCException e) {
			log.error("/execute", e);			
			throw new ApiException(500, e);
		} catch (BPMNParserException e) {
			log.error("/execute", e);			
			throw new ApiException(500, e);
		} catch (IOException e) {
			log.error("/execute", e);			
			throw new ApiException(500, e);
		} finally {
		}

	}

}
