package eu.pericles.processcompiler.ng;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
}
