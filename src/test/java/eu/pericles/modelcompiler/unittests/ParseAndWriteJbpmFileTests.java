package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;
import eu.pericles.modelcompiler.unittests.Utils;

import org.junit.Test;

public class ParseAndWriteJbpmFileTests {

	Utils util = new Utils();

	@Test
	public void testParseAndWriteHelloWorldExample() {
		parseAndWrite("HelloWorld");
	}

	@Test
	public void testParseAndWriteSubprocessExample() {
		parseAndWrite("Subprocess");
	}

	@Test
	public void testParseAndWriteSignalEventsExample() {
		parseAndWrite("SignalEvents");
	}

	@Test
	public void testParseAndWriteMessageEventsExample() {
		parseAndWrite("MessageEvents");
	}
	
	@Test
	public void testParseAndWriteTimerEventsExample() {
		parseAndWrite("TimerEvents");
	}

	private void parseAndWrite(String scenario) {
		String inputFileName = "src/test/resources/" + scenario + "Input.bpmn2";
		String testFileName = "src/test/resources/" + scenario + "Test.bpmn2";
		String outputFileName = "src/test/resources/" + scenario + "Output.bpmn2";

		assertTrue(util.checkParseAndWrite(inputFileName, testFileName, outputFileName));
	}
}
