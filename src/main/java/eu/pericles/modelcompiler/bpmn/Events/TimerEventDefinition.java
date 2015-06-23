package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:timerEventDefinition")
public class TimerEventDefinition {
	
	@XStreamAlias("bpmn2:timeCycle")
	private TimeCycle timeCycle;
	@XStreamAlias("bpmn2:timeDate")
	private TimeDate timeDate;
	@XStreamAlias("bpmn2:timeDuration")
	private TimeDuration timeDuration;

	public TimeCycle getTimeCycle() {
		return timeCycle;
	}
	public void setTimeCycle(TimeCycle timeCycle) {
		this.timeCycle = timeCycle;
	}
	public TimeDate getTimeDate() {
		return timeDate;
	}
	public void setTimeDate(TimeDate timeDate) {
		this.timeDate = timeDate;
	}
	public TimeDuration getTimeDuration() {
		return timeDuration;
	}
	public void setTimeDuration(TimeDuration timeDuration) {
		this.timeDuration = timeDuration;
	}

}
