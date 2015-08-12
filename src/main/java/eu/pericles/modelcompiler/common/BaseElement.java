package eu.pericles.modelcompiler.common;

public class BaseElement {
	private String uid;
	private static UUIDGeneration generator = new RandomUUIDGenerator();

	public BaseElement() {
		init();
	}

	public BaseElement(UUIDGeneration generator) {
		BaseElement.generator = generator;
		init();
	}

	private void init() {
		uid = generator.requestUUID();
	}

	public String getUid() {
		return uid;
	}
	
	public void setUUIDGenerator (UUIDGeneration generator) {
		BaseElement.generator = generator;
	}

}
