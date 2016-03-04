package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import eu.pericles.processcompiler.core.ProcessAggregator.SequenceSubprocess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;

public class DataConnectionsHandler {
	
	public static HashMap<DataFlowNode, Object> getIndividualInputConnections(List<SequenceSubprocess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> inputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			SequenceSubprocess subprocess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, sequenceStep);
			for (Entry<InputSlot, Object> inputConnection : subprocess.getInputConnections().entrySet())
				inputConnections.put(new DataFlowNode(sequenceStep, inputConnection.getKey().getId()), inputConnection.getValue());
		}
		return inputConnections;
	}
	
	public static HashMap<DataFlowNode, Object> getIndividualOutputConnections(List<SequenceSubprocess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> dataOutputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			SequenceSubprocess subprocess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, sequenceStep);
			for (Entry<OutputSlot, Object> outputConnection : subprocess.getOutputConnections().entrySet())
				dataOutputConnections.put(new DataFlowNode(sequenceStep, outputConnection.getKey().getId()), outputConnection.getValue());
		}
		return dataOutputConnections;
	}
	
	public static HashMap<DataFlowNode, Object> getAggregatedConnections(List<DataConnection> dataFlow, List<SequenceSubprocess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> aggregatedConnections = new HashMap<DataFlowNode, Object>();
		HashMap<DataFlowNode, Object> availableResources = new HashMap<DataFlowNode, Object>();
		HashMap<DataFlowNode, Object> individualInputConnections = getIndividualInputConnections(sequenceSubprocesses);
		HashMap<DataFlowNode, Object> individualOutputConnections = getIndividualOutputConnections(sequenceSubprocesses);
		
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			List<DataConnection> dataFlowConnections = getDataFlowConnectionsAtSequenceStep(dataFlow, sequenceStep);
			for (DataConnection dataFlowConnection : dataFlowConnections) {
				if (availableResources.containsKey(dataFlowConnection.getResourceNode())) 
					aggregatedConnections.put(dataFlowConnection.getSlotNode(),availableResources.get(dataFlowConnection.getResourceNode()));
				else 
					availableResources.put(dataFlowConnection.getResourceNode(), individualInputConnections.get(dataFlowConnection.getSlotNode()));
			}
			availableResources.putAll(getIndividualOutputConnectionsAtSequenceStep(individualOutputConnections, sequenceStep));
		}
	
		return aggregatedConnections;
	}
	
	private static HashMap<DataFlowNode, Object> getIndividualOutputConnectionsAtSequenceStep(HashMap<DataFlowNode, Object> individualOutputConnections,
			int sequenceStep) {
		HashMap<DataFlowNode, Object> outputConnections = new HashMap<DataFlowNode, Object>();
		for (Entry<DataFlowNode, Object> outputConnection : individualOutputConnections.entrySet())
			if (outputConnection.getKey().getSequenceStep() == sequenceStep)
				outputConnections.put(outputConnection.getKey(), outputConnection.getValue());
		return outputConnections;
	}

	private static List<DataConnection> getDataFlowConnectionsAtSequenceStep(List<DataConnection> dataFlow, int sequenceStep) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : dataFlow) {
			if (dataConnection.getSequenceStep() == sequenceStep)
				dataConnections.add(dataConnection);
		}
		return dataConnections;
	}

	private static SequenceSubprocess getSequenceSubprocessAtSequenceStep(List<SequenceSubprocess> sequenceSubprocesses, int sequenceStep) {
		return sequenceSubprocesses.get(sequenceStep -1); 
	}

}
