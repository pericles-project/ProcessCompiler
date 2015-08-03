package eu.pericles.modelcompiler.jbpm;

import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;


public class JbpmFileWriter {
	private String outputFile;	
	private JbpmFile jbmpFile;
	
	public JbpmFileWriter(String outputFile) {
		this.setOutputFile(outputFile);
	}
	
	public void write(JbpmFile jbpmFile) {
		this.setJbmpFile(jbpmFile);
		
		try{  
	        FileOutputStream fos = new FileOutputStream(getOutputFile());
			XStream xstream = new XStream();
	        String xml = xstream.toXML(getJbmpFile());
	        
	        fos.write(xml.getBytes());
	        
	        fos.close();

	    }catch (Exception e){
	        System.err.println("Error in XML Write: " + e.getMessage());
	    }
	}
	
	
	//---- Getters and setters ----// 

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public JbpmFile getJbmpFile() {
		return jbmpFile;
	}

	public void setJbmpFile(JbpmFile jbmpFile) {
		this.jbmpFile = jbmpFile;
	}

}
