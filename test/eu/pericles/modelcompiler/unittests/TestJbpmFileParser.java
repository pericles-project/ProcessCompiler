package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;

public class TestJbpmFileParser {

	@Test
	public void testJbpmFileParser() {
		String nameFile = "test/testFiles/ExternalVariablesExample.bpmn2";
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(nameFile);
		BpmnProcess bpmnProcess = jbpmFileParser.getJbpmFile().getBpmnProcess();
		
		assertEquals("StartEvent_1", bpmnProcess.getStartEvents().get(0).getId());
		assertEquals("EndEvent_1", bpmnProcess.getEndEvents().get(0).getId());
		assertEquals("ScriptTask_1", bpmnProcess.getScriptTasks().get(0).getId());
		assertEquals("printf(\"Hello World\");", bpmnProcess.getScriptTasks().get(0).getScript());
		assertEquals("SequenceFlow_1", bpmnProcess.getSequenceFlows().get(0).getId());
		assertEquals("StartEvent_1", bpmnProcess.getSequenceFlows().get(0).getSource());
		assertEquals("ScriptTask_1", bpmnProcess.getSequenceFlows().get(0).getTarget());
		assertEquals("SequenceFlow_2", bpmnProcess.getSequenceFlows().get(1).getId());
		assertEquals("ScriptTask_1", bpmnProcess.getSequenceFlows().get(1).getSource());
		assertEquals("EndEvent_1", bpmnProcess.getSequenceFlows().get(1).getTarget());
		assertEquals("message_1", bpmnProcess.getMessages().get(0).getId());
		assertEquals("itemDefinition_1", bpmnProcess.getItemDefinitions().get(0).getId());
		assertEquals("Signal_1", bpmnProcess.getSignals().get(0).getId());
		assertEquals("Signal_2", bpmnProcess.getSignals().get(1).getId());
	}

}
