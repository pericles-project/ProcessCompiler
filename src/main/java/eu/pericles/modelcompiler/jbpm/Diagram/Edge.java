package eu.pericles.modelcompiler.jbpm.Diagram;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bpmndi:BPMNEdge")
public class Edge {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamAsAttribute
	private String sourceElement;
	@XStreamAsAttribute
	private String targetElement;
	@XStreamImplicit
	private List<Waypoint> points;

}
