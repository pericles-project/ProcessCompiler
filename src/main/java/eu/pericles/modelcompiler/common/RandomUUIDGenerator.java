package eu.pericles.modelcompiler.common;

import java.util.UUID;

public class RandomUUIDGenerator implements UUIDGeneration {

	@Override
	public String requestUUID() {
		return UUID.randomUUID().toString();
	}

}
