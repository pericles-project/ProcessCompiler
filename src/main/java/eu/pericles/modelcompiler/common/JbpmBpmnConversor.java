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
		bpmnProcess = new BpmnProcess();
		jbpmFile = new JbpmFile();
	}
	
	public void convert(JbpmFile jbpmFile) {		
		setJbpmFile(jbpmFile);
		setBpmnProcess(jbpmFile.getBpmnProcess());
		
		/* When reading a process from a bpmn file with DOM, those lists of elements that are not in the file remain 
		 * pointing to NULL. With checkAndComplete(), those lists are created as empty lists.
		 */
		getBpmnProcess().checkAndComplete();

		/* Add lists of external variables: item definitions, messages, etc. */
		addExternalVariablesToBpmnProcess();
	}

	private void addExternalVariablesToBpmnProcess() {
		
		if (jbpmFile.getItemDefinitions() == null) 
			bpmnProcess.setItemDefinitions(new ArrayList<ItemDefinition>());
		else 
			getBpmnProcess().setItemDefinitions(jbpmFile.getItemDefinitions());
		
		if (jbpmFile.getMessages() == null)
			bpmnProcess.setMessages(new ArrayList<Message>());
		else
			bpmnProcess.setMessages(jbpmFile.getMessages());
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
