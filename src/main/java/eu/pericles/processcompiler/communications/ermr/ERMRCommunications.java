package eu.pericles.processcompiler.communications.ermr;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.Implementation;

public class ERMRCommunications {
	
	private ERMRClientAPI client;
	
	public ERMRCommunications() throws KeyManagementException, NoSuchAlgorithmException {
		client = new ERMRClientAPI();
	}
	
	public Implementation getImplementationEntity(String repository, String id) throws Exception {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationEntity(id));
		return JSONParser.parseGetImplementationEntityResponse(response);
	}
	
	public String getImplementationLocation(String repository, String processID) throws Exception {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationLocation(processID));
		return JSONParser.parseGetImplementationLocationResponse(response);
	}

	public ERMRClientAPI getClient() {
		return client;
	}

}
