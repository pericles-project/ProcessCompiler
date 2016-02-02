package eu.pericles.processcompiler.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.Slot;

/**
 * Three important concepts about Data Flow in an Aggregated Process:
 * 
 * Sequence Step: each step in the data flow, identified by the current process
 * 
 * - INITIAL_STEP: corresponds to the first step, that is the data at the
 * inputslots of the aggregated process (0)
 * 
 * - intermediate steps: correspond to intermediate steps, that is the data at
 * the inputslots of the subprocesses (1, 2, ...), in the same order as in the
 * Process Flow
 * 
 * - FINAL_STEP: corresponds to the final step, that is the data at the
 * outputslots of the aggregated process (0)
 * 
 * Data Connection: data are connected by linking two nodes of the flow
 * (DataFlowNode)
 * 
 * - SlotNode: this is a slot of the process corresponding to the current
 * sequence step
 * 
 * - ResourceNode: this is a slot from which comes the data
 * 
 * In a DataConnection:
 * 
 * - Slot = Slot of the SlotNode
 * 
 * - Resource = Slot of the ResourceNode
 * 
 * - SequenceStep = Sequence Step of the SlotNode
 */

public class DataFlowValidator {

	public static final int INITIAL_STEP = 0;
	public static final int FINAL_STEP = 0;

	private String repository;
	private AggregatedProcess process;
	private ERMRCommunications ermrCommunications;
	private ArrayList<DataFlowNode> availableResources;

	public DataFlowValidator(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		this.repository = repository;
		this.process = aggregatedProcess;
		this.ermrCommunications = new ERMRCommunications();
	}

	public boolean validateDataFlow() throws Exception {
		// At the beginning, only inputs of the aggregated process are available
		initiateAvailableResourcesList();

		// Validate the data connections for each subprocess in the sequence and
		// update the list of available resource with the outputs of the
		// subprocess
		for (int sequenceStep = 1; sequenceStep <= getSequenceSteps(); sequenceStep++) {
			if (validateDataConnectionsAtSequenceStep(sequenceStep) == false)
				return false;
			updateAvailableResourcesList(sequenceStep);
		}
		// Validate the data connections with the aggregated process outputs
		if (validateDataConnectionsAtSequenceStep(FINAL_STEP) == false)
			return false;

		return true;
	}

	private boolean validateDataConnectionsAtSequenceStep(int sequenceStep) throws Exception {
		List<DataConnection> dataConnections = getDataConnectionsAtSequenceStep(sequenceStep);
		for (DataConnection dataConnection : dataConnections) {
			if (validateDataConnection(dataConnection) == false)
				return false;
		}
		return true;
	}

	/** 
	 * A Data Connection is valid if:
	 *  - the slot exists in the process
	 *  - the resource is available
	 *  - the data type of the slot and the resource are compatible
	 */
	private boolean validateDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		if (existSlotInDataConnection(dataConnection) == false)
			return false;
		if (existResourceInDataConnection(dataConnection) == false)
			return false;
		if (isDataTypeCompatibleInDataConnection(dataConnection) == false)
			return false;
		return true;
	}

	private boolean existSlotInDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		List<String> slots = getSlotsAtSequenceStep(dataConnection.getSequenceStep());
		for (String slot : slots) {
			if (dataConnection.getSlot().equals(slot))
				return true;
		}
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
	 * The data types are compatible if the resource class is the same
	 * class or a subclass of the slot data type
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
	
	private List<String> getSlotsAtSequenceStep(int sequenceStep) throws UnsupportedEncodingException {
		List<String> slots;
		if (sequenceStep == FINAL_STEP)
			slots = ermrCommunications.getOutputSlotURIList(getRepository(), getProcess().getId());
		else
			slots = ermrCommunications.getInputSlotURIList(getRepository(), getSubprocessAtSequenceStep(sequenceStep));
		return slots;
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
