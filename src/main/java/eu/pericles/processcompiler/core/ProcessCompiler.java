package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.core.ImplementationValidator.ImplementationValidationResult;
import eu.pericles.processcompiler.core.Validator.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;
import eu.pericles.processcompiler.exceptions.ProcessProcessFlowException;
import eu.pericles.processcompiler.exceptions.ValidationException;

public class ProcessCompiler {

	private ERMRCommunications ermrCommunications;

	public ProcessCompiler() throws ERMRClientException {
		ermrCommunications = new ERMRCommunications();
	}
	
	public ProcessCompiler(String service) throws ERMRClientException {
		ermrCommunications = new ERMRCommunications(service);
	}
	
	public ValidationResult validateAggregation(String repository, AggregatedProcess aggregatedProcess) throws ERMRClientException {
		return new DataFlowValidator(ermrCommunications, repository, aggregatedProcess).validate();
	}
	
	public ValidationResult validateImplementation(Process process, BPMNProcess bpmnProcess)  {
		return new ImplementationValidator(process, bpmnProcess).validate();
	}

	/**
	 * Compile an AggregatedProcess, a model-based description of an Aggregated
	 * Process Entity in the Ecosystem, into a BPMN process. The
	 * AggregatedProcess describes a combination of processes to create a new
	 * and more complex one, that is a process flow, as well as the data flow in
	 * the process flow.
	 * 
	 * The compilation is done by:
	 * - validate data flow
	 * - validate and get the implementation files of the processes used to
	 * create the new aggregated process
	 * - connect the BPMN processes together as described in the data flow and
	 * the process flow by using a DataFlowHandler and a ProcessFlowHandler respectively
	 * 
	 * @param repository
	 * @param aggregatedProcess
	 * @return BPMNProcess
	 * @throws ValidationException 
	 * 		- when an error occurs while validating processes
	 * @throws ERMRClientException 
	 * 		- when an error occurs while communicating with the ERMR
	 * @throws BPMNParseException 
	 * 		- when parsing the BPMN file of a subprocess
	 * @throws ProcessDataFlowException 
	 * 		- when an error occurs while processing the data flow
	 * @throws ProcessProcessFlowException 
	 * 		- when an error occurs while processing the process flow
	 * 
	 */
	public BPMNProcess compileAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws ValidationException, ERMRClientException, BPMNParseException, ProcessDataFlowException, ProcessProcessFlowException {
		List<DataConnection> dataFlow = validateDataFlow(repository, aggregatedProcess);
		List<ValidatedProcess> sequenceSubprocesses = validateAndGetProcesses(repository, aggregatedProcess);
		List<BPMNProcess> bpmnProcesses = DataFlowHandler.processDataFlow(dataFlow, sequenceSubprocesses);
		BPMNProcess bpmnProcess = ProcessFlowHandler.processProcessFlow(aggregatedProcess, bpmnProcesses);

		return bpmnProcess;
	}

	/**
	 * Validate a data flow related to a process aggregation sequence. It uses a
	 * DataFlowValidator which is initialised with the triple-store location and
	 * the AggregatedProcess entity.
	 * 
	 * @param repository
	 *            - location to the triples in the ERMR
	 * @param aggregatedProcess
	 *            - AggregatedProcess is a class that represents a model-based
	 *            description of an Aggregated Process Entity in the Ecosystem
	 * @return dataFlow, a list of DataConnections
	 * @throws ERMRClientException
	 *             - when an error occurs while creating the interface to
	 *             communicate with the ERMR
	 * @throws ValidationException
	 *             - when an error occurs during the data flow validation
	 */
	public List<DataConnection> validateDataFlow(String repository, AggregatedProcess aggregatedProcess) throws ValidationException, ERMRClientException {
		ValidationResult validationResult = new DataFlowValidator(ermrCommunications, repository, aggregatedProcess).validate();
		if (validationResult.isValid() == false) {
			throw new ValidationException(validationResult.getMessage(), validationResult.getException());
		}
		return aggregatedProcess.getSequence().getDataFlow();
	}

