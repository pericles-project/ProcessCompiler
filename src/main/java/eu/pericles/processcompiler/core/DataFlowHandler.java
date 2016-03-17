package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;

/**
 * * Some important concepts about Data Flow Handler class:
 * 
 * - HashMap<DataFlowNode, Object> : represents the relationship between a
 * slot of a sequence process (DataFLowNode) and the resource connected to
 * it (Object)
 * 
 * - INDIVIDUAL CONNECTIONS: relationship of process slots and resources of
 * their implementations (before aggregation)
 * 
 * - AGGREGATED CONNECTIONS: relationship of process slots and resources of
 * their implementations (after aggregation) - only contains the
 * relationships that have changed after the aggregation
 * 
 * - VALIDATED PROCESS: is the result of validate a Process entity (RDF-based
 * description) with its implementation. Therefore, it consists on:
 * --- bpmnProcess: implementation of the process (BPMNProcess)
 * --- inputConnections: hashmap <input slot of Process, resource in the
 * implementation BPMNProcess>
 * --- outputConnections: hashmap <output slot of Process, resource in the
 * implementation BPMNProcess>
 * 
 * - DATA CONNECTION: data are connected by linking two nodes of the flow
 * (DataFlowNode)
 * --- SlotNode: this is a slot to which the data goes
 * --- ResourceNode: this is a slot from which the data comes
 *
 */

public class DataFlowHandler {

	public final static int AGGREGATED_PROCESS = 0;

	/**
	 * Process the data flow of an aggregated processes by deleting the data
	 * objects that are duplicated and updating the data associations with the
	 * valid data object for each process in the process flow.
	 * 
	 * The process of the data flow evaluates each DataConnection (slot node -
	 * resource node) in the data flow by doing:
	 * - if the slot node is in AGGREGATED CONNECTIONS, then it has to be
	 * updated with the corresponded resource available in the aggregated
	 * process
	 * - if any of the slots in the data connection belongs to the aggregated
	 * process, the data inputs(outputs) related to the input(output) slot have
	 * to be updated with the aggregated process slot
	 * 
	 * @param dataFlow
	 *            - data flow of an aggregated processes (list of
	 *            DataConnections)
	 * @param sequenceSubprocesses
	 *            - list of validated processes that conform the process flow of
	 *            the aggregated process
	 * @return bpmnProcesses
	 *         - list of updated bpmn processes prepared to be process for
	 *         aggregation
	 * @throws ProcessDataFlowException 
	 * 
	 */
	public static List<BPMNProcess> processDataFlow(List<DataConnection> dataFlow, List<ValidatedProcess> sequenceSubprocesses) throws ProcessDataFlowException {
		try {
			HashMap<DataFlowNode, Object> aggregatedConnections = getAggregatedConnections(dataFlow, sequenceSubprocesses);
			for (DataConnection dataConnection : dataFlow) {
				if (aggregatedConnections.containsKey(dataConnection.getSlotNode()))
					updateResource(sequenceSubprocesses, aggregatedConnections, dataConnection);
				if (isAggregatedProcessInputSlot(dataConnection))
					updateInputSlot(sequenceSubprocesses, dataConnection);
				if (isAggregatedProcessOutputSlot(dataConnection))
					updateOutputSlot(sequenceSubprocesses, dataConnection);
			}
			return getBPMNProcesses(sequenceSubprocesses);
		} catch (Exception e) {
			throw new ProcessDataFlowException(e.getMessage(), e);
		}
	}

