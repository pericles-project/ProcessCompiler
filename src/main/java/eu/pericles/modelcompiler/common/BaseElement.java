package eu.pericles.modelcompiler.common;

public class BaseElement {
	private String uid;
	private static UUIDGeneration uniqueIdentifier = new RandomUUIDGenerator();

	public BaseElement() {
		System.out.println("BaseElement()");
		init();
	}

	public BaseElement(UUIDGeneration uniqueIdentifier) {
		System.out.println("BaseElement(UUIDGeneration uniqueIdentifier)");
		BaseElement.uniqueIdentifier = uniqueIdentifier;
		init();
	}

	private void init() {
		System.out.println("init(): " + uniqueIdentifier.getClass());
		uid = uniqueIdentifier.requestUniqueIdentifier();
		System.out.println(uid);
	}

	public String getUid() {
		return uid;
	}

}
