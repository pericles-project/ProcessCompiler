package eu.pericles.processcompiler.ng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.camunda.bpm.model.bpmn.instance.FlowElement;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.exceptions.BPMNFileException;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ecosystem.InputSlot;
import eu.pericles.processcompiler.ng.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ng.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ng.ecosystem.Slot;
import eu.pericles.processcompiler.ng.ermr.ERMRCommunications;

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

	private ERMRCommunications ermrCommunications;
	private BpmnModelInstance model;
	private Definitions definitions;
	private Process process;
	private StartEvent startEvent;
	private EndEvent endEvent;
	private FlowNode previousElement;
	private HashMap<String, String> dataItemDefConnections = new HashMap<String, String>();
	private HashMap<Pair, PCDataObject> dataMap = new HashMap<Pair, PCDataObject>();
	static ObjectMapper mapper = new ObjectMapper();

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

	public class Pair {
		private int step;
		private String slot;

		public Pair(int step, String slot) {
			this.step = step;
			this.slot = slot;
		}

		public int getStep() {
			return step;
		}

		public String getSlot() {
			return slot;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((slot == null) ? 0 : slot.hashCode());
			result = prime * result + step;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (slot == null) {
				if (other.slot != null)
					return false;
			} else if (!slot.equals(other.slot))
				return false;
			if (step != other.step)
				return false;
			return true;
		}

		private ProcessCompiler getOuterType() {
			return ProcessCompiler.this;
		}

	}

	public ProcessCompiler(String service) throws ERMRClientException {
		ermrCommunications = new ERMRCommunications(service);
	}

	public String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	public String getRandomId(String id) {
		return id + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	public ValidationResult validateImplementation(ProcessBase process, BPMNProcess bpmnProcess) throws BPMNFileException {
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
			String dataObjectType = item.getStructureRef().getNamespaceURI() + item.getStructureRef().getLocalPart();
			if ((slot.getId().contains(dataObject.getId())) && (slot.getType().contains(dataObjectType)))
				valid = true;
		}
		return valid;
	}

	public ValidationResult validateAggregation(String repository, AggregatedProcess aggregatedProcess) throws ERMRClientException {
		// List<ProcessBase> subprocesses = getSubprocesses(repository,
		// aggregatedProcess);
		return new ValidationResult(true, "OK");
	}

	private List<PCProcess> getSubprocesses(String repository, AggregatedProcess aggregatedProcess) throws ERMRClientException,
			BPMNParseException {
		List<PCProcess> subprocesses = new ArrayList<PCProcess>();
		List<String> subprocessIDs = Arrays.asList(aggregatedProcess.getProcessFlow().split("\\s\\s*"));
		for (String subprocessID : subprocessIDs) {
			ProcessBase process = ermrCommunications.getProcessEntity(repository, subprocessID);
			subprocesses.add(createPCProcess(repository, process));
		}
		return subprocesses;
	}

	public PCProcess createPCProcess(String repository, ProcessBase process) throws BPMNParseException {
		PCProcess pcProcess = new PCProcess();
		pcProcess.setId(process.getId());
		pcProcess.setName(process.getName());
		pcProcess.setInputSlots(process.getInputSlots());
		pcProcess.setOutputSlots(process.getOutputSlots());
		pcProcess
				.setBpmnProcess(new BPMNParser().parse(ermrCommunications.getImplementationFile(process.getImplementation().getLocation())));
		return pcProcess;
	}

	public PCAggregatedProcess createPCAggregatedProcess(String repository, AggregatedProcess aggregatedProcess)
			throws ERMRClientException, BPMNParseException, JsonParseException, JsonMappingException, IOException {
		PCAggregatedProcess pcAggregatedProcess = new PCAggregatedProcess();
		pcAggregatedProcess.setId(aggregatedProcess.getId());
		pcAggregatedProcess.setName(aggregatedProcess.getName());
		pcAggregatedProcess.setInputSlots(aggregatedProcess.getInputSlots());
		pcAggregatedProcess.setOutputSlots(aggregatedProcess.getOutputSlots());
		pcAggregatedProcess.setSubprocesses(getSubprocesses(repository, aggregatedProcess));
		List<PCDataConnection> connections = mapper.readValue(aggregatedProcess.getDataFlow(), mapper.getTypeFactory()
				.constructCollectionType(List.class, PCDataConnection.class));
		pcAggregatedProcess.setDataConnections(connections);
		return pcAggregatedProcess;
	}

	public CompiledProcess createCompiledProcess(PCAggregatedProcess pcAggregatedProcess) throws PCException {
		int aggregatedProcess = pcAggregatedProcess.getSubprocesses().size();
		CompiledProcess compiledProcess = new CompiledProcess();
		compiledProcess.setId(getLocalPart(pcAggregatedProcess.getId()));
		compiledProcess.setName(pcAggregatedProcess.getName());
		compiledProcess.setType("Private");
		for (Slot slot : pcAggregatedProcess.getSlots()) {
			compiledProcess.getDataObjects().add(new PCDataObject(getLocalPart(slot.getId()), slot.getName(), getLocalPart(slot.getType())));
			dataMap.put(new Pair(aggregatedProcess, slot.getId()), compiledProcess.getDataObjects().get(compiledProcess.getDataObjects().size() - 1));
		}
		for (PCDataConnection connection : pcAggregatedProcess.getDataConnections()) {
			if (!dataMap.containsKey(new Pair(connection.getSourceProcess(), connection.getSourceSlot())))
				if (!dataMap.containsKey(new Pair(connection.getTargetProcess(), connection.getTargetSlot()))) {
					Slot slot = pcAggregatedProcess.getSubprocesses().get(connection.getSourceProcess())
							.findSlotByID(connection.getSourceSlot());
					compiledProcess.getDataObjects()
							.add(new PCDataObject(getRandomId(getLocalPart(slot.getId())), slot.getName(), getLocalPart(slot.getType())));
					dataMap.put(new Pair(connection.getSourceProcess(), connection.getSourceSlot()),
							compiledProcess.getDataObjects().get(compiledProcess.getDataObjects().size() - 1));
				}
		}
		for (int subprocess = 0; subprocess < pcAggregatedProcess.getSubprocesses().size(); subprocess++) {
			compiledProcess.getSubprocesses().add(new PCSubprocess());
			compiledProcess.getSubprocesses().get(subprocess)
					.setId(pcAggregatedProcess.getSubprocesses().get(subprocess).getBpmnProcess().getProcess().getId());
			for (PCDataConnection connection : pcAggregatedProcess.getDataConnectionsByTarget(subprocess)) {
				if (dataMap.containsKey(new Pair(connection.getSourceProcess(), connection.getSourceSlot()))) {
					compiledProcess
							.getSubprocesses()
							.get(connection.getTargetProcess())
							.getDataInputMap()
							.put(dataMap.get(new Pair(connection.getSourceProcess(), connection.getSourceSlot())).getId(),
									getLocalPart(connection.getTargetSlot()));
					if (connection.getSourceProcess() != aggregatedProcess) {
						compiledProcess
								.getSubprocesses()
								.get(connection.getSourceProcess())
								.getDataOutputMap()
								.put(dataMap.get(new Pair(connection.getSourceProcess(), connection.getSourceSlot())).getId(),
										getLocalPart(connection.getSourceSlot()));
					}
				} else
					throw new PCException("Bad data flow definition");
			}
		}
		for (PCDataConnection connection : pcAggregatedProcess.getDataConnectionsByTarget(aggregatedProcess)) {
			compiledProcess.getSubprocesses().get(connection.getSourceProcess()).getDataOutputMap()
					.put(getLocalPart(connection.getTargetSlot()), getLocalPart(connection.getSourceSlot()));
		}
		
		return compiledProcess;
	}

	/*
	 * public CompiledProcess createCompiledProcess(PCAggregatedProcess
	 * pcAggregatedProcess) throws PCException {
	 * //TODO better control of resources created and linked (output data
	 * connected to output slots of aggregated process)
	 * CompiledProcess compiledProcess = new CompiledProcess();
	 * compiledProcess.setId(getLocalPart(pcAggregatedProcess.getId()));
	 * compiledProcess.setName(pcAggregatedProcess.getName());
	 * compiledProcess.setType("Public");
	 * System.out.println("Add input slots of aggregated process");
	 * List<PCDataObject> dataObjects = new ArrayList<PCDataObject>();
	 * for (InputSlot inputSlot : pcAggregatedProcess.getInputSlots()) {
	 * dataObjects.add(new PCDataObject(getLocalPart(inputSlot.getId()),
	 * inputSlot.getName(), getLocalPart(inputSlot.getType())));
	 * cdoMap.put(new Pair(0, inputSlot.getId()),
	 * dataObjects.get(dataObjects.size() - 1));
	 * System.out.println("KEY: 0," + inputSlot.getId());
	 * }
	 * System.out.println("Process data inputs of subprocesses");
	 * for (int step = 1; step <= pcAggregatedProcess.getSubprocesses().size();
	 * step++) {
	 * System.out.println("Step: " + step);
	 * compiledProcess.getSubprocesses().add(new PCSubprocess());
	 * compiledProcess.getSubprocesses().get(step - 1)
	 * .setId(pcAggregatedProcess.getSubprocesses().get(step -
	 * 1).getBpmnProcess().getProcess().getId());
	 * System.out.println("data connections by target");
	 * for (PCDataConnection connection :
	 * pcAggregatedProcess.getDataConnectionsByTarget(step)) {
	 * System.out.println(connection.getTargetSlot());
	 * System.out.println("Has KEY?: " + connection.getSourceProcess() + " " +
	 * connection.getSourceSlot());
	 * if (cdoMap.containsKey(new Pair(connection.getSourceProcess(),
	 * connection.getSourceSlot()))) {
	 * System.out.println("YES");
	 * InputSlot inputSlot = pcAggregatedProcess.getSubprocesses().get(step -
	 * 1).findInputSlotByID(connection.getTargetSlot());
	 * System.out.println(inputSlot.getId());
	 * compiledProcess
	 * .getSubprocesses()
	 * .get(step - 1)
	 * .getDataInputMap()
	 * .put(cdoMap.get(new Pair(connection.getSourceProcess(),
	 * connection.getSourceSlot())),
	 * new PCDataObject(getLocalPart(inputSlot.getId()), inputSlot.getName(),
	 * getLocalPart(inputSlot.getType())));
	 * } else
	 * throw new PCException("Bad data flow definition");
	 * }
	 * System.out.println("Add output slots of subprocesses");
	 * for (OutputSlot outputSlot :
	 * pcAggregatedProcess.getSubprocesses().get(step - 1).getOutputSlots()) {
	 * System.out.println("Output Slot: " + outputSlot.getId());
	 * dataObjects
	 * .add(new PCDataObject(getRandomId(getLocalPart(outputSlot.getId())),
	 * outputSlot.getName(), getLocalPart(outputSlot.getType())));
	 * cdoMap.put(new Pair(step, outputSlot.getId()),
	 * dataObjects.get(dataObjects.size() - 1));
	 * }
	 * }
	 * System.out.println("Add output slots of aggregated process");
	 * for (OutputSlot outputSlot : pcAggregatedProcess.getOutputSlots()) {
	 * System.out.println("Output Slot: " + outputSlot.getId());
	 * dataObjects.add(new PCDataObject(getLocalPart(outputSlot.getId()),
	 * outputSlot.getName(), getLocalPart(outputSlot.getType())));
	 * cdoMap.put(new Pair(0, outputSlot.getId()),
	 * dataObjects.get(dataObjects.size() - 1));
	 * }
	 * System.out.println("Process data outputs of subprocesses");
	 * for (int step = 1; step <= pcAggregatedProcess.getSubprocesses().size();
	 * step++) {
	 * System.out.println("Step: " + step);
	 * for (PCDataConnection connection :
	 * pcAggregatedProcess.getDataConnectionsBySource(step)) {
	 * if (cdoMap.containsKey(new Pair(connection.getSourceProcess(),
	 * connection.getSourceSlot()))) {
	 * OutputSlot outputSlot = pcAggregatedProcess.getSubprocesses().get(step -
	 * 1)
	 * .findOutputSlotByID(connection.getSourceSlot());
	 * System.out.println("Output Slot: " + outputSlot.getId());
	 * compiledProcess
	 * .getSubprocesses()
	 * .get(step - 1)
	 * .getDataOutputMap()
	 * .put(cdoMap.get(new Pair(connection.getSourceProcess(),
	 * connection.getSourceSlot())),
	 * new PCDataObject(getLocalPart(outputSlot.getId()), outputSlot.getName(),
	 * getLocalPart(outputSlot.getType())));
	 * } else
	 * throw new PCException("Bad data flow definition");
	 * }
	 * }
	 * compiledProcess.setDataObjects(dataObjects);
	 * return compiledProcess;
	 * }
	 */

	private String getLocalPart(String fullName) throws PCException {
		Pattern word = Pattern.compile("#(.*?)>");
		Matcher match = word.matcher(fullName);
		if (match.find())
			return match.group(1);
		else
			throw new PCException("Bad name structure");
	}

	public void compile(CompiledProcess pcProcess, String outputFile) throws IOException {
		System.out.println("Create Process Framework");
		createProcessFramework(pcProcess);
		for (PCDataObject pcDataObject : pcProcess.getDataObjects())
			addDataObject(pcDataObject);
		for (PCSubprocess pcSubprocess : pcProcess.getSubprocesses())
			addSubprocess(pcSubprocess);
		addSequenceFlow(process, previousElement, endEvent);

		System.out.println("Print BPMN file");
		String bpmnProcess = Bpmn.convertToString(model);
		System.out.println(bpmnProcess);
		System.out.println("End");
		if (outputFile != null)
			FileUtils.writeStringToFile(new File(outputFile), Bpmn.convertToString(model));
		else
			System.out.println(Bpmn.convertToString(model));
	}

	private void createProcessFramework(CompiledProcess pcProcess) {
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
		previousElement = startEvent;
	}

	private void addDataObject(PCDataObject pcDataObject) {
		System.out.println("Add Data Object: " + pcDataObject.getId());
		ItemDefinition itemDefinition = addElement(definitions, ItemDefinition.class);
		itemDefinition.setStructureRef(pcDataObject.getType());
		DataObject dataObject = addElement(process, DataObject.class);
		dataObject.setId(pcDataObject.getId());
		dataObject.setName(pcDataObject.getName());
		dataObject.setItemSubject(itemDefinition);
		dataItemDefConnections.put(dataObject.getId(), itemDefinition.getId());
	}

	private void addSubprocess(PCSubprocess pcSubprocess) {
		System.out.println("Add Subprocess: " + pcSubprocess.getId());
		CallActivity callActivity = addElement(process, CallActivity.class);
		addSequenceFlow(process, previousElement, callActivity);
		previousElement = callActivity;

		callActivity.setCalledElement(pcSubprocess.getId());
		mapData(callActivity, pcSubprocess.getDataInputMap(), pcSubprocess.getDataOutputMap());
	}

	private void mapData(CallActivity callActivity, HashMap<String, String> dataInputMap, HashMap<String, String> dataOutputMap) {
		if (callActivity.getIoSpecification() == null)
			addElement(callActivity, IoSpecification.class);
		InputSet inputSet = addElement(callActivity.getIoSpecification(), InputSet.class);
		OutputSet outputSet = addElement(callActivity.getIoSpecification(), OutputSet.class);

		Iterator<Entry<String, String>> it = dataInputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> inputConnection = (Map.Entry<String, String>) it.next();
			System.out.println("Input Connection: " + inputConnection.getKey() + " " + inputConnection.getValue());
			DataInput input = addElement(callActivity.getIoSpecification(), DataInput.class);
			input.setName(inputConnection.getValue());
			input.setAttributeValue("itemSubjectRef", dataItemDefConnections.get(inputConnection.getKey()));
			addElement(inputSet, null, DataInputRefs.class).setTextContent(input.getId());

			DataInputAssociation dataInputAssociation = addElement(callActivity, DataInputAssociation.class);
			addElement(dataInputAssociation, null, SourceRef.class).setTextContent(inputConnection.getKey());
			addElement(dataInputAssociation, null, TargetRef.class).setTextContent(input.getId());
		}

		it = dataOutputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> outputConnection = (Map.Entry<String, String>) it.next();
			System.out.println("Output Connection: " + outputConnection.getKey() + " " + outputConnection.getValue());
			DataOutput output = addElement(callActivity.getIoSpecification(), DataOutput.class);
			output.setName(outputConnection.getValue());
			output.setAttributeValue("itemSubjectRef", dataItemDefConnections.get(outputConnection.getKey()));
			addElement(outputSet, null, DataOutputRefs.class).setTextContent(output.getId());

			DataOutputAssociation dataOutputAssociation = addElement(callActivity, DataOutputAssociation.class);
			addElement(dataOutputAssociation, null, SourceRef.class).setTextContent(output.getId());
			addElement(dataOutputAssociation, null, TargetRef.class).setTextContent(outputConnection.getKey());
		}
	}

	private SequenceFlow addSequenceFlow(Process process, FlowNode from, FlowNode to) {
		SequenceFlow sequenceFlow = addElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
		process.addChildElement(sequenceFlow);
		sequenceFlow.setSource(from);
		from.getOutgoing().add(sequenceFlow);
		sequenceFlow.setTarget(to);
		to.getIncoming().add(sequenceFlow);
		System.out.println("Sequence Flow: " + from.getId() + " " + to.getId());
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
