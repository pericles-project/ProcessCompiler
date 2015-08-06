package eu.pericles.modelcompiler.jbpm;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.jbpm.Diagram.Diagram;

@XStreamAlias("bpmn2:definitions")
public class JbpmFile {

	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xmlns_xsi;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:bpmn2")
	private String xmlns_bpmn2;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:bpmndi")
	private String xmlns_bpmndi;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:dc")
	private String xmlns_dc;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:di")
	private String xmlns_di;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:tns")
	private String xmlns_tns;
	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	private String xmlns;
	@XStreamAsAttribute
	@XStreamAlias("xsi:schemaLocation")
	private String xsi_schemaLocation;

	@XStreamAlias("id")
	@XStreamAsAttribute
	private String headerId;
	@XStreamAsAttribute
	private String expressionLanguage;
	@XStreamAsAttribute
	private String typeLanguage;
	@XStreamAsAttribute
	private String targetNamespace;
	
	@XStreamAlias("bpmn2:process")
	private BpmnProcess bpmnProcess;
	@XStreamAlias("bpmndi:BPMNDiagram")
	private Diagram diagram;
	@XStreamImplicit
	private List<Message> messages;
	@XStreamImplicit
	private List<ItemDefinition> itemDefinitions;
	
	//---- Getters and Setters ----//

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}
	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	public Diagram getDiagram() {
		return diagram;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public List<ItemDefinition> getItemDefinitions() {
		return itemDefinitions;
	}
	public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
		this.itemDefinitions = itemDefinitions;
	}

	public String getXmlns_xsi() {
		return xmlns_xsi;
	}

	public void setXmlns_xsi(String xmlns_xsi) {
		this.xmlns_xsi = xmlns_xsi;
	}

	public String getXmlns_bpmn2() {
		return xmlns_bpmn2;
	}

	public void setXmlns_bpmn2(String xmlns_bpmn2) {
		this.xmlns_bpmn2 = xmlns_bpmn2;
	}

	public String getXmlns_bpmndi() {
		return xmlns_bpmndi;
	}

	public void setXmlns_bpmndi(String xmlns_bpmndi) {
		this.xmlns_bpmndi = xmlns_bpmndi;
	}

	public String getXmlns_dc() {
		return xmlns_dc;
	}

	public void setXmlns_dc(String xmlns_dc) {
		this.xmlns_dc = xmlns_dc;
	}

	public String getXmlns_di() {
		return xmlns_di;
	}

	public void setXmlns_di(String xmlns_di) {
		this.xmlns_di = xmlns_di;
	}

	public String getXmlns_tns() {
		return xmlns_tns;
	}

	public void setXmlns_tns(String xmlns_tns) {
		this.xmlns_tns = xmlns_tns;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getXsi_schemaLocation() {
		return xsi_schemaLocation;
	}

	public void setXsi_schemaLocation(String xsi_schemaLocation) {
		this.xsi_schemaLocation = xsi_schemaLocation;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String id) {
		this.headerId = id;
	}

	public String getExpressionLanguage() {
		return expressionLanguage;
	}

	public void setExpressionLanguage(String expressionLanguage) {
		this.expressionLanguage = expressionLanguage;
	}

	public String getTypeLanguage() {
		return typeLanguage;
	}

	public void setTypeLanguage(String typeLanguage) {
		this.typeLanguage = typeLanguage;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	
}
