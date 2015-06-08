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
			
			BpmnProcess bpmnProcess = (BpmnProcess) ois.readObject();
			
			/* When reading a process from a bpmn file with DOM, those lists of elements that are not in the file remain 
			 * pointing to NULL. With checkAndComplete(), those lists are created as empty lists.
			 */
			bpmnProcess.checkAndComplete();			
			setBpmnProcess(bpmnProcess);
			
			ois.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
	
	//---- Getters and setters ----// 
	
	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;		
	}
	
	
}