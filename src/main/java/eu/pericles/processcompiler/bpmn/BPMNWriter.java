package eu.pericles.processcompiler.bpmn;

import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.ObjectFactory;

public class BPMNWriter {
	
	private BPMNProcess bpmnProcess;
	
	public void write(BPMNProcess process, String filename) {
		setBpmnProcess(process);
		
		try {
			JAXBContext jc = JAXBContext
					.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di"
							+ ":org.omg.spec.dd._20100524.dc:org.omg.spec.dd._20100524.di");
			
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			ObjectFactory object = new ObjectFactory();
			Definitions definitions = new Definitions();
			definitions.getRootElements().add(object.createProcess(getBpmnProcess().getProcess()));
			
			FileOutputStream fos = new FileOutputStream(filename);
			marshaller.marshal(definitions, fos);
			fos.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public BPMNProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	
	

}
