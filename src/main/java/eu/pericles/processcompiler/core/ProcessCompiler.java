package eu.pericles.processcompiler.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;
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
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.ItemDefinition;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ecosystem.Slot;
import eu.pericles.processcompiler.ermr.ERMRComm;
import eu.pericles.processcompiler.ermr.ERMRCommunications;
import eu.pericles.processcompiler.exceptions.BPMNParserException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;
import eu.pericles.processcompiler.exceptions.PCException;

/**
 * Create a new BPMN process out of several reusable sub-processes, a sequence
 * flow and an input/output mapping.
 * 
 * - Collect sequence flow and input/output from ERMR - Remember available
 * inputs, required outputs - Fetch all sub-processes from ERMR - Extract the
 * process ID - Extract data input, data output and their names and types -
 * Create start/end and data input/output nodes in the new aggregated process. -
 * Follow the sequence of sub-processes defined in the model - Insert
 * CallActivity node - Populate ioSpecification with data mappings, checking for
 * inconsistencies at the same time. - Generate BPMN
 *
 */
public class ProcessCompiler {

	static Logger log = LoggerFactory.getLogger(ProcessCompiler.class);

	private ERMRComm ermrCommunications;
	private BpmnModelInstance model;
	private Definitions definitions;
	private Process process;
	private StartEvent startEvent;
	private EndEvent endEvent;
	private FlowNode previousElement;
	private HashMap<String, String> itemDefMap = new HashMap<String, String>();
	private HashMap<PCPair, PCDataObject> dataMap = new HashMap<PCPair, PCDataObject>();
	private int agpStep;

	private Map<String, String> intermediateCompileResults = new HashMap<>();

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

	public ProcessCompiler(ERMRComm service) throws ERMRClientException {
		ermrCommunications = service;
	}

	public AggregatedProcess getAggregatedProcess(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		return ermrCommunications.getAggregatedProcessEntity(repository, uri);
	}

	public ProcessBase getProcess(String repository, String uri) throws ERMRClientException, JSONParserException {
		return ermrCommunications.getProcessEntity(repository, uri);
	}

	public BPMNProcess getBPMNProcess(String repository, String uri)
			throws ERMRClientException, JSONParserException, BPMNParserException {
		return new BPMNParser().parse(ermrCommunications
				.getImplementationFile(ermrCommunications.getImplementationEntity(repository, uri).getLocation()));
	}

	public ValidationResult validateImplementation(ProcessBase process, BPMNProcess bpmnProcess) {
		PCProcess pcProcess = new PCProcess().copy(process);
		pcProcess.setBpmnProcess(bpmnProcess);
		try {
			validatePCProcess(pcProcess);
			return new ValidationResult(true, "VALID IMPLEMENTATION");
		} catch (PCException e) {
			return new ValidationResult(false, "INVALID IMPLEMENTATION: " + e.getMessage());
		}
	}

	private void validatePCProcess(PCProcess pcProcess) throws PCException {
		for (Slot slot : pcProcess.getSlots())
			if (existValidSlot(slot, pcProcess.getBpmnProcess()) == false)
				throw new PCException("Slot " + slot.getId() + " is wrong or missing in the BPMN file");
	}

	private boolean existValidSlot(Slot slot, BPMNProcess bpmnProcess) throws PCException {
		boolean valid = false;
		for (TDataObject dataObject : bpmnProcess.getDataObjects()) {
			TItemDefinition item = bpmnProcess.findItemDefinitionByName(dataObject.getItemSubjectRef());
			String dataObjectType = item.getStructureRef().getNamespaceURI() + item.getStructureRef().getLocalPart();
			if ((slot.getId().contains(dataObject.getId())) && (slot.getDataType().contains(dataObjectType)))
				valid = true;
		}
		return valid;
	}

	public ValidationResult validateAggregation(String repository, AggregatedProcess aggregatedProcess)
			throws ERMRClientException {
		try {
			PCAggregatedProcess pcAggProcess = createPCAggregatedProcess(repository, aggregatedProcess);
			validatePCAggregatedProcess(repository, pcAggProcess);
			return new ValidationResult(true, "VALID AGGREGATION");
		} catch (PCException e) {
			return new ValidationResult(false, "INVALID AGGREGATION: " + e.getMessage());
		}
	}

