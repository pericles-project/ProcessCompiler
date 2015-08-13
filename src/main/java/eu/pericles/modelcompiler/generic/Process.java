package eu.pericles.modelcompiler.generic;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.modelcompiler.common.Element;


public class Process implements Element {

	private String uid;
	private String name;
	private String source;
	private List<ExternalItem> externalItems;
	private List<Variable> variables;
	private List<Activity> activities;
	private List<Event> events;
	private List<Gateway> gateways;
	private List<Process> subprocesses;
	private List<Flow> flows;
	
	public Process() {
		init();
	}
	
	private void init() {
		externalItems = new ArrayList<ExternalItem>();
		variables = new ArrayList<Variable>();
		activities = new ArrayList<Activity>();
		events = new ArrayList<Event>();
		gateways = new ArrayList<Gateway>();
		subprocesses = new ArrayList<Process>();
		flows = new ArrayList<Flow>();
	}
	
	
	//--- Getters, Setters and Add element to list methods --//
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	public void addVariable(Variable variable) {
		getVariables().add(variable);
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(Activity activity) {
		getActivities().add(activity);
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void addEvent (Event event) {
		getEvents().add(event);
	}

	public List<Gateway> getGateways() {
		return gateways;
	}

	public void setGateways(List<Gateway> gateways) {
		this.gateways = gateways;
	}
	
	public void addGateway (Gateway gateway) {
		getGateways().add(gateway);
	}

	public List<Flow> getFlows() {
		return flows;
	}

	public void setFlows(List<Flow> flows) {
		this.flows = flows;
	}
	
	public void addFlow(Flow flow) {
		getFlows().add(flow);
	}

	public List<Process> getSubprocesses() {
		return subprocesses;
	}

	public void setSubprocesses(List<Process> subprocesses) {
		this.subprocesses = subprocesses;
	}
	
	public void addSubprocess(Process subprocess) {
		getSubprocesses().add(subprocess);
	}

	public List<ExternalItem> getExternalItems() {
		return externalItems;
	}

	public void setExternalItems(List<ExternalItem> externalItems) {
		this.externalItems = externalItems;
	}
	
	public void addExternalItem(ExternalItem externalItem) {
		getExternalItems().add(externalItem);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
