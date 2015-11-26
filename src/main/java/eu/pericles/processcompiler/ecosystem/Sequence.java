package eu.pericles.processcompiler.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
	
	private List<String> processFlow;
	private List<ArrayList<SlotPair<String, String>>> dataFlow; 
	
	//--------------- GETTERS AND SETTERS ----------------//

	public List<String> getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(List<String> processFlow) {
		this.processFlow = processFlow;
	}

	public List<ArrayList<SlotPair<String, String>>> getDataFlow() {
		return dataFlow;
	}

	public void setDataFlow(ArrayList<ArrayList<SlotPair<String, String>>> arrayList) {
		this.dataFlow = arrayList;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataFlow == null) ? 0 : dataFlow.hashCode());
		result = prime * result + ((processFlow == null) ? 0 : processFlow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sequence other = (Sequence) obj;
		if (dataFlow == null) {
			if (other.dataFlow != null)
				return false;
		} else if (!dataFlow.equals(other.dataFlow))
			return false;
		if (processFlow == null) {
			if (other.processFlow != null)
				return false;
		} else if (!processFlow.equals(other.processFlow))
			return false;
		return true;
	}

}
