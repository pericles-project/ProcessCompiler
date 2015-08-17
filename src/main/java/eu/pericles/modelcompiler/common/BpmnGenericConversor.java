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
	private Map<String, String> idUidMap;
	private static UidGeneration uidGenerator;

	public BpmnGenericConversor(UidGeneration uidGenerator) {
		init(uidGenerator);
	}

	private void init(UidGeneration uidGenerator) {
		setUidGenerator(uidGenerator);
		bpmnProcess = new BpmnProcess();
		genericProcess = (Process) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.PROCESS);
		idUidMap = new HashMap<String, String>();
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

				addPairIdUidToMap(itemDefinition.getId(), item.getUid());
			}
		}
	}

	private void convertMessages(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasMessages()) {
			for (Message message : bpmnProcess.getMessages()) {

				ExternalItem item = (ExternalItem) ElementFactory.createElement(getUidGenerator().requestUUID(),
						ElementFactory.Type.EXTERNAL_ITEM);
				item.setType(ExternalItem.Type.MESSAGE);
				item.setReference(idUidMap.get(message.getItemRef()));
				genericProcess.addExternalItem(item);

				addPairIdUidToMap(message.getId(), item.getUid());
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
				variable.setReference(idUidMap.get(property.getItemSubjectRef()));
				genericProcess.addVariable(variable);

				addPairIdUidToMap(property.getId(), variable.getUid());
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

				addPairIdUidToMap(scriptTask.getId(), activity.getUid());
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
					event.setReference(idUidMap.get(startEvent.getSignalEventDefinition().getSignalRef()));
					if (startEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(startEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(startEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (startEvent.getType() == StartEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_START);
					event.setReference(idUidMap.get(startEvent.getMessageEventDefinition().getMessageRef()));
					if (startEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(startEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(startEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (startEvent.getType() == StartEvent.Type.TIMER) {
					event.setType(Event.Type.TIMER_START);
					event.setTimer(createGenericTimer(startEvent.getTimerEventDefinition()));
				}
				genericProcess.addEvent(event);

				addPairIdUidToMap(startEvent.getId(), event.getUid());
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
					event.setReference(idUidMap.get(endEvent.getSignalEventDefinition().getSignalRef()));
					if (endEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(endEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(endEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (endEvent.getType() == EndEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_END);
					event.setReference(idUidMap.get(endEvent.getMessageEventDefinition().getMessageRef()));
					if (endEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(endEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(endEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				genericProcess.addEvent(event);

				addPairIdUidToMap(endEvent.getId(), event.getUid());
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
					event.setReference(idUidMap.get(catchEvent.getSignalEventDefinition().getSignalRef()));
					if (catchEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(catchEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(catchEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (catchEvent.getType() == IntermediateCatchEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_CATCH);
					event.setReference(idUidMap.get(catchEvent.getMessageEventDefinition().getMessageRef()));
					if (catchEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(catchEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(catchEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (catchEvent.getType() == IntermediateCatchEvent.Type.TIMER) {
					event.setType(Event.Type.TIMER_CATCH);
					event.setTimer(createGenericTimer(catchEvent.getTimerEventDefinition()));
				}
				genericProcess.addEvent(event);

				addPairIdUidToMap(catchEvent.getId(), event.getUid());
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
					event.setReference(idUidMap.get(throwEvent.getSignalEventDefinition().getSignalRef()));
					if (throwEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(throwEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(throwEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				if (throwEvent.getType() == IntermediateThrowEvent.Type.MESSAGE) {
					event.setType(Event.Type.MESSAGE_THROW);
					event.setReference(idUidMap.get(throwEvent.getMessageEventDefinition().getMessageRef()));
					if (throwEvent.hasDataAssociated()) {
						Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
						data.setReference(idUidMap.get(throwEvent.getData().getItemSubjectRef()));
						data.setAssociation(idUidMap.get(throwEvent.getDataAssociation().getTarget()));
						event.setData(data);
					}
				}
				genericProcess.addEvent(event);

				addPairIdUidToMap(throwEvent.getId(), event.getUid());
			}
		}
	}
	

	private Timer createGenericTimer(TimerEventDefinition timerEventDefinition) {
		Timer timer = (Timer) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.TIMER);

		if (timerEventDefinition.getTimeCycle() != null) {
			timer.setValues(Type.CYCLE, timerEventDefinition.getTimeCycle().getTime(),
					timerEventDefinition.getTimeCycle().getType(), timerEventDefinition.getTimeCycle().getLanguage());
		}
		else if (timerEventDefinition.getTimeDate() != null) {
			timer.setValues(Type.DATE, timerEventDefinition.getTimeDate().getTime(),
					timerEventDefinition.getTimeDate().getType(), timerEventDefinition.getTimeDate().getLanguage());
		}
		else if (timerEventDefinition.getTimeDuration() != null) {
			timer.setValues(Type.DURATION, timerEventDefinition.getTimeDuration().getTime(),
					timerEventDefinition.getTimeDuration().getType(), timerEventDefinition.getTimeDuration().getLanguage());
		}
		else {
			//TODO throw here an exception
		}

		return timer;
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

				addPairIdUidToMap(parallelGateway.getId(), gateway.getUid());

			}
		}
	}

	private void convertFlows(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasSequenceFlows()) {
			for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {

				Flow flow = (Flow) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.FLOW);
				flow.setFrom(idUidMap.get(sequenceFlow.getSource()));
				flow.setTo(idUidMap.get(sequenceFlow.getTarget()));
				genericProcess.addFlow(flow);

				addPairIdUidToMap(sequenceFlow.getId(), flow.getUid());
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

				addPairIdUidToMap(bpmnSubprocess.getId(), genericSubprocess.getUid());
			}
		}
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

	public Map<String, String> getIdUidMap() {
		return idUidMap;
	}
	
	public void addPairIdUidToMap(String id, String uid) {
		getIdUidMap().put(id, uid);
	}

	public static UidGeneration getUidGenerator() {
		return uidGenerator;
	}

	public static void setUidGenerator(UidGeneration uidGenerator) {
		BpmnGenericConversor.uidGenerator = uidGenerator;
	}

}
