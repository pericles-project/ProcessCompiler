package eu.pericles.modelcompiler.common;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateCatchEvent;
import eu.pericles.modelcompiler.bpmn.Events.IntermediateThrowEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Gateways.ParallelGateway;
import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.Diagram.Bounds;
import eu.pericles.modelcompiler.jbpm.Diagram.ConnectionElement;
import eu.pericles.modelcompiler.jbpm.Diagram.Diagram;
import eu.pericles.modelcompiler.jbpm.Diagram.Edge;
import eu.pericles.modelcompiler.jbpm.Diagram.NodeElement;
import eu.pericles.modelcompiler.jbpm.Diagram.Plane;
import eu.pericles.modelcompiler.jbpm.Diagram.Shape;
import eu.pericles.modelcompiler.jbpm.Diagram.Waypoint;

public class BpmnJbpmConversor {

	private BpmnProcess bpmnProcess;
	private JbpmFile jbpmFile;
	private List<NodeElement> nodeElements;
	private List<ConnectionElement> connectionElements;

	public BpmnJbpmConversor() {
		setBpmnProcess(new BpmnProcess());
		setJbpmFile(new JbpmFile());
	}

	public void convertFromBpmnToJbpm(BpmnProcess bpmnProcess) {
		setBpmnProcess(bpmnProcess);
		setJbpmFile(new JbpmFile());

		fillDefinitionsDataByDefault();
		addExternalVariablesToJbpmFile();
		convertProcessFromBpmnToJbpm();
		createDiagram();
	}

	private void addExternalVariablesToJbpmFile() {
		if (getBpmnProcess().getItemDefinitions() != null)
			getJbpmFile().setItemDefinitions(getBpmnProcess().getItemDefinitions());
		if (getBpmnProcess().getMessages() != null)
			getJbpmFile().setMessages(getBpmnProcess().getMessages());		
	}

	private void fillDefinitionsDataByDefault() {
		getJbpmFile().setXmlns_xsi("http://www.w3.org/2001/XMLSchema-instance");
		getJbpmFile().setXmlns_bpmn2("http://www.omg.org/spec/BPMN/20100524/MODEL");
		getJbpmFile().setXmlns_bpmndi("http://www.omg.org/spec/BPMN/20100524/DI");
		getJbpmFile().setXmlns_dc("http://www.omg.org/spec/DD/20100524/DC");
		getJbpmFile().setXmlns_di("http://www.omg.org/spec/DD/20100524/DI");
		getJbpmFile().setXmlns_tns("http://www.jboss.org/drools");
		getJbpmFile().setXmlns("http://www.jboss.org/drools");
		getJbpmFile().setXsi_schemaLocation("http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd http://www.jboss.org/drools drools.xsd http://www.bpsim.org/schemas/1.0 bpsim.xsd");
		getJbpmFile().setHeaderId("Definition");
		getJbpmFile().setExpressionLanguage("http://www.mvel.org/2.0");
		getJbpmFile().setTargetNamespace("http://www.jboss.org/drools");
		getJbpmFile().setTypeLanguage("http://www.java.com/javaTypes");
	}

	private void convertProcessFromBpmnToJbpm() {
		getJbpmFile().setBpmnProcess(getBpmnProcess());
		deleteExternalVariablesFromProcess();
	}

	private void deleteExternalVariablesFromProcess() {
		getJbpmFile().getBpmnProcess().setItemDefinitions(null);
		getJbpmFile().getBpmnProcess().setMessages(null);
	}	

	private void createDiagram() {
		getJbpmFile().setDiagram(new Diagram());
		getJbpmFile().getDiagram().setPlane(new Plane());
		getJbpmFile().getDiagram().getPlane().setBpmnElement(getBpmnProcess().getId());
		addNodeElementsToDiagram();
		addConnectionElementsToDiagram();
	}

	private void addNodeElementsToDiagram() {
		createListOfNodeElements();
		addShapesToDiagram();
	}

