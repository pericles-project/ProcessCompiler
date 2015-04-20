package eu.pericles.modelcompiler.generic;

import java.util.List;

import eu.pericles.modelcompiler.common.ProcessElement;


public class Process extends ProcessElement {

	private String name;
	private List<Activity> activities;
	private List<Event> events;
	private List<Gateway> gateways;
	private List<Flow> flows;
	private String source;
	
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

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<Gateway> getGateways() {
		return gateways;
	}

	public void setGateways(List<Gateway> gateways) {
		this.gateways = gateways;
	}

	public List<Flow> getFlows() {
		return flows;
	}

	public void setFlows(List<Flow> flows) {
		this.flows = flows;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
