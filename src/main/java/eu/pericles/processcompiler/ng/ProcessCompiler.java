package eu.pericles.processcompiler.ng;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.DataInputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.DataOutputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.SourceRef;
import org.camunda.bpm.model.bpmn.impl.instance.TargetRef;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataInputAssociation;
import org.camunda.bpm.model.bpmn.instance.DataObject;
import org.camunda.bpm.model.bpmn.instance.DataOutput;
import org.camunda.bpm.model.bpmn.instance.DataOutputAssociation;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.ItemDefinition;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.Slot;
import eu.pericles.processcompiler.exceptions.BPMNFileException;

/**
 * Create a new BPMN2 process out of several reusable sub-processes, a sequence
 * flow and an input/output mapping.
 * 
 * - Collect sequence flow and input/output from ERMR
 * - Remember available inputs, required outputs
 * - Fetch all sub-processes from ERMR
 * - Extract the process ID
 * - Extract data input, data output and their names and types
 * - Create start/end and data input/output nodes in the new aggregated process.
 * - Follow the sequence of sub-processes defined in the model
 * - Insert CallActivity node
 * - Populate ioSpecification with data mappings, checking for inconsistencies
 * at the same time.
 * - Generate BPMN
 *
 */
public class ProcessCompiler {

	private BpmnModelInstance model;
	private Definitions definitions;
	private Process process;
	private StartEvent startEvent;
	private EndEvent endEvent;
	private HashMap<String, String> dataItemDefConnections = new HashMap<String, String>();

	private AtomicInteger idCounter = new AtomicInteger(1);
	
	public class ValidationResult {
		boolean valid;
		String message;
		
		public ValidationResult(boolean valid, String message) {
			this.valid = valid;
			this.message = message;
		}
		public boolean isValid() {
			return valid;
		}
		public String getMessage() {
			return message;
		}
	}

	public String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	public ValidationResult validateImplementation(eu.pericles.processcompiler.ecosystem.Process process, BPMNProcess bpmnProcess) throws BPMNFileException {
		for (Slot slot : process.getSlots()) {
			if (existValidSlot(slot, bpmnProcess) == false)
				return new ValidationResult(false, "Slot: " + slot.getId() + " is missing or invalid in BPMN file");
		}
		return new ValidationResult(true, "OK");
	}

	private boolean existValidSlot(Slot slot, BPMNProcess bpmnProcess) throws BPMNFileException {
		boolean valid = false;
		for (TDataObject dataObject : bpmnProcess.getDataObjects()) {
			TItemDefinition item = bpmnProcess.findItemDefinitionByName(dataObject.getItemSubjectRef());
			System.out.println(bpmnProcess.findItemDefinitionByName(dataObject.getItemSubjectRef()).getStructureRef());
			String dataObjectType = bpmnProcess.findItemDefinitionByName(dataObject.getItemSubjectRef()).getStructureRef().getLocalPart();
			if ((slot.getId().contains(dataObject.getId())) && (slot.getType().contains(dataObjectType)))
				valid = true;
		}
		return valid;
	}

	public void compile(PCProcess pcProcess, String outputFile) throws IOException {
		createProcessFramework(pcProcess);
		for (PCDataObject pcDataObject : pcProcess.getDataObjects())
			addDataObject(pcDataObject);
		for (PCSubprocess pcSubprocess : pcProcess.getSubprocesses())
			addSubprocess(pcSubprocess);

		if (outputFile != null)
			FileUtils.writeStringToFile(new File(outputFile), Bpmn.convertToString(model));
		else
			System.out.println(Bpmn.convertToString(model));
	}

	private void createProcessFramework(PCProcess pcProcess) {
		model = Bpmn.createEmptyModel();

		definitions = model.newInstance(Definitions.class);
		definitions.setTargetNamespace("http://camunda.org/examples");
		definitions.setId(getRandomId(Definitions.class));
		model.setDefinitions(definitions);

		process = addElement(definitions, pcProcess.getId(), Process.class);
		process.setName(pcProcess.getName());
		process.setAttributeValue("processType", pcProcess.getType());

		startEvent = addElement(process, StartEvent.class);
		endEvent = addElement(process, EndEvent.class);
	}

	private void addDataObject(PCDataObject pcDataObject) {
		ItemDefinition itemDefinition = addElement(definitions, ItemDefinition.class);
		itemDefinition.setStructureRef(pcDataObject.getType());
		DataObject dataObject = addElement(process, DataObject.class);
		dataObject.setId(pcDataObject.getId());
		dataObject.setName(pcDataObject.getName());
		dataObject.setItemSubject(itemDefinition);
		dataItemDefConnections.put(dataObject.getId(), itemDefinition.getId());
	}

	private void addSubprocess(PCSubprocess pcSubprocess) {
		CallActivity callActivity = addElement(process, CallActivity.class);
		addSequenceFlow(process, startEvent, callActivity);
		addSequenceFlow(process, callActivity, endEvent);

		callActivity.setCalledElement(pcSubprocess.getId());
		mapData(callActivity, pcSubprocess.getDataInputMap(), pcSubprocess.getDataOutputMap());
	}

