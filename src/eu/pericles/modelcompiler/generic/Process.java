package eu.pericles.modelcompiler.generic;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.modelcompiler.common.ProcessElement;


public class Process extends ProcessElement {

	private String name;
	private List<Activity> activities;
	private List<Event> events;
	private List<Gateway> gateways;
	private List<Flow> flows;
	private List<Process> subprocesses;
	private String source;
	
	public Process() {
		init();
	}
	
	private void init() {
		activities = new ArrayList<Activity>();
		events = new ArrayList<Event>();
		gateways = new ArrayList<Gateway>();
		flows = new ArrayList<Flow>();
		subprocesses = new ArrayList<Process>();
	}
	
	
	//--- Getters, Setters and Add element to list methods --//
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
