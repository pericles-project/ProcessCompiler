package eu.pericles.modelcompiler.bpmn;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:sequenceFlow")
public class SequenceFlow{
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("sourceRef")
	private String source;
	@XStreamAsAttribute
	@XStreamAlias("targetRef")
	private String target;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
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
