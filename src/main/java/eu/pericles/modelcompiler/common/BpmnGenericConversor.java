package eu.pericles.modelcompiler.common;

import java.util.HashMap;
import java.util.Map;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateCatchEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateThrowEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.Events.TimerEventDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Gateways.ParallelGateway;
import eu.pericles.modelcompiler.bpmn.Variables.Property;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Data;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.ExternalItem;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;
import eu.pericles.modelcompiler.generic.Timer;
import eu.pericles.modelcompiler.generic.Timer.Type;
import eu.pericles.modelcompiler.generic.Variable;

public class BpmnGenericConversor {

	private BpmnProcess bpmnProcess;
	private Process genericProcess;
	private Map<String, String> mapBpmnIDtoGenericUID;
	private static UidGeneration uidGenerator;

	public BpmnGenericConversor(UidGeneration uidGenerator) {
		init(uidGenerator);
	}

	private void init(UidGeneration uidGenerator) {
		setUidGenerator(uidGenerator);
		bpmnProcess = new BpmnProcess();
		genericProcess = (Process) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.PROCESS);
		mapBpmnIDtoGenericUID = new HashMap<String, String>();
	}

	public void convert(BpmnProcess bpmnProcess) {

		setBpmnProcess(bpmnProcess);
		convertExternalItems(getBpmnProcess(), getGenericProcess());
		convertProcess(getBpmnProcess(), getGenericProcess());
	}

	private void convertExternalItems(BpmnProcess bpmnProcess, Process genericProcess) {
		convertItems(bpmnProcess, genericProcess);
		convertMessages(bpmnProcess, genericProcess);

	}

	private void convertItems(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasItemDefinitions()) {
			for (ItemDefinition itemDefinition : bpmnProcess.getItemDefinitions()) {

				ExternalItem item = (ExternalItem) ElementFactory.createElement(getUidGenerator().requestUUID(),
						ElementFactory.Type.EXTERNAL_ITEM);
				item.setType(ExternalItem.Type.ITEM);
				item.setStructure(itemDefinition.getStructureRef());
				genericProcess.addExternalItem(item);

				mapBpmnIDtoGenericUID.put(itemDefinition.getId(), item.getUid());
			}
		}

	}

	private void convertMessages(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasMessages()) {
			for (Message message : bpmnProcess.getMessages()) {

				ExternalItem item = (ExternalItem) ElementFactory.createElement(getUidGenerator().requestUUID(),
						ElementFactory.Type.EXTERNAL_ITEM);
				item.setType(ExternalItem.Type.MESSAGE);
				item.setReference(mapBpmnIDtoGenericUID.get(message.getItemRef()));
				genericProcess.addExternalItem(item);

				mapBpmnIDtoGenericUID.put(message.getId(), item.getUid());
			}
		}

	}

	private void convertProcess(BpmnProcess bpmnProcess, Process genericProcess) {

		genericProcess.setName(bpmnProcess.getName());
		genericProcess.setSource(bpmnProcess.getId());

		convertVariables(bpmnProcess, genericProcess);
		convertActivities(bpmnProcess, genericProcess);
		convertEvents(bpmnProcess, genericProcess);
		convertGateways(bpmnProcess, genericProcess);
		convertSubprocesses(bpmnProcess, genericProcess);
		convertFlows(bpmnProcess, genericProcess);
	}

	private void convertVariables(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasProperties()) {
			for (Property property : bpmnProcess.getProperties()) {

				Variable variable = (Variable) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.VARIABLE);
				variable.setType(Variable.Type.PROPERTY);
				variable.setReference(mapBpmnIDtoGenericUID.get(property.getItemSubjectRef()));
				genericProcess.addVariable(variable);

				mapBpmnIDtoGenericUID.put(property.getId(), variable.getUid());
			}
		}
	}

	private void convertActivities(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasScriptTasks()) {
			for (ScriptTask scriptTask : bpmnProcess.getScriptTasks()) {

				Activity activity = (Activity) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.ACTIVITY);
				activity.setName(scriptTask.getName());
				activity.setType(Activity.Type.SCRIPT);
				activity.setScript(scriptTask.getScript());
				genericProcess.addActivity(activity);

				mapBpmnIDtoGenericUID.put(scriptTask.getId(), activity.getUid());
			}
		}
	}

	private void convertEvents(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasStartEvents()) {
			for (StartEvent startEvent : bpmnProcess.getStartEvents()) {

				Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
				if (startEvent.getType() == StartEvent.Type.NONE) {
					event.setType(Event.Type.NONE_START);
				}
				if (startEvent.getType() == StartEvent.Type.SIGNAL) {
					event.setType(Event.Type.SIGNAL_START);
					event.setReference(mapBpmnIDtoGenericUID.get(startEvent.getSignalEventDefinition().getSignalRef()));
					if (startEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(startEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(startEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (startEvent.getType() == StartEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_START);
					event.setReference(mapBpmnIDtoGenericUID.get(startEvent.getMessageEventDefinition().getMessageRef()));
					if (startEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(startEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(startEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (startEvent.getType() == StartEvent.Type.TIMER) {
					event.setType(Event.Type.TIMER_START);
					event.setTimer(createGenericTimer(startEvent.getTimerEventDefinition()));
				}
				genericProcess.addEvent(event);

				mapBpmnIDtoGenericUID.put(startEvent.getId(), event.getUid());
			}
		}
		if (bpmnProcess.hasEndEvents()) {
			for (EndEvent endEvent : bpmnProcess.getEndEvents()) {

				Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
				if (endEvent.getType() == EndEvent.Type.NONE) {
					event.setType(Event.Type.NONE_END);
				}
				if (endEvent.getType() == EndEvent.Type.SIGNAL) {
					event.setType(Event.Type.SIGNAL_END);
					event.setReference(mapBpmnIDtoGenericUID.get(endEvent.getSignalEventDefinition().getSignalRef()));
					if (endEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(endEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(endEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (endEvent.getType() == EndEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_END);
					event.setReference(mapBpmnIDtoGenericUID.get(endEvent.getMessageEventDefinition().getMessageRef()));
					if (endEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(endEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(endEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				genericProcess.addEvent(event);

				mapBpmnIDtoGenericUID.put(endEvent.getId(), event.getUid());
			}
		}
		if (bpmnProcess.hasIntermediateCatchEvents()) {
			for (IntermediateCatchEvent catchEvent : bpmnProcess.getIntermediateCatchEvents()) {

				Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
				if (catchEvent.getType() == IntermediateCatchEvent.Type.NONE) {
					event.setType(Event.Type.NONE_CATCH);
				}
				if (catchEvent.getType() == IntermediateCatchEvent.Type.SIGNAL) {
					event.setType(Event.Type.SIGNAL_CATCH);
					event.setReference(mapBpmnIDtoGenericUID.get(catchEvent.getSignalEventDefinition().getSignalRef()));
					if (catchEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(catchEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(catchEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (catchEvent.getType() == IntermediateCatchEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_CATCH);
					event.setReference(mapBpmnIDtoGenericUID.get(catchEvent.getMessageEventDefinition().getMessageRef()));
					if (catchEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(catchEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(catchEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (catchEvent.getType() == IntermediateCatchEvent.Type.TIMER) {
					event.setType(Event.Type.TIMER_CATCH);
					event.setTimer(createGenericTimer(catchEvent.getTimerEventDefinition()));
				}
				genericProcess.addEvent(event);

				mapBpmnIDtoGenericUID.put(catchEvent.getId(), event.getUid());
			}
		}
		if (bpmnProcess.hasIntermediateThrowEvents()) {
			for (IntermediateThrowEvent throwEvent : bpmnProcess.getIntermediateThrowEvents()) {

				Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
				if (throwEvent.getType() == IntermediateThrowEvent.Type.NONE) {
					event.setType(Event.Type.NONE_THROW);
				}
				if (throwEvent.getType() == IntermediateThrowEvent.Type.SIGNAL) {
					event.setType(Event.Type.SIGNAL_THROW);
					event.setReference(mapBpmnIDtoGenericUID.get(throwEvent.getSignalEventDefinition().getSignalRef()));
					if (throwEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(throwEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(throwEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (throwEvent.getType() == IntermediateThrowEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_THROW);
					event.setReference(mapBpmnIDtoGenericUID.get(throwEvent.getMessageEventDefinition().getMessageRef()));
					if (throwEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(mapBpmnIDtoGenericUID.get(throwEvent.getData().getItemSubjectRef()));
						data.setAssociation(mapBpmnIDtoGenericUID.get(throwEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				genericProcess.addEvent(event);

				mapBpmnIDtoGenericUID.put(throwEvent.getId(), event.getUid());
			}
		}
	}

	private void convertGateways(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasParallelGateways()) {
			for (ParallelGateway parallelGateway : bpmnProcess.getParallelGateways()) {

				Gateway gateway = (Gateway) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.GATEWAY);
				if (parallelGateway.getDirection().equals("Converging"))
					gateway.setType(Gateway.Type.CONVERGING_PARALLEL);
				else
					gateway.setType(Gateway.Type.DIVERGING_PARALLEL);
				genericProcess.addGateway(gateway);

				mapBpmnIDtoGenericUID.put(parallelGateway.getId(), gateway.getUid());

			}
		}
	}

	private void convertFlows(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasSequenceFlows()) {
			for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {

				Flow flow = (Flow) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.FLOW);
				flow.setFrom(mapBpmnIDtoGenericUID.get(sequenceFlow.getSource()));
				flow.setTo(mapBpmnIDtoGenericUID.get(sequenceFlow.getTarget()));
				genericProcess.addFlow(flow);

				mapBpmnIDtoGenericUID.put(sequenceFlow.getId(), flow.getUid());
			}
		}
	}

	private void convertSubprocesses(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasSubprocesses()) {
			for (Subprocess bpmnSubprocess : bpmnProcess.getSubprocesses()) {

				Process genericSubprocess = (Process) ElementFactory.createElement(getUidGenerator().requestUUID(),
						ElementFactory.Type.PROCESS);
				convertProcess(bpmnSubprocess, genericSubprocess);
				genericProcess.addSubprocess(genericSubprocess);

				mapBpmnIDtoGenericUID.put(bpmnSubprocess.getId(), genericSubprocess.getUid());
			}
		}
	}

	private Timer createGenericTimer(TimerEventDefinition timerEventDefinition) {
		Timer timer = (Timer) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.TIMER);

		if (timerEventDefinition.getTimeCycle() != null) {
			timer.setType(Type.CYCLE);
			timer.setTime(timerEventDefinition.getTimeCycle().getTime());
			timer.setTimeType(timerEventDefinition.getTimeCycle().getType());
			timer.setLanguage(timerEventDefinition.getTimeCycle().getLanguage());
		}
		if (timerEventDefinition.getTimeDate() != null) {
			timer.setType(Type.DATE);
			timer.setTime(timerEventDefinition.getTimeDate().getTime());
			timer.setTimeType(timerEventDefinition.getTimeDate().getType());
			timer.setLanguage(timerEventDefinition.getTimeDate().getLanguage());
		}
		if (timerEventDefinition.getTimeDuration() != null) {
			timer.setType(Type.DURATION);
			timer.setTime(timerEventDefinition.getTimeDuration().getTime());
			timer.setTimeType(timerEventDefinition.getTimeDuration().getType());
			timer.setLanguage(timerEventDefinition.getTimeDuration().getLanguage());
		}

		return timer;
	}

	// ---- Getters and setters ----//

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

	public static UidGeneration getUidGenerator() {
		return uidGenerator;
	}

	public static void setUidGenerator(UidGeneration uidGenerator) {
		BpmnGenericConversor.uidGenerator = uidGenerator;
	}

}
