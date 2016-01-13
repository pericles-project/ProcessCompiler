package eu.pericles.processcompiler.bpmn;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.omg.spec.dd._20100524.di.DiagramElement;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;

public class BPMNProcessesAggregator {

	private BPMNProcess aggregatedBPMNProcess;
	private Object lastFlowElement;

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

		initialiseAggregatedBPMNProcess(aggregatedProcess, bpmnProcesses.get(0));
		for (int process = 1; process <= bpmnProcesses.size() - 1; process++) {
			aggregateBPMNProcessToAggregatedBPMNProcess(bpmnProcesses.get(process));
		}

		return getAggregatedBPMNProcess();
	}

	private void initialiseAggregatedBPMNProcess(AggregatedProcess aggregatedProcess, BPMNProcess bpmnProcess) {
		aggregatedBPMNProcess = bpmnProcess;
		aggregatedBPMNProcess.setId(aggregatedProcess.getId());
		aggregatedBPMNProcess.getProcess().setName(aggregatedProcess.getName());
	}

	private void aggregateBPMNProcessToAggregatedBPMNProcess(BPMNProcess bpmnProcess) throws Exception {
		prepareAggregatedBPMNProcessForAggregation();
		prepareBPMNProcessToBeAggregated(bpmnProcess);
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
		setSourceOfSequenceFlowDiagramElement(diagramSequenceFlow, new QName(findDiagramElementByBPMNElementRef(aggregatedBPMNProcess,
				lastFlowElement).getValue().getId()));
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
