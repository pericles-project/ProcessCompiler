package eu.pericles.modelcompiler.common;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.jbpm.JbpmFile;

public class BpmnJbpmConversor {
	
	private BpmnProcess bpmnProcess;
	private JbpmFile jbpmFile;
	
	public BpmnJbpmConversor() {
		bpmnProcess = new BpmnProcess();
		jbpmFile = new JbpmFile();
	}
	
	public void convertFromBpmnToJbpm(BpmnProcess bpmnProcess) {
		setBpmnProcess(bpmnProcess);
		setJbpmFile(new JbpmFile());
		
		fillDefinitionsDataByDefault();
		addExternalVariablesToJbpmFile();
		convertProcessFromBpmnToJbpm();
		
	}
	
	private void addExternalVariablesToJbpmFile() {
		if (bpmnProcess.getItemDefinitions() != null)
			jbpmFile.setItemDefinitions(bpmnProcess.getItemDefinitions());
		if (bpmnProcess.getMessages() != null)
			jbpmFile.setMessages(bpmnProcess.getMessages());		
	}

	private void fillDefinitionsDataByDefault() {
		jbpmFile.setXmlns_xsi("http://www.w3.org/2001/XMLSchema-instance");
		jbpmFile.setXmlns_bpmn2("http://www.omg.org/spec/BPMN/20100524/MODEL");
		jbpmFile.setXmlns_bpmndi("http://www.omg.org/spec/BPMN/20100524/DI");
		jbpmFile.setXmlns_dc("http://www.omg.org/spec/DD/20100524/DC");
		jbpmFile.setXmlns_di("http://www.omg.org/spec/DD/20100524/DI");
		jbpmFile.setXmlns_tns("http://www.jboss.org/drools");
		jbpmFile.setXmlns("http://www.jboss.org/drools");
		jbpmFile.setXsi_schemaLocation("http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd http://www.jboss.org/drools drools.xsd http://www.bpsim.org/schemas/1.0 bpsim.xsd");
		jbpmFile.setHeaderId("Definition");
		jbpmFile.setExpressionLanguage("http://www.mvel.org/2.0");
		jbpmFile.setTargetNamespace("http://www.jboss.org/drools");
		jbpmFile.setTypeLanguage("http://www.java.com/javaTypes");
	}
	
	private void convertProcessFromBpmnToJbpm() {
		jbpmFile.setBpmnProcess(bpmnProcess);
		deleteExternalVariablesFromProcess();
	}
	
	private void deleteExternalVariablesFromProcess() {
		jbpmFile.getBpmnProcess().setItemDefinitions(null);//.getItemDefinitions().removeAll(jbpmFile.getBpmnProcess().getItemDefinitions());
		jbpmFile.getBpmnProcess().setMessages(null);//.getMessages().removeAll(jbpmFile.getBpmnProcess().getMessages());		
	}	
	
	//---- Getters and setters ----// 

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public JbpmFile getJbpmFile() {
		return jbpmFile;
	}

	public void setJbpmFile(JbpmFile jbpmFile) {
		this.jbpmFile = jbpmFile;
	}
	
}
