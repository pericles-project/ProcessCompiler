package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.Element;

public class Timer implements Element {

	public enum Type {
		CYCLE, DURATION, DATE
	}

	private String uid;
	private String time;
	private String timeType;
	private String language;
	private Type type;

	// ---- Getters and setters ----//

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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
	
	public void setValues(Type type, String time, String timeType, String language) {
		setType(type);
		setTime(time);
		setTimeType(timeType);
		setLanguage(language);
	}
}
