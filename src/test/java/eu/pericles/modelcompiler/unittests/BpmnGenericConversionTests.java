package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.pericles.modelcompiler.common.BaseElement;
import eu.pericles.modelcompiler.testutils.PredefinedUUIDGenerator;
import eu.pericles.modelcompiler.testutils.Utils;

public class BpmnGenericConversionTests {

	Utils util = new Utils();
	//BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());;
	
	@Test
	public void testGenericConversionHelloWorld() {
		BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/helloworld/");
	}
	
	@Test
	public void testGenericConversionSubprocess() {
		BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/subprocesses/");
	}

	@Test
	public void testGenericConversionSignalEvents() {
		BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/signalevents/");
	}

	@Test
	public void testGenericConversionMessageEvents() {
		BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/messageevents/");
	}
	
	@Test
	public void testGenericConversionTimerEvents() {
		BaseElement baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/timerevents/");
	}

	private void parseConvertAndWrite(String path) {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "GenericConversionTest.bpmn2";
		String outputFileName = path + "GenericConversionOutput.bpmn2";

		assertTrue(Utils.checkParseGenericConvertAndWrite(inputFileName, testFileName, outputFileName));
	}

}
