package eu.pericles.modelcompiler.unittests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import eu.pericles.modelcompiler.common.BpmnJbpmConversor;
import eu.pericles.modelcompiler.common.JbpmBpmnConversor;
import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;
import eu.pericles.modelcompiler.jbpm.JbpmFileWriter;

public class Utils {
	
	public boolean checkParseAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(parse(inputFileName), outputFileName);

		return fileContentEquals(outputFileName, testFileName);
	}
	
	public boolean checkParseBpmnConvertAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(convertJbpmBpmnJbpm(parse(inputFileName)), outputFileName);
		
		return fileContentEquals(outputFileName, testFileName);
	}
	
	private void write(JbpmFile jbpmFile, String outputFileName) {
		JbpmFileWriter jbpmFileWriter = new JbpmFileWriter();
		jbpmFileWriter.write(jbpmFile, outputFileName);	
	}
	
	private JbpmFile parse(String inputFileName) {
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(inputFileName);

		return jbpmFileParser.getJbpmFile();
	}
	
	private JbpmFile convertJbpmBpmnJbpm(JbpmFile jbpmFile) {
		JbpmBpmnConversor jbpmBpmnConversor = new JbpmBpmnConversor();
		jbpmBpmnConversor.convert(jbpmFile);
		BpmnJbpmConversor bpmnJbpmConversor = new BpmnJbpmConversor();
		bpmnJbpmConversor.convert(jbpmBpmnConversor.getBpmnProcess());
		
		return bpmnJbpmConversor.getJbpmFile();
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
