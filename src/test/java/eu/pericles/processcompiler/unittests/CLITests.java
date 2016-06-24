package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.cli.CommandlineInterface;
import eu.pericles.processcompiler.testutils.Utils;

public class CLITests {
	
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	
	@Before
	public void changeOutputStream() {
		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}
	
	@After
	public void restoreDefaultOutputStream() {
		System.setOut(defaultOutputStream);
	}

	@Test
	public void compileAggregatedProcessTest() {
		String[] args = {"processcompiler.jar", "compile", "src/test/resources/cli/inputCompile.json", "src/test/resources/cli/output.bpmn2"};		
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("201 Created"));
		
		Utils.fileContentEquals("src/test/resources/cli/output.bpmn2", "src/test/resources/cli/CompiledAggregatedProcessTest.bpmn2");
	}
	
	@Test
	public void validateAggregationTest() {
		String[] args = {"processcompiler.jar", "validate_aggregation", "src/test/resources/cli/inputValidateAggregation.json"};		
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	}
	
	@Test
	public void validateImplementationTest() {
		String[] args = {"processcompiler.jar", "validate_implementation", "src/test/resources/cli/inputValidateImplementation.json", "src/test/resources/cli/EncapsulateDOMDProcess.bpmn2"};		
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid implementation"));
	}

}
