package eu.pericles.processcompiler.ecosystem;

public class SlotPair<L,R> {
	private final L slot;
	private final R connection;
	
	public SlotPair(L slot, R connection) {
		this.slot = slot;
		this.connection = connection;
	}

	//--------------- GETTERS AND SETTERS ----------------//
	
	public L getSlot() {
		return slot;
	}

	public R getConnection() {
		return connection;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connection == null) ? 0 : connection.hashCode());
		result = prime * result + ((slot == null) ? 0 : slot.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlotPair other = (SlotPair) obj;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!connection.equals(other.connection))
			return false;
		if (slot == null) {
			if (other.slot != null)
				return false;
		} else if (!slot.equals(other.slot))
			return false;
		return true;
	}

	
}
