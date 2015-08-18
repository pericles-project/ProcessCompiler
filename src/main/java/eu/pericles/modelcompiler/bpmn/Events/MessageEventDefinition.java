package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.BpmnElement;

@XStreamAlias("bpmn2:messageEventDefinition")
public class MessageEventDefinition implements BpmnElement {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String messageRef;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageRef() {
		return messageRef;
	}
	public void setMessageRef(String messageRef) {
		this.messageRef = messageRef;
	}

}
