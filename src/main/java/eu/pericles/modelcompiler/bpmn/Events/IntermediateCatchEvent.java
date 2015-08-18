package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.BpmnElement;
import eu.pericles.modelcompiler.bpmn.Data.DataOutput;
import eu.pericles.modelcompiler.bpmn.Data.DataOutputAssociation;
import eu.pericles.modelcompiler.bpmn.Data.OutputSet;

@XStreamAlias("bpmn2:intermediateCatchEvent")
public class IntermediateCatchEvent implements BpmnElement {
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
	@XStreamAlias("bpmn2:dataOutput")
	private DataOutput data;
	@XStreamAlias("bpmn2:dataOutputAssociation")
	private DataOutputAssociation dataAssociation;
	@XStreamAlias("bpmn2:outputSet")
	private OutputSet dataSet;
	
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
	
	public boolean hasDataAssociated() {
		if (getData() != null)
			return true;
		else
			return false;
	}

	//---- Getters and setters ----// 
	
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
	
	public DataOutput getData() {
		return data;
	}

	public void setData(DataOutput data) {
		this.data = data;
	}

	public DataOutputAssociation getDataAssociation() {
		return dataAssociation;
	}

	public void setDataAssociation(DataOutputAssociation dataAssociation) {
		this.dataAssociation = dataAssociation;
	}

	public OutputSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(OutputSet dataSet) {
		this.dataSet = dataSet;
	}
	

}
