package eu.pericles.processcompiler.ecosystem;

public class Implementation {

	private String id;
	private String version;
	private String type;
	private String location;
	private Fixity fixity;
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Fixity getFixity() {
		return fixity;
	}
	public void setFixity(Fixity fixity) {
		this.fixity = fixity;
	}
}
