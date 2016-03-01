package eu.pericles.processcompiler.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
	/**
	 * - processFlow: list of strings, where each String represents the id of a
	 * process entity (URI). It represents the process flow, not only the
	 * subprocesses used to perform a function. Therefore, a process entity can
	 * be more than once in the list, i.e. we could want to use the process
	 * "Save file" more than once.
	 * 
	 * A Process Flow in a model is specified as a String with the URIs of the
	 * subprocesses in execution order and separated by a space, i.e.:
	 * "<http://www.pericles-project.eu/ns/ecosystem#atpVirusCheck> <http://www.pericles-project.eu/ns/ecosystem#atpExtractMD> <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>"
	 * 
	 * - dataFlow: list of data connections, where each DataConnection represents
	 * the connection between two DataFlowNode:
	 * 
	 * --- slotNode: target, to where the data flows. That is, output slots of the
	 * aggregated process, and input slots of sequence processes (subprocesses).
	 * The slot node is unique, it cannot appear more than once in the data
	 * flow.
	 * 
	 * --- resourceNode: source, from where the data is coming. That is, input
	 * slots of the aggregated process, and output slots of sequence processes
	 * (subprocesses). A resource node can appear more than once in the data
	 * flow.
	 * 
	 * A Data Flow in a model is specified as a String with the DataConnections
	 * inside {} and separated by a space. The DataConnection is specified as
	 * two DataFlowNodes (slot node and resource node) inside [] and separated
	 * by a space. The DataFlowNode is specified by an integer (the sequence
	 * step, where 0 represents the aggregated process, 1 represents the first
	 * subprocess, 2 - second subprocess, etc.) and a string (the input or
	 * output slot URI), i.e.:
	 * "{[1 <#isVirusCheckDM>] [0 <#isIngestAWSWAW>]} {[2 <#isExtractMDDM>] [0 <#isIngestAWSWAW>]} {[3 <#isEncapsulateDOMDDO>] [0 <#isIngestAWSWAW>]}"
	 * +
	 * " {[3 <#isEncapsulateDOMDPF>] [0 <#isIngestAWSWPF>]} {[3 <#isEncapsulateDOMDMD>] [2 <#osExtractMDMD>]} {[0 <#osIngestAWSWP>] [3 <#osEncapsulateDOMDP>]}"
	 * ));
	 * 
	 * DataFlow = "{DataConnection} {DataConnection}"
	 * DataConnection = "[SlotNode ResourceNode] [SlotNode ResourceNode]"
	 * DataFlowNode = "SequenceStep ProcessSlot"
	 */

	private List<String> processFlow;
	private List<DataConnection> dataFlow;

	// --------------- GETTERS AND SETTERS ----------------//

	public List<String> getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(List<String> processFlow) {
		this.processFlow = processFlow;
	}

	public List<DataConnection> getDataFlow() {
		return dataFlow;
	}

	public void setDataFlow(List<DataConnection> dataFlow) {
		this.dataFlow = dataFlow;
	}

	public DataConnection findDataConnectionBySlot(DataFlowNode dataFlowNode) {
		for (DataConnection dataConnection : getDataFlow())
			if (dataConnection.getSlotNode().equals(dataFlowNode))
				return dataConnection;
		return null;
	}
	
	public List<DataConnection> findDataConnectionsByResource(DataFlowNode dataFlowNode) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : getDataFlow())
			if (dataConnection.getResourceNode().equals(dataFlowNode))
				dataConnections.add(dataConnection);
		return dataConnections;
	}
	
	public List<DataConnection> getDataConnectionsWithSlotNodeAtSequenceStep(int sequenceStep) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : this.getDataFlow()) {
			if (isDataConnectionWithSlotNodeAtSequenceStep(dataConnection, sequenceStep))
				dataConnections.add(dataConnection);
		}
		return dataConnections;
	}
	
	public boolean isDataConnectionWithSlotNodeAtSequenceStep(DataConnection dataConnection, int sequenceStep) {
		return (dataConnection.getSlotNode().getSequenceStep() == sequenceStep);
	}
	
	public List<DataConnection> getDataConnectionsWithResourceNodeAtSequenceStep(int sequenceStep) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : this.getDataFlow()) {
			if (isDataConnectionWithResourceNodeAtSequenceStep(dataConnection, sequenceStep))
				dataConnections.add(dataConnection);
		}
		return dataConnections;
	}

	private boolean isDataConnectionWithResourceNodeAtSequenceStep(DataConnection dataConnection, int sequenceStep) {
		return (dataConnection.getResourceNode().getSequenceStep() == sequenceStep);
	}

	// --------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataFlow == null) ? 0 : dataFlow.hashCode());
		result = prime * result + ((processFlow == null) ? 0 : processFlow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sequence other = (Sequence) obj;
		if (dataFlow == null) {
			if (other.dataFlow != null)
				return false;
		} else if (!dataFlow.equals(other.dataFlow))
			return false;
		if (processFlow == null) {
			if (other.processFlow != null)
				return false;
		} else if (!processFlow.equals(other.processFlow))
			return false;
		return true;
	}

}
