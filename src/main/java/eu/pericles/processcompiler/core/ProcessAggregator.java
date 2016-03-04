package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.model.DataInput;
import org.omg.spec.bpmn._20100524.model.DataInputAssociation;
import org.omg.spec.bpmn._20100524.model.DataOutput;
import org.omg.spec.bpmn._20100524.model.DataOutputAssociation;
import org.omg.spec.bpmn._20100524.model.TActivity;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.omg.spec.dd._20100524.di.DiagramElement;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.core.ProcessFlowHandler.ParametersForAggregation;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;

public class ProcessAggregator {

	public static final int INITIAL_STEP = 1;
	public static final int AGGREGATED_PROCESS_STEP = 0;
	private AggregatedProcess aggregatedProcess;
	private BPMNProcess aggregatedBPMNProcess;
	private List<BPMNProcess> bpmnProcesses;
	private List<SequenceSubprocess> sequenceSubprocesses;
	private Object lastFlowElement;
	private HashMap<DataFlowNode, Object> availableDataObjects = new HashMap<DataFlowNode, Object>();
	
	public static class SequenceSubprocess {
		private Process process;
		private BPMNProcess bpmnProcess;
		private HashMap<InputSlot, Object> inputConnections;
		private HashMap<OutputSlot, Object> outputConnections;
		
		public Process getProcess() {
			return process;
		}
		public void setProcess(Process process) {
			this.process = process;
		}
		public BPMNProcess getBpmnProcess() {
			return bpmnProcess;
		}
		public void setBpmnProcess(BPMNProcess bpmnProcess) {
			this.bpmnProcess = bpmnProcess;
		}
		public HashMap<InputSlot, Object> getInputConnections() {
			return inputConnections;
		}
		public void setInputConnections(HashMap<InputSlot, Object> inputConnections) {
			this.inputConnections = inputConnections;
		}
		public HashMap<OutputSlot, Object> getOutputConnections() {
			return outputConnections;
		}
		public void setOutputConnections(HashMap<OutputSlot, Object> outputConnections) {
			this.outputConnections = outputConnections;
		}
	}

	/**
	 * Create a BPMN process (aggregatedBPMNProcess) by connecting together a
	 * list of BPMN processes as specified in an aggregated process that
	 * details:
	 * 
	 * - the process flow: describes how the processes are linked
	 * 
	 * - the data flow: describes how the data is generated and linked during
	 * the process execution
	 * 
	 * The process aggregation is done by:
	 * 
	 * - Initialise the aggregated BPMN process with the first BPMN process in
	 * the process flow and the values provided by the AggregatedProcess entity
	 * 
	 * - Aggregate remaining BPMN processes, doing the following for each one:
	 * 
	 * --- Process the Data Flow: by deleting the data objects that are
	 * duplicated and updating the data associations with the valid data object
	 * 
	 * --- Process the Process Flow: by deleting the end event of the previous
	 * process and the start event of the next process, and connecting the last
	 * element of the previous process to the first element of the next process
	 * 
	 * --- Add the BPMNProcess to the AggregatedBPMNProcess: by adding its flow
	 * and diagram elements
	 * 
	 * @param aggregatedProcess
	 *            an AggregatedProcess entity (definition in the ecosystem
	 *            model), containing some parameters of the aggregated process,
	 *            i.e. name, ID, process flow and data flow
	 * @param bpmnProcesses
	 *            list of BPMNProcess that conform the aggregated process
	 * @return BPMNProcess the BPMNProcess resulting of combining the
	 *         bpmnProcesses as indicated in the process and data flow of the
	 *         aggregated process entity
	 * @throws Exception
	 */
	public BPMNProcess createBPMNProcessByProcessAggregation(AggregatedProcess aggregatedProcess, List<SequenceSubprocess> sequenceSubprocesses) 
			throws Exception {

		this.aggregatedProcess = aggregatedProcess;
		this.sequenceSubprocesses = sequenceSubprocesses;
		
		//prepareDataFlowConnections();

		initialiseAggregatedBPMNProcess();
		for (int sequenceStep = 2; sequenceStep <= sequenceSubprocesses.size(); sequenceStep++) {
			aggregateBPMNProcessToAggregatedBPMNProcess(sequenceStep);
		}
		return getAggregatedBPMNProcess();
	}


	private void initialiseAggregatedBPMNProcess() throws Exception {
		initialiseAggregatedBPMNProcessWithFirstSubprocess();
		setAggregatedBPMNProcessParameters();
	}

