package eu.pericles.processcompiler.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications.ERMRException;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.Slot;

/**
 * Important concepts about Data Flow in an Aggregated Process:
 * 
 * SEQUENCE STEP: each step in the data flow, identified by the current process
 * --- Initial Step: corresponds to the first step, that is the data at the
 * inputslots of the aggregated process (0)
 * --- Intermediate Steps: correspond to intermediate steps, that is the data at
 * the inputslots of the subprocesses (1, 2, ...), in the same order as in the
 * Process Flow
 * --- Final Step: corresponds to the final step, that is the data at the
 * outputslots of the aggregated process (0)
 * 
 * DATA CONNECTION: data are connected by linking two nodes of the flow
 * (DataFlowNode class)
 * --- SlotNode: this is a slot of the process corresponding to the current
 * sequence step
 * --- ResourceNode: this is a slot from which comes the data
 * 
 * The DataFlow Validator validates the data flow of an aggregated process, that
 * is, validates each of data connections in the data flow.
 * 
 * A Data Connection is VALID if:
 * - the slot node exists in the process
 * - the resource node exists in the process
 * - the resource is available
 * - the data type of the slot and the resource are compatible
 * 
 * The validate() function returns a ValidationResult containing:
 * - when the validation is not valid: a message with the error/cause of
 * invalidation
 * - when the validation is valid: the valid message
 * 
 */

public class DataFlowValidator implements Validator {

	public static final int INITIAL_STEP = 0;
	public static final int FINAL_STEP = 0;

	private String repository;
	private AggregatedProcess process;
	private ERMRCommunications ermrCommunications;
	private ArrayList<DataFlowNode> availableResources;

	public DataFlowValidator(String repository, AggregatedProcess aggregatedProcess) throws ERMRException {
		this.repository = repository;
		this.process = aggregatedProcess;
		this.ermrCommunications = new ERMRCommunications();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		try {
			validateDataFlow();
			result.setMessage(ValidationResult.VALID_MESSAGE);
		} catch (Exception e) {
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * INITIALLY: only inputs of the aggregated process are available
	 * 
	 * FOR EACH SUBPROCESS: validate the data connections for each subprocess in
	 * the sequence and update the list of available resource with the outputs
	 * of the subprocess
	 * 
	 * FINALLY: validate the data connections with the aggregated process
	 * outputs
	 */
	private void validateDataFlow() throws Exception {
		initiateAvailableResourcesList();
		for (int sequenceStep = 1; sequenceStep <= getSequenceSteps(); sequenceStep++) {
			validateDataConnectionsAtSequenceStep(sequenceStep);
			updateAvailableResourcesList(sequenceStep);
		}
		validateDataConnectionsAtSequenceStep(FINAL_STEP);
	}

	private void validateDataConnectionsAtSequenceStep(int sequenceStep) throws Exception {
		List<DataConnection> dataConnections = getDataConnectionsAtSequenceStep(sequenceStep);
		for (DataConnection dataConnection : dataConnections)
			validateDataConnection(dataConnection);
	}

	private void validateDataConnection(DataConnection dataConnection) throws Exception {
		if (existSlotNodeInDataConnection(dataConnection) == false)
			throw new Exception("The slot in data connection doesn't exist");
		if (existResourceNodeInDataConnection(dataConnection) == false)
			throw new Exception("The resource in data connection doesn't exist");
		if (existResourceInDataConnection(dataConnection) == false)
			throw new Exception("The resource in data connection is not available");
		if (isDataTypeCompatibleInDataConnection(dataConnection) == false)
			throw new Exception("The data type in data connection is not compatible");
	}

	private boolean existSlotNodeInDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		List<String> slotNodes = getSlotNodesAtSequenceStep(dataConnection.getSlotNode().getSequenceStep());
		for (String slotNode : slotNodes)
			if (dataConnection.getSlot().equals(slotNode))
				return true;
		return false;
	}

	private boolean existResourceNodeInDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		List<String> resourceNodes = getResourceNodesAtSequenceStep(dataConnection.getResourceNode().getSequenceStep());
		for (String resourceNode : resourceNodes)
			if (dataConnection.getResource().equals(resourceNode))
				return true;
		return false;
	}

	private boolean existResourceInDataConnection(DataConnection dataConnection) {
		for (DataFlowNode resource : getAvailableResources()) {
			if (dataConnection.getResourceNode().equals(resource))
				return true;
		}
		return false;
	}

	/**
	 * The data types are compatible if the resource class is the same class or
	 * a subclass of the slot data type
	 * 
	 * @param dataConnection
	 * @return boolean
	 * @throws UnsupportedEncodingException
	 */
	private boolean isDataTypeCompatibleInDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		String dataTypeSlot = ermrCommunications.getDataTypeURI(getRepository(), dataConnection.getSlot());
		String dataTypeResource = ermrCommunications.getDataTypeURI(getRepository(), dataConnection.getResource());
		return isSubclass(dataTypeSlot, dataTypeResource);
	}

	public boolean isSubclass(String parentClass, String childClass) throws UnsupportedEncodingException {
		while (childClass != null) {
			if (childClass.equals(parentClass))
				return true;
			childClass = ermrCommunications.getParentEntityURI(getRepository(), childClass);
		}
		return false;
	}

	private List<String> getSlotNodesAtSequenceStep(int sequenceStep) throws UnsupportedEncodingException {
		List<String> nodes;
		if (sequenceStep == FINAL_STEP)
			nodes = ermrCommunications.getOutputSlotURIList(getRepository(), getProcess().getId());
		else
			nodes = ermrCommunications.getInputSlotURIList(getRepository(), getSubprocessAtSequenceStep(sequenceStep));
		return nodes;
	}

	private List<String> getResourceNodesAtSequenceStep(int sequenceStep) throws UnsupportedEncodingException {
		List<String> nodes;
		if (sequenceStep == INITIAL_STEP)
			nodes = ermrCommunications.getInputSlotURIList(getRepository(), getProcess().getId());
		else
			nodes = ermrCommunications.getOutputSlotURIList(getRepository(), getSubprocessAtSequenceStep(sequenceStep));
		return nodes;
	}

	private List<DataConnection> getDataConnectionsAtSequenceStep(int sequenceStep) {
		return getProcess().getSequence().getDataConnectionsWithSlotNodeAtSequenceStep(sequenceStep);
	}

	private void initiateAvailableResourcesList() {
		setAvailableResources(new ArrayList<DataFlowNode>());
		for (Slot slot : getProcess().getInputs()) {
			getAvailableResources().add(new DataFlowNode(INITIAL_STEP, slot.getId()));
		}
	}

	private void updateAvailableResourcesList(int sequenceStep) throws UnsupportedEncodingException {
		List<String> slots = ermrCommunications.getOutputSlotURIList(getRepository(), getSubprocessAtSequenceStep(sequenceStep));
		for (String slot : slots) {
			getAvailableResources().add(new DataFlowNode(sequenceStep, slot));
		}
	}

	private String getSubprocessAtSequenceStep(int sequenceStep) {
		return getProcess().getSequence().getProcessFlow().get(sequenceStep - 1);
	}

	private int getSequenceSteps() {
		return getProcess().getSequence().getProcessFlow().size();
	}

	// --------------- GETTERS AND SETTERS ----------------//

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public AggregatedProcess getProcess() {
		return process;
	}

	public void setProcess(AggregatedProcess process) {
		this.process = process;
	}

	public ArrayList<DataFlowNode> getAvailableResources() {
		return availableResources;
	}

	public void setAvailableResources(ArrayList<DataFlowNode> availableResources) {
		this.availableResources = availableResources;
	}

}
