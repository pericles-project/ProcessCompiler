package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.Element;

public class Data implements Element {

	private String uid;
	private String association;
	private String reference;
	
	//---- Getters and setters ----// 

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	
}
