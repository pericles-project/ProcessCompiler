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

	//--------------- HASHCODE AND EQUALS ----------------//
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((operators == null) ? 0 : operators.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomicProcess other = (AtomicProcess) obj;
		if (operators == null) {
			if (other.operators != null)
				return false;
		} else if (!operators.equals(other.operators))
			return false;
		return true;
	}
}
