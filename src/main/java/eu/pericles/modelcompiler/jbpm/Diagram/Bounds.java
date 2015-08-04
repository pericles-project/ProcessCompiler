package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("dc:Bounds")
public class Bounds {
	@XStreamAsAttribute
	private String height;
	@XStreamAsAttribute
	private String width;
	@XStreamAsAttribute
	private String x;
	@XStreamAsAttribute
	private String y;

}
