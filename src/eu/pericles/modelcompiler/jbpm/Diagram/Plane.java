package eu.pericles.modelcompiler.jbpm.Diagram;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bpmndi:BPMNPlane")
public class Plane {
	
	@XStreamImplicit
	private List<Shape> shapes;
	@XStreamImplicit
	private List<Edge> edges;

}
