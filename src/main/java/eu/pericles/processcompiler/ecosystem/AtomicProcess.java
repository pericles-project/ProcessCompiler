package eu.pericles.processcompiler.ecosystem;

import java.util.List;

public class AtomicProcess extends Process {

	private List<Operator> operators;
	
	//--------------- GETTERS AND SETTERS ----------------//

	public List<Operator> getOperators() {
		return operators;
	}

	public void setOperators(List<Operator> operators) {
		this.operators = operators;
	}
}
