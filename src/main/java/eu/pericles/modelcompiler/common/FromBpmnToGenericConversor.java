package eu.pericles.modelcompiler.common;

import java.util.HashMap;
import java.util.Map;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Gateways.ParallelGateway;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;

public class FromBpmnToGenericConversor {

	private BpmnProcess bpmnProcess;
	private Process genericProcess;
	private Map<String,String> mapBpmnIDtoGenericUID;

	public FromBpmnToGenericConversor() {
		bpmnProcess = new BpmnProcess();
		genericProcess = new Process();
		mapBpmnIDtoGenericUID = new HashMap<String,String>();
	}

	public void convertFromBpmnToGeneric(BpmnProcess bpmnProcess) {
		
		setBpmnProcess(bpmnProcess);		
		convertProcessFromBpmnToGeneric(getBpmnProcess(), getGenericProcess());
	}

	private void convertProcessFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		
		genericProcess.setName(bpmnProcess.getName());
		genericProcess.setSource(bpmnProcess.getId());	
		
		convertActivitiesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertEventsFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertGatewaysFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertSubprocessesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertFlowsFromBpmnToGeneric(bpmnProcess, genericProcess);
	}

	private void convertActivitiesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {		
		for (ScriptTask scriptTask : bpmnProcess.getScriptTasks()) {
			
			Activity activity = new Activity();
			activity.setName(scriptTask.getName());
			activity.setType(Activity.Type.SCRIPT);
			genericProcess.addActivity(activity);	
			
			mapBpmnIDtoGenericUID.put(scriptTask.getId(), activity.getUid());
		}
	}

	private void convertEventsFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (StartEvent startEvent : bpmnProcess.getStartEvents()) {	
			
			Event event = new Event();
			event.setType(Event.Type.START);
			genericProcess.addEvent(event);
			
			mapBpmnIDtoGenericUID.put(startEvent.getId(), event.getUid());
		}
		for (EndEvent endEvent : bpmnProcess.getEndEvents()) {
			
			Event event = new Event();
			event.setType(Event.Type.END);
			genericProcess.addEvent(event);
			
			mapBpmnIDtoGenericUID.put(endEvent.getId(), event.getUid());
		}
	}

	private void convertGatewaysFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (ParallelGateway parallelGateway : bpmnProcess.getParallelGateways()) {
			
			Gateway gateway = new Gateway();
			if (parallelGateway.getDirection().equals("Converging"))
				gateway.setType(Gateway.Type.CONVERGING_PARALLEL);
			else
				gateway.setType(Gateway.Type.DIVERGING_PARALLEL);
			genericProcess.addGateway(gateway);
			
			mapBpmnIDtoGenericUID.put(parallelGateway.getId(), gateway.getUid());
			
		}
	}

	private void convertFlowsFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {
			
			Flow flow = new Flow();
			flow.setFrom(mapBpmnIDtoGenericUID.get(sequenceFlow.getSource()));
			flow.setTo(mapBpmnIDtoGenericUID.get(sequenceFlow.getTarget()));
			genericProcess.addFlow(flow);
			
			mapBpmnIDtoGenericUID.put(sequenceFlow.getId(), flow.getUid());
		}
	}

	private void convertSubprocessesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (Subprocess bpmnSubprocess : bpmnProcess.getSubprocesses()) {
			
			Process genericSubprocess = new Process();			
			convertProcessFromBpmnToGeneric(bpmnSubprocess, genericSubprocess);			
			genericProcess.addSubprocess(genericSubprocess);
			
			mapBpmnIDtoGenericUID.put(bpmnSubprocess.getId(), genericSubprocess.getUid());
		}
	}
	
	//---- Getters and setters ----// 
	
	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public Process getGenericProcess() {
		return genericProcess;
	}

	public void setGenericProcess(Process genericProcess) {
		this.genericProcess = genericProcess;
	}

	public Map<String, String> getMapBpmnIDtoGenericUID() {
		return mapBpmnIDtoGenericUID;
	}

}
