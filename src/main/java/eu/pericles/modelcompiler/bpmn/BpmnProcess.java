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
	private List<Property> properties;
	@XStreamImplicit
	private List<StartEvent> startEvents;
	@XStreamImplicit
	private List<EndEvent> endEvents;
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

	@XStreamImplicit
	private List<IntermediateCatchEvent> intermediateCatchEvents;
	@XStreamImplicit
	private List<IntermediateThrowEvent> intermediateThrowEvents;
	@XStreamImplicit
	private List<ScriptTask> scriptTasks;
	@XStreamImplicit
	private List<ParallelGateway> parallelGateways;
	@XStreamImplicit
	private List<SequenceFlow> sequenceFlows;
	@XStreamImplicit
	private List<Subprocess> subprocesses;
	
	private List<Message> messages;
	private List<ItemDefinition> itemDefinitions;
	
	public BpmnProcess() {
		init();		
	}
	
	private void init() {
		properties = new ArrayList<Property>();
		startEvents = new ArrayList<StartEvent>();
		endEvents = new ArrayList<EndEvent>();
		intermediateCatchEvents = new ArrayList<IntermediateCatchEvent>();
		intermediateThrowEvents = new ArrayList<IntermediateThrowEvent>();
		scriptTasks = new ArrayList<ScriptTask>();
		parallelGateways = new ArrayList<ParallelGateway>();
		sequenceFlows = new ArrayList<SequenceFlow>();
		subprocesses = new ArrayList<Subprocess>();
	}
	
	/** 
	 * Check if there is any list of bpmn elements pointing to null. 
	 * If so, create the corresponding empty list.
	 */
	public void checkAndComplete() {
		
		if (getProperties() == null)
			setProperties(new ArrayList<Property>());
		
		if (getStartEvents() == null)
			setStartEvents(new ArrayList<StartEvent>());
		
		if (getEndEvents() == null)
			setEndEvents(new ArrayList<EndEvent>());
		
		if (getIntermediateCatchEvents() == null)
			setIntermediateCatchEvents(new ArrayList<IntermediateCatchEvent>());
		
		if (getIntermediateThrowEvents() == null)
			setIntermediateThrowEvents(new ArrayList<IntermediateThrowEvent>());
		
		if (getScriptTasks() == null)
			setScriptTasks(new ArrayList<ScriptTask>());
		
		if (getParallelGateways() == null)
			setParallelGateways(new ArrayList<ParallelGateway>());
		
		if (getSequenceFlows() == null)
			setSequenceFlows(new ArrayList<SequenceFlow>());
		
		if (getSubprocesses() == null) {
			setSubprocesses(new ArrayList<Subprocess>());
		}
		else {
			for (Subprocess subprocess : getSubprocesses()) {
				subprocess.checkAndComplete();			
			}
		}
	}
	
	//---- Getters and setters ----// 
	
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

}
