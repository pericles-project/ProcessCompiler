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
		/** The order on how the conversion methods are called matters, as some elements refer to other. At least, 
		 *  the following order has to be assure:
		 *  1- ItemDefinitions
		 *  2- Other external items: Messages, Signals, etc.
		 *  3- Process variables: Properties, Data, etc.
		 *  4- Process elements: Activities, Events, Gateways, Subprocesses
		 *  5- Flows
		 */
		convertExternalItems(getBpmnProcess(), getGenericProcess());
		convertProcess(getBpmnProcess(), getGenericProcess());
	}

	// ---- Convert External Items ----//
	
	private void convertExternalItems(BpmnProcess bpmnProcess, Process genericProcess) {
		convertItems(bpmnProcess, genericProcess);
		convertMessages(bpmnProcess, genericProcess);
	}

	// ---- Convert Items ----//
	
	private void convertItems(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasItemDefinitions()) {
			for (ItemDefinition itemDefinition : bpmnProcess.getItemDefinitions()) {
				genericProcess.addExternalItem(createItemDefinition(itemDefinition));
			}
		}
	}
	
	private ExternalItem createItemDefinition(ItemDefinition itemDefinition) {
		ExternalItem item = (ExternalItem) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EXTERNAL_ITEM);
		item.setType(ExternalItem.Type.ITEM);
		item.setStructure(itemDefinition.getStructureRef());
		
		addPairIdUidToMap(itemDefinition.getId(), item.getUid());
		
		return item;
	}

	// ---- Convert Messages ----//

	private void convertMessages(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasMessages()) {
			for (Message message : bpmnProcess.getMessages()) {
				genericProcess.addExternalItem(createMessage(message));
			}
		}
	}
	
	private ExternalItem createMessage(Message message) {
		ExternalItem item = (ExternalItem) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EXTERNAL_ITEM);
		item.setType(ExternalItem.Type.MESSAGE);
		item.setReference(getIdUidMap().get(message.getItemRef()));
		
		addPairIdUidToMap(message.getId(), item.getUid());
		
		return item;
	}

	// ---- Convert Process ----//

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
	
	// ---- Convert Variables ----//

	private void convertVariables(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasProperties()) {
			for (Property property : bpmnProcess.getProperties()) {
				genericProcess.addVariable(createProperty(property));
			}
		}
	}

	private Variable createProperty(Property property) {
		Variable variable = (Variable) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.VARIABLE);
		variable.setType(Variable.Type.PROPERTY);
		variable.setReference(getIdUidMap().get(property.getItemSubjectRef()));
		
		addPairIdUidToMap(property.getId(), variable.getUid());
		
		return variable;
	}
	
	// ---- Convert Activities ----//

	private void convertActivities(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasScriptTasks()) {
			for (ScriptTask scriptTask : bpmnProcess.getScriptTasks()) {
				genericProcess.addActivity(createScriptActivity(scriptTask));
			}
		}
	}

	private Activity createScriptActivity(ScriptTask scriptTask) {
		Activity activity = (Activity) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.ACTIVITY);
		activity.setType(Activity.Type.SCRIPT);
		activity.setName(scriptTask.getName());
		activity.setScript(scriptTask.getScript());
		
		addPairIdUidToMap(scriptTask.getId(), activity.getUid());
		
		return activity;
	}
	
	// ---- Convert Events ----//

	private void convertEvents(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasStartEvents()) {
			for (StartEvent startEvent : bpmnProcess.getStartEvents()) {
				genericProcess.addEvent(createStartEvent(startEvent));
			}
		}
		if (bpmnProcess.hasEndEvents()) {
			for (EndEvent endEvent : bpmnProcess.getEndEvents()) {
				genericProcess.addEvent(createEndEvent(endEvent));
			}
		}
		if (bpmnProcess.hasIntermediateCatchEvents()) {
			for (IntermediateCatchEvent catchEvent : bpmnProcess.getIntermediateCatchEvents()) {
				genericProcess.addEvent(createCatchEvent(catchEvent));
			}
		}
		if (bpmnProcess.hasIntermediateThrowEvents()) {
			for (IntermediateThrowEvent throwEvent : bpmnProcess.getIntermediateThrowEvents()) {
				genericProcess.addEvent(createThrowEvent(throwEvent));
			}
		}
	}
	

	private Event createStartEvent(StartEvent startEvent) {
		Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
		switch (startEvent.getType()) {
		case NONE:
			event.setType(Event.Type.NONE_START);
			break;
		case SIGNAL:
			event.setType(Event.Type.SIGNAL_START);
			event.setReference(getIdUidMap().get(startEvent.getSignalEventDefinition().getSignalRef()));
			break;
		case MESSAGE:
			event.setType(Event.Type.MESSAGE_START);
			event.setReference(getIdUidMap().get(startEvent.getMessageEventDefinition().getMessageRef()));
			break;
		case TIMER:
			event.setType(Event.Type.TIMER_START);
			event.setTimer(createGenericTimer(startEvent.getTimerEventDefinition()));
			break;
		default:
			//TODO throw here an exception
			break;
		}
			
		if (startEvent.hasDataAssociated()) 
			event.setData(createEventData(getIdUidMap().get(startEvent.getData().getItemSubjectRef()), getIdUidMap().get(startEvent.getDataAssociation().getTarget())));
		
		addPairIdUidToMap(startEvent.getId(), event.getUid());
		
		return event;
	}
	
	private Event createCatchEvent(IntermediateCatchEvent catchEvent) {
		Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
		switch (catchEvent.getType()) {
		case NONE:
			event.setType(Event.Type.NONE_CATCH);
			break;
		case SIGNAL:
			event.setType(Event.Type.SIGNAL_CATCH);
			event.setReference(getIdUidMap().get(catchEvent.getSignalEventDefinition().getSignalRef()));
			break;
		case MESSAGE:
			event.setType(Event.Type.MESSAGE_CATCH);
			event.setReference(getIdUidMap().get(catchEvent.getMessageEventDefinition().getMessageRef()));
			break;
		case TIMER:
			event.setType(Event.Type.TIMER_CATCH);
			event.setTimer(createGenericTimer(catchEvent.getTimerEventDefinition()));
			break;
		default:
			//TODO throw here an exception
			break;
		}
			
		if (catchEvent.hasDataAssociated()) 
			event.setData(createEventData(getIdUidMap().get(catchEvent.getData().getItemSubjectRef()), getIdUidMap().get(catchEvent.getDataAssociation().getTarget())));
		
		addPairIdUidToMap(catchEvent.getId(), event.getUid());
		
		return event;
	}
	
	private Event createThrowEvent(IntermediateThrowEvent throwEvent) {
		Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
		switch (throwEvent.getType()) {
		case NONE:
			event.setType(Event.Type.NONE_THROW);
			break;
		case SIGNAL:
			event.setType(Event.Type.SIGNAL_THROW);
			event.setReference(getIdUidMap().get(throwEvent.getSignalEventDefinition().getSignalRef()));
			break;
		case MESSAGE:
			event.setType(Event.Type.MESSAGE_THROW);
			event.setReference(getIdUidMap().get(throwEvent.getMessageEventDefinition().getMessageRef()));
			break;
		default:
			//TODO throw here an exception
			break;
		}
			
		if (throwEvent.hasDataAssociated()) 
			event.setData(createEventData(getIdUidMap().get(throwEvent.getData().getItemSubjectRef()), getIdUidMap().get(throwEvent.getDataAssociation().getTarget())));
		
		addPairIdUidToMap(throwEvent.getId(), event.getUid());
		
		return event;
	}

	private Event createEndEvent(EndEvent endEvent) {
		Event event = (Event) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.EVENT);
		switch (endEvent.getType()) {
		case NONE:
			event.setType(Event.Type.NONE_END);
			break;
		case SIGNAL:
			event.setType(Event.Type.SIGNAL_END);
			event.setReference(getIdUidMap().get(endEvent.getSignalEventDefinition().getSignalRef()));
			break;
		case MESSAGE:
			event.setType(Event.Type.MESSAGE_END);
			event.setReference(getIdUidMap().get(endEvent.getMessageEventDefinition().getMessageRef()));
			break;
		default:
			//TODO throw here an exception
			break;
		}
			
		if (endEvent.hasDataAssociated()) 
			event.setData(createEventData(getIdUidMap().get(endEvent.getData().getItemSubjectRef()), getIdUidMap().get(endEvent.getDataAssociation().getTarget())));
		
		addPairIdUidToMap(endEvent.getId(), event.getUid());
		
		return event;
	}
	
	private Data createEventData(String dataReference, String dataAssociation) {
		Data data = (Data) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.DATA);
		data.setReference(dataReference);
		data.setAssociation(dataAssociation);
		
		return data;
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

	// ---- Convert Gateways ----//
	
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

	// ---- Convert Flows ----//
	
	private void convertFlows(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasSequenceFlows()) {
			for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {
				genericProcess.addFlow(createFlow(sequenceFlow));
			}
		}
	}

	private Flow createFlow(SequenceFlow sequenceFlow) {
		Flow flow = (Flow) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.FLOW);
		flow.setFrom(getIdUidMap().get(sequenceFlow.getSource()));
		flow.setTo(getIdUidMap().get(sequenceFlow.getTarget()));
		
		addPairIdUidToMap(sequenceFlow.getId(), flow.getUid());
		
		return flow;
	}
	
	// ---- Convert Subprocesses ----//

	private void convertSubprocesses(BpmnProcess bpmnProcess, Process genericProcess) {
		if (bpmnProcess.hasSubprocesses()) {
			for (Subprocess bpmnSubprocess : bpmnProcess.getSubprocesses()) {
				genericProcess.addSubprocess(createSubprocess(bpmnSubprocess));
			}
		}
	}

	private Process createSubprocess(Subprocess bpmnSubprocess) {
		Process genericSubprocess = (Process) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.PROCESS);
		convertProcess(bpmnSubprocess, genericSubprocess);
		
		addPairIdUidToMap(bpmnSubprocess.getId(), genericSubprocess.getUid());
		
		return genericSubprocess;
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
