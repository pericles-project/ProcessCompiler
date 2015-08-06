package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.modelcompiler.common.BpmnJbpmConversor;
import eu.pericles.modelcompiler.common.JbpmBpmnConversor;
import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;
import eu.pericles.modelcompiler.jbpm.JbpmFileWriter;

public class TestJbpmFileWriter {

	@Test
	public void testParseAndWriteHelloWorldExample() {
		String inputFileName = "src/test/resources/HelloWorldInput.bpmn2";
		String testFileName = "src/test/resources/HelloWorldTest.bpmn2";
		String outputFileName = "src/test/resources/HelloWorldOutput.bpmn2";

		assertTrue(checkParseAndWrite(inputFileName, testFileName, outputFileName));

	}
	
	@Test
	public void testParseConvertAndWriteHelloWorldExample() {
		String inputFileName = "src/test/resources/HelloWorldInput.bpmn2";
		String testFileName = "src/test/resources/HelloWorldConversionTest.bpmn2";
		String outputFileName = "src/test/resources/HelloWorldConversionOutput.bpmn2";

		assertTrue(checkParseConvertAndWrite(inputFileName, testFileName, outputFileName));

	}

	private boolean checkParseAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(parse(inputFileName), outputFileName);

		return fileContentEquals(outputFileName, testFileName);
	}

	private boolean checkParseConvertAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(convertJbpmBpmnJbpm(parse(inputFileName)), outputFileName);
		
		return fileContentEquals(outputFileName, testFileName);
	}

	private JbpmFile parse(String inputFileName) {
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(inputFileName);

		return jbpmFileParser.getJbpmFile();
	}
	
	private JbpmFile convertJbpmBpmnJbpm(JbpmFile jbpmFile) {
		// Jbpm - Bpmn conversion
		JbpmBpmnConversor jbpmBpmnConversor = new JbpmBpmnConversor();
		jbpmBpmnConversor.convertFromJbpmToBpmn(jbpmFile);
		
		// Bpmn - Jbpm conversion
		BpmnJbpmConversor bpmnJbpmConversor = new BpmnJbpmConversor();
		bpmnJbpmConversor.convertFromBpmnToJbpm(jbpmBpmnConversor.getBpmnProcess());
		
		return bpmnJbpmConversor.getJbpmFile();
	}

	private void write(JbpmFile jbpmFile, String outputFileName) {
		JbpmFileWriter jbpmFileWriter = new JbpmFileWriter();
		jbpmFileWriter.write(jbpmFile, outputFileName);	
	}

	private boolean fileContentEquals(String outputFileName, String testFileName) {
		boolean result = false;
		File outputFile = new File(outputFileName);
		File testFile = new File(testFileName);
		try {
			result = FileUtils.contentEquals(outputFile, testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}



