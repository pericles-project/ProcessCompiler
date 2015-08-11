package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.BaseElement;

public class Activity extends BaseElement {
	
	public enum Type {
		SCRIPT, USER_TASK, SERVICE_TASK
	}
	
	private String name;
	private Type type;
	private String script;
	
	//---- Getters and setters ----// 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
