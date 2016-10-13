package eu.pericles.processcompiler.ermr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.JSONParserException;

public class JSONParser {

	// ------- PARSE ENTITIES ------//

	public static ProcessBase parseGetProcessAttributesResponse(Response response, String uri) throws JSONParserException {
		try {
			JsonArray values = getValues(response).getJsonArray(0);

			ProcessBase process = new ProcessBase();
			process.setId(uri);
			process.setName(values.getString(0).replace("\"", ""));
			process.setDescription(values.getString(1).replace("\"", ""));
			process.setVersion(values.getString(2).replace("\"", ""));
			return process;

		} catch (Exception e) {
			throw new JSONParserException("Process " + uri + " is bad defined or missing");
		}
	}

	public static Implementation parseGetImplementationEntityResponse(Response response, String uri) throws JSONParserException {
		try {
			JsonArray values = getValues(response).getJsonArray(0);

			Implementation implementation = new Implementation();
			implementation.setId(uri);
			implementation.setVersion(values.getString(0).replace("\"", ""));
			implementation.setImplementationType(values.getString(1).replace("\"", ""));
			implementation.setLocation(values.getString(2).replace("\"", ""));
			implementation.setChecksum(values.getString(3).replace("\"", ""));			

			return implementation;

		} catch (Exception e) {
			throw new JSONParserException("Implementation " + uri + " is bad defined or missing");
		}
	}

	public static InputSlot parseGetInputSlotEntityResponse(Response response, String uri) throws JSONParserException {
		try {
			JsonArray values = getValues(response).getJsonArray(0);

			InputSlot inputSlot = new InputSlot();
			inputSlot.setId(uri);
			inputSlot.setName(values.getString(0).replace("\"", ""));
			inputSlot.setDescription(values.getString(1).replace("\"", ""));
			inputSlot.setDataType(values.getString(2).replace("\"", ""));
			if (values.getString(3).replace("\"", "").equals("true"))
				inputSlot.setOptional(true);
			else
				inputSlot.setOptional(false);

			return inputSlot;

		} catch (Exception e) {
			throw new JSONParserException("Input slot " + uri + " is bad defined or missing");
		}
	}

	public static OutputSlot parseGetOutputSlotEntityResponse(Response response, String uri) throws JSONParserException {
		try {
			JsonArray values = getValues(response).getJsonArray(0);

			OutputSlot outputSlot = new OutputSlot();
			outputSlot.setId(uri);
			outputSlot.setName(values.getString(0).replace("\"", ""));
			outputSlot.setDescription(values.getString(1).replace("\"", ""));
			outputSlot.setDataType(values.getString(2).replace("\"", ""));

			return outputSlot;

		} catch (Exception e) {
			throw new JSONParserException("Output slot " + uri + " is bad defined or missing");
		}
	}

	public static String parseGetProcessFlowResponse(Response response) {
		return parseGetURIResponse(response);
	}

	public static String parseGetDataFlowResponse(Response response) {
		JsonArray array = getValues(response);
		if (array.isEmpty())
			return null;
		else {
			String dataFlow = array.getJsonArray(0).getString(0);
			return dataFlow.substring(1, dataFlow.length() - 1).replace("\\", "");
		}
	}
	
	public static InputStream parseGetImplementationFile(Response response, String uri) throws JSONParserException {
		
		try {
			InputStream istream = response.readEntity(InputStream.class);
			return istream;
		} catch (Exception e) {
			throw new JSONParserException("Implementation file " + uri + " is bad defined or missing");
		}
	}

	// ---------- PARSE URIs ---------//

	public static List<String> parseGetURIListResponse(Response response, String uri) {
		JsonArray values = getValues(response);

		List<String> uriList = new ArrayList<String>();
		for (int array = 0; array < values.size(); array++)
			uriList.add(values.getJsonArray(array).getString(0).replace("\"", ""));

		return uriList;
	}

	// TODO Manage errors and empty responses as corresponded
	public static String parseGetURIResponse(Response response) {
		JsonArray array = getValues(response);
		if (array.isEmpty())
			return null;
		else
			return array.getJsonArray(0).getString(0).replace("\"", "");
	}

	// -------- PRIVATE FUNCTIONS -------//

	private static JsonArray getValues(Response response) {
		return getJSONObject(response).getJsonArray("values");
	}

	private static JsonObject getJSONObject(Response response) {
		String json = response.readEntity(String.class);
		return Json.createReader(new ByteArrayInputStream(json.getBytes())).readObject();
	}

}
