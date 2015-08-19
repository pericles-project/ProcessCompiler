package eu.pericles.modelcompiler.testutils;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TRootElement;

public class XLSTest {

	@Test
	public void fooTest() throws JAXBException, IOException {
		JAXBContext jc = JAXBContext
				.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di"
						+ ":org.omg.spec.dd._20100524.dc:org.omg.spec.dd._20100524.di");

		// Example to read an XML file:
		Unmarshaller unmarshaller = jc.createUnmarshaller();				
		JAXBElement<Definitions> feed = unmarshaller.unmarshal(
				new StreamSource(getClass().getResourceAsStream(
						"/helloworld/Input.bpmn2")), Definitions.class);
		Definitions definitions = feed.getValue();
		for(JAXBElement<? extends TRootElement> rootElement: definitions.getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TProcess.class)) {
				TProcess progress = (TProcess) rootElement.getValue();
				progress.getName(); //...
			} else {
				throw new RuntimeException("Unsupported root element of type:" + rootElement.getName());
			}
		}
		
		// Write the XML file to system out.
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(feed, System.out);
	}

}
