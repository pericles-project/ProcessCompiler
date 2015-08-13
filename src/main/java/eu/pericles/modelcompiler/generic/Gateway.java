package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.Element;

public class Gateway implements Element {

	public enum Type {
		CONVERGING_PARALLEL, DIVERGING_PARALLEL
	}

	private String uid;
	private Type type;

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

}
