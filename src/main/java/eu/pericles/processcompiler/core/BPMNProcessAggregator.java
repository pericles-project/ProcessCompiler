package eu.pericles.processcompiler.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;

public class BPMNProcessAggregator {
	
	private String repository;
	private AggregatedProcess process;
	private ERMRCommunications ermrCommunications;
	
	public BPMNProcessAggregator(String repository, AggregatedProcess aggregatedProcess) throws Exception {
		this.repository = repository;
		this.process = aggregatedProcess;
		this.ermrCommunications = new ERMRCommunications();
	}
	
	public BPMNProcess compileAggregatedProcess() throws Exception {
		BPMNProcess bpmnProcess = new BPMNProcess();
		
		List<BPMNProcess> bpmnSubprocesses = getBPMNSubprocesses();
		//TODO manage exceptions correctly
		if (bpmnSubprocesses.isEmpty())
			throw new Exception("List of processes in the sequence is empty");
		
		return bpmnProcess;
	}
	
	public List<BPMNProcess> getBPMNSubprocesses() throws Exception {
		List<BPMNProcess> bpmnSubprocesses = new ArrayList<BPMNProcess>();
		for (String subprocess : process.getSequence().getProcessFlow()) {
			bpmnSubprocesses.add(getBPMNSubprocess(subprocess));
		}
		return bpmnSubprocesses;
	}

	public BPMNProcess getBPMNSubprocess(String subprocess) throws Exception {
		String location = ermrCommunications.getProcessEntity(getRepository(), subprocess).getImplementation().getLocation();
		System.out.println(location);
		InputStream inputStream = ermrCommunications.getImplementationFile(location);
		System.out.println(inputStream);
		BPMNProcess bpmnSubprocess = new BPMNParser().parse(ermrCommunications.getImplementationFile(ermrCommunications.getProcessEntity(repository, subprocess).getImplementation().getLocation()));
	return bpmnSubprocess;
}
	
	// --------------- GETTERS AND SETTERS ----------------//

		public String getRepository() {
			return repository;
		}

		public void setRepository(String repository) {
			this.repository = repository;
		}

		public AggregatedProcess getProcess() {
			return process;
		}

		public void setProcess(AggregatedProcess process) {
			this.process = process;
		}

}
