package eu.pericles.processcompiler.ng.ermr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ecosystem.Implementation;
import eu.pericles.processcompiler.ng.ecosystem.InputSlot;
import eu.pericles.processcompiler.ng.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ng.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class ERMRCommunications {

	private ERMRClientAPI client;

	public ERMRCommunications() throws ERMRClientException {
		client = new ERMRClientAPI();
	}

	public ERMRCommunications(String service) throws ERMRClientException {
		client = new ERMRClientAPI(service);
	}

	public ERMRClientAPI getClient() {
		return client;
	}

	// ------- GET ENTITIES ---------//

	public AggregatedProcess getAggregatedProcessEntity(String repository, String uri) throws ERMRClientException {
		AggregatedProcess aggregatedProcess = new AggregatedProcess(getProcessEntity(repository, uri));
		aggregatedProcess.setProcessFlow(getProcessFlow(repository, uri));
		aggregatedProcess.setDataFlow(getDataFlow(repository, uri));
		return aggregatedProcess;
	}

	public String getProcessFlow(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessFlow(uri));
		return JSONParser.parseGetProcessFlowResponse(response);
	}

	public String getDataFlow(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetDataFlow(uri));
		return JSONParser.parseGetDataFlowResponse(response);
	}

	public ProcessBase getProcessEntity(String repository, String uri) throws ERMRClientException {
		ProcessBase process = getProcessAttributes(repository, uri);
		process.setInputSlots(getProcessInputSlots(repository, uri));
		process.setOutputSlots(getProcessOutputSlots(repository, uri));
		if (getImplementationURI(repository, uri) != null)
			process.setImplementation(getProcessImplementation(repository, uri));

		return process;
	}

	public ProcessBase getProcessAttributes(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessAttributes(uri));
		return JSONParser.parseGetProcessAttributesResponse(response, uri);
	}

	public List<InputSlot> getProcessInputSlots(String repository, String uri) throws ERMRClientException {
		List<InputSlot> inputSlots = new ArrayList<InputSlot>();
		for (String inputSlotURI : getInputSlotURIList(repository, uri))
			inputSlots.add(getInputSlotEntity(repository, inputSlotURI));
		return inputSlots;
	}

	public InputSlot getInputSlotEntity(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotEntity(uri));
		return JSONParser.parseGetInputSlotEntityResponse(response, uri);
	}

	public List<OutputSlot> getProcessOutputSlots(String repository, String uri) throws ERMRClientException {
		List<OutputSlot> outputSlots = new ArrayList<OutputSlot>();
		for (String outputSlotURI : getOutputSlotURIList(repository, uri))
			outputSlots.add(getOutputSlotEntity(repository, outputSlotURI));
		return outputSlots;
	}

	public OutputSlot getOutputSlotEntity(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotEntity(uri));
		return JSONParser.parseGetOutputSlotEntityResponse(response, uri);
	}

	public Implementation getProcessImplementation(String repository, String uri) throws ERMRClientException {
		return getImplementationEntity(repository, getImplementationURI(repository, uri));
	}

	public Implementation getImplementationEntity(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationEntity(uri));
		return JSONParser.parseGetImplementationEntityResponse(response, uri);
	}

	public InputStream getProcessImplementationFile(String repository, String uri) throws ERMRClientException {
		return getImplementationFile(getProcessImplementation(repository, uri).getLocation());
	}

	public InputStream getImplementationFile(String uri) {
		return client.getDigitalObject(uri).readEntity(InputStream.class);
	}

	// ---------- GET URIs ------------//

	public List<String> getInputSlotURIList(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(response, uri);
	}

	public List<String> getOutputSlotURIList(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(response, uri);
	}

	public String getImplementationURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

	public String getSequenceURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetSequenceURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

	public String getParentEntityURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetParentEntityURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

	public String getEntityTypeURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetEntityTypeURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

	public String getDataTypeURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetDataTypeURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}
	
	// ---------- CHECK FUNCTIONS ------------//
	
	public boolean isSubclass(String repository, String parentClass, String childClass) throws ERMRClientException {
		while (childClass != null) {
			if (childClass.equals(parentClass))
				return true;
			childClass = getParentEntityURI(repository, childClass);
		}
		return false;
	}

}
