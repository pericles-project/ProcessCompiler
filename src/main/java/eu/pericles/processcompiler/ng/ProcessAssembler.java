package eu.pericles.processcompiler.ng;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.DataInputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.SourceRef;
import org.camunda.bpm.model.bpmn.impl.instance.TargetRef;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataInputAssociation;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.ItemDefinition;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.Property;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * Create a new BPMN2 process out of several reusable sub-processes, a sequence
 * flow and an input/output mapping.
 * 
 * - Collect sequence flow and input/output from ERMR
 *   - Remember available inputs, required outputs
 * - Fetch all sub-processes from ERMR
 *   - Extract the process ID
 *   - Extract data input, data output and their names and types
 * - Create start/end and data input/output nodes in the new aggregated process.
 * - Follow the sequence of sub-processes defined in the model
 *   - Insert CallActivity node
 *   - Populate ioSpecification with data mappings, checking for inconsistencies at the same time.
 * - Generate BPMN
 *
 */
public class ProcessAssembler {

	private BpmnModelInstance model;
	private Definitions definitions;
	private Process process;
	private StartEvent startEvent;
	private EndEvent endEvent;
	
	private AtomicInteger idCounter = new AtomicInteger(1);

	public ProcessAssembler() {

		model = Bpmn.createEmptyModel();

		definitions = model.newInstance(Definitions.class);
		definitions.setTargetNamespace("http://camunda.org/examples");
		definitions.setId(getRandomId(Definitions.class));
		model.setDefinitions(definitions);
		process = addElement(definitions, Process.class);
		startEvent = addElement(process, StartEvent.class);
		endEvent = addElement(process, EndEvent.class);

		CallActivity callActivity = addElement(process, CallActivity.class);
		addSequenceFlow(process, startEvent, callActivity);
		addSequenceFlow(process, callActivity, endEvent);

		Property p = addInput("inputName", "inputType");
		mapInput(callActivity, p, "localName");

		System.out.println(Bpmn.convertToString(model));
	}
	
	public String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	public static void main(String[] args) {
		new ProcessAssembler();
	}

	private Property addInput(String name, String type) {
		ItemDefinition itemDef = addElement(definitions, ItemDefinition.class);
		itemDef.setStructureRef(type);
		Property prop = addElement(process, Property.class);
		prop.setName(name);
		return prop;
	}

	private void mapInput(CallActivity task, Property src, String name) {

		if (task.getIoSpecification() == null) {
			addElement(task, IoSpecification.class);
			addElement(task.getIoSpecification(), InputSet.class);
			addElement(task.getIoSpecification(), OutputSet.class);
		}

		IoSpecification ios = task.getIoSpecification();

		DataInput input = addElement(task.getIoSpecification(), DataInput.class);
		input.setName(name);

		InputSet is = task.getIoSpecification().getInputSets().iterator().next();
		addElement(is, null, DataInputRefs.class).setTextContent(input.getId());

		if (task.getDataInputAssociations().isEmpty())
			addElement(task, DataInputAssociation.class);

		DataInputAssociation dias = task.getDataInputAssociations().iterator().next();
		addElement(dias, null, SourceRef.class).setTextContent(src.getId());
		addElement(dias, null, TargetRef.class).setTextContent(input.getId());

	}

	protected <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, String id,
			Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		return element;
	}

	protected <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement,
			Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	protected <T extends BpmnModelElementInstance> T addElement(Collection<T> parentElement, String id,
			Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.add(element);
		return element;
	}

	protected <T extends BpmnModelElementInstance> T addElement(Collection<T> parentElement, Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	public SequenceFlow addSequenceFlow(Process process, FlowNode from, FlowNode to) {
		SequenceFlow sequenceFlow = addElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
		process.addChildElement(sequenceFlow);
		sequenceFlow.setSource(from);
		from.getOutgoing().add(sequenceFlow);
		sequenceFlow.setTarget(to);
		to.getIncoming().add(sequenceFlow);
		return sequenceFlow;
	}

}