	private void mapData(CallActivity callActivity, HashMap<PCDataObject, PCDataObject> dataInputMap,
			HashMap<PCDataObject, PCDataObject> dataOutputMap) {
		if (callActivity.getIoSpecification() == null) {
			addElement(callActivity, IoSpecification.class);
			addElement(callActivity.getIoSpecification(), InputSet.class);
			addElement(callActivity.getIoSpecification(), OutputSet.class);
		}

		Iterator<Entry<PCDataObject, PCDataObject>> it = dataInputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<PCDataObject, PCDataObject> inputConnection = (Map.Entry<PCDataObject, PCDataObject>) it.next();
			DataInput input = addElement(callActivity.getIoSpecification(), DataInput.class);
			input.setName(inputConnection.getValue().getId());
			input.setAttributeValue("itemSubjectRef", dataItemDefConnections.get(inputConnection.getKey().getId()));
			InputSet inputSet = callActivity.getIoSpecification().getInputSets().iterator().next();
			addElement(inputSet, null, DataInputRefs.class).setTextContent(input.getId());

			if (callActivity.getDataInputAssociations().isEmpty())
				addElement(callActivity, DataInputAssociation.class);

			DataInputAssociation dataInputAssociation = callActivity.getDataInputAssociations().iterator().next();
			addElement(dataInputAssociation, null, SourceRef.class).setTextContent(inputConnection.getKey().getId());
			addElement(dataInputAssociation, null, TargetRef.class).setTextContent(input.getId());
		}

		it = dataOutputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<PCDataObject, PCDataObject> outputConnection = (Map.Entry<PCDataObject, PCDataObject>) it.next();
			DataOutput output = addElement(callActivity.getIoSpecification(), DataOutput.class);
			output.setName(outputConnection.getValue().getId());
			output.setAttributeValue("itemSubjectRef", dataItemDefConnections.get(outputConnection.getKey().getId()));
			OutputSet outputSet = callActivity.getIoSpecification().getOutputSets().iterator().next();
			addElement(outputSet, null, DataOutputRefs.class).setTextContent(output.getId());

			if (callActivity.getDataOutputAssociations().isEmpty())
				addElement(callActivity, DataOutputAssociation.class);

			DataOutputAssociation dataOutputAssociation = callActivity.getDataOutputAssociations().iterator().next();
			addElement(dataOutputAssociation, null, SourceRef.class).setTextContent(output.getId());
			addElement(dataOutputAssociation, null, TargetRef.class).setTextContent(outputConnection.getKey().getId());
		}
	}

	private SequenceFlow addSequenceFlow(Process process, FlowNode from, FlowNode to) {
		SequenceFlow sequenceFlow = addElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
		process.addChildElement(sequenceFlow);
		sequenceFlow.setSource(from);
		from.getOutgoing().add(sequenceFlow);
		sequenceFlow.setTarget(to);
		to.getIncoming().add(sequenceFlow);
		return sequenceFlow;
	}

	protected <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, String id, Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		return element;
	}

	protected <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	protected <T extends BpmnModelElementInstance> T addElement(Collection<T> parentElement, String id, Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.add(element);
		return element;
	}

	protected <T extends BpmnModelElementInstance> T addElement(Collection<T> parentElement, Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	/*
	 * 
	 * private org.camunda.bpm.model.bpmn.instance.DataObject addInput(String
	 * name, String type) {
	 * ItemDefinition itemDef = addElement(definitions, ItemDefinition.class);
	 * itemDef.setStructureRef(type);
	 * org.camunda.bpm.model.bpmn.instance.DataObject dataObject =
	 * addElement(process,
	 * org.camunda.bpm.model.bpmn.instance.DataObject.class);
	 * dataObject.setName(name);
	 * return dataObject;
	 * }
	 * 
	 * private void mapInput(CallActivity task, Property src, String name) {
	 * 
	 * if (task.getIoSpecification() == null) {
	 * addElement(task, IoSpecification.class);
	 * addElement(task.getIoSpecification(), InputSet.class);
	 * addElement(task.getIoSpecification(), OutputSet.class);
	 * }
	 * 
	 * DataInput input = addElement(task.getIoSpecification(), DataInput.class);
	 * input.setName(name);
	 * 
	 * InputSet is = task.getIoSpecification().getInputSets().iterator().next();
	 * addElement(is, null, DataInputRefs.class).setTextContent(input.getId());
	 * 
	 * if (task.getDataInputAssociations().isEmpty())
	 * addElement(task, DataInputAssociation.class);
	 * 
	 * DataInputAssociation dias =
	 * task.getDataInputAssociations().iterator().next();
	 * addElement(dias, null, SourceRef.class).setTextContent(src.getId());
	 * addElement(dias, null, TargetRef.class).setTextContent(input.getId());
	 * 
	 * }
	 */
}