	private static HashMap<DataFlowNode, Object> getAggregatedConnections(List<DataConnection> dataFlow,
			List<ValidatedProcess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> aggregatedConnections = new HashMap<DataFlowNode, Object>();
		HashMap<DataFlowNode, Object> availableResources = new HashMap<DataFlowNode, Object>();
		HashMap<DataFlowNode, Object> individualInputConnections = getIndividualInputConnections(sequenceSubprocesses);
		HashMap<DataFlowNode, Object> individualOutputConnections = getIndividualOutputConnections(sequenceSubprocesses);

		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep++) {
			List<DataConnection> dataFlowConnections = getDataFlowConnectionsAtSequenceStep(dataFlow, sequenceStep);
			for (DataConnection dataFlowConnection : dataFlowConnections) {
				if (availableResources.containsKey(dataFlowConnection.getResourceNode()))
					aggregatedConnections.put(dataFlowConnection.getSlotNode(),
							availableResources.get(dataFlowConnection.getResourceNode()));
				else
					availableResources.put(dataFlowConnection.getResourceNode(),
							individualInputConnections.get(dataFlowConnection.getSlotNode()));
			}
			availableResources.putAll(getIndividualOutputConnectionsAtSequenceStep(individualOutputConnections, sequenceStep));
		}
		return aggregatedConnections;
	}

	private static HashMap<DataFlowNode, Object> getIndividualInputConnections(List<ValidatedProcess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> inputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep++) {
			ValidatedProcess subprocess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, sequenceStep);
			for (Entry<InputSlot, Object> inputConnection : subprocess.getInputConnections().entrySet())
				inputConnections.put(new DataFlowNode(sequenceStep, inputConnection.getKey().getId()), inputConnection.getValue());
		}
		return inputConnections;
	}

	private static HashMap<DataFlowNode, Object> getIndividualOutputConnections(List<ValidatedProcess> sequenceSubprocesses) {
		HashMap<DataFlowNode, Object> dataOutputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep++) {
			ValidatedProcess subprocess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, sequenceStep);
			for (Entry<OutputSlot, Object> outputConnection : subprocess.getOutputConnections().entrySet())
				dataOutputConnections.put(new DataFlowNode(sequenceStep, outputConnection.getKey().getId()), outputConnection.getValue());
		}
		return dataOutputConnections;
	}

	private static HashMap<DataFlowNode, Object> getIndividualOutputConnectionsAtSequenceStep(
			HashMap<DataFlowNode, Object> individualOutputConnections, int sequenceStep) {
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

	private static void updateResource(List<ValidatedProcess> sequenceSubprocesses, HashMap<DataFlowNode, Object> aggregatedConnections,
			DataConnection dataConnection) throws Exception {
		BPMNProcess bpmnProcess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, dataConnection.getSlotNode().getSequenceStep())
				.getBpmnProcess();
		Object oldResource = getIndividualInputConnections(sequenceSubprocesses).get(dataConnection.getSlotNode());
		Object newResource = aggregatedConnections.get(dataConnection.getSlotNode());
		bpmnProcess.updateSourceOfDataInputAssociations(oldResource, newResource);
		bpmnProcess.deleteProcessElement(oldResource);
	}

	private static boolean isAggregatedProcessInputSlot(DataConnection dataConnection) {
		return (dataConnection.getResourceNode().getSequenceStep() == AGGREGATED_PROCESS);
	}

	private static void updateInputSlot(List<ValidatedProcess> sequenceSubprocesses, DataConnection dataConnection) {
		BPMNProcess bpmnProcess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses, dataConnection.getSlotNode().getSequenceStep())
				.getBpmnProcess();
		bpmnProcess.updateNameOfDataInput(dataConnection.getSlotNode().getProcessSlot(), dataConnection.getResourceNode().getProcessSlot());
	}

	private static boolean isAggregatedProcessOutputSlot(DataConnection dataConnection) {
		return (dataConnection.getSlotNode().getSequenceStep() == AGGREGATED_PROCESS);
	}

	private static void updateOutputSlot(List<ValidatedProcess> sequenceSubprocesses, DataConnection dataConnection) {
		BPMNProcess bpmnProcess = getSequenceSubprocessAtSequenceStep(sequenceSubprocesses,
				dataConnection.getResourceNode().getSequenceStep()).getBpmnProcess();
		bpmnProcess
				.updateNameOfDataOutput(dataConnection.getResourceNode().getProcessSlot(), dataConnection.getSlotNode().getProcessSlot());
	}

	private static List<BPMNProcess> getBPMNProcesses(List<ValidatedProcess> sequenceSubprocesses) {
		List<BPMNProcess> processes = new ArrayList<BPMNProcess>();
		for (ValidatedProcess sequenceSubprocess : sequenceSubprocesses)
			processes.add(sequenceSubprocess.getBpmnProcess());
		return processes;
	}

	private static ValidatedProcess getSequenceSubprocessAtSequenceStep(List<ValidatedProcess> sequenceSubprocesses, int sequenceStep) {
		return sequenceSubprocesses.get(sequenceStep - 1);
	}

}
