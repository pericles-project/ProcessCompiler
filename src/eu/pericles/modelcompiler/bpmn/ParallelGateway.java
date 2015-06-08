package eu.pericles.modelcompiler.bpmn;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:parallelGateway")
public class ParallelGateway {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("gatewayDirection")
	private String direction;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
