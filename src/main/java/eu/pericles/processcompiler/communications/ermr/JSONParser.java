package eu.pericles.processcompiler.communications.ermr;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

public class JSONParser {

	public static String parseGetImplementationLocationResponse(Response response) throws Exception {
		JsonObject jsonObject = Json.createReader(response.readEntity(InputStream.class)).readObject();

		if (isValidGetImplementationLocationResponse(jsonObject)) {
			String location = jsonObject.getJsonArray("values").getJsonArray(0).getString(0);
			return location.replace("\"", "");
		} else {
			throw new Exception("Response not valid");
		}
	}

	private static boolean isValidGetImplementationLocationResponse(JsonObject jsonObject) {
		if (jsonObject.getJsonArray("names").size() != 1)
			return false;
		if (!jsonObject.getJsonArray("names").getString(0).equals("location"))
		 return false;
		if (jsonObject.getJsonArray("values").size() != 1)
			return false;
		return true;
	}

}
