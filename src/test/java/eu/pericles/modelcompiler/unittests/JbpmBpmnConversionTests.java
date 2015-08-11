package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.pericles.modelcompiler.testutils.Utils;

public class JbpmBpmnConversionTests {
	
	Utils util = new Utils();

	@Test
	public void testBpmnConversionHelloWorld() {
		parseConvertAndWrite("src/test/resources/helloworld/");
	}

	@Test
	public void testBpmnConversionSubprocess() {
		parseConvertAndWrite("src/test/resources/subprocess/");
	}

	@Test
	public void testBpmnConversionSignalEvents() {
		parseConvertAndWrite("src/test/resources/signalevents/");
	}

	@Test
	public void testBpmnConversionMessageEvents() {
		parseConvertAndWrite("src/test/resources/messageevents/");
	}
	
	@Test
	public void testBpmnConversionTimerEvents() {
		parseConvertAndWrite("src/test/resources/timerevents/");
	}

	private void parseConvertAndWrite(String path) {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "BpmnConversionTest.bpmn2";
		String outputFileName = path + "BpmnConversionOutput.bpmn2";

		assertTrue(util.checkParseBpmnConvertAndWrite(inputFileName, testFileName, outputFileName));
	}
}
