package eu.pericles.modelcompiler.common;

import java.util.UUID;

public class RandomUidGenerator implements UidGeneration {

	@Override
	public String requestUUID() {
		return UUID.randomUUID().toString();
	}
	
	public boolean isValidStringFormat(String uid) {
		return uid.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
	}

}
