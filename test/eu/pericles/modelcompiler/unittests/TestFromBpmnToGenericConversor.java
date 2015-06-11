package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.common.FromBpmnToGenericConversor;
import eu.pericles.modelcompiler.common.Uid;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;

public class TestFromBpmnToGenericConversor {

	@Test
	public void testHelloWorldConversion() {
		Process genericProcess = getGenericProcess("test/testFiles/HelloWorldExample.bpmn2");
		
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
	public void testSubprocessBasicConversion() {
		Process genericProcess = getGenericProcess("test/testFiles/SubprocessBasicExample.bpmn2");
		
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
	
	@Test
	public void testGatewayParallelConversion() {
		Process genericProcess = getGenericProcess("test/testFiles/GatewayParallelExample.bpmn2");
		
		assertEquals("GatewayExample", genericProcess.getName());
		assertEquals(Gateway.Type.DIVERGING_PARALLEL, genericProcess.getGateways().get(0).getType());
		assertEquals(Gateway.Type.CONVERGING_PARALLEL, genericProcess.getGateways().get(1).getType());
		
		Uid uid = new Uid();
		assertTrue(uid.checkAndSetUid(genericProcess.getGateways().get(0).getUid())); 
		assertTrue(uid.checkAndSetUid(genericProcess.getGateways().get(1).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(0).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(1).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(2).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(3).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(4).getUid()));
		assertTrue(uid.checkAndSetUid(genericProcess.getFlows().get(5).getUid()));
		assertTrue(genericProcess.getFlows().get(0).getFrom()==genericProcess.getEvents().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(0).getTo()==genericProcess.getGateways().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getFrom()==genericProcess.getGateways().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(1).getTo()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(2).getFrom()==genericProcess.getGateways().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(2).getTo()==genericProcess.getActivities().get(1).getUid());
		assertTrue(genericProcess.getFlows().get(3).getFrom()==genericProcess.getActivities().get(0).getUid());
		assertTrue(genericProcess.getFlows().get(3).getTo()==genericProcess.getGateways().get(1).getUid());
		assertTrue(genericProcess.getFlows().get(4).getFrom()==genericProcess.getActivities().get(1).getUid());
		assertTrue(genericProcess.getFlows().get(4).getTo()==genericProcess.getGateways().get(1).getUid());
		assertTrue(genericProcess.getFlows().get(5).getFrom()==genericProcess.getGateways().get(1).getUid());
		assertTrue(genericProcess.getFlows().get(5).getTo()==genericProcess.getEvents().get(1).getUid());
		
	}

	private Process getGenericProcess(String file) {
		FromBpmnToGenericConversor conversor = new FromBpmnToGenericConversor();
		conversor.convertFromBpmnToGeneric(getBpmnProcess(file));
		
		return conversor.getGenericProcess();
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
