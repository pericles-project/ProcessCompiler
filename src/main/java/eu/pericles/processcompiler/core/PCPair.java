package eu.pericles.processcompiler.core;

public class PCPair {
	
	private int step;
	private String slot;

	public PCPair(int step, String slot) {
		this.step = step;
		this.slot = slot;
	}

	public int getStep() {
		return step;
	}

	public String getSlot() {
		return slot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((slot == null) ? 0 : slot.hashCode());
		result = prime * result + step;
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
		PCPair other = (PCPair) obj;
		if (slot == null) {
			if (other.slot != null)
				return false;
		} else if (!slot.equals(other.slot))
			return false;
		if (step != other.step)
			return false;
		return true;
	}

}
