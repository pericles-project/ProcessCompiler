package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("di:waypoint")
public class Waypoint {
	@XStreamAlias("xsi:type")
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String x;
	@XStreamAsAttribute
	private String y;

}
