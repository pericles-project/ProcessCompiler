package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;

public class TestJbpmFileParser {

	@Test
	public void testHelloWorldParse() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/HelloWorldExample.bpmn2");

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

	@Test
	public void testSubprocessBasicParse() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/SubprocessBasicExample.bpmn2");

		assertEquals("StartEvent_1", bpmnProcess.getStartEvents().get(0).getId());
		assertEquals("EndEvent_2", bpmnProcess.getEndEvents().get(0).getId());
		assertEquals("SequenceFlow_1", bpmnProcess.getSequenceFlows().get(0).getId());
		assertEquals("StartEvent_1", bpmnProcess.getSequenceFlows().get(0).getSource());
		assertEquals("SubProcess_1", bpmnProcess.getSequenceFlows().get(0).getTarget());

		assertEquals("SubProcess_1", bpmnProcess.getSubprocesses().get(0).getId());
		assertEquals("StartEvent_2", bpmnProcess.getSubprocesses().get(0).getStartEvents().get(0).getId());
		assertEquals("EndEvent_1", bpmnProcess.getSubprocesses().get(0).getEndEvents().get(0).getId());
		assertEquals("SequenceFlow_2", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(0).getId());
		assertEquals("StartEvent_2", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(0).getSource());
		assertEquals("ScriptTask_1", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(0).getTarget());
		assertEquals("SequenceFlow_3", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(1).getId());
		assertEquals("ScriptTask_1", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(1).getSource());
		assertEquals("EndEvent_1", bpmnProcess.getSubprocesses().get(0).getSequenceFlows().get(1).getTarget());
	}

	@Test
	public void testSignalStartEvent() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/SignalStartEventExample.bpmn2");
		assertEquals("StartEvent_1", bpmnProcess.getStartEvents().get(0).getId());
	}	
	@Test
	public void testSignalEndEvent() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/SignalEndEventExample.bpmn2");
		assertEquals("EndEvent_1", bpmnProcess.getEndEvents().get(0).getId());
	}	
	@Test
	public void testMessageStartEvent() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/MessageStartEventExample.bpmn2");
		assertEquals("StartEvent_1", bpmnProcess.getStartEvents().get(0).getId());
		assertEquals(bpmnProcess.getMessages().get(0).getId(), bpmnProcess.getStartEvents().get(0).getMessageEventDefinition().getMessageRef());
	}
	@Test
	public void testMessageEndEvent() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/MessageEndEventExample.bpmn2");
		assertEquals("EndEvent_1", bpmnProcess.getEndEvents().get(0).getId());
		assertEquals(bpmnProcess.getMessages().get(0).getId(), bpmnProcess.getEndEvents().get(0).getMessageEventDefinition().getMessageRef());
	}	
	@Test
	public void testTimerStartEvent() {
		BpmnProcess bpmnProcess = getBpmnProcess("src/test/resources/TimerStartEventExample.bpmn2");
		assertEquals("StartEvent_1", bpmnProcess.getStartEvents().get(0).getId());
		assertEquals("500ms", bpmnProcess.getStartEvents().get(0).getTimerEventDefinition().getTimeCycle().getTime());
		assertEquals("tFormalExpression", bpmnProcess.getStartEvents().get(0).getTimerEventDefinition().getTimeCycle().getType());
	}


	private BpmnProcess getBpmnProcess(String file) {		
		JbpmFileParser jbpmFileParser = parseJbpmFile(file);

		return jbpmFileParser.getJbpmFile().getBpmnProcess();
	}

	private JbpmFileParser parseJbpmFile(String file) {
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(file);

		return jbpmFileParser;
	}

}
