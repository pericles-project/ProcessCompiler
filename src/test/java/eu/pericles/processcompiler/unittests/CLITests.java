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
	public void compileAggregatedProcessByFileTest() {
		String command = "compile https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -f src/test/resources/cli/inputCompile.json src/test/resources/cli/output.bpmn2";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("201 Created"));
		
		Utils.fileContentEquals("src/test/resources/cli/output.bpmn2", "src/test/resources/cli/CompiledAggregatedProcessTest.bpmn2");
	}
	
	@Test
	public void compileAggregatedProcessByIDTest() {
		String command = "compile https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -i <http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW> src/test/resources/cli/output.bpmn2";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("201 Created"));
		
		Utils.fileContentEquals("src/test/resources/cli/output.bpmn2", "src/test/resources/cli/CompiledAggregatedProcessTest.bpmn2");
	}
	
	@Test
	public void validateAggregationByFileTest() {
		String command = "validate_aggregation https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -f src/test/resources/cli/inputValidateAggregation.json";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	}
	
	@Test
	public void validateAggregationByIDTest() {
		String command = "validate_aggregation https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -i <http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	}
	
	@Test
	public void validateImplementationByFileTest() {
		String command = "validate_implementation https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -f src/test/resources/cli/inputValidateImplementation.json -f src/test/resources/cli/EncapsulateDOMDProcess.bpmn2";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid implementation"));
	}
	
	@Test
	public void validateImplementationByIDTest() {
		String command = "validate_implementation https://pericles1:PASSWORD@141.5.100.67/api NoaCLITest -i <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD> -i <http://www.pericles-project.eu/ns/ecosystem#impEncapsulateDOMD>";
		String[] args = command.split(" ");
		CommandlineInterface.main(args);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid implementation"));
	}

}
