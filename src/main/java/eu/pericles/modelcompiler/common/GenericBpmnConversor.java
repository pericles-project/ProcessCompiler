package eu.pericles.modelcompiler.common;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Data.DataInput;
import eu.pericles.modelcompiler.bpmn.Data.DataInputAssociation;
import eu.pericles.modelcompiler.bpmn.Data.DataOutput;
import eu.pericles.modelcompiler.bpmn.Data.DataOutputAssociation;
import eu.pericles.modelcompiler.bpmn.Data.InputSet;
import eu.pericles.modelcompiler.bpmn.Data.OutputSet;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateCatchEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateThrowEvent;
import eu.pericles.modelcompiler.bpmn.Events.MessageEventDefinition;
import eu.pericles.modelcompiler.bpmn.Events.SignalEventDefinition;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.Events.TimeCycle;
import eu.pericles.modelcompiler.bpmn.Events.TimeDate;
import eu.pericles.modelcompiler.bpmn.Events.TimeDuration;
import eu.pericles.modelcompiler.bpmn.Events.TimerEventDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Variables.Property;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.ExternalItem;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Process;
import eu.pericles.modelcompiler.generic.Timer;
import eu.pericles.modelcompiler.generic.Variable;

public class GenericBpmnConversor {
	private Process genericProcess;
	private BpmnProcess bpmnProcess;
	private UidGeneration uidGenerator;

	public GenericBpmnConversor(UidGeneration uidGenerator) {
		init(uidGenerator);
	}

	private void init(UidGeneration uidGenerator) {
		setUidGenerator(uidGenerator);
		genericProcess = (Process) ElementFactory.createElement(getUidGenerator().requestUUID(), ElementFactory.Type.PROCESS);
		bpmnProcess = new BpmnProcess();
	}

	public void convert(Process genericProcess) {
		setGenericProcess(genericProcess);
		convertProcess(getGenericProcess(), getBpmnProcess());
	}

	private void convertProcess(Process genericProcess, BpmnProcess bpmnProcess) {
		bpmnProcess.setId(genericProcess.getSource());
		bpmnProcess.setName(genericProcess.getName());
		bpmnProcess.setType("Public");
		convertExternalItems(genericProcess, bpmnProcess);
		convertVariables(genericProcess, bpmnProcess);
		convertActivities(genericProcess, bpmnProcess);
		convertEvents(genericProcess, bpmnProcess);
		convertGateways(genericProcess, bpmnProcess);
		convertSubprocesses(genericProcess, bpmnProcess);
		convertFlows(genericProcess, bpmnProcess);

	}

	// ---- Convert External Items ----//

	private void convertExternalItems(Process genericProcess, BpmnProcess bpmnProcess) {
		for (ExternalItem externalItem : genericProcess.getExternalItems()) {
			if (externalItem.getType() == ExternalItem.Type.ITEM)
				bpmnProcess.getItemDefinitions().add(createItemDefinition(externalItem));
			if (externalItem.getType() == ExternalItem.Type.MESSAGE)
				bpmnProcess.getMessages().add(createMessage(externalItem));
		}
	}

	private ItemDefinition createItemDefinition(ExternalItem externalItem) {
		ItemDefinition itemDefinition = new ItemDefinition();
		itemDefinition.setId(externalItem.getUid());
		itemDefinition.setStructureRef(externalItem.getStructure());
		
		return itemDefinition;
	}

	private Message createMessage(ExternalItem externalItem) {
		Message message = new Message();
		message.setId(externalItem.getUid());
		message.setItemRef(externalItem.getReference());
		
		return message;
	}

	// ---- Convert Variables ----//

