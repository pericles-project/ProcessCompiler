package eu.pericles.modelcompiler.bpmn;

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

public class BpmnElementFactory {
	public enum Type {
		ITEM_DEFINITION, MESSAGE, PROPERTY, DATA_INPUT, DATA_OUTPUT, DATA_INPUT_ASSOCIATION, DATA_OUTPUT_ASSOCIATION, INPUT_SET, OUTPUT_SET, START_EVENT, CATCH_EVENT, THROW_EVENT, END_EVENT, MESSAGE_EVENT_DEFINITION, SIGNAL_EVENT_DEFINITION, TIMER_EVENT_DEFINITION, TIME_CYCLE, TIME_DURATION, TIME_DATE, SCRIPT_TASK, SUBPROCESS, SEQUENCE_FLOW
	}

	public static BpmnElement createBpmnElement(Type elementType, String id) {
		switch (elementType) {
		case ITEM_DEFINITION:
			ItemDefinition itemDefinition = new ItemDefinition();
			itemDefinition.setId(id);
			return itemDefinition;
		case MESSAGE:
			Message message = new Message();
			message.setId(id);
			return message;
		case PROPERTY:
			Property property = new Property();
			property.setId(id);
			return property;
		case DATA_INPUT:
			DataInput dataInput = new DataInput();
			dataInput.setId(id);
			return dataInput;
		case DATA_OUTPUT:
			DataOutput dataOutput = new DataOutput();
			dataOutput.setId(id);
			return dataOutput;
		case DATA_INPUT_ASSOCIATION:
			DataInputAssociation dataInputAssociation = new DataInputAssociation();
			dataInputAssociation.setId(id);
			return dataInputAssociation;
		case DATA_OUTPUT_ASSOCIATION:
			DataOutputAssociation dataOutputAssociation = new DataOutputAssociation();
			dataOutputAssociation.setId(id);
			return dataOutputAssociation;
		case INPUT_SET:
			InputSet inputSet = new InputSet();
			inputSet.setId(id);
			return inputSet;
		case OUTPUT_SET:
			OutputSet outputSet = new OutputSet();
			outputSet.setId(id);
			return outputSet;
		case START_EVENT:
			StartEvent startEvent = new StartEvent();
			startEvent.setId(id);
			return startEvent;
		case CATCH_EVENT:
			IntermediateCatchEvent catchEvent = new IntermediateCatchEvent();
			catchEvent.setId(id);
			return catchEvent;
		case THROW_EVENT:
			IntermediateThrowEvent throwEvent = new IntermediateThrowEvent();
			throwEvent.setId(id);
			return throwEvent;
		case END_EVENT:
			EndEvent endEvent = new EndEvent();
			endEvent.setId(id);
			return endEvent;
		case MESSAGE_EVENT_DEFINITION:
			MessageEventDefinition messageEventDefinition = new MessageEventDefinition();
			messageEventDefinition.setId(id);
			return messageEventDefinition;
		case SIGNAL_EVENT_DEFINITION:
			SignalEventDefinition signalEventDefinition = new SignalEventDefinition();
			signalEventDefinition.setId(id);
			return signalEventDefinition;
		case TIMER_EVENT_DEFINITION:
			TimerEventDefinition timerEventDefinition = new TimerEventDefinition();
			timerEventDefinition.setId(id);
			return timerEventDefinition;
		case SCRIPT_TASK:
			ScriptTask scriptTask = new ScriptTask();
			scriptTask.setId(id);
			return scriptTask;
		case SUBPROCESS:
			Subprocess subprocess = new Subprocess();
			subprocess.setId(id);
			return subprocess;
		case SEQUENCE_FLOW:
			SequenceFlow sequenceFlow = new SequenceFlow();
			sequenceFlow.setId(id);
			return sequenceFlow;
		default:
			// TODO throw an exception here
			return null;
		}
	}

	public static BpmnElement createBpmnElement(Type elementType, String id, String reference) {
		switch (elementType) {
		case ITEM_DEFINITION:
			ItemDefinition itemDefinition = new ItemDefinition();
			itemDefinition.setId(id);
			itemDefinition.setStructureRef(reference);
			return itemDefinition;
		case MESSAGE:
			Message message = new Message();
			message.setId(id);
			message.setItemRef(reference);
			return message;
		case PROPERTY:
			Property property = new Property();
			property.setId(id);
			property.setItemSubjectRef(reference);
			return property;
		case DATA_INPUT:
			DataInput dataInput = new DataInput();
			dataInput.setId(id);
			dataInput.setItemSubjectRef(reference);
			return dataInput;
		case DATA_OUTPUT:
			DataOutput dataOutput = new DataOutput();
			dataOutput.setId(id);
			dataOutput.setItemSubjectRef(reference);
			return dataOutput;
		case INPUT_SET:
			InputSet inputSet = new InputSet();
			inputSet.setId(id);
			inputSet.setReference(reference);
			return inputSet;
		case OUTPUT_SET:
			OutputSet outputSet = new OutputSet();
			outputSet.setId(id);
			outputSet.setReference(reference);
			return outputSet;
		case MESSAGE_EVENT_DEFINITION:
			MessageEventDefinition messageEventDefinition = new MessageEventDefinition();
			messageEventDefinition.setId(id);
			messageEventDefinition.setMessageRef(reference);
			return messageEventDefinition;
		case SIGNAL_EVENT_DEFINITION:
			SignalEventDefinition signalEventDefinition = new SignalEventDefinition();
			signalEventDefinition.setId(id);
			signalEventDefinition.setSignalRef(reference);
			return signalEventDefinition;
		default:
			// TODO throw an exception here
			return null;
		}
	}

	public static BpmnElement createBpmnTimeElement(Type elementType, String id, String timeExpression, String timeType) {
		switch (elementType) {
		case TIME_CYCLE:
			TimeCycle timeCycle = new TimeCycle();
			timeCycle.setId(id);
			timeCycle.setType(timeType);
			timeCycle.setTime(timeExpression);
			return timeCycle;
		case TIME_DURATION:
			TimeDuration timeDuration = new TimeDuration();
			timeDuration.setId(id);
			timeDuration.setType(timeType);
			timeDuration.setTime(timeExpression);
			return timeDuration;
		case TIME_DATE:
			TimeDate timeDate = new TimeDate();
			timeDate.setId(id);
			timeDate.setType(timeType);
			timeDate.setTime(timeExpression);
			return timeDate;
		default:
			// TODO throw an exception here
			return null;
		}
	}
}
