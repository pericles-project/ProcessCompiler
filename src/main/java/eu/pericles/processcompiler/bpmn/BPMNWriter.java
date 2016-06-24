package eu.pericles.processcompiler.bpmn;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import eu.pericles.processcompiler.bpmnx.FancyDefinitions;
import eu.pericles.processcompiler.bpmnx.FancyObjectFactory;
import eu.pericles.processcompiler.exceptions.BPMNWriteException;

public class BPMNWriter {

	private BPMNProcess bpmnProcess;
	private FancyDefinitions definitions;

	public void write(BPMNProcess process, String file) throws BPMNWriteException {
		setBpmnProcess(process);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			marshal(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			throw new BPMNWriteException("Error when writing a BPMN process - File Not Found: " + file, e);
		} catch (JAXBException e) {
			throw new BPMNWriteException("Error when writing a BPMN process - JAXB error (marshal) with file: " + file, e);
		} catch (IOException e) {
			throw new BPMNWriteException("Error when writing a BPMN process - IO problem with file: " + file, e);
		}
	}

	public void write(BPMNProcess process, OutputStream target) throws BPMNWriteException {
		setBpmnProcess(process);
		try {
			marshal(target);
		} catch (JAXBException e) {
			throw new BPMNWriteException("Error when writing a BPMN process - JAXB error (marshal)", e);
		}
	}

	private void marshal(OutputStream fos) throws JAXBException {
		setDefinitions((new FancyObjectFactory()).createDefinitions());

		getDefinitions().copyFromBpmnProcess(getBpmnProcess());

		createMarshaller().marshal(getDefinitions(), fos);
	}

	private Marshaller createMarshaller() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class, org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);

		Marshaller marshaller = jc.createMarshaller();

		final Map<String, String> prefixes = new HashMap<>();
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/MODEL", "bpmn2");
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/DI", "bpmndi");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DI", "di");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DC", "dc");
		prefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		prefixes.put("http://www.jboss.org/drools", "tns");
		// prefixes.put(getDefinitions().getTargetNamespace(), "tns");

		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
			@Override
			public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {
				return prefixes.get(arg0);
			}
		});
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
				"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd http://www.jboss.org/drools "
						+ "drools.xsd http://www.bpsim.org/schemas/1.0 bpsim.xsd");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return marshaller;
	}

	// ------------------- GETTERS AND SETTERS ----------------------//

	public BPMNProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public FancyDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(FancyDefinitions definitions) {
		this.definitions = definitions;
	}
}
