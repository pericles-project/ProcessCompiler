package eu.pericles.processcompiler.core;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.communications.ermr.ERMRCommunications;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Slot;

public class ProcessCompiler {
	
	private ERMRCommunications ermrCommunications;
	
	public ProcessCompiler() throws KeyManagementException, NoSuchAlgorithmException {
		ermrCommunications = new ERMRCommunications();
	}

	public boolean validateDataFlow(AggregatedProcess process) {
		boolean isValid = true;
		ArrayList<String> availableResources = new ArrayList<String>();
		updateAvailableResourcesList(availableResources, process.getInputs());
		
		return isValid;
	}
	
	private void updateAvailableResourcesList(List<String> availableResources, List<? extends Slot> slots) {
		for (Slot slot : slots) {
			availableResources.add(slot.getId());
		}
	}

}
