package eu.pericles.processcompiler.testutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import eu.pericles.processcompiler.bpmnx.FancyDefinitions;
import eu.pericles.processcompiler.bpmnx.FancyObjectFactory;

public class XLSTest {

	@Test
	public void fooTest() throws JAXBException, IOException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);

		// Example to read an XML file:
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		JAXBElement<FancyDefinitions> feed = unmarshaller.unmarshal(
				new StreamSource(getClass().getResourceAsStream(
						"/helloworld/Input.bpmn2")), FancyDefinitions.class);
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
		final Map<String, String> prefixes = new HashMap<>();
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/MODEL","bpnm");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DI","dddi");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DC","dddc");
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/DI","di");

    	marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
			@Override
			public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {
				return prefixes.get(arg0);
			}
		});
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(feed, System.out);
	}

}
