package eu.pericles.modelcompiler.jbpm;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmndi:BPMNDiagram")
public class Diagram {
	
	@XStreamAsAttribute
	@XStreamAlias("bpmndi:BPMNPlane")
	private Plane plane;

}
