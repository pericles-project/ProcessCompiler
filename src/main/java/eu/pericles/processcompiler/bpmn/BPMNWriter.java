package eu.pericles.processcompiler.bpmn;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import eu.pericles.processcompiler.bpmnx.FancyDefinitions;
import eu.pericles.processcompiler.bpmnx.FancyObjectFactory;

public class BPMNWriter {

	private BPMNProcess bpmnProcess;
	private FancyDefinitions definitions;

	public void write(BPMNProcess process, String file) throws Exception {
		setBpmnProcess(process);

		FileOutputStream fos = new FileOutputStream(file);
		marshal(fos);
		fos.close();
	}

	private void marshal(FileOutputStream fos) throws JAXBException {
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
		//if (getDefinitions().getTargetNamespace() != null)
			prefixes.put(getDefinitions().getTargetNamespace(), "tns");
		//else
			//prefixes.put("http://www.jboss.org/drools", "tns");

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
