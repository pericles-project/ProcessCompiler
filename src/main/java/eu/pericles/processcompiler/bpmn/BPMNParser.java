package eu.pericles.processcompiler.bpmn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TRootElement;

import eu.pericles.processcompiler.bpnmx.FancyObjectFactory;

public class BPMNParser {
	
	private BPMNProcess bpmnProcess;
	private String file;
	private Definitions definitions;
	
	public void parse(String file) {		
		setBPMNProcess(new BPMNProcess());
		setFile(file);
		
		try {
			unmarshal();
			
			getBPMNProcess().setTargetNamespace(getDefinitions().getTargetNamespace());
			
			parseRootElements();
			parseDiagram();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void unmarshal() throws JAXBException, FileNotFoundException {
		JAXBElement<Definitions> feed = createUnmarshaller().unmarshal(
				new StreamSource(new InputStreamReader(new FileInputStream(new File(getFile())))), Definitions.class);
		
		setDefinitions(feed.getValue());
	}

	private void parseRootElements() {
		for(JAXBElement<? extends TRootElement> rootElement: getDefinitions().getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TProcess.class)) {
				TProcess progress = (TProcess) rootElement.getValue();
				getBPMNProcess().setProcess(progress);
			} else {
				throw new RuntimeException("Unsupported root element of type:" + rootElement.getName());
			}
		}
	}

	private void parseDiagram() {
		if (!(getDefinitions().getBPMNDiagrams().isEmpty())) {
			getBPMNProcess().setDiagram(getDefinitions().getBPMNDiagrams().get(0));
		}		
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);
		
		return jc.createUnmarshaller();
	}

	public BPMNProcess getBPMNProcess() {
		return bpmnProcess;
	}

	public void setBPMNProcess(BPMNProcess process) {
		this.bpmnProcess = process;
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
