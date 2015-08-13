package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.Element;

public class Variable implements Element {

	public enum Type {
		PROPERTY
	}

	private String uid;
	private Type type;
	private String reference;

	// ---- Getters and setters ----//

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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
