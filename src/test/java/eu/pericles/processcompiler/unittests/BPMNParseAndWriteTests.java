package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.testutils.Utils;

@RunWith(Parameterized.class)
public class BPMNParseAndWriteTests {
	
	static String[] folders = {"helloworld", "completeprocess", "serviceprocess"};
	private String path;
	
    public BPMNParseAndWriteTests(String folder) {
        this.path = "src/test/resources/bpmnfiles/" + folder + "/";
    }
	
    @Parameters(name="{0}")
    public static Collection<String> folders() {
        return Arrays.asList(folders);
    }

	@Test
	public void parseAndWriteBPMNFile() {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "Test.bpmn2";
		String outputFileName = path + "Output.bpmn2";
		
		try {
			new BPMNWriter().write(new BPMNParser().parse(inputFileName), outputFileName);
		} catch (Exception e) {
			fail("ParseAndWriteBPMNFile(): " + e.getMessage());
		}
		
		Utils.fileContentEquals(outputFileName, testFileName);
	}

}
