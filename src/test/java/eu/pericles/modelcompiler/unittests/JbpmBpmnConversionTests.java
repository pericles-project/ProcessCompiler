package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.pericles.modelcompiler.unittests.Utils;

public class JbpmBpmnConversionTests {
	
	Utils util = new Utils();

	@Test
	public void testBpmnConversionHelloWorldExample() {
		parseConvertAndWrite("HelloWorld");
	}

	@Test
	public void testBpmnConversionSubprocessExample() {
		parseConvertAndWrite("Subprocess");
	}

	@Test
	public void testBpmnConversionSignalEventsExample() {
		parseConvertAndWrite("SignalEvents");
	}

	@Test
	public void testBpmnConversionMessagesEventsExample() {
		parseConvertAndWrite("MessageEvents");
	}
	
	@Test
	public void testBpmnConversionTimerEventsExample() {
		parseConvertAndWrite("TimerEvents");
	}

	private void parseConvertAndWrite(String scenario) {
		String inputFileName = "src/test/resources/" + scenario + "Input.bpmn2";
		String testFileName = "src/test/resources/" + scenario + "ConversionTest.bpmn2";
		String outputFileName = "src/test/resources/" + scenario + "ConversionOutput.bpmn2";

		assertTrue(util.checkParseBpmnConvertAndWrite(inputFileName, testFileName, outputFileName));
	}
}
