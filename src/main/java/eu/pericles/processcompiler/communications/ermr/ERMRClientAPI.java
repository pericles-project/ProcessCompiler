package eu.pericles.processcompiler.communications.ermr;

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
	final static String password = "Per1cles1Pw";
	final static String tripleStore = "https://141.5.102.86/api/triple";
	final static String objectStore = "https://141.5.102.86/api/cdmi";
	
	public ERMRClientAPI() throws KeyManagementException, NoSuchAlgorithmException {
		client = AllTrustingClient.getAllTrustingClientWithCredentials(user, password);
	}
	
	public Builder getBuilder(String target, String path) {
		return client.target(target).path(path).request();
	}
	
	public Client getClient() {
		return client;
	}
	
	public Response createCollection(String collection) {
		String entity = "aa";
		return getBuilder(objectStore, collection).put(Entity.entity(entity, MediaType.APPLICATION_JSON));
	}
	
	public Response getCollection(String collection) {
		return getBuilder(objectStore, collection).get();
	}
	
	public Response deleteCollection(String collection) {
		return getBuilder(objectStore, collection).delete();
	}
	
	public Response createRepository(String collection) {
		return getBuilder(tripleStore, collection).put(null);
	}
	
	public Response getRepository(String repository) {
		return getBuilder(tripleStore, repository).get();
	}
	
	public Response deleteRepository(String repository) {
		return getBuilder(tripleStore, repository).delete();
	}
}
