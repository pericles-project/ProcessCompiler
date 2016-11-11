package eu.pericles.processcompiler.ermr;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class ERMRClientAPI {

	Client client;
	private URI tripleStore = URI.create("https://pericles1:PASSWORD@141.5.100.67/api/triple");
	private URI objectStore = URI.create("https://pericles1:PASSWORD@141.5.100.67/api/cdmi");

	public ERMRClientAPI() throws ERMRClientException {
		client = createClient();
	}

	private Client createClient() throws ERMRClientException {
		try {
			Client client = AllTrustingClient.getAllTrustingClient();
			return client;
		} catch (Exception e) {
			throw new ERMRClientException("Error when creating the ERMR client API: " + e.getMessage());
		}
	}

	public ERMRClientAPI(String service) throws ERMRClientException {
		this.tripleStore = URI.create(service + "/triple");
		this.objectStore = URI.create(service + "/cdmi");

		client = createClient();
	}

	public Builder getTripleStoreBuilder(URI target, String path) {
		return client.target(target).path(path).request();
	}

	public Builder getObjectStoreBuilder(URI target, String path) {
		String[] credentials = target.getUserInfo().split(":");
		return client.register(HttpAuthenticationFeature.basic(credentials[0], credentials[1])).target(target)
				.path(path).request();
	}

	public Client getClient() {
		return client;
	}

	public Response createCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1")
				.put(Entity.entity(collection, new MediaType("application", "cdmi-container")));
	}

	public Response getCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1")
				.accept(new MediaType("application", "cdmi-container")).get();
	}

	public Response deleteCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).delete();
	}

	public Response createRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).header("X-CDMI-Specification-Version", "1.1")
				.put(Entity.entity(repository, new MediaType("application", "cdmi-container")));
	}

	public Response getRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).accept(MediaType.APPLICATION_JSON_TYPE).get();
	}

	public Response deleteRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).delete();
	}

	public Response createDigitalObject(String digitalObjectPath, String digitalObject, String mediaType) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath)
				.put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response deleteDigitalObject(String digitalObjectPath) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).delete();
	}

	public Response getDigitalObject(String digitalObjectPath) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).accept(MediaType.APPLICATION_JSON).get();
	}

	public Response updateDigitalObject(String digitalObjectPath, String digitalObject, String mediaType) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath)
				.put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response addTriples(String repository, String triples, String mediaType) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements")
				.put(Entity.entity(new File(triples), mediaType));
	}

	public Response deleteTriples(String repository) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements").delete();
	}

	public Response getTriples(String repository) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements").accept(MediaType.APPLICATION_JSON).get();
	}

	public Response query(String repository, String query) {
		return getClient().target(tripleStore.toString() + "/" + repository + "?query=" + query).request()
				.accept(MediaType.APPLICATION_JSON).get();
	}

	public List<Map<String, String>> queryRows(String repository, String query) {
		Response response = query(repository, query);
		if (response.getStatus() != 200) {
			return Collections.emptyList();
		}
		InputStream io = response.readEntity(InputStream.class);
		JsonObject node = Json.createReader(io).readObject();
		JsonArray jsonKeys = node.getJsonArray("keys");
		List<Map<String, String>> results = new ArrayList<>(node.getJsonArray("values").size());
		for (JsonArray row : node.getJsonArray("values").getValuesAs(JsonArray.class)) {
			Map<String, String> resultRow = new HashMap<>();
			results.add(resultRow);
			for (int i = 0; i < jsonKeys.size(); i++) {
				String value = row.getString(i);
				if (value.startsWith("\"")) {
					// TODO: Unquote properly
					value = value.substring(1, value.length() - 1);
				}
				resultRow.put(jsonKeys.getString(i), value);
			}
		}
		return results;
	}
}
