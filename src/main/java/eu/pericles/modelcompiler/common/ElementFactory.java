package eu.pericles.modelcompiler.common;

import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Data;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.ExternalItem;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;
import eu.pericles.modelcompiler.generic.Timer;
import eu.pericles.modelcompiler.generic.Variable;
import eu.pericles.modelcompiler.jbpm.Diagram.ConnectionElement;
import eu.pericles.modelcompiler.jbpm.Diagram.Diagram;
import eu.pericles.modelcompiler.jbpm.Diagram.Edge;
import eu.pericles.modelcompiler.jbpm.Diagram.NodeElement;
import eu.pericles.modelcompiler.jbpm.Diagram.Plane;
import eu.pericles.modelcompiler.jbpm.Diagram.Shape;

public class ElementFactory {
	public enum Type {
		ACTIVITY, EVENT, GATEWAY, PROCESS, FLOW, DATA, EXTERNAL_ITEM, VARIABLE, TIMER, 
		DIAGRAM, PLANE, SHAPE, EDGE, NODE_ELEMENT, CONNECTION_ELEMENT
	}

	public static Element createElement(String uid, Type elementType) {

		switch (elementType) {
		case ACTIVITY:
			Activity activity = new Activity();
			activity.setUid(uid);
			return activity;
		case EVENT:
			Event event = new Event();
			event.setUid(uid);
			return event;
		case GATEWAY:
			Gateway gateway = new Gateway();
			gateway.setUid(uid);
			return gateway;
		case PROCESS:
			Process process = new Process();
			process.setUid(uid);
			return process;
		case FLOW:
			Flow flow = new Flow();
			flow.setUid(uid);
			return flow;
		case DATA:
			Data data = new Data();
			data.setUid(uid);
			return data;
		case EXTERNAL_ITEM:
			ExternalItem externalItem = new ExternalItem();
			externalItem.setUid(uid);
			return externalItem;
		case VARIABLE:
			Variable variable = new Variable();
			variable.setUid(uid);
			return variable;
		case TIMER:
			Timer timer = new Timer();
			timer.setUid(uid);
			return timer;
		case DIAGRAM:
			Diagram diagram = new Diagram();
			diagram.setUid(uid);
			return diagram;
		case PLANE:
			Plane plane = new Plane();
			plane.setUid(uid);
			return plane;
		case SHAPE:
			Shape shape = new Shape();
			shape.setUid(uid);
			return shape;
		case EDGE:
			Edge edge = new Edge();
			edge.setUid(uid);
			return edge;
		case NODE_ELEMENT:
			NodeElement nodeElement = new NodeElement();
			nodeElement.setUid(uid);
			return nodeElement;
		case CONNECTION_ELEMENT:
			ConnectionElement connectionElement = new ConnectionElement();
			connectionElement.setUid(uid);
			return connectionElement;
		default:
			// TODO throw an exception here
			return null;

		}
	}
}
