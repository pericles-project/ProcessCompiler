package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.pericles.modelcompiler.testutils.Utils;

public class BpmnGenericConversionTests {
	
	@Test
	public void testGenericConversionHelloWorld() {
		parseConvertAndWrite("src/test/resources/helloworld/");
	}
	
	@Test
	public void testGenericConversionSubprocess() {
		parseConvertAndWrite("src/test/resources/subprocesses/");
	}

	@Test
	public void testGenericConversionSignalEvents() {
		parseConvertAndWrite("src/test/resources/signalevents/");
	}

	@Test
	public void testGenericConversionMessageEvents() {
		parseConvertAndWrite("src/test/resources/messageevents/");
	}
	
	@Test
	public void testGenericConversionTimerEvents() {
		parseConvertAndWrite("src/test/resources/timerevents/");
	}

	private void parseConvertAndWrite(String path) {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "GenericConversionTest.bpmn2";
		String outputFileName = path + "GenericConversionOutput.bpmn2";

		assertTrue(Utils.checkParseGenericConvertAndWrite(inputFileName, testFileName, outputFileName));
	}

}
