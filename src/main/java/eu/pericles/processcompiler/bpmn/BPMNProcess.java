package eu.pericles.processcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.DataInput;
import org.omg.spec.bpmn._20100524.model.DataInputAssociation;
import org.omg.spec.bpmn._20100524.model.DataOutput;
import org.omg.spec.bpmn._20100524.model.DataOutputAssociation;
import org.omg.spec.bpmn._20100524.model.Import;
import org.omg.spec.bpmn._20100524.model.TActivity;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TDataStore;
import org.omg.spec.bpmn._20100524.model.TError;
import org.omg.spec.bpmn._20100524.model.TEscalation;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TInterface;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.omg.spec.bpmn._20100524.model.TMessage;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TScriptTask;
import org.omg.spec.bpmn._20100524.model.TSignal;
import org.omg.spec.dd._20100524.di.DiagramElement;

public class BPMNProcess {

	private String id;
	private String targetNamespace;
	private String typeLanguage;
	private String expressionLanguage;
	private List<Import> imports = new ArrayList<>();
	private List<TItemDefinition> itemDefinitions = new ArrayList<>();
	private List<TError> errors = new ArrayList<>();
	private List<TEscalation> escalations = new ArrayList<>();
	private List<TMessage> messages = new ArrayList<>();
	private List<TSignal> signals = new ArrayList<>();
	private List<TInterface> interfaces = new ArrayList<>();
	private List<TDataStore> dataStores = new ArrayList<>();
	private TProcess process;
	private BPMNDiagram diagram;

	public void addBPMNProcess(BPMNProcess bpmnProcess) {
		this.addItemDefinitions(bpmnProcess.getItemDefinitions());
		this.addFlowElements(bpmnProcess.getFlowElements());
		if (bpmnProcess.hasDiagram())
			this.addDiagramElements(bpmnProcess.getDiagramElements());
	}

	public boolean hasDiagram() {
		return (this.getDiagram() != null);
	}

	// -------------- ADD FUNCTIONS -------------//

	public void addFlowElements(List<JAXBElement<? extends TFlowElement>> flowElements) {
		for (JAXBElement<? extends TFlowElement> flowElement : flowElements) {
			getProcess().getFlowElements().add(flowElement);
		}
	}

	public void addDiagramElements(List<JAXBElement<? extends DiagramElement>> diagramElements) {
		for (JAXBElement<? extends DiagramElement> diagramElement : diagramElements) {
			getDiagram().getBPMNPlane().getDiagramElements().add(diagramElement);
		}
	}

	public void addItemDefinitions(List<TItemDefinition> itemDefinitions) {
		for (TItemDefinition itemDefinition : itemDefinitions) {
			getItemDefinitions().add(itemDefinition);
		}
	}

	// -------------- FIND FUNCTIONS -------------//

	public JAXBElement<? extends TFlowElement> findFlowElement(Object element) throws Exception {
		for (JAXBElement<? extends TFlowElement> flowElement : getFlowElements()) {
			if (flowElement.getValue().equals(element))
				return flowElement;
		}
		throw new Exception("There is not a flow element corresponding to the element: " + element.toString());
	}

	public List<JAXBElement<? extends TFlowElement>> findFlowElementsByClass(String className) throws Exception {
		List<JAXBElement<? extends TFlowElement>> elements = new ArrayList<JAXBElement<? extends TFlowElement>>();
		for (JAXBElement<? extends TFlowElement> element : getFlowElements()) {
			if (element.getDeclaredType().getSimpleName().equals(className))
				elements.add(element);
		}
		if (elements.isEmpty())
			throw new Exception("There is not flow elements of class: " + className);
		return elements;
	}

	public JAXBElement<? extends DiagramElement> findDiagramElementByFlowElement(Object flowElement) throws Exception {
		JAXBElement<? extends TFlowElement> element = findFlowElement(flowElement);
		return findDiagramElementByFlowElement(element);
	}

	public JAXBElement<? extends DiagramElement> findDiagramElementByFlowElement(JAXBElement<? extends TFlowElement> bpmnElement)
			throws Exception {
		for (JAXBElement<? extends DiagramElement> element : getDiagramElements()) {
			// The function getBpmnElement() returns the QName (prefix + ID).
			// Use getLocalPart() to get the same ID as the BPMNElement
			if (element.getDeclaredType().isAssignableFrom(BPMNShape.class))
				if (((BPMNShape) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()))
					return element;
			if (element.getDeclaredType().isAssignableFrom(BPMNEdge.class))
				if (((BPMNEdge) element.getValue()).getBpmnElement().getLocalPart().equals(bpmnElement.getValue().getId()))
					return element;
		}
		throw new Exception("There is not a diagram element corresponding to the element " + bpmnElement.getValue().getId());
	}

	// -------------- UPDATE FUNCTIONS -------------//

	public void updateSourceOfDataInputAssociations(Object oldResource, Object newResource) {
		for (DataInputAssociation inputAssociation : getDataInputAssociations())
			if (inputAssociation.getSourceReves().get(0).getValue().equals(oldResource))
				inputAssociation.getSourceReves().get(0).setValue(newResource);
	}

	public void updateNameOfDataInput(String oldName, String newName) {
		for (DataInput dataInput : getDataInputs()) {
			if (dataInput.getName().equals(oldName))
				dataInput.setName(newName);
		}
	}

	public void updateNameOfDataOutput(String oldName, String newName) {
		for (DataOutput dataOutput : getDataOutputs()) {
			if (dataOutput.getName().equals(oldName))
				dataOutput.setName(newName);
		}
	}

