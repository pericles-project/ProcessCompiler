package eu.pericles.modelcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
	@XStreamImplicit
	private List<Subprocess> subprocesses;
	
	public BpmnProcess() {
		initialiseVariables();
	}
	
	private void initialiseVariables() {
		startEvents = new ArrayList<StartEvent>();
		endEvents = new ArrayList<EndEvent>();
		scriptTasks = new ArrayList<ScriptTask>();
		sequenceFlows = new ArrayList<SequenceFlow>();
		subprocesses = new ArrayList<Subprocess>();
	}

	public void copyBpmnProcess(BpmnProcess newProcess, BpmnProcess bpmnProcess) {
		setId(bpmnProcess.getId());
		setName(bpmnProcess.getName());
		setType(bpmnProcess.getType());
		
		if (bpmnProcess.getStartEvents() == null)
			setStartEvents(null);
		else
			setStartEvents(bpmnProcess.getStartEvents());
		
		if (bpmnProcess.getEndEvents() == null)
			setEndEvents(null);
		else
			setEndEvents(bpmnProcess.getEndEvents());
		
		if (bpmnProcess.getScriptTasks() == null)
			setScriptTasks(null);
		else
			setScriptTasks(bpmnProcess.getScriptTasks());
		
		if (bpmnProcess.getSequenceFlows() == null)
			setSequenceFlows(null);
		else
			setSequenceFlows(bpmnProcess.getSequenceFlows());
		
		System.out.println("Process ID: " + bpmnProcess.getId());
		if (bpmnProcess.getSubprocesses() == null)
		{
			System.out.println("bpmnProcess.getSubprocesses() == null");
			setSubprocesses(null);
		}
		else
		{
			System.out.println("bpmnProcess.getSubprocesses() != null");
			copyBpmnSubprocesses(newProcess, bpmnProcess);
		}
		
	}

	private void copyBpmnSubprocesses(BpmnProcess newProcess, BpmnProcess bpmnProcess) {
		System.out.println("copyBpmnSubprocesses");		
		
		for (Subprocess subprocess : bpmnProcess.getSubprocesses()) {
			System.out.println("Subprocess ID: " + subprocess.getId());
			Subprocess newSubprocess = new Subprocess();
			newSubprocess.copyBpmnProcess(newProcess, subprocess);
			System.out.println("New Subprocess: " + newSubprocess.getId());
			System.out.println("Add Subprocesses");
			
			// Porque tengo aqui el problema?????????????
			newProcess.getSubprocesses().add(newSubprocess);
			System.out.println("Leave copy Subprocesses");
		}
		
	}
	
	/** 
	 * Getters and setters 
	 * */
	
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

	public List<Subprocess> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<Subprocess> subprocesses) {
		this.subprocesses = subprocesses;
	}

}
