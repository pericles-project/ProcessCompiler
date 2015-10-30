package eu.pericles.processcompiler.ecosystem;

public class AggregatedProcess extends Process {
	
	private Sequence sequence;

	//--------------- GETTERS AND SETTERS ----------------//
	
	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
}
