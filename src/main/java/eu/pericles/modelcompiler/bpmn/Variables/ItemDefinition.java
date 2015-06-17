package eu.pericles.modelcompiler.bpmn.Variables;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:itemDefinition")
public class ItemDefinition {

	@XStreamAsAttribute
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
