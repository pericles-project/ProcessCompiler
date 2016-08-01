package eu.pericles.processcompiler.ng.ecosystem;


public class AggregatedProcess extends ProcessBase {
	
	private String processFlow;
	private String dataFlow;
	
	public AggregatedProcess() {
		super();
	}
	
	public AggregatedProcess(ProcessBase process) {
		super(process);
	}
	
	//--------------- GETTERS AND SETTERS ----------------//

	public String getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
	}

	public String getDataFlow() {
		return dataFlow;
	}

	public void setDataFlow(String dataFlow) {
		this.dataFlow = dataFlow;
	}
	

	//--------------- HASHCODE AND EQUALS ----------------//
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataFlow == null) ? 0 : dataFlow.hashCode());
		result = prime * result + ((processFlow == null) ? 0 : processFlow.hashCode());
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
		AggregatedProcess other = (AggregatedProcess) obj;
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
