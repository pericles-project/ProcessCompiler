package eu.pericles.modelcompiler.common;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Process;

public class GenericBpmnConversor {
	private Process genericProcess;
	private BpmnProcess bpmnProcess;
	
	public GenericBpmnConversor() {
		init();
	}

	private void init() {
		genericProcess = new Process();
		bpmnProcess = new BpmnProcess();
	}
	
	public void convert(Process genericProcess) {
		setGenericProcess(genericProcess);
		convertProcess(getGenericProcess(),getBpmnProcess());
	}

	private void convertProcess(Process genericProcess, BpmnProcess bpmnProcess) {
		System.out.println("Genreic Process: " + genericProcess.getName() + " " + genericProcess.getSource());
		bpmnProcess.setId(genericProcess.getSource());
		bpmnProcess.setName(genericProcess.getName());
		bpmnProcess.setType("Public");
		System.out.println("Bpmn Process: " + bpmnProcess.getName() + " " + bpmnProcess.getId());
		convertExternalItems(genericProcess, bpmnProcess);
		convertVariables(genericProcess, bpmnProcess);
		convertActivities(genericProcess, bpmnProcess);
		convertEvents(genericProcess, bpmnProcess);
		convertGateways(genericProcess, bpmnProcess);
		convertSubprocesses(genericProcess, bpmnProcess);
		convertFlows(genericProcess, bpmnProcess);
		
	}

	private void convertExternalItems(Process genericProcess, BpmnProcess bpmnProcess) {
		
	}
	
	private void convertVariables(Process genericProcess, BpmnProcess bpmnProcess) {
		
	}
	
	private void convertActivities(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Activity activity : genericProcess.getActivities()) {
			if (activity.getType() == Activity.Type.SCRIPT) 
				bpmnProcess.getScriptTasks().add(createScriptTask(activity));
		}
	}
	
	private ScriptTask createScriptTask(Activity activity) {
		ScriptTask scriptTask = new ScriptTask();
		scriptTask.setId(activity.getUid());
		scriptTask.setName(activity.getName());
		scriptTask.setScript(activity.getScript());
		
		return scriptTask;
	}

	private void convertEvents(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Event event : genericProcess.getEvents()) {
			if (event.getType() == Event.Type.NONE_START)
				bpmnProcess.getStartEvents().add(createNoneStart(event));
			if (event.getType() == Event.Type.NONE_END)
				bpmnProcess.getEndEvents().add(createNoneEnd(event));
		}
	}
	
	private StartEvent createNoneStart(Event event) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(event.getUid());
		return startEvent;
	}
	
	private EndEvent createNoneEnd(Event event) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(event.getUid());
		return endEvent;
	}

	private void convertGateways(Process genericProcess, BpmnProcess bpmnProcess) {
		
	}
	
	private void convertSubprocesses(Process genericProcess, BpmnProcess bpmnProcess) {
		
	}
	
	private void convertFlows(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Flow flow : genericProcess.getFlows()) {
			bpmnProcess.getSequenceFlows().add(createSequenceFlow(flow));
		}
	}

	private SequenceFlow createSequenceFlow(Flow flow) {
		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setId(flow.getUid());
		sequenceFlow.setSource(flow.getFrom());
		sequenceFlow.setTarget(flow.getTo());
		
		return sequenceFlow;
	}

	//---- Getters and setters ----// 

	public Process getGenericProcess() {
		return genericProcess;
	}

	public void setGenericProcess(Process genericProcess) {
		this.genericProcess = genericProcess;
	}

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

}
