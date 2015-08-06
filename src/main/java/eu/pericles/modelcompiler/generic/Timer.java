package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.BaseElement;

public class Timer extends BaseElement {
	
	public enum Type { CYCLE, DURATION, DATE }
	
	String time;
	String timeType;
	String language;
	Type type;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String type) {
		this.timeType = type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
