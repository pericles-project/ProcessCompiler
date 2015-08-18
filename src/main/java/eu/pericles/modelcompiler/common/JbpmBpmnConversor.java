package eu.pericles.modelcompiler.common;

import java.util.ArrayList;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.jbpm.JbpmFile;

public class JbpmBpmnConversor {
	
	private BpmnProcess bpmnProcess;
	private JbpmFile jbpmFile;
	
	public JbpmBpmnConversor() {
		init();
	}
	
	private void init() {
		bpmnProcess = new BpmnProcess();
		jbpmFile = new JbpmFile();
	}
	
	public void convert(JbpmFile jbpmFile) {		
		setJbpmFile(jbpmFile);
		setBpmnProcess(jbpmFile.getBpmnProcess());

		/* Add lists of external variables: item definitions, messages, etc. */
		addExternalVariablesToBpmnProcess();
	}

	private void addExternalVariablesToBpmnProcess() {
		
		if (getJbpmFile().getItemDefinitions() == null) 
			getBpmnProcess().setItemDefinitions(new ArrayList<ItemDefinition>());
		else 
			getBpmnProcess().setItemDefinitions(getJbpmFile().getItemDefinitions());
		
		if (getJbpmFile().getMessages() == null)
			getBpmnProcess().setMessages(new ArrayList<Message>());
		else
			getBpmnProcess().setMessages(getJbpmFile().getMessages());
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
