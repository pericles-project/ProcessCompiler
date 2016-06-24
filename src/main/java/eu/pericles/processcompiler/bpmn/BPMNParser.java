package eu.pericles.processcompiler.bpmn;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import eu.pericles.processcompiler.bpmnx.FancyDefinitions;
import eu.pericles.processcompiler.bpmnx.FancyObjectFactory;
import eu.pericles.processcompiler.exceptions.BPMNParseException;

public class BPMNParser {

	private BPMNProcess bpmnProcess;
	private FancyDefinitions definitions;

	public BPMNProcess parse(String file) throws BPMNParseException {
		try {
			return parse(new FileInputStream(new File(file)));
		} catch (Exception e) {
			throw new BPMNParseException("Error when parsing the BPMN file: " + file, e);
		}
	}

	public BPMNProcess parse(InputStream inputStream) throws BPMNParseException {
		try {
			setBPMNProcess(new BPMNProcess());
			unmarshal(inputStream);

			parseAttributes();
			parseImports();
			parseRootElements();
			parseDiagram();
			return getBPMNProcess();

		} catch (Exception e) {
			throw new BPMNParseException("Error when parsing the BPMN inputstream", e);
		}
	}

	private void unmarshal(InputStream inputStream) throws JAXBException {
		JAXBElement<FancyDefinitions> feed = createUnmarshaller().unmarshal(new StreamSource(new InputStreamReader(inputStream)),
				FancyDefinitions.class);

		setDefinitions(feed.getValue());
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(
				FancyObjectFactory.class, // org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
				org.omg.spec.bpmn._20100524.di.ObjectFactory.class, org.omg.spec.dd._20100524.di.ObjectFactory.class,
				org.omg.spec.dd._20100524.dc.ObjectFactory.class);
		return jc.createUnmarshaller();
	}

	private void parseAttributes() {
		getBPMNProcess().setId(getDefinitions().getId());
		getBPMNProcess().setTargetNamespace(getDefinitions().getTargetNamespace());
		getBPMNProcess().setExpressionLanguage(getDefinitions().getExpressionLanguage());
		getBPMNProcess().setTypeLanguage(getDefinitions().getTypeLanguage());
	}

	private void parseImports() {
		getBPMNProcess().setImports(getDefinitions().getImports());
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

	// ------------------- GETTERS AND SETTERS ----------------------//

	public BPMNProcess getBPMNProcess() {
		return bpmnProcess;
	}

	public void setBPMNProcess(BPMNProcess process) {
		this.bpmnProcess = process;
	}

	public FancyDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(FancyDefinitions definitions) {
		this.definitions = definitions;
	}

}
