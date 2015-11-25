package eu.pericles.processcompiler.communications.ermr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.AtomicProcess;
import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;

public class JSONParser {
	
	public static Process parseGetProcessAttributesResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		Process process = new Process();
		process.setId(uri);
		process.setName(values.getString(0).replace("\"", ""));
		process.setDescription(values.getString(1).replace("\"", ""));
		process.setVersion(values.getString(2).replace("\"", ""));
		
		return process;
	}

	public static Implementation parseGetImplementationEntityResponse(Response response, String uri) throws Exception {
		JsonArray values = getValues(response).getJsonArray(0);
		
		Implementation implementation = new Implementation();
		implementation.setId(uri);
		implementation.setVersion(values.getString(0).replace("\"", ""));
		implementation.setType(values.getString(1).replace("\"", ""));
		implementation.setLocation(values.getString(2).replace("\"", ""));
		implementation.setFixity(new Fixity());
		implementation.getFixity().setChecksum(values.getString(3).replace("\"", ""));
		implementation.getFixity().setAlgorithm(values.getString(4).replace("\"", ""));
		
		return implementation;
	}
	
	public static InputSlot parseGetInputSlotEntityResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId(uri);
		inputSlot.setName(values.getString(0).replace("\"", ""));
		inputSlot.setDescription(values.getString(1).replace("\"", ""));
		inputSlot.setType(values.getString(2).replace("\"", ""));
		if (values.getString(3).replace("\"", "").equals("true"))
			inputSlot.setOptional(true);
		else
			inputSlot.setOptional(false);
		
		return inputSlot;
	}

	public static List<String> parseGetInputSlotURIListResponse(Response response, String uri) {
		JsonArray values = getValues(response);
		
		List<String> inputSlotURIs = new ArrayList<String>();
		for (int array = 0; array < values.size(); array++)
			inputSlotURIs.add(values.getJsonArray(array).getString(0).replace("\"", ""));
		
		return inputSlotURIs;
	}
	
	public static OutputSlot parseGetOutputSlotEntityResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId(uri);
		outputSlot.setName(values.getString(0).replace("\"", ""));
		outputSlot.setDescription(values.getString(1).replace("\"", ""));
		outputSlot.setType(values.getString(2).replace("\"", ""));
		
		return outputSlot;
	}

	public static List<String> parseGetOutputSlotURIListResponse(Response response, String uri) {
		JsonArray values = getValues(response);
		
		List<String> outputSlotURIs = new ArrayList<String>();
		for (int array = 0; array < values.size(); array++)
			outputSlotURIs.add(values.getJsonArray(array).getString(0).replace("\"", ""));
		
		return outputSlotURIs;
	}


	public static String parseGetURIResponse(Response response) {
		return getValues(response).getString(0).replace("\"", "");
	}
	
	private static JsonArray getValues(Response response) {
		return getJSONObject(response).getJsonArray("values");
	}

	private static JsonObject getJSONObject(Response response) {
		return Json.createReader(response.readEntity(InputStream.class)).readObject();
	}

}
