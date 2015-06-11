package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.SignalEventDefinition;

@XStreamAlias("bpmn2:startEvent")
public class StartEvent {

	@XStreamAsAttribute
	private String id;
	@XStreamAlias("bpmn2:outgoing")
	private String outgoing;
	@XStreamAlias("bpmn2:signalEventDefinition")
	private SignalEventDefinition signalEventDefinition;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
