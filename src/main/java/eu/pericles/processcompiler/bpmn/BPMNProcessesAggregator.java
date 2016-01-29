package eu.pericles.processcompiler.bpmn;

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

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;

public class BPMNProcessesAggregator {

	public static final int INITIAL_STEP = 1;
	private AggregatedProcess aggregatedProcess;
	private BPMNProcess aggregatedBPMNProcess;
	private List<BPMNProcess> bpmnProcesses;
	private Object lastFlowElement;
	private HashMap<DataFlowNode, Object> availableDataObjects = new HashMap<DataFlowNode, Object>();

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
	 * The process aggregation (process flow) is done by:
	 * 
	 * - Initialise the aggregated BPMN process with the first bpmnprocess in
	 * the process flow and modify its values with the information given by the
	 * AggregatedProcess entity, i.e. ID, name, etc.
	 * 
	 * - Aggregate remaining BPMN processes, doing the following for each one:
	 * 
	 * --- Prepare the aggregated BPMNProcess: by deleting the end event and its
	 * incoming sequence flow. The last flow of the aggregated BPMNProcess is
	 * therefore updated to the element referenced as source in the incoming
	 * sequence flow
	 * 
	 * --- Prepare the BPMNProcess that is going to be aggregated: by deleting
	 * the start event and connecting the outgoing flow to the last flow element
	 * of the aggregated BPMNProcess
	 * 
	 * --- Add the BPMNProcess to the aggregated BPMNProcess by adding its flow
	 * and diagram elements
	 * 
	 * @param aggregatedProcess
	 * @param bpmnProcesses
	 * @return BPMNProcess
	 * @throws Exception
	 */
	public BPMNProcess createBPMNProcessByProcessAggregation(AggregatedProcess aggregatedProcess, List<BPMNProcess> bpmnProcesses)
			throws Exception {

		this.aggregatedProcess = aggregatedProcess;
		this.bpmnProcesses = bpmnProcesses;

		initialiseAggregatedBPMNProcess();
		for (int sequenceStep = 2; sequenceStep <= bpmnProcesses.size(); sequenceStep++) {
			aggregateBPMNProcessToAggregatedBPMNProcess(sequenceStep);
		}
		return getAggregatedBPMNProcess();
	}

	private void initialiseAggregatedBPMNProcess() throws Exception {
		processDataFlow(INITIAL_STEP);
		aggregatedBPMNProcess = getBPMNProcessAtSequenceStep(INITIAL_STEP);
		aggregatedBPMNProcess.setId(aggregatedProcess.getId());
		aggregatedBPMNProcess.getProcess().setName(aggregatedProcess.getName());
	}

	private void aggregateBPMNProcessToAggregatedBPMNProcess(int sequenceStep) throws Exception {
		prepareAggregatedBPMNProcessForAggregation();
		prepareBPMNProcessToBeAggregated(sequenceStep);

		addBPMNProcessToAggregatedBPMNProcess(sequenceStep);
	}

	private void prepareAggregatedBPMNProcessForAggregation() throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findAndDeleteEndEvent(aggregatedBPMNProcess);
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteSequenceFlowByTargetRef(aggregatedBPMNProcess, endEvent);
		setLastFlowElement(((TSequenceFlow) sequenceFlow.getValue()).getSourceRef());
	}

	private void prepareBPMNProcessToBeAggregated(int sequenceStep) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findAndDeleteStartEvent(getBPMNProcessAtSequenceStep(sequenceStep));
		pointBPMNProcessToLastFlowElement(getBPMNProcessAtSequenceStep(sequenceStep), startEvent);
		processDataFlow(sequenceStep);
	}

	private void addBPMNProcessToAggregatedBPMNProcess(int sequenceStep) {
		aggregatedBPMNProcess.addBPMNProcess(getBPMNProcessAtSequenceStep(sequenceStep));
	}

	/*
	 * For each new subprocess to be aggregated, we have to update their
	 * DataObjects as available resources. Therefore, we look for
	 * DataInputAssociations and DataOutputAssociations.
	 * 
	 * With DataOutputAssociations: the targetRef corresponds to the new
	 * available resource which is related to the DataFlowNode compounded by the
	 * sourceRef and the sequenceStep
	 * 
	 * With DataInputAssociations: if the DataObject is already available, we
	 * update the association with it (update sourceRef, delete the previous
	 * dataObject (bpmnElement and diagramElement)). If not, we update available
	 * resources with the DataObject associated to the sourceRef (DataFlowNode
	 * of the corresponded DataConnection)
	 */
	private void processDataFlow(int sequenceStep) throws Exception {
		List<TDataObject> dataObjectsToBeDeleted = processDataInputs(sequenceStep);
		processDataOutputs(sequenceStep);
		deleteDataObjects(getBPMNProcessAtSequenceStep(sequenceStep), dataObjectsToBeDeleted);
	}

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
		DataInput dataInput = (DataInput) dataInputAssociation.getTargetRef();
		/**
		 * Subprocesses can contain activities that produce and consume data
		 * that is not input/output entities. Therefore, these data is not
		 * going to appear in any connection of the aggregated process, as
		 * it is only concerned on the connections between input/output
		 * entities of subprocesses
		 */
		try {
			DataFlowNode resourceNode = findResourceNodeInDataConnectionBySlotNode(new DataFlowNode(sequenceStep, dataInput.getName()));
			if (availableDataObjects.containsKey(resourceNode)) {
				updateDataInputAssociation(dataInputAssociation, resourceNode);
				return dataObject;
			} else {
				availableDataObjects.put(resourceNode, dataObject);
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private void processDataOutputs(int sequenceStep) {
		for (DataOutputAssociation dataOutputAssociation : getDataOutputAssociations(getBPMNProcessAtSequenceStep(sequenceStep))) {
			updateAvailableDataObjectsWithDataOutput(sequenceStep, dataOutputAssociation);
		}
	}

	private void updateAvailableDataObjectsWithDataOutput(int sequenceStep, DataOutputAssociation dataOutputAssociation) {
		TDataObject dataObject = (TDataObject) dataOutputAssociation.getTargetRef();
		DataOutput dataOutput = (DataOutput) dataOutputAssociation.getSourceReves().get(0).getValue();
		DataFlowNode resourceNode = new DataFlowNode(sequenceStep, dataOutput.getName());
		availableDataObjects.put(resourceNode, dataObject);
	}

	// -----------------------------------------------------------------------------------------------------------

	/**
	 * Connect the first sequence flow of a BPMN process (the one with source
	 * reference the specified in sourceRef, i.e. a start event) to the last
	 * flow element of the aggregated BPMNProcess
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

	private DataFlowNode findResourceNodeInDataConnectionBySlotNode(DataFlowNode slotNode) throws Exception {
		DataConnection dataConnection = aggregatedProcess.getSequence().findDataConnectionBySlot(slotNode);
		return dataConnection.getResourceNode();
	}

	private void deleteDataObjects(BPMNProcess bpmnProcess, List<TDataObject> dataObjects) throws Exception {
		for (TDataObject dataObject : dataObjects)
			bpmnProcess.deleteProcessElement(bpmnProcess.findFlowElement(dataObject));
	}

	// -------------------- FIND FUNCTIONS BY USING BPMN STANDARD ELEMENTS
	// ---------------------------//

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
		return bpmnProcesses.get(sequenceStep - 1);
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
