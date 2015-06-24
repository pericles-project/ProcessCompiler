package eu.pericles.modelcompiler.bpmn.Data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:inputSet")
public class InputSet {
	
	@XStreamAlias("bpmn2:dataInputRefs")
	String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
