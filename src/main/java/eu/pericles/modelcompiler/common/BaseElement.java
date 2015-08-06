package eu.pericles.modelcompiler.common;

public abstract class BaseElement {
	
	private Uid uid;
	
	public BaseElement() {
		uid = new Uid();
	}

	public String getUid() {
		return uid.getUid();
	}
	
	public void setUid(String uid) {
		this.uid.checkAndSetUid(uid);
	}

}
