package eu.pericles.processcompiler.testutils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XMPCompareTest {
	@Test
	public void identityTest() throws SAXException, IOException, ParserConfigurationException, XMLStreamException, TransformerException {
		InputStream lstream = getClass().getResourceAsStream("/helloworld/Input.bpmn2");
		InputStream rstream = getClass().getResourceAsStream("/helloworld/Input.bpmn2");
		XMLCompare.assertEquals(lstream, rstream);
	}
	
	@Test
	public void compareTest() throws SAXException, IOException, ParserConfigurationException, XMLStreamException, TransformerException {
		InputStream lstream = getClass().getResourceAsStream("/helloworld/Test.bpmn2");
		InputStream rstream = getClass().getResourceAsStream("/helloworld/Output.bpmn2");
		XMLCompare.assertEquals(lstream, rstream);
	}
}