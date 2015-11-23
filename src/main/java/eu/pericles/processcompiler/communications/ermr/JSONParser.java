package eu.pericles.processcompiler.communications.ermr;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.Implementation;

public class JSONParser {

	public static String parseGetImplementationLocationResponse(Response response) throws Exception {
		try {
			String location = getJSONObject(response).getJsonArray("values").getJsonArray(0).getString(0);
			return location.replace("\"", "");
		} catch (Exception e) {
			throw new Exception("Response from ERMR is not valid");
		}
	}

	public static Implementation parseGetImplementationEntityResponse(Response response) throws Exception {
		Implementation implementation = new Implementation();
		try {
			String location = getJSONObject(response).getJsonArray("values").getJsonArray(0).getString(0);
			return implementation;
		} catch (Exception e) {
			throw new Exception("Response from ERMR is not valid");
		}
	}

	private static JsonObject getJSONObject(Response response) {
		return Json.createReader(response.readEntity(InputStream.class)).readObject();
	}

}
