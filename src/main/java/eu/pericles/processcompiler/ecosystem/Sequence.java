package eu.pericles.processcompiler.ecosystem;

import java.util.List;

public class Sequence {
	
	private List<SequenceStep> steps;
	
	//--------------- GETTERS AND SETTERS ----------------//

	public List<SequenceStep> getSteps() {
		return steps;
	}

	public void setSteps(List<SequenceStep> steps) {
		this.steps = steps;
	}

}
