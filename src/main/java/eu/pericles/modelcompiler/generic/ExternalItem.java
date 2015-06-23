package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.ProcessElement;


public class ExternalItem extends ProcessElement{
	
	public enum Type {
		ITEM, MESSAGE, SIGNAL, OPERATION
	}

	private Type type;
	private String reference;
	private String structure;

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

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

}