	/**
	 * Validate the implementation of a process (BPMNProcess) with its RDF-based
	 * definition (Process). It uses an Implementation Validator which
	 * implements the Validator interface.
	 * 
	 * @param process
	 *            - RDF-based process description
	 * @param bpmnProcess
	 *            - implementation of the process via BPMN
	 * @return validatedProcess with the attributes
	 *         - bpmnProcess
	 *         - input connections: hashmap of input slots and corresponded
	 *         resources in the implementation file
	 *         - output connections: hashmap of output slots and corresponded
	 *         resources in the implementation file
	 * @throws ValidationException
	 * 		- when an error occurs during the process validation
	 */
	public ValidatedProcess validateProcess(Process process, BPMNProcess bpmnProcess) throws ValidationException {
		ImplementationValidationResult validationResult = new ImplementationValidator(process, bpmnProcess).validate();
		if (validationResult.isValid() == false)
			throw new ValidationException(validationResult.getMessage(), validationResult.getException());
		ValidatedProcess validatedProcess = new ValidatedProcess(bpmnProcess, validationResult.getInputConnections(),
				validationResult.getOutputConnections());
		return validatedProcess;
	}

	/**
	 * Validate and get the processes that conform an aggregated process
	 * 
	 * @param repository
	 * @param aggregatedProcess
	 * @return list of validated processes
	 * @throws ValidationException 
	 * 		- when an error occurs while validating processes
	 * @throws ERMRClientException 
	 * 		- when an error occurs while communicating with the ERMR
	 * @throws BPMNParseException 
	 * 		- when an error occurs during the parsing the BPMN file of a subprocess
	 * 
	 */
	public List<ValidatedProcess> validateAndGetProcesses(String repository, AggregatedProcess aggregatedProcess) throws ValidationException, ERMRClientException, BPMNParseException {
		List<ValidatedProcess> validatedProcesses = new ArrayList<ValidatedProcess>();
		for (String processID : aggregatedProcess.getSequence().getProcessFlow()) {
			Process process = getProcess(repository, processID);
			BPMNProcess bpmnProcess = getBPMNProcess(repository, processID);
			ValidatedProcess validatedProcess = validateProcess(process, bpmnProcess);
			validatedProcesses.add(validatedProcess);
		}
		return validatedProcesses;
	}

	/**
	 * Get a Process entity (as RDF-based description) by ID by requesting the
	 * triplestore through the ERMRCommunications.
	 * 
	 * @param repository
	 * @param processID
	 * @return Process
	 * @throws ERMRClientException 
	 * 		- when an error occurs when communicating with the ERMR
	 */
	public Process getProcess(String repository, String processID) throws ERMRClientException  {
		return ermrCommunications.getProcessEntity(repository, processID);
	}
	
	/**
	 * Get a Aggregated Process entity (as RDF-based description) by ID by 
	 * requesting the triplestore through the ERMRCommunications.
	 * 
	 * @param repository
	 * @param processID
	 * @return AggregatedProcess
	 * @throws ERMRClientException 
	 * 		- when an error occurs when communicating with the ERMR
	 */
	public AggregatedProcess getAggregatedProcess(String repository, String processID) throws ERMRClientException {
		return ermrCommunications.getAggregatedProcessEntity(repository, processID);
	}

	/**
	 * Get a BPMN process by ID. It requests the triplestore for the
	 * implementation file (i.e. BPMN file) of a process entity. It parses the
	 * implementation file into a BPMNProcess.
	 * 
	 * @param repository
	 * @param process
	 * @return BPMNProcess
	 * @throws ERMRClientException 
	 * 		- when an error occurs when communicating with the ERMR
	 * @throws BPMNParseException
	 * 		- when parsing the BPMN file of a subprocess
	 */
	public BPMNProcess getBPMNProcess(String repository, String process) throws ERMRClientException, BPMNParseException {
		BPMNProcess bpmnSubprocess = new BPMNParser().parse(ermrCommunications.getProcessImplementationFile(repository, process));
		return bpmnSubprocess;
	}
	
	public BPMNProcess getBPMNProcessFromImplementation(String repository, String implementation) throws ERMRClientException, BPMNParseException {
		BPMNProcess bpmnSubprocess = new BPMNParser().parse(ermrCommunications.getImplementationFile(ermrCommunications.getImplementationEntity(repository, implementation).getLocation()));
		return bpmnSubprocess;
	}

}
