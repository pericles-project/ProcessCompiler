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
	
	private AggregatedProcess aggregatedProcess;
	private BPMNProcess aggregatedBPMNProcess;
	private List<BPMNProcess> bpmnProcesses;
	
	public BPMNProcess createBPMNProcessByProcessAggregation(AggregatedProcess aggregatedProcess, List<BPMNProcess> bpmnProcesses) throws Exception {
		/*
		 * firstSubprocess:
		 * 		Copy BPMNProcess and modify with new values from AggregatedProcess
		 * 		findEndEvent and delete
		 * 		findArrowPointingToEndEvent and set sourceRef = arrowSourceRef
		 * intermediateSubprocesses:
		 * 		findStartEvent and delete
		 * 		findArrowPointingFromStartEvent and setSourceRef = sourceRef
		 * 		findEndEvent and delete
		 * 		findArrowPointingToEndEvent and set sourceRef = arrowSourceRef
		 * finalSubprocess:
		 * 		findStartEvent and delete
		 * 		findArrowPointingFromStartEvent and setSourceRef = sourceRef
		 * 
		 * each step:
		 * 		getFlowElements().addFlowElements()
		 * 		getDiagramElements().addDiagramElements()
		 */
		
		this.aggregatedProcess = aggregatedProcess;
		this.bpmnProcesses = bpmnProcesses;
		this.aggregatedBPMNProcess = new BPMNProcess();

		Object lastFlowElement = aggregateFirstProcess(getBpmnProcesses().get(0));
		if (getBpmnProcesses().size() > 2)
			lastFlowElement = aggregateIntermediateProcesses(lastFlowElement);		
		aggregateLastSubprocess(lastFlowElement, getBpmnProcesses().get(getBpmnProcesses().size()-1));
		
		return getAggregatedBPMNProcess();
	}

	private Object aggregateIntermediateProcesses(Object lastFlowElement) throws Exception {
		for (int subprocess = 1; subprocess < getBpmnProcesses().size()-1; subprocess++)
			lastFlowElement = aggregateIntermediateProcess(lastFlowElement, getBpmnProcesses().get(subprocess));
		return lastFlowElement;
	}

	private Object aggregateIntermediateProcess(Object lastFlowElement, BPMNProcess bpmnProcess) throws Exception {
		Object nextFlowElement = findAndDeleteEndEvent(bpmnProcess);
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteStartEvent(bpmnProcess);
		aggregatedBPMNProcess.addFlowElements(bpmnProcess.getProcess().getFlowElements());
		aggregatedBPMNProcess.addDiagramElements(bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements());
		
		updateSequenceFlowWithLastFLowElement(lastFlowElement, sequenceFlow);
		
		return nextFlowElement;
	}

	private Object aggregateFirstProcess(BPMNProcess bpmnProcess) throws Exception {
		
		Object lastFlowElement = findAndDeleteEndEvent(bpmnProcess);
		aggregatedBPMNProcess = bpmnProcess;
		aggregatedBPMNProcess.setId(aggregatedProcess.getId());
		aggregatedBPMNProcess.getProcess().setName(aggregatedProcess.getName());
		
		return lastFlowElement;
	}
	
	private void aggregateLastSubprocess(Object lastFlowElement, BPMNProcess bpmnProcess) throws Exception {		
		JAXBElement<? extends TFlowElement> sequenceFlow = findAndDeleteStartEvent(bpmnProcess);
		aggregatedBPMNProcess.addFlowElements(bpmnProcess.getProcess().getFlowElements());
		aggregatedBPMNProcess.addDiagramElements(bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements());
		
		updateSequenceFlowWithLastFLowElement(lastFlowElement, sequenceFlow);
	}

	public Object findAndDeleteEndEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> endEvent = findElementByClass(bpmnProcess, TEndEvent.class);
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowByTargetRef(bpmnProcess, endEvent);
		Object sourceRef = ((TSequenceFlow) sequenceFlow.getValue()).getSourceRef();
		
		JAXBElement<? extends DiagramElement> diagramEndEvent = findDiagramElementByBPMNElement(bpmnProcess, endEvent);
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = findDiagramElementByBPMNElement(bpmnProcess, sequenceFlow);
		
		bpmnProcess.getProcess().getFlowElements().remove(endEvent);
		bpmnProcess.getProcess().getFlowElements().remove(sequenceFlow);
		bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements().remove(diagramEndEvent);
		bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements().remove(diagramSequenceFlow);
		
		return sourceRef;
	}

	private JAXBElement<? extends TFlowElement> findAndDeleteStartEvent(BPMNProcess bpmnProcess) throws Exception {
		JAXBElement<? extends TFlowElement> startEvent = findElementByClass(bpmnProcess, TStartEvent.class);
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowBySourceRef(bpmnProcess, startEvent);
		
		JAXBElement<? extends DiagramElement> diagramStartEvent = findDiagramElementByBPMNElement(bpmnProcess, startEvent);
		
		bpmnProcess.getProcess().getFlowElements().remove(startEvent);
		bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements().remove(diagramStartEvent);
		
		return sequenceFlow;
	}

	private void updateSequenceFlowWithLastFLowElement(Object lastFlowElement, JAXBElement<? extends TFlowElement> sequenceFlow) throws Exception {
		((TSequenceFlow) sequenceFlow.getValue()).setSourceRef(lastFlowElement);
		
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = findDiagramElementByBPMNElement(aggregatedBPMNProcess, sequenceFlow);
		((BPMNEdge) diagramSequenceFlow.getValue()).setSourceElement(new QName(findDiagramElementByBPMNElementRef(aggregatedBPMNProcess, lastFlowElement).getValue().getId()));
	}

	@SuppressWarnings("rawtypes")
	private JAXBElement<? extends TFlowElement> findElementByClass(BPMNProcess bpmnProcess, Class elementClass) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(elementClass)) 
				return element;
		}
		throw new Exception("Process: " + bpmnProcess.getId() + " has not an element of class: " + elementClass.getName());
	}

	private JAXBElement<? extends TFlowElement> findSequenceFlowByTargetRef(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class)) 
				if ( ((TSequenceFlow) element.getValue()).getTargetRef().equals(flowElement.getValue()) ) 
					return element;
		}
		throw new Exception("There is not a sequence flow pointing to " + flowElement.getValue().getId());
	}

	private JAXBElement<? extends TFlowElement> findSequenceFlowBySourceRef(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class)) 
				if ( ((TSequenceFlow) element.getValue()).getSourceRef().equals(flowElement.getValue()) ) 
					return element;
		}
		throw new Exception("There is not a sequence flow pointing from " + flowElement.getValue().getId());
	}
	
	private JAXBElement<? extends DiagramElement> findDiagramElementByBPMNElement(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> bpmnElement) throws Exception {
		for (JAXBElement<? extends DiagramElement> element : bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements()) {
			// The function getBpmnElement() returns the QName (prefix + ID). Use getLocalPart() to get the same ID as the BPMNElement
			if (element.getDeclaredType().isAssignableFrom(BPMNShape.class))
				if ( ((BPMNShape) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()) )
					return element;
			if (element.getDeclaredType().isAssignableFrom(BPMNEdge.class)) 
				if ( ((BPMNEdge) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()) )
					return element;
		}
		throw new Exception("There is not a diagram element corresponding to the element " + bpmnElement.getValue().getId());
	}

	private JAXBElement<? extends DiagramElement> findDiagramElementByBPMNElementRef(BPMNProcess bpmnProcess, Object bpmnElementRef) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getValue().equals(bpmnElementRef))
				return findDiagramElementByBPMNElement(bpmnProcess, element);			
		}
		throw new Exception("There is not a diagram element corresponding to the reference " + bpmnElementRef.toString());
	}
	
	/*
	
	private BPMNProcess createAggregatedBPMNProcess(AggregatedProcess aggregatedProcess) {
		System.out.println("createAggregatedProcess");
		BPMNProcess bpmnProcess = new BPMNProcess();
		bpmnProcess.setId(aggregatedProcess.getId());
		bpmnProcess.setProcess(new TProcess());
		bpmnProcess.getProcess().setName(aggregatedProcess.getName());
		bpmnProcess.setDiagram(new BPMNDiagram());
		bpmnProcess.getDiagram().setBPMNPlane(new BPMNPlane());
		
		System.out.println("End createAggregatedProcess");
		
		return bpmnProcess;
		
	}

	private BPMNProcess createBPMNProcess(AggregatedProcess aggregatedProcess) {
		BPMNProcess bpmnProcess = new BPMNProcess();
		bpmnProcess.setProcess(new TProcess());
		bpmnProcess.setDiagram(new BPMNDiagram());
		bpmnProcess.getDiagram().setBPMNPlane(new BPMNPlane());
		
		return bpmnProcess;
	}

	private List<BPMNSubprocess> convertBPMNProcessesIntoSubprocesses(List<BPMNProcess> bpmnProcesses) {
		List<BPMNSubprocess> bpmnSubprocesses = new ArrayList<BPMNSubprocess>();
		for (BPMNProcess bpmnProcess : bpmnProcesses) {
			bpmnSubprocesses.add(new BPMNSubprocess(bpmnProcess));
		}
		
		return bpmnSubprocesses;
	}

	private void createProcessFlow(BPMNProcess bpmnProcess, List<BPMNSubprocess> bpmnSubprocesses) {
		
	}
	*/
	
	// --------------- GETTERS AND SETTERS ----------------//

	public AggregatedProcess getAggregatedProcess() {
		return aggregatedProcess;
	}

	public void setAggregatedProcess(AggregatedProcess aggregatedProcess) {
		this.aggregatedProcess = aggregatedProcess;
	}

	public BPMNProcess getAggregatedBPMNProcess() {
		return aggregatedBPMNProcess;
	}

	public void setAggregatedBPMNProcess(BPMNProcess aggregatedBPMNProcess) {
		this.aggregatedBPMNProcess = aggregatedBPMNProcess;
	}

	public List<BPMNProcess> getBpmnProcesses() {
		return bpmnProcesses;
	}

	public void setBpmnProcesses(List<BPMNProcess> bpmnProcesses) {
		this.bpmnProcesses = bpmnProcesses;
	}

	
}
