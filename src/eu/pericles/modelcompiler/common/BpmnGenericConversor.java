package eu.pericles.modelcompiler.common;

import java.util.HashMap;
import java.util.Map;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.EndEvent;
import eu.pericles.modelcompiler.bpmn.ScriptTask;
import eu.pericles.modelcompiler.bpmn.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.StartEvent;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Process;

public class BpmnGenericConversor {
	
	private BpmnProcess bpmnProcess;
	private Process genericProcess;
	private Map<String,String> mapBpmnIDtoGenericUID;
	
	public BpmnGenericConversor() {
		bpmnProcess = new BpmnProcess();
		genericProcess = new Process();
		mapBpmnIDtoGenericUID = new HashMap<String,String>();
	}
	
	public void convertFromBpmnToGeneric(BpmnProcess bpmnProcess) {
		setBpmnProcess(bpmnProcess);
		
		convertProcessAttributesFromBpmnToGeneric();
		convertActivitiesFromBpmnToGeneric();
		convertEventsFromBpmnToGeneric();
		convertGatewaysFromBpmnToGeneric();
		convertFlowsFromBpmnToGeneric();
	}

	private void convertProcessAttributesFromBpmnToGeneric() {
		genericProcess.setName(bpmnProcess.getName());
		genericProcess.setSource(bpmnProcess.getId());		
	}

	private void convertActivitiesFromBpmnToGeneric() {
		for (ScriptTask scriptTask : bpmnProcess.getScriptTasks()) {			
			Activity activity = new Activity();		
			
			activity.setName(scriptTask.getName());
			activity.setType(Activity.Type.SCRIPT);
			
			genericProcess.addActivity(activity);			
			mapBpmnIDtoGenericUID.put(scriptTask.getId(), activity.getUid());
			
		}
		
	}
	
	private void convertEventsFromBpmnToGeneric() {
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

	private void convertGatewaysFromBpmnToGeneric() {
		// TODO Auto-generated method stub
		
	}

	private void convertFlowsFromBpmnToGeneric() {
		for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {						
			Flow flow = new Flow();
			
			flow.setFrom(mapBpmnIDtoGenericUID.get(sequenceFlow.getSource()));
			flow.setTo(mapBpmnIDtoGenericUID.get(sequenceFlow.getTarget()));
			
			genericProcess.addFlow(flow);
			mapBpmnIDtoGenericUID.put(sequenceFlow.getId(), flow.getUid());
		}
		
	}

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
