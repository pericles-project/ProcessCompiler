package eu.pericles.processcompiler.ng;

import java.util.HashMap;

public class Subprocess {
	
	private String processID;
	private HashMap<DataObject, DataObject> dataInputMap;
	private HashMap<DataObject, DataObject> dataOutputMap;
	
	public Subprocess(String processID, HashMap<DataObject, DataObject> dataInputMap, HashMap<DataObject, DataObject> dataOutputMap) {
		this.processID = processID;
		this.dataInputMap = dataInputMap;
		this.dataOutputMap = dataOutputMap;
	}

	public String getProcessID() {
		return processID;
	}

	public void setProcessID(String processID) {
		this.processID = processID;
	}

	public HashMap<DataObject, DataObject> getDataInputMap() {
		return dataInputMap;
	}

	public void setDataInputMap(HashMap<DataObject, DataObject> dataInputMap) {
		this.dataInputMap = dataInputMap;
	}

	public HashMap<DataObject, DataObject> getDataOutputMap() {
		return dataOutputMap;
	}

	public void setDataOutputMap(HashMap<DataObject, DataObject> dataOutputMap) {
		this.dataOutputMap = dataOutputMap;
	}

}
