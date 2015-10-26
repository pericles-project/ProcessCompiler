package eu.pericles.processcompiler.bpmn;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TRootElement;

public class BPMNParser {
	
	private BPMNProcess bpmnProcess;
	
	public void parse(String filepath) {
		
		bpmnProcess = new BPMNProcess();
		
		try {
			JAXBContext jc = JAXBContext
					.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di"
							+ ":org.omg.spec.dd._20100524.dc:org.omg.spec.dd._20100524.di");

			Unmarshaller unmarshaller = jc.createUnmarshaller();				
			JAXBElement<Definitions> feed = unmarshaller.unmarshal(
					new StreamSource(getClass().getResourceAsStream(
							"/helloworld/Input.bpmn2")), Definitions.class);
			Definitions definitions = feed.getValue();
			
			for(JAXBElement<? extends TRootElement> rootElement: definitions.getRootElements()) {
				if(rootElement.getDeclaredType().isAssignableFrom(TProcess.class)) {
					TProcess progress = (TProcess) rootElement.getValue();
					bpmnProcess.setProcess(progress);
				} else {
					throw new RuntimeException("Unsupported root element of type:" + rootElement.getName());
				}
			}
		} catch (JAXBException exception) {
			exception.printStackTrace();
		}
	}

	public BPMNProcess getBPMNProcess() {
		return bpmnProcess;
	}

	public void setBPMNProcess(BPMNProcess process) {
		this.bpmnProcess = process;
	}

}
