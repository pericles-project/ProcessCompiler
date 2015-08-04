package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;
import eu.pericles.modelcompiler.jbpm.JbpmFileWriter;

public class TestJbpmFileWriter {

	@Test
	public void testParseAndWriteHelloWorldExample() {
		String inputFileName = "src/test/resources/HelloWorldExample.bpmn2";
		String testOutputFileName = "src/test/resources/HelloWorldOutput.bpmn2";
		
		assertTrue(checkParseAndWrite(inputFileName, testOutputFileName));
		
	}
	
	private boolean checkParseAndWrite(String inputFileName, String testOutputFileName) {
		
		boolean result = false;
		
		// Parse
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(inputFileName);
		
		// Get jBPM File Object
		JbpmFile jbpmFile = jbpmFileParser.getJbpmFile();
		
		// Write
		String realOutputFileName = "output.bpmn2";
		JbpmFileWriter jbpmFileWriter = new JbpmFileWriter(realOutputFileName);
		jbpmFileWriter.write(jbpmFile);	
		
		// Compare files and delete generated files
		File realOutputFile = new File(realOutputFileName);
		File testOutputFile = new File(testOutputFileName);
		try {
			result = FileUtils.contentEquals(realOutputFile, testOutputFile);
			Files.delete(realOutputFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
