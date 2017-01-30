package eu.pericles.processcompiler.ermr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;

public class ERMRCommunications implements ERMRComm {

	private ERMRClientAPI client;

	public ERMRCommunications(String service) throws ERMRClientException {
		client = new ERMRClientAPI(service);
	}

	public ERMRCommunications(ERMRClientAPI client) {
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getClient()
	 */
	@Override
	public ERMRClientAPI getClient() {
		return client;
	}

	// ------- GET ENTITIES ---------//

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getAggregatedProcessEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public AggregatedProcess getAggregatedProcessEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		AggregatedProcess aggregatedProcess = new AggregatedProcess(getProcessEntity(repository, uri));
		aggregatedProcess.setProcessFlow(getProcessFlow(repository, uri));
		aggregatedProcess.setDataFlow(getDataFlow(repository, uri));
		return aggregatedProcess;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessFlow(java.lang.String, java.lang.String)
	 */
	@Override
	public String getProcessFlow(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessFlow(uri));
		return JSONParser.parseGetProcessFlowResponse(validateResponse(response));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getDataFlow(java.lang.String, java.lang.String)
	 */
	@Override
	public String getDataFlow(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetDataFlow(uri));
		return JSONParser.parseGetDataFlowResponse(validateResponse(response));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public ProcessBase getProcessEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		ProcessBase process = getProcessAttributes(repository, uri);
		process.setInputSlots(getProcessInputSlots(repository, uri));
		process.setOutputSlots(getProcessOutputSlots(repository, uri));
		if (getImplementationURI(repository, uri) != null)
			process.setImplementation(getProcessImplementation(repository, uri));

		return process;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessAttributes(java.lang.String, java.lang.String)
	 */
	@Override
	public ProcessBase getProcessAttributes(String repository, String uri) throws ERMRClientException, JSONParserException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessAttributes(uri));
		return JSONParser.parseGetProcessAttributesResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessInputSlots(java.lang.String, java.lang.String)
	 */
	@Override
	public List<InputSlot> getProcessInputSlots(String repository, String uri) throws ERMRClientException, JSONParserException {
		List<InputSlot> inputSlots = new ArrayList<InputSlot>();
		for (String inputSlotURI : getInputSlotURIList(repository, uri))
			inputSlots.add(getInputSlotEntity(repository, inputSlotURI));
		return inputSlots;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getInputSlotEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSlot getInputSlotEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotEntity(uri));
		return JSONParser.parseGetInputSlotEntityResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessOutputSlots(java.lang.String, java.lang.String)
	 */
	@Override
	public List<OutputSlot> getProcessOutputSlots(String repository, String uri) throws ERMRClientException, JSONParserException {
		List<OutputSlot> outputSlots = new ArrayList<OutputSlot>();
		for (String outputSlotURI : getOutputSlotURIList(repository, uri))
			outputSlots.add(getOutputSlotEntity(repository, outputSlotURI));
		return outputSlots;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getOutputSlotEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputSlot getOutputSlotEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotEntity(uri));
		return JSONParser.parseGetOutputSlotEntityResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessImplementation(java.lang.String, java.lang.String)
	 */
	@Override
	public Implementation getProcessImplementation(String repository, String uri) throws ERMRClientException, JSONParserException {
		return getImplementationEntity(repository, getImplementationURI(repository, uri));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getImplementationEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public Implementation getImplementationEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationEntity(uri));
		return JSONParser.parseGetImplementationEntityResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getProcessImplementationFile(java.lang.String, java.lang.String)
	 */
	@Override
	public InputStream getProcessImplementationFile(String repository, String uri) throws ERMRClientException, JSONParserException {
		return getImplementationFile(getProcessImplementation(repository, uri).getLocation());
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getImplementationFile(java.lang.String)
	 */
	@Override
	public InputStream getImplementationFile(String uri) throws ERMRClientException, JSONParserException {
		Response response = client.getDigitalObject(uri);
		return JSONParser.parseGetImplementationFile(validateResponse(response), uri);
	}

	// ---------- GET URIs ------------//

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getInputSlotURIList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getInputSlotURIList(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getOutputSlotURIList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getOutputSlotURIList(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(validateResponse(response), uri);
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getImplementationURI(java.lang.String, java.lang.String)
	 */
	@Override
	public String getImplementationURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationURI(uri));
		return JSONParser.parseGetURIResponse(validateResponse(response));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getParentEntityURI(java.lang.String, java.lang.String)
	 */
	@Override
	public String getParentEntityURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetParentEntityURI(uri));
		return JSONParser.parseGetURIResponse(validateResponse(response));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getEntityTypeURI(java.lang.String, java.lang.String)
	 */
	@Override
	public String getEntityTypeURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetEntityTypeURI(uri));
		return JSONParser.parseGetURIResponse(validateResponse(response));
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#getSlotDataTypeURI(java.lang.String, java.lang.String)
	 */
	@Override
	public String getSlotDataTypeURI(String repository, String uri) throws ERMRClientException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetSlotDataTypeURI(uri));
		return JSONParser.parseGetURIResponse(validateResponse(response));
	}

	// ---------- CHECK FUNCTIONS ------------//

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#isSubclass(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSubclass(String repository, String parentClass, String childClass) throws ERMRClientException {
		while (childClass != null) {
			if (childClass.equals(parentClass))
				return true;
			childClass = getParentEntityURI(repository, childClass);
		}
		return false;
	}

	private Response validateResponse(Response response) throws ERMRClientException {
		if (response.getStatus() != 200)
			throw new ERMRClientException(Integer.toString(response.getStatus()) + " " + response.getStatusInfo());
		return response;
	}

	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#isAggregatedProcess(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isAggregatedProcess(String repository, String processId) throws ERMRClientException {
		return getEntityTypeURI(repository, processId).contains("AggregatedProcess");
	}
	
	/* (non-Javadoc)
	 * @see eu.pericles.processcompiler.ermr.ERMRComm#existsEntity(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean existsEntity(String repository, String entityId) {
		try {
			if (getEntityTypeURI(repository, entityId) != null)
				return true;
			else
				return false;
		} catch (ERMRClientException e) {
			return false;
		}
	}

}
