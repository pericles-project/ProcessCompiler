package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.BaseElement;

public class Event extends BaseElement {
	
	public enum Type {
		NONE_START, SIGNAL_START, MESSAGE_START, TIMER_START, NONE_END, SIGNAL_END, MESSAGE_END, 
		NONE_CATCH, SIGNAL_CATCH, MESSAGE_CATCH, TIMER_CATCH, NONE_THROW, SIGNAL_THROW, MESSAGE_THROW
	}

	private Type type;
	private String reference;
	private Timer timer;
	private Data data;
	
	//---- Getters and setters ----// 

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String ref) {
		this.reference = ref;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}

