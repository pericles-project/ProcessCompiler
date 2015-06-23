package eu.pericles.modelcompiler.jbpm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.DomReader;

public class JbpmFileParser {
	
	private JbpmFile jbpmFile;
	
	public void parse(String inputFile) {

		try {
			
			File processedFile = processInputFile(inputFile);
			
			try {
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(processedFile);	
			document.getDocumentElement().normalize();

			XStream xstream = new XStream(new DomDriver());
			xstream.processAnnotations(JbpmFile.class);
			
			ObjectInputStream ois = xstream.createObjectInputStream(new DomReader(document.getDocumentElement()));
			
			JbpmFile jbpmFile = (JbpmFile) ois.readObject();
			jbpmFile.organiseInfo();
			setJbpmFile(jbpmFile);
			
			ois.close();
			
			} catch (Exception exception) {
				exception.printStackTrace();
				
			} finally {
				Files.delete(processedFile.toPath());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/* Modify the input file in a way can be read by xstream as a single element. It is not a very elegant
	 * solution but it is ok for now, it works and keeps things simple.
	 */
	private File processInputFile(String nameFile) throws Exception {
		
		File processedFile = new File("temp.bpmn2");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nameFile))));
		BufferedWriter out = new BufferedWriter(new FileWriter(processedFile, true));
		
		String line = null;
		while ((line = in.readLine()) != null) {
			if (line.startsWith("<bpmn2:definitions")) {
				out.write(line);
				out.newLine();
				line = "<jbpm>";
			}
			if (line.startsWith("</bpmn2:definitions")){
					out.write("</jbpm>");
					out.newLine();
			}
			out.write(line);
			out.newLine();
		}
 
		in.close();
		out.close();
		
		return processedFile;
	}
	
	//---- Getters and setters ----// 

	public JbpmFile getJbpmFile() {
		return jbpmFile;
	}

	public void setJbpmFile(JbpmFile jbpmFile) {
		this.jbpmFile = jbpmFile;
	}

}
