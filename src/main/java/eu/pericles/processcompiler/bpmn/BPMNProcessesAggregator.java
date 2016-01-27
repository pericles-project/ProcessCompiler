package eu.pericles.processcompiler.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.DataInput;
import org.omg.spec.bpmn._20100524.model.DataInputAssociation;
import org.omg.spec.bpmn._20100524.model.DataOutput;
import org.omg.spec.bpmn._20100524.model.DataOutputAssociation;
import org.omg.spec.bpmn._20100524.model.ExtensionElements;
import org.omg.spec.bpmn._20100524.model.IoSpecification;
import org.omg.spec.bpmn._20100524.model.TActivity;
import org.omg.spec.bpmn._20100524.model.TCallableElement;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.omg.spec.dd._20100524.di.DiagramElement;
import org.w3c.dom.Element;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;

public class BPMNProcessesAggregator {

	private AggregatedProcess aggregatedProcess;
	private BPMNProcess aggregatedBPMNProcess;
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
		initialiseAggregatedBPMNProcess(aggregatedProcess, bpmnProcesses.get(0));
		for (int process = 1; process <= bpmnProcesses.size() - 1; process++) {
			aggregateBPMNProcessToAggregatedBPMNProcess(process+1, bpmnProcesses.get(process));
		}

		return getAggregatedBPMNProcess();
	}

	private void initialiseAggregatedBPMNProcess(AggregatedProcess aggregatedProcess, BPMNProcess bpmnProcess) throws Exception {
		aggregatedBPMNProcess = bpmnProcess;
		aggregatedBPMNProcess.setId(aggregatedProcess.getId());
		aggregatedBPMNProcess.getProcess().setName(aggregatedProcess.getName());
		/*
		 * For each new subprocess to be aggregated, we have to be updated their
		 * DataObjects as available resources. Therefore, we look for
		 * DataInputAssociations and DataOutputAssociations.
		 * 
		 * With DataOutputAssociations: the targetRef corresponds to the new
		 * available resource which is related to the DataFlowNode compounded by
		 * the sourceRef and the sequenceStep
		 * 
		 * With DataInputAssociations: if the DataObject is already available,
		 * we update the association with it (update sourceRef, delete the
		 * previous dataObject (bpmnElement and diagramElement)). If not, we
		 * update available resources with the DataObject associated to the
		 * sourceRef (DataFlowNode of the corresponded DataConnection)
		 */
		findDataAssociations(1, aggregatedBPMNProcess);
	}

	private void findDataAssociations(int sequenceStep, BPMNProcess bpmnProcess) throws Exception {
		System.out.println("Sequence step: " + sequenceStep);
		List<TDataObject> dataObjectsToBeDeleted = new ArrayList<TDataObject>();
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (TActivity.class.isAssignableFrom(element.getValue().getClass())) {
				System.out.println("Activity: " + element.getValue().getName());
				TActivity activity = (TActivity) element.getValue();
				List<DataInputAssociation> inputAssociations = activity.getDataInputAssociations();
				for (DataInputAssociation inputAssociation : inputAssociations) {
					Object source = inputAssociation.getSourceReves().get(0).getValue();
					Object target = inputAssociation.getTargetRef();
					//System.out.println("Input Association: " + source.toString() + " " + target.toString());
					TDataObject dataObject = (TDataObject) inputAssociation.getSourceReves().get(0).getValue();
					System.out.println("DataObject ID: " + dataObject.getId());
					DataInput dataInput = (DataInput) target;
					//System.out.println("Slot Name: " + dataInput.getName());
					DataFlowNode resourceNode = findResourceNodeOfDataConnection(sequenceStep, dataInput.getName());
					System.out.println("Resource Node: " + resourceNode.getSequenceStep() + " " + resourceNode.getProcessSlot());
					if (availableDataObjects.containsKey(resourceNode)) {
						System.out.println("Resource already exists");
						//updateDataInputAssociation(inputAssociation, resourceNode);
						System.out.println("New Target Ref: " + ((TDataObject) availableDataObjects.get(resourceNode)).getId());
						inputAssociation.getSourceReves().get(0).setValue(availableDataObjects.get(resourceNode));
						dataObjectsToBeDeleted.add(dataObject);//deleteDataObject(bpmnProcess, dataObject);
					} else {
						availableDataObjects.put(resourceNode, dataObject);
					}
					System.out.println("");
				}
				List<DataOutputAssociation> outputAssociations = activity.getDataOutputAssociations();
				for (DataOutputAssociation outputAssociation : outputAssociations) {
					Object source = outputAssociation.getSourceReves().get(0).getValue();
					Object target = outputAssociation.getTargetRef();
					//System.out.println("Output Association: " + source.toString() + " " + target.toString());
					TDataObject dataObject = (TDataObject) target;
					System.out.println("DataObject ID: " + dataObject.getId());
					DataOutput dataOutput = (DataOutput) source;
					//System.out.println("Slot Name: " + dataOutput.getName());
					DataFlowNode resourceNode = new DataFlowNode(sequenceStep, dataOutput.getName());
					System.out.println("Resource Node: " + resourceNode.getSequenceStep() + " " + resourceNode.getProcessSlot());
					System.out.println("");
					availableDataObjects.put(resourceNode, dataObject);
				}
				System.out.println("");
			}
		}
		deleteDataObjects(bpmnProcess,dataObjectsToBeDeleted);
	}

	private void deleteDataObjects(BPMNProcess bpmnProcess, List<TDataObject> dataObjects) throws Exception {
		for (TDataObject dataObject : dataObjects)
			deleteDataObject(bpmnProcess, dataObject);
	}

	private void deleteDataObject(BPMNProcess bpmnProcess, TDataObject dataObject) throws Exception {
		JAXBElement<? extends TFlowElement> bpmnElement = bpmnProcess.getFlowElementByID(dataObject.getId());
		JAXBElement<? extends DiagramElement> diagramElement = findDiagramElementByBPMNElement(bpmnProcess, bpmnElement);
		bpmnProcess.getFlowElements().remove(bpmnElement);
		bpmnProcess.getDiagramElements().remove(diagramElement);
	}

	private void updateDataInputAssociation(DataInputAssociation inputAssociation, DataFlowNode resourceNode) {
		inputAssociation.setTargetRef(availableDataObjects.get(resourceNode));
	}

	private DataFlowNode findResourceNodeOfDataConnection(int sequenceStep, String slot) throws Exception {
		DataConnection dataConnection = aggregatedProcess.getSequence().findDataConnectionBySlot(new DataFlowNode(sequenceStep, slot));
		return dataConnection.getResourceNode();
	}

	// -----------------------------------------------------------------------------------------------------------

	private void aggregateBPMNProcessToAggregatedBPMNProcess(int sequenceStep, BPMNProcess bpmnProcess) throws Exception {
		prepareAggregatedBPMNProcessForAggregation();
		prepareBPMNProcessToBeAggregated(bpmnProcess);
		findDataAssociations(sequenceStep, bpmnProcess);
		
		addBPMNProcessToAggregatedBPMNProcess(bpmnProcess);
	}

	private void prepareAggregatedBPMNProcessForAggregation() throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findAndDeleteEndEvent(aggregatedBPMNProcess);
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteSequenceFlowByTargetRef(aggregatedBPMNProcess, endEvent);
		setLastFlowElement(((TSequenceFlow) sequenceFlow.getValue()).getSourceRef());
	}

	private void prepareBPMNProcessToBeAggregated(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findAndDeleteStartEvent(bpmnProcess);
		pointBPMNProcessToLastFlowElement(bpmnProcess, startEvent);
	}

	private void addBPMNProcessToAggregatedBPMNProcess(BPMNProcess bpmnProcess) {
		aggregatedBPMNProcess.addItemDefinitions(bpmnProcess.getItemDefinitions());
		aggregatedBPMNProcess.addFlowElements(bpmnProcess.getFlowElements());
		aggregatedBPMNProcess.addDiagramElements(bpmnProcess.getDiagramElements());
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteEndEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findElementByClass(bpmnProcess, TEndEvent.class);
		deleteElement(bpmnProcess, endEvent);
		return endEvent;
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteStartEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findElementByClass(bpmnProcess, TStartEvent.class);
		deleteElement(bpmnProcess, startEvent);
		return startEvent;
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteSequenceFlowByTargetRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> targetRef) throws Exception {
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowByTargetRef(bpmnProcess, targetRef);
		deleteElement(bpmnProcess, sequenceFlow);
		return sequenceFlow;
	}

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
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = findDiagramElementByBPMNElement(bpmnProcess, sequenceFlow);
		setSourceOfSequenceFlowDiagramElement(diagramSequenceFlow,
				new QName(findDiagramElementByBPMNElementRef(aggregatedBPMNProcess, lastFlowElement).getValue().getId()));
	}

	private void setSourceOfSequenceFlow(JAXBElement<? extends TFlowElement> sequenceFlow, Object sourceRef) {
		((TSequenceFlow) sequenceFlow.getValue()).setSourceRef(sourceRef);
	}

	private void setSourceOfSequenceFlowDiagramElement(JAXBElement<? extends DiagramElement> diagramSequenceFlow, QName sourceElement) {
		((BPMNEdge) diagramSequenceFlow.getValue()).setSourceElement(sourceElement);
	}

	/**
	 * Deletes the specified flow element of a BPMNProcess and its corresponded
	 * diagram element
	 * 
	 * @param bpmnProcess
	 * @param element
	 * @throws Exception
	 */
	private void deleteElement(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> element) throws Exception {
		JAXBElement<? extends DiagramElement> diagramEndEvent = findDiagramElementByBPMNElement(bpmnProcess, element);
		bpmnProcess.getFlowElements().remove(element);
		bpmnProcess.getDiagramElements().remove(diagramEndEvent);
	}

	@SuppressWarnings("rawtypes")
	private JAXBElement<? extends TFlowElement> findElementByClass(BPMNProcess bpmnProcess, Class elementClass) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(elementClass))
				return element;
		}
		throw new Exception("Process: " + bpmnProcess.getId() + " has not an element of class: " + elementClass.getName());
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

	private JAXBElement<? extends DiagramElement> findDiagramElementByBPMNElement(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> bpmnElement) throws Exception {
		for (JAXBElement<? extends DiagramElement> element : bpmnProcess.getDiagramElements()) {
			// The function getBpmnElement() returns the QName (prefix + ID).
			// Use getLocalPart() to get the same ID as the BPMNElement
			if (element.getDeclaredType().isAssignableFrom(BPMNShape.class))
				if (((BPMNShape) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()))
					return element;
			if (element.getDeclaredType().isAssignableFrom(BPMNEdge.class))
				if (((BPMNEdge) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()))
					return element;
		}
		throw new Exception("There is not a diagram element corresponding to the element " + bpmnElement.getValue().getId());
	}

	private JAXBElement<? extends DiagramElement> findDiagramElementByBPMNElementRef(BPMNProcess bpmnProcess, Object bpmnElementRef)
			throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getValue().equals(bpmnElementRef))
				return findDiagramElementByBPMNElement(bpmnProcess, element);
		}
		throw new Exception("There is not a diagram element corresponding to the reference " + bpmnElementRef.toString());
	}

	// --------------- GETTERS AND SETTERS ----------------//

	public BPMNProcess getAggregatedBPMNProcess() {
		return aggregatedBPMNProcess;
	}

	public void setAggregatedBPMNProcess(BPMNProcess aggregatedBPMNProcess) {
		this.aggregatedBPMNProcess = aggregatedBPMNProcess;
	}

	public Object getLastFlowElement() {
		return lastFlowElement;
	}

	public void setLastFlowElement(Object lastFlowElement) {
		this.lastFlowElement = lastFlowElement;
	}

}
