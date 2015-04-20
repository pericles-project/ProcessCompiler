package eu.pericles.modelcompiler.bpmn;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.pericles.modelcompiler.generic.Process;

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
	private List<StartEvent> startEvents;
	@XStreamImplicit
	private List<EndEvent> endEvents;
	@XStreamImplicit
	private List<ScriptTask> scriptTasks;
	@XStreamImplicit
	private List<SequenceFlow> sequenceFlows;

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

	public List<SequenceFlow> getSequenceFlows() {
		return sequenceFlows;
	}

	public void setSequenceFlows(List<SequenceFlow> sequenceFlows) {
		this.sequenceFlows = sequenceFlows;
	}
	
	public Process convertToGenericProcess() {
		
		Process process = new Process();
		
		return process;
	}
}