	private PCAggregatedProcess createPCAggregatedProcess(String repository, AggregatedProcess aggregatedProcess)
			throws ERMRClientException, PCException {
		PCAggregatedProcess pcAggProcess = (PCAggregatedProcess) new PCAggregatedProcess().copy(aggregatedProcess);
		List<String> subprocessIDs = Arrays.asList(aggregatedProcess.getProcessFlow().split("\\s+"));
		if (subprocessIDs.isEmpty())
			throw new PCException("Process flow is empty");
		try {
			for (String subprocessID : subprocessIDs) {
				if (!ermrCommunications.existsEntity(repository, subprocessID))
					throw new PCException("Process " + subprocessID + " is bad defined or missing");				
				if (ermrCommunications.isAggregatedProcess(repository, subprocessID)) {
					AggregatedProcess process = getAggregatedProcess(repository, subprocessID);
					pcAggProcess.getSubprocesses().add(createPCAggregatedProcess(repository, process));
				} else {
					ProcessBase process = getProcess(repository, subprocessID);
					pcAggProcess.getSubprocesses().add(createPCProcess(repository, process));
				}
			}
			List<PCDataConnection> connections = mapper.readValue(aggregatedProcess.getDataFlow(),
					mapper.getTypeFactory().constructCollectionType(List.class, PCDataConnection.class));
			pcAggProcess.setDataConnections(connections);
			return pcAggProcess;
		} catch (IOException e) {
			throw new PCException("Bad data flow definition");
		} catch (JSONParserException e) {
			throw new PCException(e.getMessage());
		}
	}

	private PCProcess createPCProcess(String repository, ProcessBase process) throws ERMRClientException, PCException {
		PCProcess pcProcess = new PCProcess().copy(process);
		BPMNProcess bpmnProcess;
		try {
			if (intermediateCompileResults.containsKey(process.getId()))
				bpmnProcess = new BPMNParser()
						.parse(new ByteArrayInputStream(intermediateCompileResults.get(process.getId()).getBytes()));
			else
				bpmnProcess = new BPMNParser()
						.parse(ermrCommunications.getImplementationFile(process.getImplementation().getLocation()));
			pcProcess.setBpmnProcess(bpmnProcess);
			return pcProcess;
		} catch (BPMNParserException | JSONParserException e) {
			throw new PCException(e.getMessage());
		}
	}

	private void validatePCAggregatedProcess(String repository, PCAggregatedProcess pcAggProcess)
			throws ERMRClientException, PCException {
		for (PCProcess pcProcess : pcAggProcess.getSubprocesses()) {
			if(pcProcess instanceof PCAggregatedProcess)
				validatePCAggregatedProcess(repository, (PCAggregatedProcess) pcProcess);
			else
				validatePCProcess(pcProcess);
		}
		validateDataConnections(repository, pcAggProcess);
	}

