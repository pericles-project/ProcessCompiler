package eu.pericles.modelcompiler.bpmn.Data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:outputSet")
public class OutputSet {
	
	@XStreamAlias("bpmn2:dataOutputRefs")
	String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}