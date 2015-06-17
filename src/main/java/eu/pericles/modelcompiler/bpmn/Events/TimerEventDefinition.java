package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:timerEventDefinition")
public class TimerEventDefinition {
	
	@XStreamAlias("bpmn2:timeCycle")
	private String timeCycle;

	public String getTimeCycle() {
		return timeCycle;
	}

	public void setTimeCycle(String timeCycle) {
		this.timeCycle = timeCycle;
	}

}
