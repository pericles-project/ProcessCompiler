package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import eu.pericles.modelcompiler.exceptions.BpmnObjectNotCompletedException;

@XStreamAlias("bpmn2:timerEventDefinition")
public class TimerEventDefinition {
	
	public enum Type {
		CYCLE, DATE, DURATION
	}
	
	@XStreamAlias("bpmn2:timeCycle")
	private TimeCycle timeCycle;
	@XStreamAlias("bpmn2:timeDate")
	private TimeDate timeDate;
	@XStreamAlias("bpmn2:timeDuration")
	private TimeDuration timeDuration;
	
	

	public Type getType() throws BpmnObjectNotCompletedException {
		if (timeCycle != null)
			return Type.CYCLE;
		else if (timeDate != null)
			return Type.DATE;
		else if (timeDuration != null)
			return Type.DURATION;
		else
			throw new BpmnObjectNotCompletedException("TimerEventDefinition has incomplete definition");
	}

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
