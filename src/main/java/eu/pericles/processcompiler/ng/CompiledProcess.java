package eu.pericles.processcompiler.ng;

import java.util.ArrayList;
import java.util.List;

public class CompiledProcess {
	private String id;
	private String name;
	private String type;
	private List<PCDataObject> dataObjects = new ArrayList<>();;
	private List<PCSubprocess> subprocesses = new ArrayList<>();;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PCDataObject> getDataObjects() {
		return dataObjects;
	}

	public void setDataObjects(List<PCDataObject> dataObjects) {
		this.dataObjects = dataObjects;
	}

	public List<PCSubprocess> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<PCSubprocess> subprocesses) {
		this.subprocesses = subprocesses;
	}

}