	private void addConnectionElementsToDiagram() {
		createListOfConnectionElements();
		addEdgesToDiagram();
	}

	private void createListOfNodeElements() {
		setNodeElements(new ArrayList<NodeElement>());
		iterateOverNodeElementLists(getBpmnProcess());		
	}	

	private void createListOfConnectionElements() {
		setConnectionElements(new ArrayList<ConnectionElement>());
		iterateOverConnectionElementLists(getBpmnProcess());		
	}	

	private void addShapesToDiagram() {
		getJbpmFile().getDiagram().getPlane().setShapes(new ArrayList<Shape>());
		for (NodeElement node : getNodeElements()) {
			Shape shape = new Shape();
			shape.setBpmnElement(node.getBpmnElement());
			shape.setBounds(new Bounds());
			getJbpmFile().getDiagram().getPlane().getShapes().add(shape);
		}
	}
	
	private void addEdgesToDiagram() {
		getJbpmFile().getDiagram().getPlane().setEdges(new ArrayList<Edge>());
		for (ConnectionElement connection : getConnectionElements()) {
			Edge edge = new Edge();
			edge.setBpmnElement(connection.getBpmnElement());
			edge.setSourceElement(connection.getSourceElement());
			edge.setTargetElement(connection.getTargetElement());
			edge.setPoints(new ArrayList<Waypoint>());
			for (int i=0; i<4; i++) {
				edge.getPoints().add(new Waypoint());
			}
			getJbpmFile().getDiagram().getPlane().getEdges().add(edge);
		}
	}

	private void iterateOverNodeElementLists(BpmnProcess bpmnProcess) {
		for (StartEvent element : bpmnProcess.getStartEvents()) {
			addNodeElement(element.getId());
		}
		for (IntermediateCatchEvent element : bpmnProcess.getIntermediateCatchEvents()) {
			addNodeElement(element.getId());
		}
		for (IntermediateThrowEvent element : bpmnProcess.getIntermediateThrowEvents()) {
			addNodeElement(element.getId());
		}
		for (EndEvent element : bpmnProcess.getEndEvents()) {
			addNodeElement(element.getId());
		}
		for (ScriptTask element : bpmnProcess.getScriptTasks()) {
			addNodeElement(element.getId());
		}
		for (ParallelGateway element : bpmnProcess.getParallelGateways()) {
			addNodeElement(element.getId());
		}
		for (Subprocess element : bpmnProcess.getSubprocesses()) {
			addNodeElement(element.getId());
			iterateOverNodeElementLists(element);
		}
	}

	private void iterateOverConnectionElementLists(BpmnProcess bpmnProcess) {
		for (SequenceFlow element : bpmnProcess.getSequenceFlows()) {
			addConnectionElement(element.getId(), element.getSource(), element.getTarget());
		}

		for (Subprocess element : bpmnProcess.getSubprocesses()) {
			iterateOverConnectionElementLists(element);
		}
	}

	private void addNodeElement(String id) {
		NodeElement node = new NodeElement();
		node.setBpmnElement(id);
		getNodeElements().add(node);
	}

	private void addConnectionElement(String id, String source, String target) {
		ConnectionElement connection = new ConnectionElement();
		connection.setBpmnElement(id);
		connection.setSourceElement(source);
		connection.setTargetElement(target);
		getConnectionElements().add(connection);
	}

	//---- Getters and setters ----// 

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public JbpmFile getJbpmFile() {
		return jbpmFile;
	}

	public void setJbpmFile(JbpmFile jbpmFile) {
		this.jbpmFile = jbpmFile;
	}

	public List<NodeElement> getNodeElements() {
		return nodeElements;
	}

	public void setNodeElements(List<NodeElement> nodeElements) {
		this.nodeElements = nodeElements;
	}

	public List<ConnectionElement> getConnectionElements() {
		return connectionElements;
	}

	public void setConnectionElements(List<ConnectionElement> connectionElements) {
		this.connectionElements = connectionElements;
	}
}
