package eu.pericles.modelcompiler.jbpm;

import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;


public class JbpmFileWriter {	
	
	public void write(JbpmFile jbpmFile, String outputFile) {
		
		try{  
	        FileOutputStream fos = new FileOutputStream(outputFile);
	        
			XStream xstream = new XStream();
			xstream.processAnnotations(JbpmFile.class);
	        String xml = xstream.toXML(jbpmFile);
	        
	        fos.write(xml.getBytes());	        
	        fos.close();

	    }catch (Exception e){
	        System.err.println("Error in XML Write: " + e.getMessage());
	    }
	}

}
