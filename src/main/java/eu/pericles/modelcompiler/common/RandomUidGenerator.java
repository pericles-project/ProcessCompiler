package eu.pericles.modelcompiler.common;

import java.util.UUID;

public class RandomUidGenerator implements UidGeneration {

	@Override
	public String requestUUID() {
		return UUID.randomUUID().toString();
	}

}