	private void initialiseAggregatedBPMNProcessWithFirstSubprocess() throws Exception {
		processDataFlow(INITIAL_STEP);
		aggregatedBPMNProcess = getBPMNProcessAtSequenceStep(INITIAL_STEP);
	}

	private void setAggregatedBPMNProcessParameters() {
		aggregatedBPMNProcess.setId(aggregatedProcess.getId());
		aggregatedBPMNProcess.getProcess().setName(aggregatedProcess.getName());
	}

	private void aggregateBPMNProcessToAggregatedBPMNProcess(int sequenceStep) throws Exception {
		processDataFlow(sequenceStep);
		BPMNProcess process = processProcessFlow(sequenceStep);
		aggregatedBPMNProcess.addBPMNProcess(process);//addBPMNProcessToAggregatedBPMNProcess(sequenceStep);
	}

	/**
	 * For each new BPMNProcess to be aggregated, we process the DataInputs and
	 * DataOutputs of its activities.
	 * 
	 * - If the DataObjects associated to the DataInputs are already available,
	 * the are deleted from the BPMNProcess and the DataAssociations are updated
	 * to the available DataObject. If not, the DataObjects are updated as new
	 * available resources.
	 * 
	 * - The DataObjects associated to the DataOutputs are updated as new
	 * available resources.
	 * 
	 * @param sequenceStep
	 * @throws Exception
	 */
	private void processDataFlow(int sequenceStep) throws Exception {
		List<TDataObject> dataObjectsToBeDeleted = processDataInputs(sequenceStep);
		processDataOutputs(sequenceStep);
		deleteDataObjects(getBPMNProcessAtSequenceStep(sequenceStep), dataObjectsToBeDeleted);
	}

	/**
	 * The Process Flow is processed in two steps: process the aggregated
	 * BPMNProcess and the BPMNProcess to be aggregated
	 * 
	 * - Process the aggregated BPMNProcess: by deleting the end event and its
	 * incoming sequence flow. The last flow of the aggregated BPMNProcess is
	 * therefore updated to the element referenced as source in the incoming
	 * sequence flow
	 * 
	 * - Process the BPMNProcess that is going to be aggregated: by deleting the
	 * start event and connecting the outgoing flow to the last flow element of
	 * the aggregated BPMNProcess
	 * 
	 * @param sequenceStep
	 * @throws Exception
	 * @return BPMNProcess prepared to be aggregated
	 */
	private BPMNProcess processProcessFlow(int sequenceStep) throws Exception {
		lastFlowElement = ProcessFlowHandler.processAggregatedBPMNProcessForAggregation(aggregatedBPMNProcess);
		ParametersForAggregation parameters = new ParametersForAggregation(getBPMNProcessAtSequenceStep(sequenceStep), lastFlowElement, getLastDiagramElement());
		BPMNProcess process = ProcessFlowHandler.processBPMNProcessForAggregation(parameters);
		
		return process;
	}

	/**
	 * Process Data Inputs:
	 * 
	 * - if the DataObject is already available (based on the Data Flow
	 * specified in the aggregated process entity), we update the
	 * DataInputAssociation with the available one (update sourceRef) and delete
	 * the current DataObject (bpmnElement and diagramElement) to avoid
	 * duplications. If not, we update the available resources list with the
	 * DataObject associated to the sourceRef and the DataFlowNode associated to
	 * the DataConnection that has as Slot Node the one compounded by the
	 * sequenceStep and the targetRef of the DataInputAssociation.
	 * 
	 * - if the resource node corresponds to an aggregated process slot, we
	 * update the DataInput name with the aggregated process slot.
	 * 
	 * @param sequenceStep
	 * @return list of DataObjects to be deleted as they are already available
	 *         in the AggregatedBPMNProcess
	 */
	private List<TDataObject> processDataInputs(int sequenceStep) {
		List<TDataObject> dataObjectsToBeDeleted = new ArrayList<TDataObject>();
		for (DataInputAssociation dataInputAssociation : getBPMNProcessAtSequenceStep(sequenceStep).getDataInputAssociations()) {
			TDataObject dataObjectToBeDeleted = processDataInput(sequenceStep, dataInputAssociation);
			if (dataObjectToBeDeleted != null)
				dataObjectsToBeDeleted.add(dataObjectToBeDeleted);
		}
		return dataObjectsToBeDeleted;
	}

