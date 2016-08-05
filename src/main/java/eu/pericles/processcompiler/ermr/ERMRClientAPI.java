package eu.pericles.processcompiler.ermr;

import java.io.File;
import java.net.URI;

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
			throw new ERMRClientException("Error when creating the ERMR client API", e);
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
		return client.register(HttpAuthenticationFeature.basic(credentials[0], credentials[1])).target(target).path(path).request();
	}

	public Client getClient() {
		return client;
	}

	public Response createCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1").put(
				Entity.entity(collection, new MediaType("application", "cdmi-container")));
	}

	public Response getCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1")
				.accept(new MediaType("application", "cdmi-container")).get();
	}

	public Response deleteCollection(String collection) {
		return getObjectStoreBuilder(objectStore, collection).delete();
	}

	public Response createRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).header("X-CDMI-Specification-Version", "1.1").put(
				Entity.entity(repository, new MediaType("application", "cdmi-container")));
	}

	public Response getRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).accept(MediaType.APPLICATION_JSON_TYPE).get();
	}

	public Response deleteRepository(String repository) {
		return getTripleStoreBuilder(tripleStore, repository).delete();
	}

	public Response createDigitalObject(String digitalObjectPath, String digitalObject, String mediaType) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response deleteDigitalObject(String digitalObjectPath) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).delete();
	}

	public Response getDigitalObject(String digitalObjectPath) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).accept(MediaType.APPLICATION_JSON).get();
	}

	public Response updateDigitalObject(String digitalObjectPath, String digitalObject, String mediaType) {
		return getObjectStoreBuilder(objectStore, digitalObjectPath).put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response addTriples(String repository, String triples, String mediaType) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements").put(Entity.entity(new File(triples), mediaType));
	}

	public Response deleteTriples(String repository) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements").delete();
	}

	public Response getTriples(String repository) {
		return getTripleStoreBuilder(tripleStore, repository + "/statements").accept(MediaType.APPLICATION_JSON).get();
	}

	public Response query(String repository, String query) {
		return getClient().target(tripleStore.toString() + "/" + repository + "?query=" + query).request().accept(MediaType.APPLICATION_JSON).get();
	}
}
