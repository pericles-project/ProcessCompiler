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
import eu.pericles.modelcompiler.common.ElementFactory.Type;
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
	private static UidGeneration uidGenerator;
	
	public BpmnJbpmConversor(UidGeneration uidGenerator) {
		setUidGenerator(uidGenerator);
	}

	public void convert(BpmnProcess bpmnProcess) {
		setBpmnProcess(bpmnProcess);
		setJbpmFile(new JbpmFile());

		setDefinitions();
		setProcess();
		setAndOrganiseExternalVariables();
		setDiagram();
	}

	private void setDefinitions() {
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

	private void setProcess() {
		getJbpmFile().setBpmnProcess(getBpmnProcess());
	}

	private void setAndOrganiseExternalVariables() {
		/**
		 * The jBPM file has the external variables defined outside the process.
		 * To avoid duplication, delete the external variables from the process
		 * in the JbpmFile object. deleteExternalVariablesFromProcess();
		 */
		copyExternalVariablesToJbpmFile();
		deleteExternalVariablesFromProcess();
	}

	private void copyExternalVariablesToJbpmFile() {
		if (getBpmnProcess().getItemDefinitions() != null)
			getJbpmFile().setItemDefinitions(getBpmnProcess().getItemDefinitions());
		if (getBpmnProcess().getMessages() != null)
			getJbpmFile().setMessages(getBpmnProcess().getMessages());
	}

	private void deleteExternalVariablesFromProcess() {
		getJbpmFile().getBpmnProcess().setItemDefinitions(null);
		getJbpmFile().getBpmnProcess().setMessages(null);
	}

	private void setDiagram() {
		getJbpmFile().setDiagram((Diagram) ElementFactory.createElement(uidGenerator.requestUUID(), Type.DIAGRAM));
		getJbpmFile().getDiagram().setPlane((Plane) ElementFactory.createElement(uidGenerator.requestUUID(), Type.PLANE));
		getJbpmFile().getDiagram().getPlane().setBpmnElement(getBpmnProcess().getId());
		setShapesToDiagram();
		setEdgesToDiagram();
	}

	private void setShapesToDiagram() {
		createNodeElementList();
		addShapesToDiagram();
	}

	private void createNodeElementList() {
		setNodeElements(new ArrayList<NodeElement>());
		getNodeElementsFromProcess(getBpmnProcess());
	}

	private void getNodeElementsFromProcess(BpmnProcess bpmnProcess) {
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
			getNodeElementsFromProcess(element);
		}
	}

	private void addNodeElement(String id) {
		NodeElement node = (NodeElement) ElementFactory.createElement(uidGenerator.requestUUID(), Type.NODE_ELEMENT);
		node.setBpmnElement(id);
		getNodeElements().add(node);
	}

	private void addShapesToDiagram() {
		getJbpmFile().getDiagram().getPlane().setShapes(new ArrayList<Shape>());
		for (NodeElement node : getNodeElements()) {
			addShape(node, getJbpmFile().getDiagram().getPlane().getShapes());
		}
	}

	private void addShape(NodeElement node, List<Shape> shapes) {
		Shape shape = (Shape) ElementFactory.createElement(uidGenerator.requestUUID(), Type.SHAPE);
		shape.setBpmnElement(node.getBpmnElement());
		shape.setBounds(new Bounds());
		shapes.add(shape);
	}

	private void createConnectionElementList() {
		setConnectionElements(new ArrayList<ConnectionElement>());
		getConnectionElementsFromProcess(getBpmnProcess());
	}

	private void getConnectionElementsFromProcess(BpmnProcess bpmnProcess) {
		for (SequenceFlow element : bpmnProcess.getSequenceFlows()) {
			addConnectionElement(element.getId(), element.getSource(), element.getTarget());
		}

		for (Subprocess element : bpmnProcess.getSubprocesses()) {
			getConnectionElementsFromProcess(element);
		}
	}

	private void addConnectionElement(String id, String source, String target) {
		ConnectionElement connection = (ConnectionElement) ElementFactory.createElement(uidGenerator.requestUUID(), Type.CONNECTION_ELEMENT);
		connection.setBpmnElement(id);
		connection.setSourceElement(source);
		connection.setTargetElement(target);
		getConnectionElements().add(connection);
	}

	private void setEdgesToDiagram() {
		createConnectionElementList();
		addEdgesToDiagram();
	}

	private void addEdgesToDiagram() {
		getJbpmFile().getDiagram().getPlane().setEdges(new ArrayList<Edge>());
		for (ConnectionElement connection : getConnectionElements()) {
			addEdge(connection, getJbpmFile().getDiagram().getPlane().getEdges());
		}
	}

	private void addEdge(ConnectionElement connection, List<Edge> edges) {
		Edge edge = (Edge) ElementFactory.createElement(uidGenerator.requestUUID(), Type.EDGE);
		edge.setBpmnElement(connection.getBpmnElement());
		edge.setSourceElement(connection.getSourceElement());
		edge.setTargetElement(connection.getTargetElement());
		edge.setPoints(new ArrayList<Waypoint>(4));
		for (int i = 0; i < 4; i++) {
			edge.getPoints().add(new Waypoint());
		}
		edges.add(edge);
	}

	// ---- Getters and setters ----//

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

	public static UidGeneration getUidGenerator() {
		return uidGenerator;
	}

	public static void setUidGenerator(UidGeneration uidGenerator) {
		BpmnJbpmConversor.uidGenerator = uidGenerator;
	}
}
