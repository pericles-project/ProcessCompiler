package eu.pericles.modelcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateCatchEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateThrowEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Gateways.ParallelGateway;
import eu.pericles.modelcompiler.bpmn.Variables.Property;

@XStreamAlias("bpmn2:process")
public class BpmnProcess {
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("processType")
	private String type;

	@XStreamImplicit
	private List<Property> properties = new ArrayList<>();
	@XStreamImplicit
	private List<StartEvent> startEvents = new ArrayList<>();
	@XStreamImplicit
	private List<EndEvent> endEvents = new ArrayList<>();
	@XStreamImplicit
	private List<IntermediateCatchEvent> intermediateCatchEvents = new ArrayList<>();
	@XStreamImplicit
	private List<IntermediateThrowEvent> intermediateThrowEvents = new ArrayList<>();
	@XStreamImplicit
	private List<ScriptTask> scriptTasks = new ArrayList<ScriptTask>();
	@XStreamImplicit
	private List<ParallelGateway> parallelGateways = new ArrayList<>();
	@XStreamImplicit
	private List<SequenceFlow> sequenceFlows = new ArrayList<>();
	@XStreamImplicit
	private List<Subprocess> subprocesses = new ArrayList<>();
	
	private List<Message> messages = new ArrayList<>();
	private List<ItemDefinition> itemDefinitions = new ArrayList<>();
	
	public BpmnProcess() {
	}
	
	//---- Getters, setters and hasElementsOnList methods ----// 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<StartEvent> getStartEvents() {
		return startEvents;
	}

	public void setStartEvents(List<StartEvent> startEvents) {
		this.startEvents = startEvents;
	}

	public List<EndEvent> getEndEvents() {
		return endEvents;
	}

	public void setEndEvents(List<EndEvent> endEvents) {
		this.endEvents = endEvents;
	}
	
	
	public List<IntermediateCatchEvent> getIntermediateCatchEvents() {
		return intermediateCatchEvents;
	}

	public void setIntermediateCatchEvents(List<IntermediateCatchEvent> intermediateCatchEvents) {
		this.intermediateCatchEvents = intermediateCatchEvents;
	}

	public List<IntermediateThrowEvent> getIntermediateThrowEvents() {
		return intermediateThrowEvents;
	}

	public void setIntermediateThrowEvents(List<IntermediateThrowEvent> intermediateThrowEvents) {
		this.intermediateThrowEvents = intermediateThrowEvents;
	}

	public List<ScriptTask> getScriptTasks() {
		return scriptTasks;
	}

	public void setScriptTasks(List<ScriptTask> scriptTasks) {
		this.scriptTasks = scriptTasks;
	}

	public List<ParallelGateway> getParallelGateways() {
		return parallelGateways;
	}

	public void setParallelGateways(List<ParallelGateway> parallelGateways) {
		this.parallelGateways = parallelGateways;
	}

	public List<SequenceFlow> getSequenceFlows() {
		return sequenceFlows;
	}

	public void setSequenceFlows(List<SequenceFlow> sequenceFlows) {
		this.sequenceFlows = sequenceFlows;
	}

	public List<Subprocess> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<Subprocess> subprocesses) {
		this.subprocesses = subprocesses;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<ItemDefinition> getItemDefinitions() {
		return itemDefinitions;
	}

	public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
		this.itemDefinitions = itemDefinitions;
	}
	
	
	public boolean hasProperties() {
		if (getProperties() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasItemDefinitions() {
		if (getItemDefinitions() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasMessages() {
		if (getMessages() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasStartEvents() {
		if (getStartEvents() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasIntermediateCatchEvents() {
		if (getIntermediateCatchEvents() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasIntermediateThrowEvents() {
		if (getIntermediateThrowEvents() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasEndEvents() {
		if (getEndEvents() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasScriptTasks() {
		if (getScriptTasks() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasSubprocesses() {
		if (getSubprocesses() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasParallelGateways() {
		if (getParallelGateways() == null)
			return false;
		else 
			return true;
	}
	
	public boolean hasSequenceFlows() {
		if (getSequenceFlows() == null)
			return false;
		else 
			return true;
	}

}
