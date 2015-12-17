package eu.pericles.processcompiler.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.Slot;

public class DataFlowValidator {
	
	public static final int INITIAL_STEP = 0;
	
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

		for (int sequenceStep = 1; sequenceStep <= getSequenceSteps(); sequenceStep++) {
			if (validateDataConnectionsAtSequenceStep(sequenceStep) == false)
				return false;
		}
		return true;
	}

	private boolean validateDataConnectionsAtSequenceStep(int sequenceStep) throws Exception {
		List<DataConnection> dataConnections = getDataConnectionsAtSequenceStep(sequenceStep);
		for (DataConnection dataConnection : dataConnections) {
			if (validateDataConnection(dataConnection) == false)
				return false;
		}
		updateAvailableResourcesList(sequenceStep);
		return true;
	}

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
		List<String> slots;
		int sequenceStep = dataConnection.getEntryNode().getSequenceStep();
		if (sequenceStep == INITIAL_STEP)
			slots = ermrCommunications.getOutputSlotURIList(getRepository(), getProcess().getId());
		else
			slots = ermrCommunications.getInputSlotURIList(getRepository(), getSubprocessAtSequenceStep(sequenceStep));
		for (String slot : slots) {
			if (dataConnection.getEntryNode().getSlot().equals(slot))
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

	private boolean isDataTypeCompatibleInDataConnection(DataConnection dataConnection) throws UnsupportedEncodingException {
		String dataTypeSlot = ermrCommunications.getDataTypeURI(getRepository(), dataConnection.getEntryNode().getSlot());
		String dataTypeResource = ermrCommunications.getDataTypeURI(getRepository(), dataConnection.getResourceNode().getSlot());
		return isDataTypeCompatible(dataTypeSlot, dataTypeResource);
	}
	
	public boolean isDataTypeCompatible(String dataTypeSlot, String dataTypeResource) throws UnsupportedEncodingException {
		while (dataTypeResource != null) {
			if (dataTypeResource.equals(dataTypeSlot))
				return true;
			dataTypeResource = ermrCommunications.getParentEntityURI(getRepository(), dataTypeResource);
		}
		return false;
	}

	private List<DataConnection> getDataConnectionsAtSequenceStep(int sequenceStep) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : getProcess().getSequence().getDataFlow()) {
			if (isDataConnectionAtSequenceStep(dataConnection, sequenceStep))
				dataConnections.add(dataConnection);
		}
		return dataConnections;
	}

	private boolean isDataConnectionAtSequenceStep(DataConnection dataConnection, int sequenceStep) {
		return (dataConnection.getEntryNode().getSequenceStep() == sequenceStep);
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
