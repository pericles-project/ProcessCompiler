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

import eu.pericles.processcompiler.bpmnx.FancyDefinitions;
import eu.pericles.processcompiler.bpmnx.FancyObjectFactory;

public class BPMNParser {
	
	private BPMNProcess bpmnProcess;
	private String file;
	private FancyDefinitions definitions;
	
	public void parse(String file) {		
		setBPMNProcess(new BPMNProcess());
		setFile(file);
		
		try {
			unmarshal();
			
			parseAttributes();
			parseRootElements();
			parseDiagram();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void unmarshal() throws JAXBException, FileNotFoundException {
		JAXBElement<FancyDefinitions> feed = createUnmarshaller().unmarshal(
				new StreamSource(new InputStreamReader(new FileInputStream(new File(getFile())))), FancyDefinitions.class);
		
		setDefinitions(feed.getValue());
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);		
		return jc.createUnmarshaller();
	}

	private void parseAttributes() {
		getBPMNProcess().setId(getDefinitions().getId());
		getBPMNProcess().setTargetNamespace(getDefinitions().getTargetNamespace());
		getBPMNProcess().setExpressionLanguage(getDefinitions().getExpressionLanguage());
		getBPMNProcess().setTypeLanguage(getDefinitions().getTypeLanguage());		
	}

	private void parseRootElements() {
		getBPMNProcess().setProcess(getDefinitions().getProcess());
		getBPMNProcess().setItemDefinitions(getDefinitions().getItemDefinitions());
		getBPMNProcess().setErrors(getDefinitions().getErrors());
		getBPMNProcess().setEscalations(getDefinitions().getEscalations());
		getBPMNProcess().setMessages(getDefinitions().getMessages());
		getBPMNProcess().setSignals(getDefinitions().getSignals());
		getBPMNProcess().setInterfaces(getDefinitions().getInterfaces());
		getBPMNProcess().setDataStores(getDefinitions().getDataStores());
	}

	private void parseDiagram() {
		getBPMNProcess().setDiagram(getDefinitions().getDiagram());
	}

	//------------------- GETTERS AND SETTERS ----------------------//
	
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

	public FancyDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(FancyDefinitions definitions) {
		this.definitions = definitions;
	}

}
