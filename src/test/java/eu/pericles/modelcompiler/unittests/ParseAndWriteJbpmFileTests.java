package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;
import eu.pericles.modelcompiler.testutils.Utils;

import org.junit.Test;

public class ParseAndWriteJbpmFileTests {

	Utils util = new Utils();

	@Test
	public void testParseAndWriteHelloWorld() {
		parseAndWrite("src/test/resources/helloworld/");
	}

	@Test
	public void testParseAndWriteSubprocess() {
		parseAndWrite("src/test/resources/subprocess/");
	}

	@Test
	public void testParseAndWriteSignalEvents() {
		parseAndWrite("src/test/resources/signalevents/");
	}

	@Test
	public void testParseAndWriteMessageEvents() {
		parseAndWrite("src/test/resources/messageevents/");
	}
	
	@Test
	public void testParseAndWriteTimerEvents() {
		parseAndWrite("src/test/resources/timerevents/");
	}

	private void parseAndWrite(String path) {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "JbpmParseTest.bpmn2";
		String outputFileName = path + "JbpmParseOutput.bpmn2";

		assertTrue(util.checkParseAndWrite(inputFileName, testFileName, outputFileName));
	}
}
