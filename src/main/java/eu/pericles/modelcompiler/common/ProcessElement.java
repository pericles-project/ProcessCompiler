package eu.pericles.modelcompiler.common;

public abstract class ProcessElement {
	
	private Uid uid;
	
	public ProcessElement() {
		uid = new Uid();
	}

	public String getUid() {
		return uid.getUid();
	}
	
	public void setUid(String uid) {
		this.uid.checkAndSetUid(uid);
	}

}
