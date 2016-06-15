package eu.pericles.processcompiler.communications.ermr;

import java.io.File;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class ERMRClientAPI {
	
	Client client;
	private String user = "pericles1";
	private String password = "PASSWORD";
	private String tripleStore = "https://141.5.100.67/api/triple";//"https://141.5.102.86/api/triple";
	private String objectStore = "https://141.5.100.67/api/cdmi";//"https://141.5.102.86/api/cdmi";
	private String findApi = "https://141.5.100.67/api/find";//"https://141.5.102.86/api/find";
	
	public ERMRClientAPI() throws ERMRClientException {
		client = createClient();
	}
	
	private Client createClient() throws ERMRClientException {
		try {
			Client client = AllTrustingClient.getAllTrustingClientWithCredentials(user, password);
			return client;
		} catch (Exception e) {
			throw new ERMRClientException("Error when creating the ERMR client API", e);
		}
	}

	public ERMRClientAPI(HashMap<String, String> parameters) throws ERMRClientException {
		if (parameters.containsKey("user"))
			this.user = parameters.get("user");
		if (parameters.containsKey("password"))
			this.password = parameters.get("password");
		if (parameters.containsKey("tripleStore"))
			this.tripleStore = parameters.get("tripleStore");
		if (parameters.containsKey("objectStore"))
			this.objectStore = parameters.get("objectStore");
		if (parameters.containsKey("findApi"))
			this.findApi = parameters.get("findApi");
		client = createClient();
	}
	
	public Builder getBuilder(String target, String path) {
		return client.target(target).path(path).request();
	}
	
	public Client getClient() {
		return client;
	}

	public Response createCollection(String collection) {
		return getBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1").put(Entity.entity(collection, new MediaType("application", "cdmi-container")));
	}

	public Response getCollection(String collection) {
		return getBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1").accept(new MediaType("application", "cdmi-container")).get();
	}
	
	public Response deleteCollection(String collection) {
		return getBuilder(objectStore, collection).delete();
	}
	
	public Response createRepository(String repository) {
		return getBuilder(tripleStore, repository).header("X-CDMI-Specification-Version", "1.1").put(Entity.entity(repository, new MediaType("application", "cdmi-container")));
	}
	
	public Response getRepository(String repository) {
		return getBuilder(tripleStore, repository).get();
	}
	
	public Response deleteRepository(String repository) {
		return getBuilder(tripleStore, repository).delete();
	}

	public Response createDigitalObject(String digitalObjectPath, String digitalObject, String mediaType){
		return getBuilder(objectStore, digitalObjectPath).put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response deleteDigitalObject(String digitalObjectPath) {
		return getBuilder(objectStore, digitalObjectPath).delete();
	}

	public Response getDigitalObject(String digitalObjectPath) {
		return getBuilder(objectStore, digitalObjectPath).get();
	}

	public Response updateDigitalObject(String digitalObjectPath, String digitalObject, String mediaType) {
		return getBuilder(objectStore, digitalObjectPath).put(Entity.entity(new File(digitalObject), mediaType));
	}

	public Response addTriples(String repository, String triples, String mediaType) {
		return getBuilder(tripleStore, repository + "/statements").put(Entity.entity(new File(triples), mediaType));
	}

	public Response deleteTriples(String repository) {
		return getBuilder(tripleStore, repository + "/statements").delete();
	}

	public Response getTriples(String repository) {
		return getBuilder(tripleStore, repository + "/statements").get();
	}

	public Response find(String term) {
		return getClient().target(findApi + "?findTerms=" + term).request().get();
	}

	public Response query(String repository, String query) {
		return getClient().target(tripleStore + "/" + repository + "?query=" + query).request().get();
	}
}
