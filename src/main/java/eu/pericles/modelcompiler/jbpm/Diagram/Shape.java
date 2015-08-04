package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmndi:BPMNShape")
public class Shape {

	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamAlias("dc:Bounds")
	private Bounds bounds;
}
