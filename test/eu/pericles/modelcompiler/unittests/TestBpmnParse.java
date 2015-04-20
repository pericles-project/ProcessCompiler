package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.pericles.modelcompiler.bpmn.BpmnParser;
import eu.pericles.modelcompiler.bpmn.BpmnProcess;

public class TestBpmnParse {

	@Test
	public void testBpmnParse() {
		String nameFile = "test/PrintHelloWorld.bpmn2";
		BpmnParser bpmnParser = new BpmnParser();
		bpmnParser.parse(nameFile);
		BpmnProcess bpmnProcess = bpmnParser.getBpmnProcess();
		
		assertEquals("eu.pericles.modelcompiler.unittests.PrintHelloWorld", bpmnProcess.getId());
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
		
	}

}
