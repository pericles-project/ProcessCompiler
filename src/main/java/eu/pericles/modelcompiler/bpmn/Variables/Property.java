package eu.pericles.modelcompiler.bpmn.Variables;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmn2:property")
public class Property {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String itemSubjectRef;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemSubjectRef() {
		return itemSubjectRef;
	}
	public void setItemSubjectRef(String itemSubjectRef) {
		this.itemSubjectRef = itemSubjectRef;
	}
	

}
