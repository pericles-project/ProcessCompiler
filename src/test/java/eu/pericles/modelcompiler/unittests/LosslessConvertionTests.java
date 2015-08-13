package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import eu.pericles.modelcompiler.testutils.Utils;

@RunWith(Parameterized.class)
public class LosslessConvertionTests {
	static String[] folders = {"helloworld", "subprocesses", "signalevents", "messageevents", "timerevents"};
	private String path;
	
    public LosslessConvertionTests(String folder) {
        this.path = "src/test/resources/" + folder + "/";
    }
	
    @Parameters(name="{0}")
    public static Collection<String> folders() {
        return Arrays.asList(folders);
    }
	
	@Test
	public void parseGenericConvertAndWrite() {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "GenericConversionTest.bpmn2";
		String outputFileName = path + "GenericConversionOutput.bpmn2";
		Utils.checkParseGenericConvertAndWrite(inputFileName, testFileName, outputFileName);
	}

	@Test
	public void parseBpmnConvertAndWrite() {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "BpmnConversionTest.bpmn2";
		String outputFileName = path + "BpmnConversionOutput.bpmn2";
		Utils.checkParseBpmnConvertAndWrite(inputFileName, testFileName, outputFileName);
	}

	@Test
	public void parseAndWrite() {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "JbpmParseTest.bpmn2";
		String outputFileName = path + "JbpmParseOutput.bpmn2";
		Utils.checkParseAndWrite(inputFileName, testFileName, outputFileName);
	}

}
