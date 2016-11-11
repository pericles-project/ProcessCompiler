package eu.pericles.processcompiler.web.providers;
import java.util.HashMap;
import java.util.Map;

public class Template {
	
	Map<String, Object> namespace = new HashMap<>();
	String template;
	
	public Template(String name) {
		template = name;
	}

	public String getTemplate() {
		return template;
	}

	public Map<String, Object> getNamespace() {
		return namespace;
	}

	public Template put(String key, Object value) {
		namespace.put(key, value);
		return this;
	}

}