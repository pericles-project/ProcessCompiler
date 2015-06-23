package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.ProcessElement;

public class Event extends ProcessElement {
	
	public enum Type {
		NONE_START, SIGNAL_START, MESSAGE_START, TIMER_START, NONE_END, SIGNAL_END, MESSAGE_END, 
		NONE_CATCH, SIGNAL_CATCH, MESSAGE_CATCH, TIMER_CATCH, NONE_THROW, SIGNAL_THROW, MESSAGE_THROW
	}

	private Type type;
	private String ref;
	
	
	//---- Getters and setters ----// 

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

}

