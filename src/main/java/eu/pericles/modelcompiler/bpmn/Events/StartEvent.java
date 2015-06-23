package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:startEvent")
public class StartEvent {
	
	public enum Type {
		NONE, SIGNAL, MESSAGE, TIMER
	}

	@XStreamAsAttribute
	private String id;
	@XStreamAlias("bpmn2:outgoing")
	private String outgoing;
	@XStreamAlias("bpmn2:signalEventDefinition")
	private SignalEventDefinition signalEventDefinition;
	@XStreamAlias("bpmn2:messageEventDefinition")
	private MessageEventDefinition messageEventDefinition;
	@XStreamAlias("bpmn2:timerEventDefinition")
	private TimerEventDefinition timerEventDefinition;

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

	public TimerEventDefinition getTimerEventDefinition() {
		return timerEventDefinition;
	}

	public void setTimerEventDefinition(TimerEventDefinition timerEventDefinition) {
		this.timerEventDefinition = timerEventDefinition;
	}
	
	public Type getType() {
		Type type = Type.NONE;
		
		if (getSignalEventDefinition() != null)
			type = Type.SIGNAL;
		if (getMessageEventDefinition() != null)
			type = Type.MESSAGE;
		if (getTimerEventDefinition() != null)
			type = Type.TIMER;
		
		return type;
	}

}
