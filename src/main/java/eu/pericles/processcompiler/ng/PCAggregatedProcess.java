package eu.pericles.processcompiler.ng;

import java.util.ArrayList;
import java.util.List;

public class PCAggregatedProcess extends PCProcess {
	List<PCProcess> subprocesses = new ArrayList<>();;
	List<PCDataConnection> dataConnections = new ArrayList<>();;

	public List<PCProcess> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<PCProcess> subprocesses) {
		this.subprocesses = subprocesses;
	}

	public List<PCDataConnection> getDataConnections() {
		return dataConnections;
	}

	public void setDataConnections(List<PCDataConnection> dataConnections) {
		this.dataConnections = dataConnections;
	}

	public List<PCDataConnection> getDataConnectionsByTarget(int step) {
		List<PCDataConnection> stepConnections = new ArrayList<PCDataConnection>();
		for (PCDataConnection dataConnection : dataConnections)
			if (dataConnection.getTargetProcess() == step)
				stepConnections.add(dataConnection);
		return stepConnections;
	}
	
	public List<PCDataConnection> getDataConnectionsBySource(int step) {
		List<PCDataConnection> stepConnections = new ArrayList<PCDataConnection>();
		for (PCDataConnection dataConnection : dataConnections)
			if (dataConnection.getSourceProcess() == step)
				stepConnections.add(dataConnection);
		return stepConnections;
	}

}
