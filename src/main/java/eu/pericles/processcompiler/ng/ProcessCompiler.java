package eu.pericles.processcompiler.ng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private HashMap<PCPair, PCDataObject> dataMap = new HashMap<PCPair, PCDataObject>();
	private AtomicInteger idCounter = new AtomicInteger(1);
	static ObjectMapper mapper = new ObjectMapper();

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

	public ProcessCompiler(String service) throws ERMRClientException {
		ermrCommunications = new ERMRCommunications(service);
	}

	public AggregatedProcess getAggregatedProcess(String repository, String uri) throws ERMRClientException {
		return ermrCommunications.getAggregatedProcessEntity(repository, uri);
	}

	public ProcessBase getProcess(String repository, String uri) throws ERMRClientException {
		return ermrCommunications.getProcessEntity(repository, uri);
	}

	public BPMNProcess getBPMNProcess(String repository, String uri) throws ERMRClientException, BPMNParseException {
		return new BPMNParser().parse(ermrCommunications.getImplementationFile(ermrCommunications.getImplementationEntity(repository, uri)
				.getLocation()));
	}

	public ValidationResult validateImplementation(ProcessBase process, BPMNProcess bpmnProcess) throws BPMNFileException {
		PCProcess pcProcess = new PCProcess().copy(process);
		pcProcess.setBpmnProcess(bpmnProcess);
		return validateImplementation(pcProcess);
	}

	public ValidationResult validateAggregation(String repository, AggregatedProcess aggregatedProcess) throws JsonParseException,
			JsonMappingException, IOException, ERMRClientException, BPMNParseException, BPMNFileException {
		PCAggregatedProcess pcAggProcess = createPCAggregatedProcess(repository, aggregatedProcess);
		return validatePCAggregatedProcess(repository, pcAggProcess);
	}

	public void compile(String repository, AggregatedProcess aggregatedProcess, String outputFile) throws JsonParseException,
			JsonMappingException, PCException, IOException {
		CompiledProcess compiledProcess = createCompiledProcess(repository, createPCAggregatedProcess(repository, aggregatedProcess));
		compile(compiledProcess, outputFile);
	}

	private PCProcess createPCProcess(String repository, ProcessBase process) throws BPMNParseException {
		PCProcess pcProcess = new PCProcess().copy(process);
		BPMNProcess bpmnProcess = new BPMNParser().parse(ermrCommunications
				.getImplementationFile(process.getImplementation().getLocation()));
		pcProcess.setBpmnProcess(bpmnProcess);
		return pcProcess;
	}

	private PCAggregatedProcess createPCAggregatedProcess(String repository, AggregatedProcess aggregatedProcess)
			throws JsonParseException, JsonMappingException, IOException, ERMRClientException, BPMNParseException {
		PCAggregatedProcess pcAggProcess = (PCAggregatedProcess) new PCAggregatedProcess().copy(aggregatedProcess);
		List<String> subprocessIDs = Arrays.asList(aggregatedProcess.getProcessFlow().split("\\s\\s*"));
		for (String subprocessID : subprocessIDs) {
			ProcessBase process = ermrCommunications.getProcessEntity(repository, subprocessID);
			pcAggProcess.getSubprocesses().add(createPCProcess(repository, process));
		}
		List<PCDataConnection> connections = mapper.readValue(aggregatedProcess.getDataFlow(), mapper.getTypeFactory()
				.constructCollectionType(List.class, PCDataConnection.class));
		pcAggProcess.setDataConnections(connections);
		return pcAggProcess;
	}

	private ValidationResult validateImplementation(PCProcess pcProcess) throws BPMNFileException {
		for (Slot slot : pcProcess.getSlots()) {
			if (existValidSlot(slot, pcProcess.getBpmnProcess()) == false)
				return new ValidationResult(false, "NOT VALID IMPLEMENTATION: Slot " + slot.getId() + " in process " + pcProcess.getId()
						+ " is wrong or missing in the BPMN file");
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

	private ValidationResult validatePCAggregatedProcess(String repository, PCAggregatedProcess pcAggProcess) throws BPMNFileException {
		for (PCProcess pcProcess : pcAggProcess.getSubprocesses()) {
			ValidationResult result = validateImplementation(pcProcess);
			if (!result.isValid())
				return new ValidationResult(false, "NOT VALID PROCESS FLOW: " + result.getMessage());
		}
		ValidationResult result = validateDataConnections(repository, pcAggProcess);
		if (!result.isValid())
			return new ValidationResult(false, "NOT VALID DATA FLOW:" + result.getMessage());
		return new ValidationResult(true, "OK");
	}

	private ValidationResult validateDataConnections(String repository, PCAggregatedProcess pcAggProcess) {
		int agpStep = pcAggProcess.getSubprocesses().size();
		List<PCProcess> processes = new ArrayList<PCProcess>();
		processes.addAll(pcAggProcess.getSubprocesses());
		processes.add(pcAggProcess);
		// Check source and target slots exist and are compatible
		for (PCDataConnection connection : pcAggProcess.getDataConnections()) {
			try {
				Slot source = processes.get(connection.getSourceProcess()).findSlotByID(connection.getSourceSlot());
				Slot target = processes.get(connection.getTargetProcess()).findSlotByID(connection.getTargetSlot());
				String sourceType = ermrCommunications.getDataTypeURI(repository, source.getId());
				String targetType = ermrCommunications.getDataTypeURI(repository, target.getId());
				if (!ermrCommunications.isSubclass(repository, targetType, sourceType))
					return new ValidationResult(false, "NOT VALID TYPE in data connection with source " + source.getId() + " and target "
							+ target.getId());
			} catch (PCException e) {
				return new ValidationResult(false, e.getMessage());
			}
		}
		// Check data flow is consistent (resources are already available)
		List<PCPair> availableResources = new ArrayList<PCPair>();
		for (InputSlot inputSlot : pcAggProcess.getInputSlots())
			availableResources.add(new PCPair(agpStep, inputSlot.getId()));
		for (int step = 0; step <= agpStep; step++) {
			for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(step)) {
				if (!availableResources.contains(new PCPair(connection.getSourceProcess(), connection.getSourceSlot())))
					return new ValidationResult(false, "NOT AVAILABLE SOURCE (" + connection.getSourceProcess() + ","
							+ connection.getSourceSlot() + ")");
			}
			for (OutputSlot outputSlot : processes.get(step).getOutputSlots())
				availableResources.add(new PCPair(step, outputSlot.getId()));
		}
		return new ValidationResult(true, "OK");
	}

	private CompiledProcess createCompiledProcess(String repository, PCAggregatedProcess pcAggProcess) throws PCException {
		ValidationResult result = validatePCAggregatedProcess(repository, pcAggProcess);
		if (!result.isValid())
			throw new PCException(result.getMessage());
		int agpStep = pcAggProcess.getSubprocesses().size();
		CompiledProcess compiledProcess = new CompiledProcess();
		compiledProcess.setId(getLocalPart(pcAggProcess.getId()));
		compiledProcess.setName(pcAggProcess.getName());
		for (PCProcess pcProcess : pcAggProcess.getSubprocesses())
			compiledProcess.getSubprocesses().add(new PCSubprocess(pcProcess.getBpmnProcess().getId()));
		// Add data objects related to aggregated process
		for (Slot slot : pcAggProcess.getSlots()) {
			PCDataObject dataObject = new PCDataObject(getLocalPart(slot.getId()), slot.getName(), getLocalPart(slot.getType()));
			compiledProcess.getDataObjects().add(dataObject);
			dataMap.put(new PCPair(agpStep, slot.getId()), dataObject);
		}
		// Add temporary data objects
		for (PCDataConnection connection : pcAggProcess.getDataConnections()) {
			if (!dataMap.containsKey(connection.getSource()) && !dataMap.containsKey(connection.getTarget())) {
				Slot slot = pcAggProcess.getSubprocesses().get(connection.getSourceProcess()).findSlotByID(connection.getSourceSlot());
				PCDataObject dataObject = new PCDataObject(getRandomId(getLocalPart(slot.getId())), slot.getName(),
						getLocalPart(slot.getType()));
				compiledProcess.getDataObjects().add(dataObject);
				dataMap.put(connection.getSource(), dataObject);
			}
		}
		// Add input and output data to subprocesses
		for (int step = 0; step < agpStep; step++) {
			for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(step)) {
				if (dataMap.containsKey(connection.getSource())) {
					compiledProcess.getSubprocesses().get(connection.getTargetProcess()).getDataInputMap()
							.put(dataMap.get(connection.getSource()).getId(), getLocalPart(connection.getTargetSlot()));
					if (connection.getSourceProcess() != agpStep) {
						compiledProcess.getSubprocesses().get(connection.getSourceProcess()).getDataOutputMap()
								.put(dataMap.get(connection.getSource()).getId(), getLocalPart(connection.getSourceSlot()));
					}
				} else
					throw new PCException("Bad data flow definition");
			}
		}
		for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(agpStep)) {
			compiledProcess.getSubprocesses().get(connection.getSourceProcess()).getDataOutputMap()
					.put(getLocalPart(connection.getTargetSlot()), getLocalPart(connection.getSourceSlot()));
		}
		return compiledProcess;
	}

	private void compile(CompiledProcess compiledProcess, String outputFile) throws IOException {
		createProcessFramework(compiledProcess);
		for (PCDataObject pcDataObject : compiledProcess.getDataObjects())
			addDataObject(pcDataObject);
		for (PCSubprocess pcSubprocess : compiledProcess.getSubprocesses())
			addSubprocess(pcSubprocess);
		addSequenceFlow(process, previousElement, endEvent);

		if (outputFile != null)
			FileUtils.writeStringToFile(new File(outputFile), Bpmn.convertToString(model));
		else
			System.out.println(Bpmn.convertToString(model));
	}

	private void createProcessFramework(CompiledProcess compiledProcess) {
		model = Bpmn.createEmptyModel();

		definitions = model.newInstance(Definitions.class);
		definitions.setTargetNamespace("http://camunda.org/examples");
		definitions.setId(getRandomId(Definitions.class));
		model.setDefinitions(definitions);

		process = addElement(definitions, compiledProcess.getId(), Process.class);
		process.setName(compiledProcess.getName());
		process.setAttributeValue("processType", "Private");

		startEvent = addElement(process, StartEvent.class);
		endEvent = addElement(process, EndEvent.class);
		previousElement = startEvent;
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
		return sequenceFlow;
	}

	private <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, String id, Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		return element;
	}

	private <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	private String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	private String getRandomId(String id) {
		return id + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	private String getLocalPart(String fullName) throws PCException {
		Pattern word = Pattern.compile("#(.*?)>");
		Matcher match = word.matcher(fullName);
		if (match.find())
			return match.group(1);
		else
			throw new PCException("Bad name structure");
	}

}
