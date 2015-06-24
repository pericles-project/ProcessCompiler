package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.Data.DataInput;
import eu.pericles.modelcompiler.bpmn.Data.DataInputAssociation;
import eu.pericles.modelcompiler.bpmn.Data.InputSet;

@XStreamAlias("bpmn2:intermediateThrowEvent")
public class IntermediateThrowEvent {
	
	
	public enum Type {
		NONE, SIGNAL, MESSAGE
	}
	
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
	@XStreamAlias("bpmn2:dataInput")
	private DataInput data;
	@XStreamAlias("bpmn2:dataInputAssociation")
	private DataInputAssociation dataAssociation;
	@XStreamAlias("bpmn2:inputSet")
	private InputSet dataSet;
	
	public Type getType() {
		Type type = Type.NONE;
		
		if (getSignalEventDefinition() != null)
			type = Type.SIGNAL;
		if (getMessageEventDefinition() != null)
			type = Type.MESSAGE;
		
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

	public DataInput getData() {
		return data;
	}

	public void setData(DataInput data) {
		this.data = data;
	}

	public DataInputAssociation getDataAssociation() {
		return dataAssociation;
	}

	public void setDataAssociation(DataInputAssociation dataAssociation) {
		this.dataAssociation = dataAssociation;
	}

	public InputSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(InputSet dataSet) {
		this.dataSet = dataSet;
	}


}
