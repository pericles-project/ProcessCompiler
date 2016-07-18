package eu.pericles.processcompiler.ng;

import java.util.HashMap;

public class PCSubprocess {
	
	private String id;
	private HashMap<PCDataObject, PCDataObject> dataInputMap;
	private HashMap<PCDataObject, PCDataObject> dataOutputMap;
	
	public PCSubprocess(String id, HashMap<PCDataObject, PCDataObject> dataInputMap, HashMap<PCDataObject, PCDataObject> dataOutputMap) {
		this.id = id;
		this.dataInputMap = dataInputMap;
		this.dataOutputMap = dataOutputMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String processID) {
		this.id = processID;
	}

	public HashMap<PCDataObject, PCDataObject> getDataInputMap() {
		return dataInputMap;
	}

	public void setDataInputMap(HashMap<PCDataObject, PCDataObject> dataInputMap) {
		this.dataInputMap = dataInputMap;
	}

	public HashMap<PCDataObject, PCDataObject> getDataOutputMap() {
		return dataOutputMap;
	}

	public void setDataOutputMap(HashMap<PCDataObject, PCDataObject> dataOutputMap) {
		this.dataOutputMap = dataOutputMap;
	}

}
