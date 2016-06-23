package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.pericles.processcompiler.cli.CommandlineInterface;
import eu.pericles.processcompiler.testutils.Utils;

public class CLITests {

	@Test
	public void compileAggregatedProcessTest() {
		String[] args = {"processcompiler.jar", "src/test/resources/cli/input.json"};
		try {
			CommandlineInterface.main(args);
			Utils.fileContentEquals("src/test/resources/cli/output.bpmn2", "src/test/resources/cli/CompiledAggregatedProcessTest.bpmn2");
		} catch (Exception e) {
			fail("compileAggregatedProcess(): " + e.getMessage());
		}
	}

}
