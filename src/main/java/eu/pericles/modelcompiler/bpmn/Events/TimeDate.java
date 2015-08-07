package eu.pericles.modelcompiler.bpmn.Events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("bpmn2:timeDate")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"time"})
public class TimeDate {
	
	@XStreamAsAttribute
	private String id;
	String time;
	@XStreamAlias("xsi:type")
	String type;
	@XStreamAsAttribute
	String language;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
