package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNProcessesAggregator;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;

public class ProcessCompiler {

	private ERMRCommunications ermrCommunications;

	public ProcessCompiler() {
		try {
			ermrCommunications = new ERMRCommunications();
		} catch (Exception e) {
			System.out.println("Error when creating an API with the ERMR");
			e.printStackTrace();
		}
	}

	/**
	 * Validate a data flow related to a process aggregation sequence. It uses a
	 * DataFlowValidator which is initialised with the triplestore location and
	 * the AggregatedProcess entity
	 * 
	 * @param repository
	 *            - location to the triples in the ERMR
	 * @param aggregatedProcess
	 *            - AggregatedProcess is a class that represents a model-based
	 *            description of an Aggregated Process Entity in the Ecosystem
	 * @return true if the data flow is valid
	 * @throws Exception
	 */
	public boolean validateDataFlow(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		return new DataFlowValidator(repository, aggregatedProcess).validateDataFlow();
	}

	/**
	 * Compile an AggregatedProcess, a model-based description of an Aggregated
	 * Process Entity in the Ecosystem, into a BPMN process. The
	 * AggregatedProcess describes a combination of processes to create a new
	 * and more complex one, that is a process flow, as well as the data flow in
	 * the process flow. The compilation is done by: - get and parse the
	 * implementation files of the other processes - validate data flow -
	 * connect BPMN processes together as described in the process flow by using
	 * a BPMNProcessesAggregator
	 * 
	 * @param repository
	 * @param aggregatedProcess
	 * @return BPMNProcess
	 * @throws Exception
	 */
	public BPMNProcess compileAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		if (new DataFlowValidator(repository, aggregatedProcess).validateDataFlow()) {
			BPMNProcess bpmnProcess = new BPMNProcessesAggregator().createBPMNProcessByProcessAggregation(aggregatedProcess,
					getBPMNProcessesOfAggregatedProcess(repository, aggregatedProcess));
			return bpmnProcess;
		}
		else
			throw new Exception("The data flow of " + aggregatedProcess.getName() + " is not valid");
	}

	/**
	 * Get the list of BPMN processes used to conform an aggregated process
	 * 
	 * @param repository
	 * @param aggregatedProcess
	 * @return list of BPMNProcess
	 * @throws Exception
	 */
	public List<BPMNProcess> getBPMNProcessesOfAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		List<BPMNProcess> bpmnSubprocesses = new ArrayList<BPMNProcess>();
		for (String subprocess : aggregatedProcess.getSequence().getProcessFlow()) {
			bpmnSubprocesses.add(getBPMNProcess(repository, subprocess));
		}
		return bpmnSubprocesses;
	}

	/**
	 * Get a BPMN process by ID. It requests the triplestore for the
	 * implementation file (i.e. BPMN file) of a process entity. It parses the
	 * implementation file into a BPMNProcess.
	 * 
	 * @param repository
	 * @param process
	 * @return BPMNProcess
	 * @throws Exception
	 */

	public BPMNProcess getBPMNProcess(String repository, String process) throws Exception {
		BPMNProcess bpmnSubprocess = new BPMNParser().parse(ermrCommunications.getProcessImplementationFile(repository, process));
		return bpmnSubprocess;
	}

}
