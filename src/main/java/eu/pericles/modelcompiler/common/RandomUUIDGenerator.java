package eu.pericles.modelcompiler.common;

import java.util.UUID;

public class RandomUUIDGenerator implements UUIDGeneration {

	@Override
	public String requestUniqueIdentifier() {
		return UUID.randomUUID().toString();
	}

}
