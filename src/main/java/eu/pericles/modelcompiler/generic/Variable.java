package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.ProcessElement;

public class Variable extends ProcessElement{
	
	public enum Type {
		PROPERTY
	}

	private Type type;
	private String reference;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