	private void validateDataConnections(String repository, PCAggregatedProcess pcAggProcess)
			throws ERMRClientException, PCException {
		agpStep = pcAggProcess.getSubprocesses().size();
		List<PCProcess> processes = new ArrayList<PCProcess>();
		processes.addAll(pcAggProcess.getSubprocesses());
		processes.add(pcAggProcess);
		// Check source and target slots exist and are compatible
		for (PCDataConnection connection : pcAggProcess.getDataConnections()) {
			Slot source = processes.get(connection.getSourceProcess()).findSlotByID(connection.getSourceSlot());
			Slot target = processes.get(connection.getTargetProcess()).findSlotByID(connection.getTargetSlot());
			String sourceType = ermrCommunications.getSlotDataTypeURI(repository, source.getId());
			String targetType = ermrCommunications.getSlotDataTypeURI(repository, target.getId());
			if (!ermrCommunications.isSubclass(repository, targetType, sourceType))
				throw new PCException(
						"Invalid data type in connection (" + source.getId() + "," + target.getId() + ")");
		}
		// Check data flow is consistent (resources are already available)
		List<PCPair> availableResources = new ArrayList<PCPair>();
		for (InputSlot inputSlot : pcAggProcess.getInputSlots())
			availableResources.add(new PCPair(agpStep, inputSlot.getId()));
		for (int step = 0; step <= agpStep; step++) {
			for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(step))
				if (!availableResources.contains(new PCPair(connection.getSourceProcess(), connection.getSourceSlot())))
					throw new PCException("Not available source (" + connection.getSourceProcess() + ","
							+ connection.getSourceSlot() + ")");

			for (OutputSlot outputSlot : processes.get(step).getOutputSlots())
				availableResources.add(new PCPair(step, outputSlot.getId()));
		}
	}

	public String compile(String repository, AggregatedProcess aggregatedProcess)
			throws ERMRClientException, PCException {
		try {
			CompiledProcess compiledProcess = createCompiledProcess(repository,
					createPCAggregatedProcess(repository, aggregatedProcess));
			return compile(compiledProcess);
		} catch (BPMNParserException | IOException e) {
			throw new PCException(e.getMessage());
		}
	}

	public Map<String, String> compileRecursively(String repository, String processId)
			throws ERMRClientException, JSONParserException, PCException, BPMNParserException, IOException {
		Map<String, String> results = new HashMap<>();
		compileRecursively(repository, processId, results);
		return results;
	}

	private void compileRecursively(String repository, String processId, Map<String, String> compilation)
			throws ERMRClientException, JSONParserException, IOException, PCException, BPMNParserException {

		String id = getLocalName(processId);
		if (compilation.containsKey(id))
			return;

		if (ermrCommunications.isAggregatedProcess(repository, processId)) {
			AggregatedProcess ap = getAggregatedProcess(repository, processId);
		
			for (String sub : ap.getProcessFlow().split("\\s+")) {
				compileRecursively(repository, sub, compilation);
			}
			
			CompiledProcess cp = createCompiledProcess(repository, createPCAggregatedProcess(repository, ap));
			log.info("Compiling (recursively): {} from {}", id, processId);
			compilation.put(id, compile(cp));
		} else {
			ProcessBase p = getProcess(repository, processId);
			log.info("Compiling: {} from {}", id, processId);
			InputStream impl = ermrCommunications.getImplementationFile(p.getImplementation().getLocation());
			compilation.put(id, IOUtils.toString(impl));
		}
	}

	private CompiledProcess createCompiledProcess(String repository, PCAggregatedProcess pcAggProcess)
			throws ERMRClientException, BPMNParserException, PCException {
		validatePCAggregatedProcess(repository, pcAggProcess);
		CompiledProcess compiledProcess = new CompiledProcess();
		compiledProcess.setId(getLocalName(pcAggProcess.getId()));
		compiledProcess.setName(pcAggProcess.getName());
		for (PCProcess pcProcess : pcAggProcess.getSubprocesses()) {
			if(pcProcess.getBpmnProcess() == null) {
				BPMNProcess bpmn = new BPMNParser()
						.parse(new ByteArrayInputStream(intermediateCompileResults.get(process.getId()).getBytes()));
				pcProcess.setBpmnProcess(bpmn);
			}
			compiledProcess.getSubprocesses().add(new PCSubprocess(pcProcess.getBpmnProcess().getId()));
		}
		// Add input and output data objects
		for (Slot slot : pcAggProcess.getSlots()) {
			PCDataObject dataObject = new PCDataObject(getLocalName(slot.getId()), slot.getName(),
					getLocalName(slot.getDataType()));
			compiledProcess.getDataObjects().add(dataObject);
			dataMap.put(new PCPair(agpStep, slot.getId()), dataObject);
		}
		// Add intermediate data objects
		for (PCDataConnection connection : pcAggProcess.getDataConnections()) {
			if (!dataMap.containsKey(connection.getSource()) && !dataMap.containsKey(connection.getTarget())) {
				Slot slot = pcAggProcess.getSubprocesses().get(connection.getSourceProcess())
						.findSlotByID(connection.getSourceSlot());
				PCDataObject dataObject = new PCDataObject(getRandomId(getLocalName(slot.getId())), slot.getName(),
						getLocalName(slot.getDataType()));
				compiledProcess.getDataObjects().add(dataObject);
				dataMap.put(connection.getSource(), dataObject);
			}
		}
		// Add input and output data to subprocesses
		for (int step = 0; step < agpStep; step++) {
			for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(step)) {
				compiledProcess.getSubprocesses().get(connection.getTargetProcess()).getDataInputMap()
						.put(dataMap.get(connection.getSource()).getId(), getLocalName(connection.getTargetSlot()));
				if (connection.getSourceProcess() != agpStep)
					compiledProcess.getSubprocesses().get(connection.getSourceProcess()).getDataOutputMap()
							.put(dataMap.get(connection.getSource()).getId(), getLocalName(connection.getSourceSlot()));
			}
		}
		for (PCDataConnection connection : pcAggProcess.getDataConnectionsByTarget(agpStep)) {
			compiledProcess.getSubprocesses().get(connection.getSourceProcess()).getDataOutputMap()
					.put(getLocalName(connection.getTargetSlot()), getLocalName(connection.getSourceSlot()));
		}

		return compiledProcess;
	}

	private String compile(CompiledProcess compiledProcess) throws IOException {
		createProcessFramework(compiledProcess);
		for (PCDataObject pcDataObject : compiledProcess.getDataObjects())
			addDataObject(pcDataObject);
		for (PCSubprocess pcSubprocess : compiledProcess.getSubprocesses())
			addSubprocess(pcSubprocess);
		addSequenceFlow(process, previousElement, endEvent);

		String bpmn = Bpmn.convertToString(model);
		intermediateCompileResults.put(compiledProcess.getId(), bpmn);
		return bpmn;
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

		//
		ExtensionElements ext = model.newInstance(ExtensionElements.class);
		process.addChildElement(ext);
		ModelElementInstance exte = ext.addExtensionElement("http://www.jboss.org/drools", "global");
		exte.setAttributeValue("identifier", "log");
		exte.setAttributeValue("type", "java.io.PrintStream");

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
		itemDefMap.put(dataObject.getId(), itemDefinition.getId());
	}

	private void addSubprocess(PCSubprocess pcSubprocess) {
		CallActivity callActivity = addElement(process, CallActivity.class);
		addSequenceFlow(process, previousElement, callActivity);
		previousElement = callActivity;
		callActivity.setCalledElement(pcSubprocess.getId());
		mapData(callActivity, pcSubprocess.getDataInputMap(), pcSubprocess.getDataOutputMap());
	}

	private void mapData(CallActivity callActivity, HashMap<String, String> dataInputMap,
			HashMap<String, String> dataOutputMap) {
		if (callActivity.getIoSpecification() == null)
			addElement(callActivity, IoSpecification.class);
		InputSet inputSet = addElement(callActivity.getIoSpecification(), InputSet.class);
		OutputSet outputSet = addElement(callActivity.getIoSpecification(), OutputSet.class);

		Iterator<Entry<String, String>> it = dataInputMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> inputConnection = (Map.Entry<String, String>) it.next();
			DataInput input = addElement(callActivity.getIoSpecification(), DataInput.class);
			input.setName(inputConnection.getValue());
			input.setAttributeValue("itemSubjectRef", itemDefMap.get(inputConnection.getKey()));
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
			output.setAttributeValue("itemSubjectRef", itemDefMap.get(outputConnection.getKey()));
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

	private <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement, String id,
			Class<T> elementClass) {
		T element = model.newInstance(elementClass);
		if (id != null)
			element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		return element;
	}

	private <T extends BpmnModelElementInstance> T addElement(BpmnModelElementInstance parentElement,
			Class<T> elementClass) {
		return addElement(parentElement, getRandomId(elementClass), elementClass);
	}

	private String getRandomId(Class<?> forElement) {
		return forElement.getSimpleName() + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	private String getRandomId(String id) {
		return id + "_" + String.valueOf(idCounter.getAndIncrement());
	}

	public String getLocalName(String fullName) throws PCException {
		Pattern word = Pattern.compile("#(.*?)>");
		Matcher match = word.matcher(fullName);
		if (match.find())
			return match.group(1);
		else
			throw new PCException("Bad name structure (" + fullName + ")");
	}

}
