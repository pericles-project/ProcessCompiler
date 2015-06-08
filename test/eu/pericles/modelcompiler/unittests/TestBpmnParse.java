package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.pericles.modelcompiler.bpmn.BpmnParser;
import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.common.BpmnGenericConversor;
import eu.pericles.modelcompiler.common.Uid;
import eu.pericles.modelcompiler.generic.Process;

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
	
	@Test
	public void testSubprocessParse() {
		String nameFile = "test/SubprocessExample.bpmn2";
		BpmnParser bpmnParser = new BpmnParser();
		bpmnParser.parse(nameFile);
		BpmnProcess bpmnProcess = bpmnParser.getBpmnProcess();
		
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
	public void testConvertBpmnToGenericProcess() {		
		String nameFile = "test/PrintHelloWorld.bpmn2";
		BpmnParser bpmnParser = new BpmnParser();
		bpmnParser.parse(nameFile);
		BpmnProcess bpmnProcess = bpmnParser.getBpmnProcess();
		
		BpmnGenericConversor conversor = new BpmnGenericConversor();
		conversor.convertFromBpmnToGeneric(bpmnProcess);
		Process genericProcess = conversor.getGenericProcess();		
		
		assertEquals("PrintHelloWorld", genericProcess.getName());		
		assertEquals("HelloWorld", genericProcess.getActivities().get(0).getName());
		
		Uid uid = new Uid();
		assertTrue(uid.checkAndSetUid(genericProcess.getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getActivities().get(0).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getEvents().get(0).getUid())); 
		assertTrue(uid.checkAndSetUid(genericProcess.getEvents().get(1).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(0).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(1).getUid()));
		assertTrue(genericProcess.getFlows().get(0).getFrom()==genericProcess.getEvents().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(0).getTo()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getFrom()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getTo()==genericProcess.getEvents().get(1).getUid());
	}

	@Test
	public void testConvertBpmnToGenericSubprocess() {
		String nameFile = "test/SubprocessExample.bpmn2";
		BpmnParser bpmnParser = new BpmnParser();
		bpmnParser.parse(nameFile);
		BpmnProcess bpmnProcess = bpmnParser.getBpmnProcess();
		
		BpmnGenericConversor conversor = new BpmnGenericConversor();
		conversor.convertFromBpmnToGeneric(bpmnProcess);
		Process genericProcess = conversor.getGenericProcess();		
		
		assertEquals("SubprocessExample", genericProcess.getName());		
		assertEquals("Sub Process 1", genericProcess.getSubprocesses().get(0).getName());
		
		Uid uid = new Uid();
		assertTrue(uid.checkAndSetUid(genericProcess.getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getActivities().get(0).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getSubprocesses().get(0).getActivities().get(0).getUid()));
		assertTrue(genericProcess.getFlows().get(0).getFrom()==genericProcess.getEvents().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(0).getTo()==genericProcess.getSubprocesses().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getFrom()==genericProcess.getSubprocesses().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getTo()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(2).getFrom()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(2).getTo()==genericProcess.getEvents().get(1).getUid());
		assertTrue(genericProcess.getSubprocesses().get(0).getFlows().get(0).getFrom()==genericProcess.getSubprocesses().get(0).getEvents().get(0).getUid());
		assertTrue(genericProcess.getSubprocesses().get(0).getFlows().get(0).getTo()==genericProcess.getSubprocesses().get(0).getActivities().get(0).getUid());
		assertTrue(genericProcess.getSubprocesses().get(0).getFlows().get(1).getFrom()==genericProcess.getSubprocesses().get(0).getActivities().get(0).getUid());
		assertTrue(genericProcess.getSubprocesses().get(0).getFlows().get(1).getTo()==genericProcess.getSubprocesses().get(0).getEvents().get(1).getUid());
		
	}

}