	private TDataObject processDataInput(int sequenceStep, DataInputAssociation dataInputAssociation) {
		TDataObject dataObject = (TDataObject) dataInputAssociation.getSourceReves().get(0).getValue();
		/*
		 * Subprocesses can contain activities that produce and consume data
		 * that is not input/output entities. Therefore, these data is not going
		 * to appear in any connection of the aggregated process, as it is only
		 * concerned on the connections between input/output entities of
		 * subprocesses.
		 * 
		 * TODO Manage Exceptions as they happen for different reasons and
		 * should imply different results.
		 */
		try {
			DataConnection dataConnection = findDataConnectionOfDataInputAssociation(sequenceStep, dataInputAssociation);
			checkAndUpdateDataInputRelatedToAggregatedProcess(dataConnection, dataInputAssociation);
			if (availableDataObjects.containsKey(dataConnection.getResourceNode())) {
				updateDataInputAssociation(dataInputAssociation, dataConnection.getResourceNode());
				return dataObject;
			} else {
				availableDataObjects.put(dataConnection.getResourceNode(), dataObject);
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private void checkAndUpdateDataInputRelatedToAggregatedProcess(DataConnection dataConnection, DataInputAssociation dataInputAssociation) throws Exception {
		if (isNodeOfAggregatedProcess(dataConnection.getResourceNode()))
			updateDataInput(dataInputAssociation, dataConnection);
	}

	private void updateDataInput(DataInputAssociation dataInputAssociation, DataConnection dataConnection) throws Exception {
		BPMNProcess bpmnProcess = getBPMNProcessAtSequenceStep(dataConnection.getSequenceStep());
		DataInput dataInput = findDataInputByReference(bpmnProcess, dataInputAssociation.getTargetRef());
		dataInput.setName(dataConnection.getResource());
	}

	private DataInput findDataInputByReference(BPMNProcess bpmnProcess, Object reference) throws Exception {
		for (DataInput dataInput : bpmnProcess.getDataInputs())
			if (dataInput.equals(reference))
				return dataInput;
		throw new Exception("There is no DataInput with the reference: " + reference.toString());
	}

	private boolean isNodeOfAggregatedProcess(DataFlowNode node) {
		return (node.getSequenceStep() == AGGREGATED_PROCESS_STEP);
	}

	/**
	 * Process Data Outputs:
	 * 
	 * - Check if the new resource is connected to an aggregated process slot.
	 * If so, update the DataOutput name with the aggregated process slot.
	 * 
	 * - Update the available objects list with the DataObject associated to the
	 * targetRef of the DataOutputAssociation and the DataFlowNode compounded by
	 * the sequenceStep and the sourceRef of the DataOutputAssociation.
	 * 
	 * @param sequenceStep
	 * @throws Exception
	 */
	private void processDataOutputs(int sequenceStep) throws Exception {
		for (DataOutputAssociation dataOutputAssociation : getBPMNProcessAtSequenceStep(sequenceStep).getDataOutputAssociations()) {
			checkAndUpdateDataOutputRelatedToAggregatedProcess(sequenceStep, dataOutputAssociation);
			updateAvailableDataObjectsWithDataOutput(sequenceStep, dataOutputAssociation);
		}
	}

	private void checkAndUpdateDataOutputRelatedToAggregatedProcess(int sequenceStep, DataOutputAssociation dataOutputAssociation)
			throws Exception {
		for (DataConnection dataConnection : findDataConnectionsOfDataOutputAssociation(sequenceStep, dataOutputAssociation))
			if (isNodeOfAggregatedProcess(dataConnection.getSlotNode()))
				updateDataOutput(dataOutputAssociation, dataConnection);
	}

	private void updateDataOutput(DataOutputAssociation dataOutputAssociation, DataConnection dataConnection) throws Exception {
		BPMNProcess bpmnProcess = getBPMNProcessAtSequenceStep(dataConnection.getResourceNode().getSequenceStep());
		DataOutput dataOutput = findDataOutputByReference(bpmnProcess, dataOutputAssociation.getSourceReves().get(0).getValue());
		dataOutput.setName(dataConnection.getSlot());
	}

	private DataOutput findDataOutputByReference(BPMNProcess bpmnProcess, Object reference) throws Exception {
		for (DataOutput dataOutput : bpmnProcess.getDataOutputs())
			if (dataOutput.equals(reference))
				return dataOutput;
		throw new Exception("There is no DataOutput with the reference: " + reference.toString());
	}

	private void updateAvailableDataObjectsWithDataOutput(int sequenceStep, DataOutputAssociation dataOutputAssociation) {
		TDataObject dataObject = (TDataObject) dataOutputAssociation.getTargetRef();
		availableDataObjects.put(findResourceNodeOfDataOutputAssociation(sequenceStep, dataOutputAssociation), dataObject);
	}

	private DataConnection findDataConnectionOfDataInputAssociation(int sequenceStep, DataInputAssociation dataInputAssociation) {
		DataInput dataInput = (DataInput) dataInputAssociation.getTargetRef();
		DataConnection dataConnection = findDataConnectionBySlotNode(new DataFlowNode(sequenceStep, dataInput.getName()));
		return dataConnection;
	}

	private List<DataConnection> findDataConnectionsOfDataOutputAssociation(int sequenceStep, DataOutputAssociation dataOutputAssociation) {
		DataOutput dataOutput = (DataOutput) dataOutputAssociation.getSourceReves().get(0).getValue();
		List<DataConnection> dataConnections = findDataConnectionsByResourceNode(new DataFlowNode(sequenceStep, dataOutput.getName()));
		return dataConnections;
	}

	private DataConnection findDataConnectionBySlotNode(DataFlowNode slotNode) {
		DataConnection dataConnection = aggregatedProcess.getSequence().findDataConnectionBySlot(slotNode);
		return dataConnection;
	}

	private List<DataConnection> findDataConnectionsByResourceNode(DataFlowNode resourceNode) {
		List<DataConnection> dataConnections = aggregatedProcess.getSequence().findDataConnectionsByResource(resourceNode);
		return dataConnections;
	}

	private DataFlowNode findResourceNodeOfDataOutputAssociation(int sequenceStep, DataOutputAssociation dataOutputAssociation) {
		DataOutput dataOutput = (DataOutput) dataOutputAssociation.getSourceReves().get(0).getValue();
		return new DataFlowNode(sequenceStep, dataOutput.getName());
	}

	private void updateDataInputAssociation(DataInputAssociation inputAssociation, DataFlowNode resourceNode) {
		inputAssociation.getSourceReves().get(0).setValue(availableDataObjects.get(resourceNode));
	}

	private void deleteDataObjects(BPMNProcess bpmnProcess, List<TDataObject> dataObjects) throws Exception {
		for (TDataObject dataObject : dataObjects)
			bpmnProcess.deleteProcessElement(bpmnProcess.findFlowElement(dataObject));
	}

	// --------------- GETTERS AND SETTERS ----------------//

	public BPMNProcess getAggregatedBPMNProcess() {
		return aggregatedBPMNProcess;
	}

	public void setAggregatedBPMNProcess(BPMNProcess aggregatedBPMNProcess) {
		this.aggregatedBPMNProcess = aggregatedBPMNProcess;
	}

	public AggregatedProcess getAggregatedProcess() {
		return aggregatedProcess;
	}

	public void setAggregatedProcess(AggregatedProcess aggregatedProcess) {
		this.aggregatedProcess = aggregatedProcess;
	}

	public List<BPMNProcess> getBpmnProcesses() {
		return bpmnProcesses;
	}

	public void setBpmnProcesses(List<BPMNProcess> bpmnProcesses) {
		this.bpmnProcesses = bpmnProcesses;
	}

	public HashMap<DataFlowNode, Object> getAvailableDataObjects() {
		return availableDataObjects;
	}

	public void setAvailableDataObjects(HashMap<DataFlowNode, Object> availableDataObjects) {
		this.availableDataObjects = availableDataObjects;
	}

	public Object getLastFlowElement() {
		return lastFlowElement;
	}

	public void setLastFlowElement(Object lastFlowElement) {
		this.lastFlowElement = lastFlowElement;
	}
	
	private DiagramElement getLastDiagramElement() throws Exception {
		return aggregatedBPMNProcess.findDiagramElementByFlowElement(lastFlowElement).getValue();
	}

	private BPMNProcess getBPMNProcessAtSequenceStep(int sequenceStep) {
		return sequenceSubprocesses.get(sequenceStep -1).getBpmnProcess();
	}
	
	private SequenceSubprocess getSequenceSubprocessAtSequenceStep(int sequenceStep) {
		return sequenceSubprocesses.get(sequenceStep -1); 
	}
	
	/*
	 * 	
	private void prepareDataFlowConnections() {
		HashMap<DataFlowNode, Object> availableResources = new HashMap<DataFlowNode, Object>();
		HashMap<DataFlowNode, Object> individualDataInputConnections = getDataInputConnections();
		HashMap<DataFlowNode, Object> individualDataOutputConnections = getDataOutputConnections();
		System.out.println("Individual Data Input Connections: ");
		for (Entry<DataFlowNode, Object> individualDataInputConnection : individualDataInputConnections.entrySet())
			System.out.println(individualDataInputConnection.getKey().getSequenceStep() + " " + individualDataInputConnection.getKey().getProcessSlot() + " - " + ((TDataObject) individualDataInputConnection.getValue()).getName() + " " + ((TDataObject) individualDataInputConnection.getValue()).getId());
		System.out.println("Individual Data Output Connections: ");
		for (Entry<DataFlowNode, Object> individualDataOutputConnection : individualDataOutputConnections.entrySet())
			System.out.println(individualDataOutputConnection.getKey().getSequenceStep() + " " + individualDataOutputConnection.getKey().getProcessSlot() + " - " + ((TDataObject) individualDataOutputConnection.getValue()).getName() + " " + ((TDataObject) individualDataOutputConnection.getValue()).getId());
		HashMap<DataFlowNode, Object> aggregatedDataConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			List<DataConnection> dataFlowConnections = aggregatedProcess.getSequence().getDataConnectionsWithSlotNodeAtSequenceStep(sequenceStep);
			for (DataConnection dataFlowConnection : dataFlowConnections) {
				if (availableResources.containsKey(dataFlowConnection.getResourceNode())) 
					individualDataInputConnections.put(dataFlowConnection.getSlotNode(),availableResources.get(dataFlowConnection.getResourceNode()));
				else
					availableResources.put(dataFlowConnection.getResourceNode(), individualDataInputConnections.get(dataFlowConnection.getSlotNode()));
			}
			for (Entry<DataFlowNode, Object> individualDataOutputConnection : individualDataOutputConnections.entrySet())
				if (individualDataOutputConnection.getKey().getSequenceStep() == sequenceStep) {
					availableResources.put(aggregatedProcess.getSequence().findDataConnectionBySlot(individualDataOutputConnection.getKey()).getResourceNode(), individualDataOutputConnection.getValue());
				}
		}
		System.out.println("Aggregated Data Input Connections: ");
		for (Entry<DataFlowNode, Object> individualDataInputConnection : individualDataInputConnections.entrySet())
			System.out.println(individualDataInputConnection.getKey().getSequenceStep() + " " + individualDataInputConnection.getKey().getProcessSlot() + " - " + ((TDataObject) individualDataInputConnection.getValue()).getName() + " " + ((TDataObject) individualDataInputConnection.getValue()).getId());
		System.out.println("Individual Data Output Connections: ");
		for (Entry<DataFlowNode, Object> individualDataOutputConnection : individualDataOutputConnections.entrySet())
			System.out.println(individualDataOutputConnection.getKey().getSequenceStep() + " " + individualDataOutputConnection.getKey().getProcessSlot() + " - " + ((TDataObject) individualDataOutputConnection.getValue()).getName() + " " + ((TDataObject) individualDataOutputConnection.getValue()).getId());
	}
	
	private HashMap<DataFlowNode, Object> getDataInputConnections() {
		HashMap<DataFlowNode, Object> dataInputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			SequenceSubprocess subprocess = getSequenceSubprocessAtSequenceStep(sequenceStep);
			for (Entry<InputSlot, Object> inputConnection : subprocess.getInputConnections().entrySet())
				dataInputConnections.put(new DataFlowNode(sequenceStep, inputConnection.getKey().getId()), inputConnection.getValue());
		}
		return dataInputConnections;
	}
	
	private HashMap<DataFlowNode, Object> getDataOutputConnections() {
		HashMap<DataFlowNode, Object> dataOutputConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			SequenceSubprocess subprocess = getSequenceSubprocessAtSequenceStep(sequenceStep);
			for (Entry<OutputSlot, Object> outputConnection : subprocess.getOutputConnections().entrySet())
				dataOutputConnections.put(new DataFlowNode(sequenceStep, outputConnection.getKey().getId()), outputConnection.getValue());
		}
		return dataOutputConnections;
	}

	private HashMap<DataFlowNode, Object> getDataConnections() {
		HashMap<DataFlowNode, Object> dataConnections = new HashMap<DataFlowNode, Object>();
		for (int sequenceStep = 1; sequenceStep <= sequenceSubprocesses.size(); sequenceStep ++) {
			SequenceSubprocess subprocess = getSequenceSubprocessAtSequenceStep(sequenceStep);
			for (Entry<InputSlot, Object> inputConnection : subprocess.getInputConnections().entrySet())
				dataConnections.put(new DataFlowNode(sequenceStep, inputConnection.getKey().getId()), inputConnection.getValue());
			for (Entry<OutputSlot, Object> outputConnection : subprocess.getOutputConnections().entrySet())
				dataConnections.put(new DataFlowNode(sequenceStep, outputConnection.getKey().getId()), outputConnection.getValue());
		}
		return dataConnections;
	}
	 */

}
