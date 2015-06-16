package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.ProcessElement;

public class Event extends ProcessElement {
	
	public enum Type {
		START, END
	}

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}

