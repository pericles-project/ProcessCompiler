package eu.pericles.modelcompiler.bpmn;

import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateCatchEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateThrowEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Variables.Property;


public class BpmnElementFactory {
	public enum Type {
		ITEM_DEFINITION, MESSAGE, PROPERTY, START_EVENT, CATCH_EVENT, THROW_EVENT, END_EVENT, SCRIPT_TASK, SUBPROCESS,
		SEQUENCE_FLOW
	}
	
	public static BpmnElement createBpmnElement(String id, Type elementType) {
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

}
