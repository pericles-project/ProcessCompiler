package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:endEvent")
public class EndEvent {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("bpmn2:incoming")
	private String incoming;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
