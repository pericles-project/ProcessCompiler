package eu.pericles.modelcompiler.common;

import java.util.UUID;

public class Uid {

	private String uid;

	public Uid() {
		generateUid();
	}

	private void generateUid() {
		uid = UUID.randomUUID().toString();
	}
	
	public String getUid() {
		return uid;
	}
	
	public boolean checkAndSetUid(String uid) {
		if (isValidStringFormat(uid)) {
			this.uid = uid;
			return true;
		}
		else 
			return false;
			
	}
	
	public boolean isValid() {
		return isValidStringFormat(uid);
	}

	public boolean isValidStringFormat(String string) {
		return string.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
	}

}
