package eu.pericles.modelcompiler.bpmn.ExternalItems;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.BpmnElement;

@XStreamAlias("bpmn2:message")
public class Message implements BpmnElement {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String itemRef;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemRef() {
		return itemRef;
	}

	public void setItemRef(String itemRef) {
		this.itemRef = itemRef;
	}

}
