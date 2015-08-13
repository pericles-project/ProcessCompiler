package eu.pericles.modelcompiler.jbpm.Diagram;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.pericles.modelcompiler.common.Element;

@XStreamAlias("bpmndi:BPMNPlane")
public class Plane implements Element {

	@XStreamAsAttribute
	@XStreamAlias("id")
	private String uid;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamImplicit
	private List<Shape> shapes;
	@XStreamImplicit
	private List<Edge> edges;

	// ---- Getters and setters ----//

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBpmnElement() {
		return bpmnElement;
	}

	public void setBpmnElement(String bpmnElement) {
		this.bpmnElement = bpmnElement;
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
}
