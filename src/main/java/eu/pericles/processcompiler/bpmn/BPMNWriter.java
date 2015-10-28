package eu.pericles.processcompiler.bpmn;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.omg.spec.bpmn._20100524.model.Definitions;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import eu.pericles.processcompiler.bpnmx.FancyObjectFactory;

public class BPMNWriter {
	
	private BPMNProcess bpmnProcess;
	private String file;
	private Definitions definitions;
	
	public void write(BPMNProcess process, String file) {
		setBpmnProcess(process);
		setFile(file);
		
		try {
			FileOutputStream fos = new FileOutputStream(getFile());
			marshal(fos);
			fos.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private void marshal(FileOutputStream fos) throws JAXBException {
		FancyObjectFactory object = new FancyObjectFactory();
		setDefinitions(new Definitions());
		
		getDefinitions().setTargetNamespace(getBpmnProcess().getTargetNamespace());
		getDefinitions().getRootElements().add(object.createProcess(getBpmnProcess().getProcess()));
		getDefinitions().getBPMNDiagrams().add(getBpmnProcess().getDiagram());
		
		createMarshaller().marshal(getDefinitions(), fos);
	}
	
	private Marshaller createMarshaller() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);
		
		Marshaller marshaller = jc.createMarshaller();
		
		final Map<String, String> prefixes = new HashMap<>();
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/MODEL","bpnm2");
		prefixes.put("http://www.omg.org/spec/BPMN/20100524/DI","bpmndi");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DI","di");
		prefixes.put("http://www.omg.org/spec/DD/20100524/DC","dc");
		prefixes.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
		prefixes.put("http://www.jboss.org/drools","tns");

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

	public BPMNProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Definitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	
	

}