	public void updateScriptTasksWithNewResources(TDataObject oldResource, TDataObject newResource) {
		for (TScriptTask scriptTask : getScriptTasks()) {
			String script = scriptTask.getScript().getContent().get(0).toString();
			String newScript = script.replaceAll(oldResource.getName(), newResource.getName());
			scriptTask.getScript().getContent().set(0, newScript);
		}
	}

	// -------------- DELETE FUNCTIONS -------------//

	public void deleteProcessElement(Object object) throws Exception {
		JAXBElement<? extends TFlowElement> element = findFlowElement(object);
		deleteProcessElement(element);
	}

	/**
	 * Deletes an element of the BPMNProcess, that means, the specified flow
	 * element and its corresponded diagram element
	 * 
	 * @param element
	 * @throws Exception
	 */
	public void deleteProcessElement(JAXBElement<? extends TFlowElement> element) throws Exception {
		if (this.hasDiagram()) {
			JAXBElement<? extends DiagramElement> diagramEndEvent = findDiagramElementByFlowElement(element);
			getDiagramElements().remove(diagramEndEvent);
		}
		getFlowElements().remove(element);
	}

	// -------------- GET FUNCTIONS -------------//

	public List<JAXBElement<? extends TFlowElement>> getFlowElements() {
		return getProcess().getFlowElements();
	}

	public List<JAXBElement<? extends DiagramElement>> getDiagramElements() {
		return getDiagram().getBPMNPlane().getDiagramElements();
	}

	public List<TActivity> getActivities() {
		List<TActivity> activities = new ArrayList<TActivity>();
		for (JAXBElement<? extends TFlowElement> element : getFlowElements())
			if (TActivity.class.isAssignableFrom(element.getValue().getClass()))
				activities.add((TActivity) element.getValue());
		return activities;
	}

	public List<TScriptTask> getScriptTasks() {
		List<TScriptTask> activities = new ArrayList<TScriptTask>();
		for (JAXBElement<? extends TFlowElement> element : getFlowElements())
			if (TScriptTask.class.isAssignableFrom(element.getValue().getClass()))
				activities.add((TScriptTask) element.getValue());
		return activities;
	}

	public List<DataInput> getDataInputs() {
		List<DataInput> dataInputs = new ArrayList<DataInput>();
		for (TActivity activity : getActivities())
			for (DataInput dataInput : activity.getIoSpecification().getDataInputs())
				dataInputs.add(dataInput);
		return dataInputs;
	}

	public List<DataOutput> getDataOutputs() {
		List<DataOutput> dataOutputs = new ArrayList<DataOutput>();
		for (TActivity activity : getActivities())
			for (DataOutput dataOutput : activity.getIoSpecification().getDataOutputs())
				dataOutputs.add(dataOutput);
		return dataOutputs;
	}

	public List<DataInputAssociation> getDataInputAssociations() {
		List<DataInputAssociation> dataInputAssociations = new ArrayList<DataInputAssociation>();
		for (TActivity activity : getActivities())
			for (DataInputAssociation dataInputAssociation : activity.getDataInputAssociations())
				dataInputAssociations.add(dataInputAssociation);
		return dataInputAssociations;
	}

	public List<DataOutputAssociation> getDataOutputAssociations() {
		List<DataOutputAssociation> dataOutputAssociations = new ArrayList<DataOutputAssociation>();
		for (TActivity activity : getActivities())
			for (DataOutputAssociation dataOutputAssociation : activity.getDataOutputAssociations())
				dataOutputAssociations.add(dataOutputAssociation);
		return dataOutputAssociations;
	}

	// ------------------- GETTERS AND SETTERS ----------------------//

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public String getTypeLanguage() {
		return typeLanguage;
	}

	public void setTypeLanguage(String typeLanguage) {
		this.typeLanguage = typeLanguage;
	}

	public String getExpressionLanguage() {
		return expressionLanguage;
	}

	public void setExpressionLanguage(String expressionLanguage) {
		this.expressionLanguage = expressionLanguage;
	}

	public List<Import> getImports() {
		return imports;
	}

	public void setImports(List<Import> imports) {
		this.imports = imports;
	}

	public List<TItemDefinition> getItemDefinitions() {
		return itemDefinitions;
	}

	public void setItemDefinitions(List<TItemDefinition> itemDefinitions) {
		this.itemDefinitions = itemDefinitions;
	}

	public List<TError> getErrors() {
		return errors;
	}

	public void setErrors(List<TError> errors) {
		this.errors = errors;
	}

	public List<TEscalation> getEscalations() {
		return escalations;
	}

	public void setEscalations(List<TEscalation> escalations) {
		this.escalations = escalations;
	}

	public List<TMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<TMessage> messages) {
		this.messages = messages;
	}

	public List<TSignal> getSignals() {
		return signals;
	}

	public void setSignals(List<TSignal> signals) {
		this.signals = signals;
	}

	public List<TInterface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<TInterface> interfaces) {
		this.interfaces = interfaces;
	}

	public List<TDataStore> getDataStores() {
		return dataStores;
	}

	public void setDataStores(List<TDataStore> dataStores) {
		this.dataStores = dataStores;
	}

	public TProcess getProcess() {
		return process;
	}

	public void setProcess(TProcess process) {
		this.process = process;
	}

	public BPMNDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(BPMNDiagram diagram) {
		this.diagram = diagram;
	}

}
