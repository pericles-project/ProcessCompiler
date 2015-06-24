package eu.pericles.modelcompiler.bpmn.Data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:dataInputAssociation")
public class DataInputAssociation {
	
	@XStreamAlias("bpmn2:sourceRef")
	String source;
	@XStreamAlias("bpmn2:targetRef")
	String target;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

}
