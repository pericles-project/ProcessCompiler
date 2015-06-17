package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:timerEventDefinition")
public class TimerEventDefinition {
	
	@XStreamAlias("bpmn2:timeCycle")
	private TimeCycle timeCycle;

	public TimeCycle getTimeCycle() {
		return timeCycle;
	}

	public void setTimeCycle(TimeCycle timeCycle) {
		this.timeCycle = timeCycle;
	}

}