	private void convertVariables(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Variable variable : genericProcess.getVariables()) {
			if (variable.getType() == Variable.Type.PROPERTY)
				bpmnProcess.getProperties().add(createProperty(variable));
		}
	}

	private Property createProperty(Variable variable) {
		Property property = new Property();
		property.setId(variable.getUid());
		property.setItemSubjectRef(variable.getReference());
		
		return property;
	}

	// ---- Convert Activities ----//

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

	// ---- Convert Events ----//

	private void convertEvents(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Event event : genericProcess.getEvents()) {
			if (event.getType() == Event.Type.NONE_START)
				bpmnProcess.getStartEvents().add(createNoneStartEvent(event));
			if (event.getType() == Event.Type.NONE_END)
				bpmnProcess.getEndEvents().add(createNoneEndEvent(event));
			if (event.getType() == Event.Type.MESSAGE_START)
				bpmnProcess.getStartEvents().add(createMessageStartEvent(event));
			if (event.getType() == Event.Type.MESSAGE_CATCH)
				bpmnProcess.getIntermediateCatchEvents().add(createMessageCatchEvent(event));
			if (event.getType() == Event.Type.MESSAGE_THROW)
				bpmnProcess.getIntermediateThrowEvents().add(createMessageThrowEvent(event));
			if (event.getType() == Event.Type.MESSAGE_END)
				bpmnProcess.getEndEvents().add(createMessageEndEvent(event));
			if (event.getType() == Event.Type.SIGNAL_START)
				bpmnProcess.getStartEvents().add(createSignalStartEvent(event));
			if (event.getType() == Event.Type.SIGNAL_CATCH)
				bpmnProcess.getIntermediateCatchEvents().add(createSignalCatchEvent(event));
			if (event.getType() == Event.Type.SIGNAL_THROW)
				bpmnProcess.getIntermediateThrowEvents().add(createSignalThrowEvent(event));
			if (event.getType() == Event.Type.SIGNAL_END)
				bpmnProcess.getEndEvents().add(createSignalEndEvent(event));
			if (event.getType() == Event.Type.TIMER_START)
				bpmnProcess.getStartEvents().add(createTimerStartEvent(event));
			if (event.getType() == Event.Type.TIMER_CATCH)
				bpmnProcess.getIntermediateCatchEvents().add(createTimerCatchEvent(event));
		}
	}

	private StartEvent createNoneStartEvent(Event event) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(event.getUid());
		return startEvent;
	}

	private EndEvent createNoneEndEvent(Event event) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(event.getUid());
		return endEvent;
	}

	private StartEvent createMessageStartEvent(Event event) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(event.getUid());

		DataOutput dataOutput = new DataOutput();
		dataOutput.setId(event.getData().getUid());
		dataOutput.setItemSubjectRef(event.getData().getReference());
		DataOutputAssociation dataOutputAssociation = new DataOutputAssociation();
		dataOutputAssociation.setId(uidGenerator.requestUUID());
		dataOutputAssociation.setSource(dataOutput.getId());
		dataOutputAssociation.setTarget(event.getData().getAssociation());
		OutputSet outputSet = new OutputSet();
		outputSet.setId(uidGenerator.requestUUID());
		outputSet.setReference(dataOutput.getId());

		startEvent.setData(dataOutput);
		startEvent.setDataAssociation(dataOutputAssociation);
		startEvent.setDataSet(outputSet);

		startEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		return startEvent;
	}

	private IntermediateCatchEvent createMessageCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
		intermediateCatchEvent.setId(event.getUid());

		DataOutput dataOutput = new DataOutput();
		dataOutput.setId(event.getData().getUid());
		dataOutput.setItemSubjectRef(event.getData().getReference());
		DataOutputAssociation dataOutputAssociation = new DataOutputAssociation();
		dataOutputAssociation.setId(uidGenerator.requestUUID());
		dataOutputAssociation.setSource(dataOutput.getId());
		dataOutputAssociation.setTarget(event.getData().getAssociation());
		OutputSet outputSet = new OutputSet();
		outputSet.setId(uidGenerator.requestUUID());
		outputSet.setReference(dataOutput.getId());

		intermediateCatchEvent.setData(dataOutput);
		intermediateCatchEvent.setDataAssociation(dataOutputAssociation);
		intermediateCatchEvent.setDataSet(outputSet);

		intermediateCatchEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		return intermediateCatchEvent;
	}

	private IntermediateThrowEvent createMessageThrowEvent(Event event) {
		IntermediateThrowEvent intermediateThrowEvent = new IntermediateThrowEvent();
		intermediateThrowEvent.setId(event.getUid());

		DataInput dataInput = new DataInput();
		dataInput.setId(event.getData().getUid());
		dataInput.setItemSubjectRef(event.getData().getReference());
		DataInputAssociation dataInputAssociation = new DataInputAssociation();
		dataInputAssociation.setId(uidGenerator.requestUUID());
		dataInputAssociation.setSource(dataInput.getId());
		dataInputAssociation.setTarget(event.getData().getAssociation());
		InputSet inputSet = new InputSet();
		inputSet.setId(uidGenerator.requestUUID());
		inputSet.setReference(dataInput.getId());

		intermediateThrowEvent.setData(dataInput);
		intermediateThrowEvent.setDataAssociation(dataInputAssociation);
		intermediateThrowEvent.setDataSet(inputSet);

		intermediateThrowEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		return intermediateThrowEvent;
	}

	private EndEvent createMessageEndEvent(Event event) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(event.getUid());

		DataInput dataInput = new DataInput();
		dataInput.setId(event.getData().getUid());
		dataInput.setItemSubjectRef(event.getData().getReference());
		DataInputAssociation dataInputAssociation = new DataInputAssociation();
		dataInputAssociation.setId(uidGenerator.requestUUID());
		dataInputAssociation.setSource(dataInput.getId());
		dataInputAssociation.setTarget(event.getData().getAssociation());
		InputSet inputSet = new InputSet();
		inputSet.setId(uidGenerator.requestUUID());
		inputSet.setReference(dataInput.getId());

		endEvent.setData(dataInput);
		endEvent.setDataAssociation(dataInputAssociation);
		endEvent.setDataSet(inputSet);

		endEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		return endEvent;
	}

	private MessageEventDefinition createMessageEventDefinition(Event event) {
		MessageEventDefinition messageEventDefinition = new MessageEventDefinition();
		messageEventDefinition.setId(uidGenerator.requestUUID());
		messageEventDefinition.setMessageRef(event.getReference());

		return messageEventDefinition;
	}

	private StartEvent createSignalStartEvent(Event event) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(event.getUid());
		startEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		return startEvent;
	}

	private IntermediateCatchEvent createSignalCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
		intermediateCatchEvent.setId(event.getUid());
		intermediateCatchEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		return intermediateCatchEvent;
	}

	private IntermediateThrowEvent createSignalThrowEvent(Event event) {
		IntermediateThrowEvent intermediateThrowEvent = new IntermediateThrowEvent();
		intermediateThrowEvent.setId(event.getUid());
		intermediateThrowEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		return intermediateThrowEvent;
	}

	private EndEvent createSignalEndEvent(Event event) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(event.getUid());
		endEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		return endEvent;
	}

	private SignalEventDefinition createSignalEventDefinition(Event event) {
		SignalEventDefinition signalEventDefinition = new SignalEventDefinition();
		signalEventDefinition.setId(uidGenerator.requestUUID());
		signalEventDefinition.setSignalRef(event.getReference());

		return signalEventDefinition;
	}

	private StartEvent createTimerStartEvent(Event event) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(event.getUid());
		startEvent.setTimerEventDefinition(createTimerEventDefinition(event));

		return startEvent;
	}

	private IntermediateCatchEvent createTimerCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
		intermediateCatchEvent.setId(event.getUid());
		intermediateCatchEvent.setTimerEventDefinition(createTimerEventDefinition(event));

		return intermediateCatchEvent;
	}

	private TimerEventDefinition createTimerEventDefinition(Event event) {
		TimerEventDefinition timerEventDefinition = new TimerEventDefinition();
		timerEventDefinition.setId(uidGenerator.requestUUID());
		if (event.getTimer().getType() == Timer.Type.CYCLE) {
			TimeCycle timeCycle = new TimeCycle();
			timeCycle.setId(uidGenerator.requestUUID());
			timeCycle.setType(event.getTimer().getTimeType());
			timeCycle.setTime(event.getTimer().getTime());
			timerEventDefinition.setTimeCycle(timeCycle);
		}
		if (event.getTimer().getType() == Timer.Type.DURATION) {
			TimeDuration timeDuration = new TimeDuration();
			timeDuration.setId(uidGenerator.requestUUID());
			timeDuration.setType(event.getTimer().getTimeType());
			timeDuration.setTime(event.getTimer().getTime());
			timerEventDefinition.setTimeDuration(timeDuration);
		}
		if (event.getTimer().getType() == Timer.Type.CYCLE) {
			TimeDate timeDate = new TimeDate();
			timeDate.setId(uidGenerator.requestUUID());
			timeDate.setType(event.getTimer().getTimeType());
			timeDate.setTime(event.getTimer().getTime());
			timerEventDefinition.setTimeDate(timeDate);
		}

		return timerEventDefinition;
	}

	// ---- Convert Gateways ----//

	private void convertGateways(Process genericProcess, BpmnProcess bpmnProcess) {

	}

	// ---- Convert Subprocesses ----//

	private void convertSubprocesses(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Process subprocess : genericProcess.getSubprocesses()) {
			bpmnProcess.getSubprocesses().add(createSubprocess(subprocess));
			convertSubprocess(genericProcess, bpmnProcess);
		}
	}

	private Subprocess createSubprocess(Process genericSubprocess) {
		Subprocess bpmnSubprocess = new Subprocess();
		bpmnSubprocess.setId(genericSubprocess.getUid());
		bpmnSubprocess.setName(genericSubprocess.getName());
		
		convertActivities(genericSubprocess, bpmnSubprocess);
		convertEvents(genericSubprocess, bpmnSubprocess);
		convertGateways(genericSubprocess, bpmnSubprocess);
		convertSubprocesses(genericSubprocess, bpmnSubprocess);
		convertFlows(genericSubprocess, bpmnSubprocess);
		
		return bpmnSubprocess;
	}

	private void convertSubprocess(Process genericProcess, BpmnProcess bpmnProcess) {
		bpmnProcess.setId(genericProcess.getSource());
		bpmnProcess.setName(genericProcess.getName());
		bpmnProcess.setType("Public");
		convertExternalItems(genericProcess, bpmnProcess);
		convertVariables(genericProcess, bpmnProcess);
		

	}

	// ---- Convert Flows ----//

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

	// ---- Getters and setters ----//

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

	public UidGeneration getUidGenerator() {
		return uidGenerator;
	}

	public void setUidGenerator(UidGeneration uidGenerator) {
		this.uidGenerator = uidGenerator;
	}

}
