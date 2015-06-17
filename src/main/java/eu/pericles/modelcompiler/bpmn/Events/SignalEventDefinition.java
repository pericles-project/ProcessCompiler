package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:signalEventDefinition")
public class SignalEventDefinition {
	
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
