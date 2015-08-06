package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.BaseElement;

public class Gateway extends BaseElement {
	
	public enum Type {
		CONVERGING_PARALLEL, DIVERGING_PARALLEL
	}

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	

}
