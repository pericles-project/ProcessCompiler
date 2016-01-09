package eu.pericles.processcompiler.bpmn;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.dd._20100524.di.DiagramElement;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;

public class BPMNProcessesAggregator {
	
	public BPMNProcess createBPMNProcessByProcessAggregation(AggregatedProcess aggregatedProcess, List<BPMNProcess> bpmnProcesses) throws Exception {

		//BPMNProcess bpmnProcess = createBPMNProcess(aggregatedProcess);
		//List<BPMNSubprocess> bpmnSubprocesses = convertBPMNProcessesIntoSubprocesses(bpmnProcesses);
		//createProcessFlow(bpmnProcess, bpmnSubprocesses);
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
		BPMNProcess bpmnProcess = bpmnProcesses.get(0);
		bpmnProcess.setId(aggregatedProcess.getId());
		bpmnProcess.getProcess().setName(aggregatedProcess.getName());
		Object sourceRef = findAndDeleteEndEvent(bpmnProcesses.get(0));
		
		return bpmnProcess;
	}

	public Object findAndDeleteEndEvent(BPMNProcess bpmnProcess) throws Exception {
		
		JAXBElement<? extends TFlowElement> endEvent = findElementByClass(bpmnProcess, TEndEvent.class);
		JAXBElement<? extends TFlowElement> sequenceFlow = findSequenceFlowByTargetSource(bpmnProcess, endEvent);
		Object sourceRef = ((TSequenceFlow) sequenceFlow.getValue()).getSourceRef();
		
		JAXBElement<? extends DiagramElement> diagramEndEvent = findDiagramElementByBPMNElement(bpmnProcess, endEvent);
		JAXBElement<? extends DiagramElement> diagramSequenceFlow = findDiagramElementByBPMNElement(bpmnProcess, sequenceFlow);
		
		bpmnProcess.getProcess().getFlowElements().remove(endEvent);
		bpmnProcess.getProcess().getFlowElements().remove(sequenceFlow);
		bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements().remove(diagramEndEvent);
		bpmnProcess.getDiagram().getBPMNPlane().getDiagramElements().remove(diagramSequenceFlow);
		
		return sourceRef;
	}

	@SuppressWarnings("rawtypes")
	private JAXBElement<? extends TFlowElement> findElementByClass(BPMNProcess bpmnProcess, Class elementClass) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(elementClass)) 
				return element;
		}
		throw new Exception("Process: " + bpmnProcess.getId() + " has not an element of class: " + elementClass.getName());
	}

	private JAXBElement<? extends TFlowElement> findSequenceFlowByTargetSource(BPMNProcess bpmnProcess, JAXBElement<? extends TFlowElement> flowElement) throws Exception {
		for (JAXBElement<? extends TFlowElement> element : bpmnProcess.getProcess().getFlowElements()) {
			if (element.getDeclaredType().isAssignableFrom(TSequenceFlow.class)) 
				if ( ((TSequenceFlow) element.getValue()).getTargetRef().equals(flowElement.getValue()) ) 
					return element;
		}
		throw new Exception("There is not a sequence flow pointing to " + flowElement.getValue().getId());
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
		throw new Exception("There is not a diagram element corresponding to " + bpmnElement.getValue().getId());
	}
	
	/*

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

	
}
