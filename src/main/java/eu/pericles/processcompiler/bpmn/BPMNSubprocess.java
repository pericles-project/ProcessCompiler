package eu.pericles.processcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TSubProcess;
import org.omg.spec.dd._20100524.di.DiagramElement;

public class BPMNSubprocess {
	
	private TSubProcess subprocess;
	private List<JAXBElement<? extends DiagramElement>> diagramElements;
	
	public BPMNSubprocess(BPMNProcess process) {
		initiliseBPMNSubprocess(process);
		addFlowElements(process.getProcess().getFlowElements());
		addDiagramElements(process.getDiagram().getBPMNPlane().getDiagramElements());
	}

	private void initiliseBPMNSubprocess(BPMNProcess process) {
		setSubprocess(new TSubProcess());
		setDiagramElements(new ArrayList<JAXBElement<? extends DiagramElement>>());
		getSubprocess().setId(process.getProcess().getId());
		getSubprocess().setName(process.getProcess().getName());
	}

	private void addFlowElements(List<JAXBElement<? extends TFlowElement>> flowElements) {
		for (JAXBElement<? extends TFlowElement> flowElement : flowElements) {
			getSubprocess().getFlowElements().add(flowElement);
		}
	}

	private void addDiagramElements(List<JAXBElement<? extends DiagramElement>> diagramElements) {
		for (JAXBElement<? extends DiagramElement> diagramElement : diagramElements) {
			getDiagramElements().add(diagramElement);
		}
	}	

	//------------------- GETTERS AND SETTERS ----------------------//

	public TSubProcess getSubprocess() {
		return subprocess;
	}

	public void setSubprocess(TSubProcess subprocess) {
		this.subprocess = subprocess;
	}

	public List<JAXBElement<? extends DiagramElement>> getDiagramElements() {
		return diagramElements;
	}

	public void setDiagramElements(List<JAXBElement<? extends DiagramElement>> diagramElements) {
		this.diagramElements = diagramElements;
	}

}
