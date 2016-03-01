package eu.pericles.processcompiler.core;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

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
	public BPMNProcess createBPMNProcessByProcessAggregation(AggregatedProcess aggregatedProcess, List<SequenceSubprocess> sequenceSubprocesses) //List<BPMNProcess> bpmnProcesses)
			throws Exception {

		this.aggregatedProcess = aggregatedProcess;
		//this.bpmnProcesses = bpmnProcesses;
		this.sequenceSubprocesses = sequenceSubprocesses;

		initialiseAggregatedBPMNProcess();
		for (int sequenceStep = 2; sequenceStep <= sequenceSubprocesses.size(); sequenceStep++) { //bpmnProcesses.size(); sequenceStep++) {
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
		processProcessFlow(sequenceStep);
		addBPMNProcessToAggregatedBPMNProcess(sequenceStep);
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
	 */
	private void processProcessFlow(int sequenceStep) throws Exception {
		processProcessFlowAtAggregatedBPMNProcess();
		processProcessFlowAtBPMNProcess(sequenceStep);
	}

	private void processProcessFlowAtAggregatedBPMNProcess() throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findAndDeleteEndEvent(aggregatedBPMNProcess);
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteSequenceFlowByTargetRef(aggregatedBPMNProcess, endEvent);
		setLastFlowElement(((TSequenceFlow) sequenceFlow.getValue()).getSourceRef());
	}

	private void processProcessFlowAtBPMNProcess(int sequenceStep) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findAndDeleteStartEvent(getBPMNProcessAtSequenceStep(sequenceStep));
		pointBPMNProcessToLastFlowElement(getBPMNProcessAtSequenceStep(sequenceStep), startEvent);
	}

	private void addBPMNProcessToAggregatedBPMNProcess(int sequenceStep) {
		aggregatedBPMNProcess.addBPMNProcess(getBPMNProcessAtSequenceStep(sequenceStep));
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
		for (DataInputAssociation dataInputAssociation : getDataInputAssociations(getBPMNProcessAtSequenceStep(sequenceStep))) {
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
		for (DataInput dataInput : getDataInputs(bpmnProcess))
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
		for (DataOutputAssociation dataOutputAssociation : getDataOutputAssociations(getBPMNProcessAtSequenceStep(sequenceStep))) {
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
		for (DataOutput dataOutput : getDataOutputs(bpmnProcess))
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

	/**
	 * Connect the first sequence flow of a BPMN process (the one with source
	 * reference the specified in sourceRef, i.e. a start event) to the last
	 * flow element of the aggregated BPMNProcess.
	 * 
	 * @param bpmnProcess
	 * @param sourceRef
	 * @throws Exception
	 */
	private void pointBPMNProcessToLastFlowElement(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> sourceRef) throws Exception {
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowBySourceRef(bpmnProcess, sourceRef);
		updateSourceOfSequenceFlowToLastFlowElement(bpmnProcess, sequenceFlow);
	}

	/**
	 * Update the source of a sequence flow element and its corresponded diagram
	 * element to refer to the last flow element of the aggregated BPMNProcess
	 * 
	 * @param bpmnProcess
	 * @param sequenceFlow
	 * @throws Exception
	 */
	private void updateSourceOfSequenceFlowToLastFlowElement(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> sequenceFlow)
			throws Exception {
		setSourceOfSequenceFlow(sequenceFlow, lastFlowElement);
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = bpmnProcess.findDiagramElementByFlowElement(sequenceFlow);
		setSourceOfSequenceFlowDiagramElement(diagramSequenceFlow,
				new QName(aggregatedBPMNProcess.findDiagramElementByFlowElement(lastFlowElement).getValue().getId()));
	}

	private void setSourceOfSequenceFlow(JAXBElement<? extends TFlowElement> sequenceFlow, Object sourceRef) {
		((TSequenceFlow) sequenceFlow.getValue()).setSourceRef(sourceRef);
	}

	private void setSourceOfSequenceFlowDiagramElement(JAXBElement<? extends DiagramElement> diagramSequenceFlow, QName sourceElement) {
		((BPMNEdge) diagramSequenceFlow.getValue()).setSourceElement(sourceElement);
	}

	private void updateDataInputAssociation(DataInputAssociation inputAssociation, DataFlowNode resourceNode) {
		inputAssociation.getSourceReves().get(0).setValue(availableDataObjects.get(resourceNode));
	}

	private void deleteDataObjects(BPMNProcess bpmnProcess, List<TDataObject> dataObjects) throws Exception {
		for (TDataObject dataObject : dataObjects)
			bpmnProcess.deleteProcessElement(bpmnProcess.findFlowElement(dataObject));
	}

	// ------------- FIND FUNCTIONS BY USING BPMN STANDARD ELEMENTS ------------

	private JAXBElement<? extends TFlowElement> findAndDeleteEndEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findEndEvent(bpmnProcess);
		bpmnProcess.deleteProcessElement(endEvent);
		return endEvent;
	}

	private JAXBElement<? extends TFlowElement> findEndEvent(BPMNProcess bpmnProcess) throws Exception {
		List<JAXBElement<? extends TFlowElement>> endEvents = bpmnProcess.findFlowElementsByClass(TEndEvent.class.getSimpleName());
		if (endEvents.isEmpty())
			throw new Exception("There is not an end event in the process: " + bpmnProcess.getId());
		if (endEvents.size() > 1)
			throw new Exception("There is more than one end event in the process: " + bpmnProcess.getId());
		return endEvents.get(0);
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteStartEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findStartEvent(bpmnProcess);
		bpmnProcess.deleteProcessElement(startEvent);
		return startEvent;
	}

	private JAXBElement<? extends TFlowElement> findStartEvent(BPMNProcess bpmnProcess) throws Exception {
		List<JAXBElement<? extends TFlowElement>> startEvents = bpmnProcess.findFlowElementsByClass(TStartEvent.class.getSimpleName());
		if (startEvents.isEmpty())
			throw new Exception("There is not an start event in the process: " + bpmnProcess.getId());
		if (startEvents.size() > 1)
			throw new Exception("There is more than one start event in the process: " + bpmnProcess.getId());
		return startEvents.get(0);
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteSequenceFlowByTargetRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> targetRef) throws Exception {
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowByTargetRef(bpmnProcess, targetRef);
		bpmnProcess.deleteProcessElement(sequenceFlow);
		return sequenceFlow;
	}

	private JAXBElement<? extends TFlowElement> findSequenceFlowByTargetRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class))
				if (((TSequenceFlow) element.getValue()).getTargetRef().equals(flowElement.getValue()))
					return element;
		}
		throw new Exception("There is not a sequence flow pointing to " + flowElement.getValue().getId());
	}

	private JAXBElement<? extends TFlowElement> findSequenceFlowBySourceRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class))
				if (((TSequenceFlow) element.getValue()).getSourceRef().equals(flowElement.getValue()))
					return element;
		}
		throw new Exception("There is not a sequence flow pointing from " + flowElement.getValue().getId());
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

	private BPMNProcess getBPMNProcessAtSequenceStep(int sequenceStep) {
		return sequenceSubprocesses.get(sequenceStep-1).getBpmnProcess();//bpmnProcesses.get(sequenceStep - 1);
	}

	/*
	 * TODO Delete these gets() and update to use them through the BPMNProcess class
	 */
	private List<DataInput> getDataInputs(BPMNProcess bpmnProcess) {
		List<DataInput> dataInputs = new ArrayList<DataInput>();
		for (TActivity activity : getActivities(bpmnProcess))
			for (DataInput dataInput : activity.getIoSpecification().getDataInputs())
				dataInputs.add(dataInput);
		return dataInputs;
	}

	private List<DataOutput> getDataOutputs(BPMNProcess bpmnProcess) {
		List<DataOutput> dataOutputs = new ArrayList<DataOutput>();
		for (TActivity activity : getActivities(bpmnProcess))
			for (DataOutput dataOutput : activity.getIoSpecification().getDataOutputs())
				dataOutputs.add(dataOutput);
		return dataOutputs;
	}

	private List<DataInputAssociation> getDataInputAssociations(BPMNProcess bpmnProcess) {
		List<DataInputAssociation> dataInputAssociations = new ArrayList<DataInputAssociation>();
		for (TActivity activity : getActivities(bpmnProcess))
			for (DataInputAssociation dataInputAssociation : activity.getDataInputAssociations())
				dataInputAssociations.add(dataInputAssociation);
		return dataInputAssociations;
	}

	private List<DataOutputAssociation> getDataOutputAssociations(BPMNProcess bpmnProcess) {
		List<DataOutputAssociation> dataOutputAssociations = new ArrayList<DataOutputAssociation>();
		for (TActivity activity : getActivities(bpmnProcess))
			for (DataOutputAssociation dataOutputAssociation : activity.getDataOutputAssociations())
				dataOutputAssociations.add(dataOutputAssociation);
		return dataOutputAssociations;
	}

	private List<TActivity> getActivities(BPMNProcess bpmnProcess) {
		List<TActivity> activities = new ArrayList<TActivity>();
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements())
			if (TActivity.class.isAssignableFrom(element.getValue().getClass()))
				activities.add((TActivity) element.getValue());
		return activities;
	}

}
