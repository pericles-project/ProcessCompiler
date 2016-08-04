package eu.pericles.processcompiler.bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.omg.spec.bpmn._20100524.model.Import;
import org.omg.spec.bpmn._20100524.model.TDataObject;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.omg.spec.bpmn._20100524.model.TProcess;

import eu.pericles.processcompiler.exceptions.BPMNFileException;

public class BPMNProcess {

	private String id;
	private String targetNamespace;
	private List<Import> imports = new ArrayList<>();
	private List<TItemDefinition> itemDefinitions = new ArrayList<>();
	private TProcess process;

	public void addBPMNProcess(BPMNProcess bpmnProcess) {
		this.addItemDefinitions(bpmnProcess.getItemDefinitions());
		this.addFlowElements(bpmnProcess.getFlowElements());
	}

	public void addFlowElements(List<JAXBElement<? extends TFlowElement>> flowElements) {
		for (JAXBElement<? extends TFlowElement> flowElement : flowElements) {
			getProcess().getFlowElements().add(flowElement);
		}
	}

	public void addItemDefinitions(List<TItemDefinition> itemDefinitions) {
		for (TItemDefinition itemDefinition : itemDefinitions) {
			getItemDefinitions().add(itemDefinition);
		}
	}

	public TItemDefinition findItemDefinitionByName(QName name) throws BPMNFileException {
		for (TItemDefinition itemDef : getItemDefinitions())
			if (itemDef.getId().equals(name.getLocalPart())) {
				return itemDef;
			}
		throw new BPMNFileException("There is not item definition with id: " + name.getLocalPart());
	}

	public List<JAXBElement<? extends TFlowElement>> getFlowElements() {
		return getProcess().getFlowElements();
	}

	public List<TDataObject> getDataObjects() {
		List<TDataObject> dataObjects = new ArrayList<TDataObject>();
		for (JAXBElement<? extends TFlowElement> element : getFlowElements())
			if (TDataObject.class.isAssignableFrom(element.getValue().getClass()))
				dataObjects.add((TDataObject) element.getValue());
		return dataObjects;
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

	public TProcess getProcess() {
		return process;
	}

	public void setProcess(TProcess process) {
		this.process = process;
	}

}
