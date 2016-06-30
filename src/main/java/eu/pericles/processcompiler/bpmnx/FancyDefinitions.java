package eu.pericles.processcompiler.bpmnx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.Import;
import org.omg.spec.bpmn._20100524.model.TDataStore;
import org.omg.spec.bpmn._20100524.model.TError;
import org.omg.spec.bpmn._20100524.model.TEscalation;
import org.omg.spec.bpmn._20100524.model.TInterface;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.omg.spec.bpmn._20100524.model.TMessage;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TRootElement;
import org.omg.spec.bpmn._20100524.model.TSignal;

import eu.pericles.processcompiler.bpmn.BPMNProcess;

@XmlRootElement(name = "bpmn2:definitions")
public class FancyDefinitions extends Definitions {
	
	public void copyFromBpmnProcess(BPMNProcess process) {
		
		this.setId(process.getId());
		this.setTargetNamespace(process.getTargetNamespace());
		this.setExpressionLanguage(process.getExpressionLanguage());
		this.setTypeLanguage(process.getTypeLanguage());
		
		this.addImports(process.getImports());
		
		this.addItemDefinitions(process.getItemDefinitions());
		
		this.addErrors(process.getErrors());
		this.addEscalations(process.getEscalations());
		this.addMessages(process.getMessages());
		this.addSignals(process.getSignals());
		this.addInterfaces(process.getInterfaces());
		this.addDataStores(process.getDataStores());
		
		this.addProcess(process.getProcess());
		
		this.addDiagram(process.getDiagram());
	}

	private void addDiagram(BPMNDiagram diagram) {
		this.getBPMNDiagrams().add(diagram);
	}
	
	private void addProcess(TProcess process) {
		FancyObjectFactory object = new FancyObjectFactory();
		this.getRootElements().add(object.createProcess(process));
	}
	
	private void addImports(List<Import> list) {
		for (Import item : list) {
			this.getImports().add(item);
		}
	}
	
	private void addItemDefinitions(List<TItemDefinition> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TItemDefinition item : list) {
			this.getRootElements().add(object.createItemDefinition(item));
		}
	}
	
	private void addErrors(List<TError> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TError item : list) {
			this.getRootElements().add(object.createError(item));
		}
	}
	
	private void addEscalations(List<TEscalation> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TEscalation item : list) {
			this.getRootElements().add(object.createEscalation(item));
		}
	}
	
	private void addMessages(List<TMessage> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TMessage item : list) {
			this.getRootElements().add(object.createMessage(item));
		}
	}
	
	private void addSignals(List<TSignal> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TSignal item : list) {
			this.getRootElements().add(object.createSignal(item));
		}
	}
	
	private void addInterfaces(List<TInterface> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TInterface item : list) {
			this.getRootElements().add(object.createInterface(item));
		}
	}
	
	private void addDataStores(List<TDataStore> list) {
		FancyObjectFactory object = new FancyObjectFactory();
		for (TDataStore item : list) {
			this.getRootElements().add(object.createDataStore(item));
		}
	}

	public BPMNDiagram getDiagram() {
		if (getBPMNDiagrams().isEmpty()) {
			//throw new RuntimeException("The file doesn't contain a BPMN Diagram");
			return null;
		}
		else if (getBPMNDiagrams().size() == 1) {
			return getBPMNDiagrams().get(0);
		}
		else {
			throw new RuntimeException("Only one BPMN Process and Diagram per file is allowed");
		}
	}
	
	public TProcess getProcess() {
		int n_processes = 0;
		TProcess process = new TProcess();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TProcess.class)) {
				process = (TProcess) rootElement.getValue();
				n_processes ++;
			}
		}
		if (n_processes == 0) {
			throw new RuntimeException("The file doesn't contain a BPMN Process");
		} else if (n_processes == 1){
			return process;
		} else {
			throw new RuntimeException("Only one BPMN Process and Diagram per file is allowed");
		}
	}
	
	public List<TItemDefinition> getItemDefinitions() {
		List<TItemDefinition> list = new ArrayList<TItemDefinition>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TItemDefinition.class)) {
				list.add((TItemDefinition) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TError> getErrors() {
		List<TError> list = new ArrayList<TError>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TError.class)) {
				list.add((TError) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TEscalation> getEscalations() {
		List<TEscalation> list = new ArrayList<TEscalation>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TEscalation.class)) {
				list.add((TEscalation) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TMessage> getMessages() {
		List<TMessage> list = new ArrayList<TMessage>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TMessage.class)) {
				list.add((TMessage) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TSignal> getSignals() {
		List<TSignal> list = new ArrayList<TSignal>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TSignal.class)) {
				list.add((TSignal) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TInterface> getInterfaces() {
		List<TInterface> list = new ArrayList<TInterface>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TInterface.class)) {
				list.add((TInterface) rootElement.getValue());
			}
		}
		return list;
	}
	
	public List<TDataStore> getDataStores() {
		List<TDataStore> list = new ArrayList<TDataStore>();
		for(JAXBElement<? extends TRootElement> rootElement: getRootElements()) {
			if(rootElement.getDeclaredType().isAssignableFrom(TDataStore.class)) {
				list.add((TDataStore) rootElement.getValue());
			}
		}
		return list;
	}

}
