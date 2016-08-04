package eu.pericles.processcompiler.bpmnx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.Import;
import org.omg.spec.bpmn._20100524.model.TItemDefinition;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TRootElement;

import eu.pericles.processcompiler.bpmn.BPMNProcess;

@XmlRootElement(name = "bpmn2:definitions")
public class FancyDefinitions extends Definitions {

	public void copyFromBpmnProcess(BPMNProcess process) {
		this.setId(process.getId());
		this.setTargetNamespace(process.getTargetNamespace());
		this.addImports(process.getImports());
		this.addItemDefinitions(process.getItemDefinitions());
		this.addProcess(process.getProcess());
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

	public TProcess getProcess() {
		int n_processes = 0;
		TProcess process = new TProcess();
		for (JAXBElement<? extends TRootElement> rootElement : getRootElements()) {
			if (rootElement.getDeclaredType().isAssignableFrom(TProcess.class)) {
				process = (TProcess) rootElement.getValue();
				n_processes++;
			}
		}
		if (n_processes == 0) {
			throw new RuntimeException("The file doesn't contain a BPMN Process");
		} else if (n_processes == 1) {
			return process;
		} else {
			throw new RuntimeException("Only one BPMN Process per file is allowed");
		}
	}

	public List<TItemDefinition> getItemDefinitions() {
		List<TItemDefinition> list = new ArrayList<TItemDefinition>();
		for (JAXBElement<? extends TRootElement> rootElement : getRootElements()) {
			if (rootElement.getDeclaredType().isAssignableFrom(TItemDefinition.class)) {
				list.add((TItemDefinition) rootElement.getValue());
			}
		}
		return list;
	}
}
