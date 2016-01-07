package eu.pericles.processcompiler.core;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;

public class ProcessCompiler {
	
	public boolean validateDataFlow(String repository, AggregatedProcess aggregatedProcess) throws Exception {
			return new DataFlowValidator(repository, aggregatedProcess).validateDataFlow();
	}
	
	public BPMNProcess compileAggregatedProcess(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		return new BPMNProcessAggregator(repository, aggregatedProcess).compileAggregatedProcess();
	}

}
