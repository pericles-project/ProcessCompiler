package eu.pericles.modelcompiler.common;

import eu.pericles.modelcompiler.bpmn.BpmnElementFactory;
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
			switch (externalItem.getType()) {
			case ITEM:
				bpmnProcess.getItemDefinitions().add(createItemDefinition(externalItem));
				break;
			case MESSAGE:
				bpmnProcess.getMessages().add(createMessage(externalItem));
				break;
			default:
				// TODO throw here an exception
				break;
			}
		}
	}

	private ItemDefinition createItemDefinition(ExternalItem externalItem) {
		return (ItemDefinition) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.ITEM_DEFINITION, externalItem.getUid(),
				externalItem.getStructure());
	}

	private Message createMessage(ExternalItem externalItem) {
		return (Message) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.MESSAGE, externalItem.getUid(),
				externalItem.getReference());
	}

	// ---- Convert Variables ----//

	private void convertVariables(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Variable variable : genericProcess.getVariables()) {
			switch (variable.getType()) {
			case PROPERTY:
				bpmnProcess.getProperties().add(createProperty(variable));
				break;
			default:
				// TODO throw here an exception
				break;
			}
		}
	}

	private Property createProperty(Variable variable) {
		return (Property) BpmnElementFactory
				.createBpmnElement(BpmnElementFactory.Type.PROPERTY, variable.getUid(), variable.getReference());
	}

	// ---- Convert Activities ----//

	private void convertActivities(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Activity activity : genericProcess.getActivities()) {
			switch (activity.getType()) {
			case SCRIPT:
				bpmnProcess.getScriptTasks().add(createScriptTask(activity));
				break;
			default:
				// TODO throw here an exception
				break;
			}
		}
	}

	private ScriptTask createScriptTask(Activity activity) {
		ScriptTask scriptTask = (ScriptTask) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.SCRIPT_TASK, activity.getUid());
		scriptTask.setName(activity.getName());
		scriptTask.setScript(activity.getScript());

		return scriptTask;
	}

	// ---- Convert Events ----//

	private void convertEvents(Process genericProcess, BpmnProcess bpmnProcess) {
		for (Event event : genericProcess.getEvents()) {
			switch (event.getType()) {
			case NONE_START:
				bpmnProcess.getStartEvents().add(createNoneStartEvent(event));
				break;
			case NONE_CATCH:
				bpmnProcess.getIntermediateCatchEvents().add(createNoneCatchEvent(event));
				break;
			case NONE_THROW:
				bpmnProcess.getIntermediateThrowEvents().add(createNoneThrowEvent(event));
				break;
			case NONE_END:
				bpmnProcess.getEndEvents().add(createNoneEndEvent(event));
				break;
			case MESSAGE_START:
				bpmnProcess.getStartEvents().add(createMessageStartEvent(event));
				break;
			case MESSAGE_CATCH:
				bpmnProcess.getIntermediateCatchEvents().add(createMessageCatchEvent(event));
				break;
			case MESSAGE_THROW:
				bpmnProcess.getIntermediateThrowEvents().add(createMessageThrowEvent(event));
				break;
			case MESSAGE_END:
				bpmnProcess.getEndEvents().add(createMessageEndEvent(event));
				break;
			case SIGNAL_START:
				bpmnProcess.getStartEvents().add(createSignalStartEvent(event));
				break;
			case SIGNAL_CATCH:
				bpmnProcess.getIntermediateCatchEvents().add(createSignalCatchEvent(event));
				break;
			case SIGNAL_THROW:
				bpmnProcess.getIntermediateThrowEvents().add(createSignalThrowEvent(event));
				break;
			case SIGNAL_END:
				bpmnProcess.getEndEvents().add(createSignalEndEvent(event));
				break;
			case TIMER_START:
				bpmnProcess.getStartEvents().add(createTimerStartEvent(event));
				break;
			case TIMER_CATCH:
				bpmnProcess.getIntermediateCatchEvents().add(createTimerCatchEvent(event));
				break;
			default:
				// TODO throw here an exception
				break;
			}
		}
	}

	private StartEvent createNoneStartEvent(Event event) {
		return (StartEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.START_EVENT, event.getUid());
	}

	private IntermediateCatchEvent createNoneCatchEvent(Event event) {
		return (IntermediateCatchEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.CATCH_EVENT, event.getUid());
	}

	private IntermediateThrowEvent createNoneThrowEvent(Event event) {
		return (IntermediateThrowEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.THROW_EVENT, event.getUid());
	}

	private EndEvent createNoneEndEvent(Event event) {
		return (EndEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.END_EVENT, event.getUid());
	}

	private StartEvent createMessageStartEvent(Event event) {
		StartEvent startEvent = (StartEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.START_EVENT, event.getUid());
		startEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		if (event.hasData()) {
			startEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			startEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			startEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return startEvent;
	}

	private IntermediateCatchEvent createMessageCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.CATCH_EVENT, event.getUid());
		intermediateCatchEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		if (event.hasData()) {
			intermediateCatchEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			intermediateCatchEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData()
					.getAssociation()));
			intermediateCatchEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return intermediateCatchEvent;
	}

	private IntermediateThrowEvent createMessageThrowEvent(Event event) {
		IntermediateThrowEvent intermediateThrowEvent = (IntermediateThrowEvent) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.THROW_EVENT, event.getUid());
		intermediateThrowEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		if (event.hasData()) {
			intermediateThrowEvent.setData(createDataInput(event.getData().getUid(), event.getData().getReference()));
			intermediateThrowEvent
					.setDataAssociation(createDataInputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			intermediateThrowEvent.setDataSet(createInputSet(event.getData().getUid()));
		}
		return intermediateThrowEvent;
	}

	private EndEvent createMessageEndEvent(Event event) {
		EndEvent endEvent = (EndEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.END_EVENT, event.getUid());
		endEvent.setMessageEventDefinition(createMessageEventDefinition(event));

		if (event.hasData()) {
			endEvent.setData(createDataInput(event.getData().getUid(), event.getData().getReference()));
			endEvent.setDataAssociation(createDataInputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			endEvent.setDataSet(createInputSet(event.getData().getUid()));
		}
		return endEvent;
	}

	private MessageEventDefinition createMessageEventDefinition(Event event) {
		return (MessageEventDefinition) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.MESSAGE_EVENT_DEFINITION,
				getUidGenerator().requestUUID(), event.getReference());
	}

	private DataInput createDataInput(String id, String reference) {
		return (DataInput) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.DATA_INPUT, id, reference);
	}

	private DataInputAssociation createDataInputAssociation(String source, String target) {
		DataInputAssociation dataInputAssociation = (DataInputAssociation) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.DATA_INPUT_ASSOCIATION, getUidGenerator().requestUUID());
		dataInputAssociation.setSource(source);
		dataInputAssociation.setTarget(target);

		return dataInputAssociation;
	}

	private InputSet createInputSet(String reference) {
		return (InputSet) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.INPUT_SET, getUidGenerator().requestUUID(),
				reference);
	}

	private DataOutput createDataOutput(String id, String reference) {
		return (DataOutput) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.DATA_OUTPUT, id, reference);
	}

	private DataOutputAssociation createDataOutputAssociation(String source, String target) {
		DataOutputAssociation dataOutputAssociation = (DataOutputAssociation) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.DATA_OUTPUT_ASSOCIATION, getUidGenerator().requestUUID());
		dataOutputAssociation.setSource(source);
		dataOutputAssociation.setTarget(target);

		return dataOutputAssociation;
	}

	private OutputSet createOutputSet(String reference) {
		return (OutputSet) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.OUTPUT_SET, getUidGenerator().requestUUID(),
				reference);
	}

	private StartEvent createSignalStartEvent(Event event) {
		StartEvent startEvent = (StartEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.START_EVENT, event.getUid());
		startEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		if (event.hasData()) {
			startEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			startEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			startEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return startEvent;
	}

	private IntermediateCatchEvent createSignalCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.CATCH_EVENT, event.getUid());
		intermediateCatchEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		if (event.hasData()) {
			intermediateCatchEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			intermediateCatchEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData()
					.getAssociation()));
			intermediateCatchEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return intermediateCatchEvent;
	}

	private IntermediateThrowEvent createSignalThrowEvent(Event event) {
		IntermediateThrowEvent intermediateThrowEvent = (IntermediateThrowEvent) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.THROW_EVENT, event.getUid());
		intermediateThrowEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		if (event.hasData()) {
			intermediateThrowEvent.setData(createDataInput(event.getData().getUid(), event.getData().getReference()));
			intermediateThrowEvent
					.setDataAssociation(createDataInputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			intermediateThrowEvent.setDataSet(createInputSet(event.getData().getUid()));
		}
		return intermediateThrowEvent;
	}

	private EndEvent createSignalEndEvent(Event event) {
		EndEvent endEvent = (EndEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.END_EVENT, event.getUid());
		endEvent.setSignalEventDefinition(createSignalEventDefinition(event));

		if (event.hasData()) {
			endEvent.setData(createDataInput(event.getData().getUid(), event.getData().getReference()));
			endEvent.setDataAssociation(createDataInputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			endEvent.setDataSet(createInputSet(event.getData().getUid()));
		}
		return endEvent;
	}

	private SignalEventDefinition createSignalEventDefinition(Event event) {
		return (SignalEventDefinition) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.SIGNAL_EVENT_DEFINITION,
				getUidGenerator().requestUUID(), event.getReference());
	}

	private StartEvent createTimerStartEvent(Event event) {
		StartEvent startEvent = (StartEvent) BpmnElementFactory.createBpmnElement(BpmnElementFactory.Type.START_EVENT, event.getUid());
		startEvent.setTimerEventDefinition(createTimerEventDefinition(event));

		if (event.hasData()) {
			startEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			startEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData().getAssociation()));
			startEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return startEvent;
	}

	private IntermediateCatchEvent createTimerCatchEvent(Event event) {
		IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.CATCH_EVENT, event.getUid());
		intermediateCatchEvent.setTimerEventDefinition(createTimerEventDefinition(event));

		if (event.hasData()) {
			intermediateCatchEvent.setData(createDataOutput(event.getData().getUid(), event.getData().getReference()));
			intermediateCatchEvent.setDataAssociation(createDataOutputAssociation(event.getData().getUid(), event.getData()
					.getAssociation()));
			intermediateCatchEvent.setDataSet(createOutputSet(event.getData().getUid()));
		}
		return intermediateCatchEvent;
	}

	private TimerEventDefinition createTimerEventDefinition(Event event) {
		TimerEventDefinition timerEventDefinition = (TimerEventDefinition) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.TIMER_EVENT_DEFINITION, getUidGenerator().requestUUID());
		switch (event.getTimer().getType()) {
		case CYCLE:
			timerEventDefinition.setTimeCycle((TimeCycle) BpmnElementFactory.createBpmnTimeElement(BpmnElementFactory.Type.TIME_CYCLE,
					getUidGenerator().requestUUID(), event.getTimer().getTime(), event.getTimer().getTimeType()));
			break;
		case DURATION:
			timerEventDefinition.setTimeDuration((TimeDuration) BpmnElementFactory.createBpmnTimeElement(BpmnElementFactory.Type.TIME_DURATION,
					getUidGenerator().requestUUID(), event.getTimer().getTime(), event.getTimer().getTimeType()));
			break;
		case DATE:
			timerEventDefinition.setTimeDate((TimeDate) BpmnElementFactory.createBpmnTimeElement(BpmnElementFactory.Type.TIME_DATE,
					getUidGenerator().requestUUID(), event.getTimer().getTime(), event.getTimer().getTimeType()));
			break;
		default:
			// TODO throw an exception here
			break;
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
		Subprocess bpmnSubprocess = (Subprocess) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.SUBPROCESS, getUidGenerator().requestUUID());
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
		SequenceFlow sequenceFlow = (SequenceFlow) BpmnElementFactory.createBpmnElement(
				BpmnElementFactory.Type.SEQUENCE_FLOW, getUidGenerator().requestUUID());
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
