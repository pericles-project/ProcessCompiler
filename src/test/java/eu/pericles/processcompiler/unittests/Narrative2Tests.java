package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.cli.CommandlineInterface;

public class Narrative2Tests {
	
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	private String baseArgs = "-s https://pericles1:PASSWORD@141.5.100.67/api -r NoaNarrative2 ";
	
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
	public void validateAggregatedProcess() {
		String command = baseArgs + "validate_aggregation <http://www.pericles-project.eu/ns/nar2#agpGetAllMD>";
		String[] args = command.split(" ");
		assertEquals(0, new CommandlineInterface().call(args));
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
		assertTrue(result.contains("Valid aggregation"));
	}

}
