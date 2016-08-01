package eu.pericles.processcompiler.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class AggregatedProcess extends ProcessBase {
	
	private Sequence sequence;
	
	public AggregatedProcess() {
		super();
	}
	
	public AggregatedProcess(ProcessBase process) {
		super(process);
	}
	
	public List<DataConnection> getDataConnectionsAtSequenceStep(int sequenceStep) {
		List<DataConnection> dataConnections = new ArrayList<DataConnection>();
		for (DataConnection dataConnection : getSequence().getDataFlow()) {
			if (isDataConnectionAtSequenceStep(dataConnection, sequenceStep))
				dataConnections.add(dataConnection);
		}
		return dataConnections;
	}
	private boolean isDataConnectionAtSequenceStep(DataConnection dataConnection, int sequenceStep) {
		return (dataConnection.getSlotNode().getSequenceStep() == sequenceStep);
	}

	//--------------- GETTERS AND SETTERS ----------------//

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
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
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}
}
