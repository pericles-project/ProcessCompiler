package eu.pericles.processcompiler.ng;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.pericles.processcompiler.ng.PCPair;

public class PCDataConnection {
	
	@JsonProperty("sourceProcess")
	private int sourceProcess;
	
	@JsonProperty("sourceSlot")
	private String sourceSlot;
	
	@JsonProperty("targetProcess")
	private int targetProcess;
	
	@JsonProperty("targetSlot")
	private String targetSlot;

	public int getSourceProcess() {
		return sourceProcess;
	}

	public String getSourceSlot() {
		return sourceSlot;
	}

	public int getTargetProcess() {
		return targetProcess;
	}

	public String getTargetSlot() {
		return targetSlot;
	}
	
	public PCPair getSource() {
		return new PCPair(this.sourceProcess, this.sourceSlot);
	}
	
	public PCPair getTarget() {
		return new PCPair(this.targetProcess, this.targetSlot);
	}
	
}
