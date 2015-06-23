package eu.pericles.modelcompiler.bpmn.ExternalItems;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:itemDefinition")
public class ItemDefinition {

	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String structureRef;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStructureRef() {
		return structureRef;
	}

	public void setStructureRef(String structureRef) {
		this.structureRef = structureRef;
	}
}
