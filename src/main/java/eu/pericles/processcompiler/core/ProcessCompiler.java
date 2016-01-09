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

	public boolean validateDataFlow(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		return new DataFlowValidator(repository, aggregatedProcess).validateDataFlow();
	}

	public BPMNProcess compileAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		BPMNProcess bpmnProcess = new BPMNProcessesAggregator().createBPMNProcessByProcessAggregation(aggregatedProcess, 
				getBPMNProcessesOfAggregatedProcess(repository, aggregatedProcess));
		return bpmnProcess;
	}

	public List<BPMNProcess> getBPMNProcessesOfAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		List<BPMNProcess> bpmnSubprocesses = new ArrayList<BPMNProcess>();
		for (String subprocess : aggregatedProcess.getSequence().getProcessFlow()) {
			bpmnSubprocesses.add(getBPMNProcess(repository, subprocess));
		}
		return bpmnSubprocesses;
	}

	public BPMNProcess getBPMNProcess(String repository, String process) throws Exception {
		BPMNProcess bpmnSubprocess = new BPMNParser().parse(
				ermrCommunications.getProcessImplementationFile(repository, process));
		return bpmnSubprocess;
	}

}
