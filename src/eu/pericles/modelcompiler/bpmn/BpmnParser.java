package eu.pericles.modelcompiler.bpmn;

import java.io.File;
import java.io.ObjectInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.DomReader;

public class BpmnParser {
	
	private BpmnProcess bpmnProcess;
	
	public BpmnParser() {
		bpmnProcess = new BpmnProcess();
	}

	/* TODO parse every kind of document location: URL, ... */
	public void parse(String nameFile) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new File(nameFile));	
			document.getDocumentElement().normalize();

			XStream xstream = new XStream(new DomDriver());
			xstream.processAnnotations(BpmnProcess.class);
			
			ObjectInputStream ois = xstream.createObjectInputStream(new DomReader(document.getDocumentElement()));
						
			setBpmnProcess((BpmnProcess) ois.readObject());
			
			ois.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		/** We cannot do that, because it would delete all the variables/lists that are not present in the file 
		this.bpmnProcess = bpmnProcess;
		*/
		getBpmnProcess().copyBpmnProcess(this.bpmnProcess, bpmnProcess);
	}
	
	
}