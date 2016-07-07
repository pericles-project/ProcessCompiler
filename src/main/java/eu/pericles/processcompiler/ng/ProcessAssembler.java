package eu.pericles.processcompiler.ng;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.ProcessType;
import org.camunda.bpm.model.bpmn.impl.instance.DataInputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.DataOutputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.SourceRef;
import org.camunda.bpm.model.bpmn.impl.instance.TargetRef;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataInputAssociation;
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
import org.camunda.bpm.model.bpmn.instance.Property;
import org.camunda.bpm.model.bpmn.instance.RootElement;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.eclipse.jdt.internal.compiler.ProcessTaskManager;

import eu.pericles.processcompiler.ng.DataObject;

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
public class ProcessAssembler {

	private BpmnModelInstance model;
	private Definitions definitions;
	private Process process;
	private StartEvent startEvent;
	private EndEvent endEvent;
	private HashMap<String, String> dataItemDefConnections = new HashMap<String, String>();

	private AtomicInteger idCounter = new AtomicInteger(1);

	public static void main(String[] args) {
		
	}

	public String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}
	
	public void assemble(AssembledProcess assembledProcess, String outputFile) throws IOException {

		model = Bpmn.createEmptyModel();

		definitions = model.newInstance(Definitions.class);
		definitions.setTargetNamespace("http://camunda.org/examples");
		definitions.setId(getRandomId(Definitions.class));
		model.setDefinitions(definitions);
		process = addElement(definitions, assembledProcess.getId(), Process.class);
		process.setName(assembledProcess.getName());
		process.setAttributeValue("processType", assembledProcess.getType());;
		startEvent = addElement(process, StartEvent.class);
		endEvent = addElement(process, EndEvent.class);
		
		for (DataObject data : assembledProcess.getDataObjects()) {
			ItemDefinition itemDefinition = addElement(definitions, ItemDefinition.class);
			itemDefinition.setStructureRef(data.getType());
			org.camunda.bpm.model.bpmn.instance.DataObject dataObject = addElement(process, org.camunda.bpm.model.bpmn.instance.DataObject.class);
			dataObject.setId(data.getId());
			dataObject.setName(data.getName());
			dataObject.setItemSubject(itemDefinition);
			dataItemDefConnections.put(dataObject.getId(), itemDefinition.getId());
		}

		for (Subprocess subprocess : assembledProcess.getSubprocesses()) {
			CallActivity callActivity = addElement(process, CallActivity.class);
			addSequenceFlow(process, startEvent, callActivity);
			addSequenceFlow(process, callActivity, endEvent);
			
			callActivity.setCalledElement(subprocess.getProcessID());
			mapData(callActivity, subprocess.getDataInputMap(), subprocess.getDataOutputMap());
		}
		if (outputFile != null) {
			FileUtils.writeStringToFile(new File(outputFile), Bpmn.convertToString(model));
		} else
			System.out.println(Bpmn.convertToString(model));
	}

	private void mapData(CallActivity callActivity, HashMap<DataObject, DataObject> dataInputMap, HashMap<DataObject, DataObject> dataOutputMap) {
		if (callActivity.getIoSpecification() == null) {
			addElement(callActivity, IoSpecification.class);
			addElement(callActivity.getIoSpecification(), InputSet.class);
			addElement(callActivity.getIoSpecification(), OutputSet.class);
		}

		Iterator<Entry<DataObject, DataObject>> it = dataInputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<DataObject,DataObject> inputConnection = (Map.Entry<DataObject, DataObject>) it.next();
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
			Map.Entry<DataObject,DataObject> outputConnection = (Map.Entry<DataObject,DataObject>) it.next();
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

	private org.camunda.bpm.model.bpmn.instance.DataObject addInput(String name, String type) {
		ItemDefinition itemDef = addElement(definitions, ItemDefinition.class);
		itemDef.setStructureRef(type);
		org.camunda.bpm.model.bpmn.instance.DataObject dataObject = addElement(process, org.camunda.bpm.model.bpmn.instance.DataObject.class);
		dataObject.setName(name);
		return dataObject;
	}

	private void mapInput(CallActivity task, Property src, String name) {

		if (task.getIoSpecification() == null) {
			addElement(task, IoSpecification.class);
			addElement(task.getIoSpecification(), InputSet.class);
			addElement(task.getIoSpecification(), OutputSet.class);
		}

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

}
