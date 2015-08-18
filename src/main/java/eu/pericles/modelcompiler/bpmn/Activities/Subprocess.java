package eu.pericles.modelcompiler.bpmn.Activities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import eu.pericles.modelcompiler.bpmn.BpmnElement;
import eu.pericles.modelcompiler.bpmn.BpmnProcess;

@XStreamAlias("bpmn2:subProcess")
public class Subprocess extends BpmnProcess implements BpmnElement {

	@XStreamAlias("bpmn2:incoming")
	private String incoming;
	@XStreamAlias("bpmn2:outgoing")
	private String outgoing;

}
