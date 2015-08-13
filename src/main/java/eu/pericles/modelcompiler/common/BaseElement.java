package eu.pericles.modelcompiler.common;

public class BaseElement {
	private String uid;
	private static UidGeneration generator = new RandomUidGenerator();

	public BaseElement() {
		init();
	}

	public BaseElement(UidGeneration generator) {
		BaseElement.generator = generator;
		init();
	}

	private void init() {
		uid = generator.requestUUID();
	}

	public String getUid() {
		return uid;
	}
	
	public void setUUIDGenerator (UidGeneration generator) {
		BaseElement.generator = generator;
	}

}
