package eu.pericles.modelcompiler.unittests;

import org.junit.Test;

import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;
import eu.pericles.modelcompiler.jbpm.JbpmFileWriter;

public class TestJbpmFileWriter {

	@Test
	public void test() {
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse("src/test/resources/HelloWorldExample.bpmn2");
		
		JbpmFile jbpmFile = jbpmFileParser.getJbpmFile();
		
		JbpmFileWriter jbpmFileWriter = new JbpmFileWriter("output.bpmn2");
		jbpmFileWriter.write(jbpmFile);
	}

}
