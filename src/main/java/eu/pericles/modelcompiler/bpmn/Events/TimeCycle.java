package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("timeCycle")
public class TimeCycle {
	
	String cycle;

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

}
