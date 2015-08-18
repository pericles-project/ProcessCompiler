package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.BpmnElement;

@XStreamAlias("bpmn2:signalEventDefinition")
public class SignalEventDefinition implements BpmnElement {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String signalRef;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSignalRef() {
		return signalRef;
	}
	public void setSignalRef(String signalRef) {
		this.signalRef = signalRef;
	}
	

}
