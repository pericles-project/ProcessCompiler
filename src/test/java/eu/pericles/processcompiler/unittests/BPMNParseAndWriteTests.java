package eu.pericles.processcompiler.unittests;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import eu.pericles.processcompiler.testutils.Utils;
import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;

@RunWith(Parameterized.class)
public class BPMNParseAndWriteTests {
	
	static String[] folders = {"helloworld", "completeprocess"};
	private String path;
	
    public BPMNParseAndWriteTests(String folder) {
        this.path = "src/test/resources/" + folder + "/";
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
		
		write(parse(inputFileName), outputFileName);
		
		Utils.fileContentEquals(outputFileName, testFileName);
	}

	private BPMNProcess parse(String inputFileName) {
		BPMNParser parser = new BPMNParser();
		parser.parse(inputFileName);
		BPMNProcess process = parser.getBPMNProcess();
		
		return process;
	}

	private void write(BPMNProcess process, String outputFileName) {
		
		BPMNWriter writer = new BPMNWriter();
		writer.write(process, outputFileName);		
	}

}
