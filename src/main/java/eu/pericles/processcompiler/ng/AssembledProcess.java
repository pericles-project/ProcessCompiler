package eu.pericles.processcompiler.ng;

import java.util.List;

public class AssembledProcess {
	private String id;
	private String name;
	private String type;
	private List<DataObject> dataObjects;
	private List<Subprocess> subprocesses;

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

	public List<DataObject> getDataObjects() {
		return dataObjects;
	}

	public void setDataObjects(List<DataObject> dataObjects) {
		this.dataObjects = dataObjects;
	}

	public List<Subprocess> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<Subprocess> subprocesses) {
		this.subprocesses = subprocesses;
	}

}
