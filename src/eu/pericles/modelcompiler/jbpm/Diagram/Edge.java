package eu.pericles.modelcompiler.jbpm.Diagram;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bpmndi:BPMNEdge")
public class Edge {
	
	@XStreamImplicit
	private List<Waypoint> points;

}
