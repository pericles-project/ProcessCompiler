package eu.pericles.processcompiler.core;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.omg.spec.dd._20100524.di.DiagramElement;

import eu.pericles.processcompiler.bpmn.BPMNProcess;

public class ProcessFlowHandler {

	public static class ParametersForAggregation {
		private BPMNProcess bpmnProcess;
		private Object previousFlowElement;
		private DiagramElement previousDiagramElement;

		public ParametersForAggregation(BPMNProcess bpmnProcess, Object previousFlowElement, DiagramElement previousDiagramElement) {
			this.bpmnProcess = bpmnProcess;
			this.previousFlowElement = previousFlowElement;
			this.previousDiagramElement = previousDiagramElement;
		}
	}

	public static BPMNProcess processBPMNProcessForAggregation(ParametersForAggregation parameters) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findAndDeleteStartEvent(parameters.bpmnProcess);
		pointBPMNProcessToPreviousFlowElement(parameters, startEvent);

		return parameters.bpmnProcess;
	}

	public static Object processAggregatedBPMNProcessForAggregation(BPMNProcess aggregatedBPMNProcess) throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findAndDeleteEndEvent(aggregatedBPMNProcess);
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteSequenceFlowByTargetRef(aggregatedBPMNProcess, endEvent);
		return ((TSequenceFlow) sequenceFlow.getValue()).getSourceRef();
	}

	private static JAXBElement<? extends TFlowElement> findAndDeleteStartEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findStartEvent(bpmnProcess);
		bpmnProcess.deleteProcessElement(startEvent);
		return startEvent;
	}

	private static JAXBElement<? extends TFlowElement> findStartEvent(BPMNProcess bpmnProcess) throws Exception {
		List<JAXBElement<? extends TFlowElement>> startEvents = bpmnProcess.findFlowElementsByClass(TStartEvent.class.getSimpleName());
		if (startEvents.isEmpty())
			throw new Exception("There is not an start event in the process: " + bpmnProcess.getId());
		if (startEvents.size() > 1)
			throw new Exception("There is more than one start event in the process: " + bpmnProcess.getId());
		return startEvents.get(0);
	}

	private static JAXBElement<? extends TFlowElement> findAndDeleteEndEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findEndEvent(bpmnProcess);
		bpmnProcess.deleteProcessElement(endEvent);
		return endEvent;
	}

	private static JAXBElement<? extends TFlowElement> findEndEvent(BPMNProcess bpmnProcess) throws Exception {
		List<JAXBElement<? extends TFlowElement>> endEvents = bpmnProcess.findFlowElementsByClass(TEndEvent.class.getSimpleName());
		if (endEvents.isEmpty())
			throw new Exception("There is not an end event in the process: " + bpmnProcess.getId());
		if (endEvents.size() > 1)
			throw new Exception("There is more than one end event in the process: " + bpmnProcess.getId());
		return endEvents.get(0);
	}

	private static JAXBElement<? extends TFlowElement> findAndDeleteSequenceFlowByTargetRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> targetRef) throws Exception {
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowByTargetRef(bpmnProcess, targetRef);
		bpmnProcess.deleteProcessElement(sequenceFlow);
		return sequenceFlow;
	}

	private static JAXBElement<? extends TFlowElement> findSequenceFlowByTargetRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class))
				if (((TSequenceFlow) element.getValue()).getTargetRef().equals(flowElement.getValue()))
					return element;
		}
		throw new Exception("There is not a sequence flow pointing to " + flowElement.getValue().getId());
	}

	/**
	 * Connect the first sequence flow of a BPMN process (the one with source
	 * reference the specified in sourceRef, i.e. a start event) to the previous
	 * flow element (the last one of the aggregated BPMNProcess).
	 * 
	 * @param bpmnProcess
	 * @param sourceRef
	 * @throws Exception
	 */
	private static void pointBPMNProcessToPreviousFlowElement(ParametersForAggregation parameters,
			JAXBElement<? extends TFlowElement> sourceRef) throws Exception {
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowBySourceRef(parameters.bpmnProcess, sourceRef);
		updateSourceOfSequenceFlowToPreviousFlowElement(parameters, sequenceFlow);
	}

	private static JAXBElement<? extends TFlowElement> findSequenceFlowBySourceRef(BPMNProcess bpmnProcess,
			JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class))
				if (((TSequenceFlow) element.getValue()).getSourceRef().equals(flowElement.getValue()))
					return element;
		}
		throw new Exception("There is not a sequence flow pointing from " + flowElement.getValue().getId());
	}

	/**
	 * Update the source of a sequence flow element and its corresponded diagram
	 * element to refer to the previous flow element (the last one of the
	 * aggregated BPMNProcess)
	 * 
	 * @param bpmnProcess
	 * @param sequenceFlow
	 * @throws Exception
	 */
	private static void updateSourceOfSequenceFlowToPreviousFlowElement(ParametersForAggregation parameters,
			JAXBElement<? extends TFlowElement> sequenceFlow) throws Exception {
		((TSequenceFlow) sequenceFlow.getValue()).setSourceRef(parameters.previousFlowElement);
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = parameters.bpmnProcess.findDiagramElementByFlowElement(sequenceFlow);
		((BPMNEdge) diagramSequenceFlow.getValue()).setSourceElement(new QName(parameters.previousDiagramElement.getId()));
	}
}
