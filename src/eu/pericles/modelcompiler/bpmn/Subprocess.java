package eu.pericles.modelcompiler.bpmn;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("bpmn2:subProcess")
public class Subprocess extends BpmnProcess {

	@XStreamAlias("bpmn2:incoming")
	private String incoming;
	@XStreamAlias("bpmn2:outgoing")
	private String outgoing;

}
