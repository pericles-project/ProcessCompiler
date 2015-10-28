package eu.pericles.processcompiler.bpnmx;

import javax.xml.bind.annotation.XmlRegistry;

import org.omg.spec.bpmn._20100524.model.ObjectFactory;

@XmlRegistry
public class FancyObjectFactory extends ObjectFactory {

	@Override
	public FancyDefinitions createDefinitions() {
		return new FancyDefinitions();
	}

}
