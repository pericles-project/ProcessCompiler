package eu.pericles.processcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.model.Import;
import org.omg.spec.bpmn._20100524.model.TDataStore;
import org.omg.spec.bpmn._20100524.model.TError;
import org.omg.spec.bpmn._20100524.model.TEscalation;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TInterface;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.omg.spec.bpmn._20100524.model.TMessage;
import org.omg.spec.bpmn._20100524.model.TProcess;
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

	//------------------- GETTERS AND SETTERS ----------------------//
	
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
