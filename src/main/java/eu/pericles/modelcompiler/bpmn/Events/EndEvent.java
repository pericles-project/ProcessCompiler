package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:endEvent")
public class EndEvent {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("bpmn2:incoming")
	private String incoming;
	@XStreamAlias("bpmn2:terminateEventDefinition")
	private String terminateEventDefinition;
	@XStreamAlias("bpmn2:signalEventDefinition")
	private SignalEventDefinition signalEventDefinition;
	@XStreamAlias("bpmn2:messageEventDefinition")
	private MessageEventDefinition messageEventDefinition;
	//@XStreamAlias("bpmn2:timerEventDefinition")
	//private TimerEventDefinition timerEventDefinition;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SignalEventDefinition getSignalEventDefinition() {
		return signalEventDefinition;
	}

	public void setSignalEventDefinition(SignalEventDefinition signalEventDefinition) {
		this.signalEventDefinition = signalEventDefinition;
	}

	public MessageEventDefinition getMessageEventDefinition() {
		return messageEventDefinition;
	}

	public void setMessageEventDefinition(MessageEventDefinition messageEventDefinition) {
		this.messageEventDefinition = messageEventDefinition;
	}
/*
	public TimerEventDefinition getTimerEventDefinition() {
		return timerEventDefinition;
	}

	public void setTimerEventDefinition(TimerEventDefinition timerEventDefinition) {
		this.timerEventDefinition = timerEventDefinition;
	}
*/
}
