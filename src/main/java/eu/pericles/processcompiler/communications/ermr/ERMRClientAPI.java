package eu.pericles.processcompiler.communications.ermr;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ERMRClientAPI {
	
	Client client;
	final static String user = "pericles1";
	final static String password = "PASSWORD";
	final static String tripleStore = "https://141.5.102.86/api/triple";
	final static String objectStore = "https://141.5.102.86/api/cdmi";
	final static String findApi = "https://141.5.102.86/api/find";
	
	public ERMRClientAPI() throws KeyManagementException, NoSuchAlgorithmException {
		client = AllTrustingClient.getAllTrustingClientWithCredentials(user, password);
	}
	
	public Builder getBuilder(String target, String path) {
		return client.target(target).path(path).request();
	}
	
	public Client getClient() {
		return client;
	}
	//TODO this doesn't work! We need to find a way to create a folder/container/whatever via put() method
	public Response createCollection(String collection) {
		return getBuilder(objectStore, collection).put(Entity.entity(collection, MediaType.APPLICATION_JSON));
	}
	
	public Response getCollection(String collection) {
		return getBuilder(objectStore, collection).header("X-CDMI-Specification-Version", "1.1").get();
		//return client.target("https://141.5.102.86/api/cdmi").path("NoaCollection/").request().header("X-CDMI-Specification-Version", "1.1").get();
	}
	
	public Response deleteCollection(String collection) {
		return getBuilder(objectStore, collection).delete();
	}
	
	//TODO this doesn't work! We need to find a way to create a folder/container/whatever via put() method
	public Response createRepository(String repository) {
		return getBuilder(tripleStore, repository).put(Entity.entity(repository, MediaType.APPLICATION_JSON));
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
