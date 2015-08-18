
package org.omg.spec.bpmn._20100524.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.omg.spec.bpmn._20100524.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _HumanPerformer_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "humanPerformer");
    private final static QName _Collaboration_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "collaboration");
    private final static QName _ScriptTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "scriptTask");
    private final static QName _SequenceFlow_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "sequenceFlow");
    private final static QName _GlobalBusinessRuleTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalBusinessRuleTask");
    private final static QName _DataAssociation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataAssociation");
    private final static QName _IntermediateThrowEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "intermediateThrowEvent");
    private final static QName _ErrorEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "errorEventDefinition");
    private final static QName _ReceiveTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "receiveTask");
    private final static QName _Conversation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "conversation");
    private final static QName _ImplicitThrowEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "implicitThrowEvent");
    private final static QName _InclusiveGateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "inclusiveGateway");
    private final static QName _IntermediateCatchEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "intermediateCatchEvent");
    private final static QName _LoopCharacteristics_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "loopCharacteristics");
    private final static QName _Process_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "process");
    private final static QName _ConditionalEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "conditionalEventDefinition");
    private final static QName _FlowNode_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "flowNode");
    private final static QName _Artifact_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "artifact");
    private final static QName _EndPoint_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "endPoint");
    private final static QName _EndEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "endEvent");
    private final static QName _SubProcess_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "subProcess");
    private final static QName _BaseElement_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "baseElement");
    private final static QName _TerminateEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "terminateEventDefinition");
    private final static QName _EventBasedGateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "eventBasedGateway");
    private final static QName _GlobalScriptTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalScriptTask");
    private final static QName _TimerEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "timerEventDefinition");
    private final static QName _ComplexGateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "complexGateway");
    private final static QName _ManualTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "manualTask");
    private final static QName _CallableElement_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "callableElement");
    private final static QName _CancelEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "cancelEventDefinition");
    private final static QName _ServiceTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "serviceTask");
    private final static QName _SubChoreography_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "subChoreography");
    private final static QName _ChoreographyActivity_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "choreographyActivity");
    private final static QName _Event_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "event");
    private final static QName _GlobalConversation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalConversation");
    private final static QName _EventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "eventDefinition");
    private final static QName _ThrowEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "throwEvent");
    private final static QName _ItemDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "itemDefinition");
    private final static QName _AdHocSubProcess_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "adHocSubProcess");
    private final static QName _GlobalUserTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalUserTask");
    private final static QName _Category_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "category");
    private final static QName _StartEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "startEvent");
    private final static QName _Performer_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "performer");
    private final static QName _FormalExpression_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "formalExpression");
    private final static QName _MessageEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "messageEventDefinition");
    private final static QName _CatchEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "catchEvent");
    private final static QName _DataObjectReference_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataObjectReference");
    private final static QName _BoundaryEvent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "boundaryEvent");
    private final static QName _SendTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "sendTask");
    private final static QName _Choreography_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "choreography");
    private final static QName _CallChoreography_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "callChoreography");
    private final static QName _GlobalChoreographyTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalChoreographyTask");
    private final static QName _MultiInstanceLoopCharacteristics_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "multiInstanceLoopCharacteristics");
    private final static QName _PotentialOwner_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "potentialOwner");
    private final static QName _UserTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "userTask");
    private final static QName _Signal_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "signal");
    private final static QName _ParallelGateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "parallelGateway");
    private final static QName _SubConversation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "subConversation");
    private final static QName _BaseElementWithMixedContent_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "baseElementWithMixedContent");
    private final static QName _SignalEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "signalEventDefinition");
    private final static QName _DataStore_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataStore");
    private final static QName _RootElement_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "rootElement");
    private final static QName _Activity_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "activity");
    private final static QName _GlobalTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalTask");
    private final static QName _Error_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "error");
    private final static QName _Task_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "task");
    private final static QName _Resource_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "resource");
    private final static QName _Interface_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "interface");
    private final static QName _CorrelationProperty_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "correlationProperty");
    private final static QName _ExclusiveGateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "exclusiveGateway");
    private final static QName _Message_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "message");
    private final static QName _DataStoreReference_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataStoreReference");
    private final static QName _GlobalManualTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "globalManualTask");
    private final static QName _CallActivity_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "callActivity");
    private final static QName _Escalation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "escalation");
    private final static QName _TextAnnotation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "textAnnotation");
    private final static QName _Group_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "group");
    private final static QName _Expression_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "expression");
    private final static QName _Transaction_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "transaction");
    private final static QName _ChoreographyTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "choreographyTask");
    private final static QName _Gateway_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "gateway");
    private final static QName _ResourceRole_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "resourceRole");
    private final static QName _PartnerEntity_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "partnerEntity");
    private final static QName _PartnerRole_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "partnerRole");
    private final static QName _BusinessRuleTask_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "businessRuleTask");
    private final static QName _FlowElement_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "flowElement");
    private final static QName _DataObject_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataObject");
    private final static QName _LinkEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "linkEventDefinition");
    private final static QName _Association_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "association");
    private final static QName _CallConversation_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "callConversation");
    private final static QName _EscalationEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "escalationEventDefinition");
    private final static QName _ConversationNode_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "conversationNode");
    private final static QName _CompensateEventDefinition_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "compensateEventDefinition");
    private final static QName _StandardLoopCharacteristics_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "standardLoopCharacteristics");
    private final static QName _TDataAssociationSourceRef_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "sourceRef");
    private final static QName _OutputSetDataOutputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataOutputRefs");
    private final static QName _OutputSetOptionalOutputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "optionalOutputRefs");
    private final static QName _OutputSetWhileExecutingOutputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "whileExecutingOutputRefs");
    private final static QName _OutputSetInputSetRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "inputSetRefs");
    private final static QName _InputSetWhileExecutingInputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "whileExecutingInputRefs");
    private final static QName _InputSetOutputSetRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "outputSetRefs");
    private final static QName _InputSetOptionalInputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "optionalInputRefs");
    private final static QName _InputSetDataInputRefs_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "dataInputRefs");
    private final static QName _LaneFlowNodeRef_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "flowNodeRef");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.omg.spec.bpmn._20100524.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TComplexGateway }
     * 
     */
    public TComplexGateway createTComplexGateway() {
        return new TComplexGateway();
    }

    /**
     * Create an instance of {@link TTimerEventDefinition }
     * 
     */
    public TTimerEventDefinition createTTimerEventDefinition() {
        return new TTimerEventDefinition();
    }

    /**
     * Create an instance of {@link TManualTask }
     * 
     */
    public TManualTask createTManualTask() {
        return new TManualTask();
    }

    /**
     * Create an instance of {@link TCallableElement }
     * 
     */
    public TCallableElement createTCallableElement() {
        return new TCallableElement();
    }

    /**
     * Create an instance of {@link TServiceTask }
     * 
     */
    public TServiceTask createTServiceTask() {
        return new TServiceTask();
    }

    /**
     * Create an instance of {@link TCancelEventDefinition }
     * 
     */
    public TCancelEventDefinition createTCancelEventDefinition() {
        return new TCancelEventDefinition();
    }

    /**
     * Create an instance of {@link TSubChoreography }
     * 
     */
    public TSubChoreography createTSubChoreography() {
        return new TSubChoreography();
    }

    /**
     * Create an instance of {@link Operation }
     * 
     */
    public Operation createOperation() {
        return new Operation();
    }

    /**
     * Create an instance of {@link Documentation }
     * 
     */
    public Documentation createDocumentation() {
        return new Documentation();
    }

    /**
     * Create an instance of {@link ExtensionElements }
     * 
     */
    public ExtensionElements createExtensionElements() {
        return new ExtensionElements();
    }

    /**
     * Create an instance of {@link CorrelationSubscription }
     * 
     */
    public CorrelationSubscription createCorrelationSubscription() {
        return new CorrelationSubscription();
    }

    /**
     * Create an instance of {@link CorrelationPropertyBinding }
     * 
     */
    public CorrelationPropertyBinding createCorrelationPropertyBinding() {
        return new CorrelationPropertyBinding();
    }

    /**
     * Create an instance of {@link TFormalExpression }
     * 
     */
    public TFormalExpression createTFormalExpression() {
        return new TFormalExpression();
    }

    /**
     * Create an instance of {@link TGlobalConversation }
     * 
     */
    public TGlobalConversation createTGlobalConversation() {
        return new TGlobalConversation();
    }

    /**
     * Create an instance of {@link TCollaboration }
     * 
     */
    public TCollaboration createTCollaboration() {
        return new TCollaboration();
    }

    /**
     * Create an instance of {@link Import }
     * 
     */
    public Import createImport() {
        return new Import();
    }

    /**
     * Create an instance of {@link ResourceAssignmentExpression }
     * 
     */
    public ResourceAssignmentExpression createResourceAssignmentExpression() {
        return new ResourceAssignmentExpression();
    }

    /**
     * Create an instance of {@link TExpression }
     * 
     */
    public TExpression createTExpression() {
        return new TExpression();
    }

    /**
     * Create an instance of {@link Monitoring }
     * 
     */
    public Monitoring createMonitoring() {
        return new Monitoring();
    }

    /**
     * Create an instance of {@link TItemDefinition }
     * 
     */
    public TItemDefinition createTItemDefinition() {
        return new TItemDefinition();
    }

    /**
     * Create an instance of {@link TAdHocSubProcess }
     * 
     */
    public TAdHocSubProcess createTAdHocSubProcess() {
        return new TAdHocSubProcess();
    }

    /**
     * Create an instance of {@link TGlobalUserTask }
     * 
     */
    public TGlobalUserTask createTGlobalUserTask() {
        return new TGlobalUserTask();
    }

    /**
     * Create an instance of {@link TCategory }
     * 
     */
    public TCategory createTCategory() {
        return new TCategory();
    }

    /**
     * Create an instance of {@link TStartEvent }
     * 
     */
    public TStartEvent createTStartEvent() {
        return new TStartEvent();
    }

    /**
     * Create an instance of {@link TPerformer }
     * 
     */
    public TPerformer createTPerformer() {
        return new TPerformer();
    }

    /**
     * Create an instance of {@link TResourceRole }
     * 
     */
    public TResourceRole createTResourceRole() {
        return new TResourceRole();
    }

    /**
     * Create an instance of {@link Participant }
     * 
     */
    public Participant createParticipant() {
        return new Participant();
    }

    /**
     * Create an instance of {@link ParticipantMultiplicity }
     * 
     */
    public ParticipantMultiplicity createParticipantMultiplicity() {
        return new ParticipantMultiplicity();
    }

    /**
     * Create an instance of {@link TMessageEventDefinition }
     * 
     */
    public TMessageEventDefinition createTMessageEventDefinition() {
        return new TMessageEventDefinition();
    }

    /**
     * Create an instance of {@link THumanPerformer }
     * 
     */
    public THumanPerformer createTHumanPerformer() {
        return new THumanPerformer();
    }

    /**
     * Create an instance of {@link TSequenceFlow }
     * 
     */
    public TSequenceFlow createTSequenceFlow() {
        return new TSequenceFlow();
    }

    /**
     * Create an instance of {@link TScriptTask }
     * 
     */
    public TScriptTask createTScriptTask() {
        return new TScriptTask();
    }

    /**
     * Create an instance of {@link TDataAssociation }
     * 
     */
    public TDataAssociation createTDataAssociation() {
        return new TDataAssociation();
    }

    /**
     * Create an instance of {@link TGlobalBusinessRuleTask }
     * 
     */
    public TGlobalBusinessRuleTask createTGlobalBusinessRuleTask() {
        return new TGlobalBusinessRuleTask();
    }

    /**
     * Create an instance of {@link InputSet }
     * 
     */
    public InputSet createInputSet() {
        return new InputSet();
    }

    /**
     * Create an instance of {@link TIntermediateThrowEvent }
     * 
     */
    public TIntermediateThrowEvent createTIntermediateThrowEvent() {
        return new TIntermediateThrowEvent();
    }

    /**
     * Create an instance of {@link DataInputAssociation }
     * 
     */
    public DataInputAssociation createDataInputAssociation() {
        return new DataInputAssociation();
    }

    /**
     * Create an instance of {@link Assignment }
     * 
     */
    public Assignment createAssignment() {
        return new Assignment();
    }

    /**
     * Create an instance of {@link TErrorEventDefinition }
     * 
     */
    public TErrorEventDefinition createTErrorEventDefinition() {
        return new TErrorEventDefinition();
    }

    /**
     * Create an instance of {@link TReceiveTask }
     * 
     */
    public TReceiveTask createTReceiveTask() {
        return new TReceiveTask();
    }

    /**
     * Create an instance of {@link TConversation }
     * 
     */
    public TConversation createTConversation() {
        return new TConversation();
    }

    /**
     * Create an instance of {@link TImplicitThrowEvent }
     * 
     */
    public TImplicitThrowEvent createTImplicitThrowEvent() {
        return new TImplicitThrowEvent();
    }

    /**
     * Create an instance of {@link TInclusiveGateway }
     * 
     */
    public TInclusiveGateway createTInclusiveGateway() {
        return new TInclusiveGateway();
    }

    /**
     * Create an instance of {@link TIntermediateCatchEvent }
     * 
     */
    public TIntermediateCatchEvent createTIntermediateCatchEvent() {
        return new TIntermediateCatchEvent();
    }

    /**
     * Create an instance of {@link OutputSet }
     * 
     */
    public OutputSet createOutputSet() {
        return new OutputSet();
    }

    /**
     * Create an instance of {@link Relationship }
     * 
     */
    public Relationship createRelationship() {
        return new Relationship();
    }

    /**
     * Create an instance of {@link TProcess }
     * 
     */
    public TProcess createTProcess() {
        return new TProcess();
    }

    /**
     * Create an instance of {@link Extension }
     * 
     */
    public Extension createExtension() {
        return new Extension();
    }

    /**
     * Create an instance of {@link TConditionalEventDefinition }
     * 
     */
    public TConditionalEventDefinition createTConditionalEventDefinition() {
        return new TConditionalEventDefinition();
    }

    /**
     * Create an instance of {@link TEndPoint }
     * 
     */
    public TEndPoint createTEndPoint() {
        return new TEndPoint();
    }

    /**
     * Create an instance of {@link MessageFlow }
     * 
     */
    public MessageFlow createMessageFlow() {
        return new MessageFlow();
    }

    /**
     * Create an instance of {@link TSubProcess }
     * 
     */
    public TSubProcess createTSubProcess() {
        return new TSubProcess();
    }

    /**
     * Create an instance of {@link TEndEvent }
     * 
     */
    public TEndEvent createTEndEvent() {
        return new TEndEvent();
    }

    /**
     * Create an instance of {@link TTerminateEventDefinition }
     * 
     */
    public TTerminateEventDefinition createTTerminateEventDefinition() {
        return new TTerminateEventDefinition();
    }

    /**
     * Create an instance of {@link TEventBasedGateway }
     * 
     */
    public TEventBasedGateway createTEventBasedGateway() {
        return new TEventBasedGateway();
    }

    /**
     * Create an instance of {@link TGlobalScriptTask }
     * 
     */
    public TGlobalScriptTask createTGlobalScriptTask() {
        return new TGlobalScriptTask();
    }

    /**
     * Create an instance of {@link TGateway }
     * 
     */
    public TGateway createTGateway() {
        return new TGateway();
    }

    /**
     * Create an instance of {@link TTransaction }
     * 
     */
    public TTransaction createTTransaction() {
        return new TTransaction();
    }

    /**
     * Create an instance of {@link TChoreographyTask }
     * 
     */
    public TChoreographyTask createTChoreographyTask() {
        return new TChoreographyTask();
    }

    /**
     * Create an instance of {@link TPartnerRole }
     * 
     */
    public TPartnerRole createTPartnerRole() {
        return new TPartnerRole();
    }

    /**
     * Create an instance of {@link TPartnerEntity }
     * 
     */
    public TPartnerEntity createTPartnerEntity() {
        return new TPartnerEntity();
    }

    /**
     * Create an instance of {@link TBusinessRuleTask }
     * 
     */
    public TBusinessRuleTask createTBusinessRuleTask() {
        return new TBusinessRuleTask();
    }

    /**
     * Create an instance of {@link IoBinding }
     * 
     */
    public IoBinding createIoBinding() {
        return new IoBinding();
    }

    /**
     * Create an instance of {@link TDataObject }
     * 
     */
    public TDataObject createTDataObject() {
        return new TDataObject();
    }

    /**
     * Create an instance of {@link TLinkEventDefinition }
     * 
     */
    public TLinkEventDefinition createTLinkEventDefinition() {
        return new TLinkEventDefinition();
    }

    /**
     * Create an instance of {@link Text }
     * 
     */
    public Text createText() {
        return new Text();
    }

    /**
     * Create an instance of {@link ResourceParameter }
     * 
     */
    public ResourceParameter createResourceParameter() {
        return new ResourceParameter();
    }

    /**
     * Create an instance of {@link TAssociation }
     * 
     */
    public TAssociation createTAssociation() {
        return new TAssociation();
    }

    /**
     * Create an instance of {@link TCallConversation }
     * 
     */
    public TCallConversation createTCallConversation() {
        return new TCallConversation();
    }

    /**
     * Create an instance of {@link TEscalationEventDefinition }
     * 
     */
    public TEscalationEventDefinition createTEscalationEventDefinition() {
        return new TEscalationEventDefinition();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link DataState }
     * 
     */
    public DataState createDataState() {
        return new DataState();
    }

    /**
     * Create an instance of {@link Definitions }
     * 
     */
    public Definitions createDefinitions() {
        return new Definitions();
    }

    /**
     * Create an instance of {@link Script }
     * 
     */
    public Script createScript() {
        return new Script();
    }

    /**
     * Create an instance of {@link DataInput }
     * 
     */
    public DataInput createDataInput() {
        return new DataInput();
    }

    /**
     * Create an instance of {@link ComplexBehaviorDefinition }
     * 
     */
    public ComplexBehaviorDefinition createComplexBehaviorDefinition() {
        return new ComplexBehaviorDefinition();
    }

    /**
     * Create an instance of {@link TCompensateEventDefinition }
     * 
     */
    public TCompensateEventDefinition createTCompensateEventDefinition() {
        return new TCompensateEventDefinition();
    }

    /**
     * Create an instance of {@link MessageFlowAssociation }
     * 
     */
    public MessageFlowAssociation createMessageFlowAssociation() {
        return new MessageFlowAssociation();
    }

    /**
     * Create an instance of {@link ConversationLink }
     * 
     */
    public ConversationLink createConversationLink() {
        return new ConversationLink();
    }

    /**
     * Create an instance of {@link TStandardLoopCharacteristics }
     * 
     */
    public TStandardLoopCharacteristics createTStandardLoopCharacteristics() {
        return new TStandardLoopCharacteristics();
    }

    /**
     * Create an instance of {@link ParticipantAssociation }
     * 
     */
    public ParticipantAssociation createParticipantAssociation() {
        return new ParticipantAssociation();
    }

    /**
     * Create an instance of {@link DataOutputAssociation }
     * 
     */
    public DataOutputAssociation createDataOutputAssociation() {
        return new DataOutputAssociation();
    }

    /**
     * Create an instance of {@link TDataObjectReference }
     * 
     */
    public TDataObjectReference createTDataObjectReference() {
        return new TDataObjectReference();
    }

    /**
     * Create an instance of {@link TBoundaryEvent }
     * 
     */
    public TBoundaryEvent createTBoundaryEvent() {
        return new TBoundaryEvent();
    }

    /**
     * Create an instance of {@link CategoryValue }
     * 
     */
    public CategoryValue createCategoryValue() {
        return new CategoryValue();
    }

    /**
     * Create an instance of {@link TSendTask }
     * 
     */
    public TSendTask createTSendTask() {
        return new TSendTask();
    }

    /**
     * Create an instance of {@link TChoreography }
     * 
     */
    public TChoreography createTChoreography() {
        return new TChoreography();
    }

    /**
     * Create an instance of {@link TMultiInstanceLoopCharacteristics }
     * 
     */
    public TMultiInstanceLoopCharacteristics createTMultiInstanceLoopCharacteristics() {
        return new TMultiInstanceLoopCharacteristics();
    }

    /**
     * Create an instance of {@link TCallChoreography }
     * 
     */
    public TCallChoreography createTCallChoreography() {
        return new TCallChoreography();
    }

    /**
     * Create an instance of {@link TGlobalChoreographyTask }
     * 
     */
    public TGlobalChoreographyTask createTGlobalChoreographyTask() {
        return new TGlobalChoreographyTask();
    }

    /**
     * Create an instance of {@link TUserTask }
     * 
     */
    public TUserTask createTUserTask() {
        return new TUserTask();
    }

    /**
     * Create an instance of {@link TSignal }
     * 
     */
    public TSignal createTSignal() {
        return new TSignal();
    }

    /**
     * Create an instance of {@link TPotentialOwner }
     * 
     */
    public TPotentialOwner createTPotentialOwner() {
        return new TPotentialOwner();
    }

    /**
     * Create an instance of {@link TParallelGateway }
     * 
     */
    public TParallelGateway createTParallelGateway() {
        return new TParallelGateway();
    }

    /**
     * Create an instance of {@link TSubConversation }
     * 
     */
    public TSubConversation createTSubConversation() {
        return new TSubConversation();
    }

    /**
     * Create an instance of {@link Lane }
     * 
     */
    public Lane createLane() {
        return new Lane();
    }

    /**
     * Create an instance of {@link LaneSet }
     * 
     */
    public LaneSet createLaneSet() {
        return new LaneSet();
    }

    /**
     * Create an instance of {@link TSignalEventDefinition }
     * 
     */
    public TSignalEventDefinition createTSignalEventDefinition() {
        return new TSignalEventDefinition();
    }

    /**
     * Create an instance of {@link TDataStore }
     * 
     */
    public TDataStore createTDataStore() {
        return new TDataStore();
    }

    /**
     * Create an instance of {@link CorrelationPropertyRetrievalExpression }
     * 
     */
    public CorrelationPropertyRetrievalExpression createCorrelationPropertyRetrievalExpression() {
        return new CorrelationPropertyRetrievalExpression();
    }

    /**
     * Create an instance of {@link ConversationAssociation }
     * 
     */
    public ConversationAssociation createConversationAssociation() {
        return new ConversationAssociation();
    }

    /**
     * Create an instance of {@link IoSpecification }
     * 
     */
    public IoSpecification createIoSpecification() {
        return new IoSpecification();
    }

    /**
     * Create an instance of {@link DataOutput }
     * 
     */
    public DataOutput createDataOutput() {
        return new DataOutput();
    }

    /**
     * Create an instance of {@link TGlobalTask }
     * 
     */
    public TGlobalTask createTGlobalTask() {
        return new TGlobalTask();
    }

    /**
     * Create an instance of {@link TError }
     * 
     */
    public TError createTError() {
        return new TError();
    }

    /**
     * Create an instance of {@link TTask }
     * 
     */
    public TTask createTTask() {
        return new TTask();
    }

    /**
     * Create an instance of {@link TResource }
     * 
     */
    public TResource createTResource() {
        return new TResource();
    }

    /**
     * Create an instance of {@link CorrelationKey }
     * 
     */
    public CorrelationKey createCorrelationKey() {
        return new CorrelationKey();
    }

    /**
     * Create an instance of {@link TInterface }
     * 
     */
    public TInterface createTInterface() {
        return new TInterface();
    }

    /**
     * Create an instance of {@link TCorrelationProperty }
     * 
     */
    public TCorrelationProperty createTCorrelationProperty() {
        return new TCorrelationProperty();
    }

    /**
     * Create an instance of {@link TExclusiveGateway }
     * 
     */
    public TExclusiveGateway createTExclusiveGateway() {
        return new TExclusiveGateway();
    }

    /**
     * Create an instance of {@link ResourceParameterBinding }
     * 
     */
    public ResourceParameterBinding createResourceParameterBinding() {
        return new ResourceParameterBinding();
    }

    /**
     * Create an instance of {@link Rendering }
     * 
     */
    public Rendering createRendering() {
        return new Rendering();
    }

    /**
     * Create an instance of {@link TMessage }
     * 
     */
    public TMessage createTMessage() {
        return new TMessage();
    }

    /**
     * Create an instance of {@link TDataStoreReference }
     * 
     */
    public TDataStoreReference createTDataStoreReference() {
        return new TDataStoreReference();
    }

    /**
     * Create an instance of {@link TGlobalManualTask }
     * 
     */
    public TGlobalManualTask createTGlobalManualTask() {
        return new TGlobalManualTask();
    }

    /**
     * Create an instance of {@link TCallActivity }
     * 
     */
    public TCallActivity createTCallActivity() {
        return new TCallActivity();
    }

    /**
     * Create an instance of {@link TEscalation }
     * 
     */
    public TEscalation createTEscalation() {
        return new TEscalation();
    }

    /**
     * Create an instance of {@link Auditing }
     * 
     */
    public Auditing createAuditing() {
        return new Auditing();
    }

    /**
     * Create an instance of {@link TGroup }
     * 
     */
    public TGroup createTGroup() {
        return new TGroup();
    }

    /**
     * Create an instance of {@link TTextAnnotation }
     * 
     */
    public TTextAnnotation createTTextAnnotation() {
        return new TTextAnnotation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link THumanPerformer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "humanPerformer", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "performer")
    public JAXBElement<THumanPerformer> createHumanPerformer(THumanPerformer value) {
        return new JAXBElement<THumanPerformer>(_HumanPerformer_QNAME, THumanPerformer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCollaboration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "collaboration", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TCollaboration> createCollaboration(TCollaboration value) {
        return new JAXBElement<TCollaboration>(_Collaboration_QNAME, TCollaboration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TScriptTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "scriptTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TScriptTask> createScriptTask(TScriptTask value) {
        return new JAXBElement<TScriptTask>(_ScriptTask_QNAME, TScriptTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSequenceFlow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "sequenceFlow", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TSequenceFlow> createSequenceFlow(TSequenceFlow value) {
        return new JAXBElement<TSequenceFlow>(_SequenceFlow_QNAME, TSequenceFlow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalBusinessRuleTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalBusinessRuleTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TGlobalBusinessRuleTask> createGlobalBusinessRuleTask(TGlobalBusinessRuleTask value) {
        return new JAXBElement<TGlobalBusinessRuleTask>(_GlobalBusinessRuleTask_QNAME, TGlobalBusinessRuleTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDataAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataAssociation")
    public JAXBElement<TDataAssociation> createDataAssociation(TDataAssociation value) {
        return new JAXBElement<TDataAssociation>(_DataAssociation_QNAME, TDataAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TIntermediateThrowEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "intermediateThrowEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TIntermediateThrowEvent> createIntermediateThrowEvent(TIntermediateThrowEvent value) {
        return new JAXBElement<TIntermediateThrowEvent>(_IntermediateThrowEvent_QNAME, TIntermediateThrowEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TErrorEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "errorEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TErrorEventDefinition> createErrorEventDefinition(TErrorEventDefinition value) {
        return new JAXBElement<TErrorEventDefinition>(_ErrorEventDefinition_QNAME, TErrorEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TReceiveTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "receiveTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TReceiveTask> createReceiveTask(TReceiveTask value) {
        return new JAXBElement<TReceiveTask>(_ReceiveTask_QNAME, TReceiveTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TConversation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "conversation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "conversationNode")
    public JAXBElement<TConversation> createConversation(TConversation value) {
        return new JAXBElement<TConversation>(_Conversation_QNAME, TConversation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TImplicitThrowEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "implicitThrowEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TImplicitThrowEvent> createImplicitThrowEvent(TImplicitThrowEvent value) {
        return new JAXBElement<TImplicitThrowEvent>(_ImplicitThrowEvent_QNAME, TImplicitThrowEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TInclusiveGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "inclusiveGateway", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TInclusiveGateway> createInclusiveGateway(TInclusiveGateway value) {
        return new JAXBElement<TInclusiveGateway>(_InclusiveGateway_QNAME, TInclusiveGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TIntermediateCatchEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "intermediateCatchEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TIntermediateCatchEvent> createIntermediateCatchEvent(TIntermediateCatchEvent value) {
        return new JAXBElement<TIntermediateCatchEvent>(_IntermediateCatchEvent_QNAME, TIntermediateCatchEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLoopCharacteristics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "loopCharacteristics")
    public JAXBElement<TLoopCharacteristics> createLoopCharacteristics(TLoopCharacteristics value) {
        return new JAXBElement<TLoopCharacteristics>(_LoopCharacteristics_QNAME, TLoopCharacteristics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TProcess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "process", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TProcess> createProcess(TProcess value) {
        return new JAXBElement<TProcess>(_Process_QNAME, TProcess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TConditionalEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "conditionalEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TConditionalEventDefinition> createConditionalEventDefinition(TConditionalEventDefinition value) {
        return new JAXBElement<TConditionalEventDefinition>(_ConditionalEventDefinition_QNAME, TConditionalEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFlowNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "flowNode")
    public JAXBElement<TFlowNode> createFlowNode(TFlowNode value) {
        return new JAXBElement<TFlowNode>(_FlowNode_QNAME, TFlowNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TArtifact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "artifact")
    public JAXBElement<TArtifact> createArtifact(TArtifact value) {
        return new JAXBElement<TArtifact>(_Artifact_QNAME, TArtifact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEndPoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "endPoint", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TEndPoint> createEndPoint(TEndPoint value) {
        return new JAXBElement<TEndPoint>(_EndPoint_QNAME, TEndPoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEndEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "endEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TEndEvent> createEndEvent(TEndEvent value) {
        return new JAXBElement<TEndEvent>(_EndEvent_QNAME, TEndEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSubProcess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "subProcess", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TSubProcess> createSubProcess(TSubProcess value) {
        return new JAXBElement<TSubProcess>(_SubProcess_QNAME, TSubProcess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TBaseElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "baseElement")
    public JAXBElement<TBaseElement> createBaseElement(TBaseElement value) {
        return new JAXBElement<TBaseElement>(_BaseElement_QNAME, TBaseElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTerminateEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "terminateEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TTerminateEventDefinition> createTerminateEventDefinition(TTerminateEventDefinition value) {
        return new JAXBElement<TTerminateEventDefinition>(_TerminateEventDefinition_QNAME, TTerminateEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEventBasedGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "eventBasedGateway", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TEventBasedGateway> createEventBasedGateway(TEventBasedGateway value) {
        return new JAXBElement<TEventBasedGateway>(_EventBasedGateway_QNAME, TEventBasedGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalScriptTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalScriptTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TGlobalScriptTask> createGlobalScriptTask(TGlobalScriptTask value) {
        return new JAXBElement<TGlobalScriptTask>(_GlobalScriptTask_QNAME, TGlobalScriptTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTimerEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "timerEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TTimerEventDefinition> createTimerEventDefinition(TTimerEventDefinition value) {
        return new JAXBElement<TTimerEventDefinition>(_TimerEventDefinition_QNAME, TTimerEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TComplexGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "complexGateway", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TComplexGateway> createComplexGateway(TComplexGateway value) {
        return new JAXBElement<TComplexGateway>(_ComplexGateway_QNAME, TComplexGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TManualTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "manualTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TManualTask> createManualTask(TManualTask value) {
        return new JAXBElement<TManualTask>(_ManualTask_QNAME, TManualTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCallableElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "callableElement")
    public JAXBElement<TCallableElement> createCallableElement(TCallableElement value) {
        return new JAXBElement<TCallableElement>(_CallableElement_QNAME, TCallableElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCancelEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "cancelEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TCancelEventDefinition> createCancelEventDefinition(TCancelEventDefinition value) {
        return new JAXBElement<TCancelEventDefinition>(_CancelEventDefinition_QNAME, TCancelEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TServiceTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "serviceTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TServiceTask> createServiceTask(TServiceTask value) {
        return new JAXBElement<TServiceTask>(_ServiceTask_QNAME, TServiceTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSubChoreography }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "subChoreography", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TSubChoreography> createSubChoreography(TSubChoreography value) {
        return new JAXBElement<TSubChoreography>(_SubChoreography_QNAME, TSubChoreography.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TChoreographyActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "choreographyActivity")
    public JAXBElement<TChoreographyActivity> createChoreographyActivity(TChoreographyActivity value) {
        return new JAXBElement<TChoreographyActivity>(_ChoreographyActivity_QNAME, TChoreographyActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "event", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TEvent> createEvent(TEvent value) {
        return new JAXBElement<TEvent>(_Event_QNAME, TEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalConversation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalConversation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "collaboration")
    public JAXBElement<TGlobalConversation> createGlobalConversation(TGlobalConversation value) {
        return new JAXBElement<TGlobalConversation>(_GlobalConversation_QNAME, TGlobalConversation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "eventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TEventDefinition> createEventDefinition(TEventDefinition value) {
        return new JAXBElement<TEventDefinition>(_EventDefinition_QNAME, TEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TThrowEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "throwEvent")
    public JAXBElement<TThrowEvent> createThrowEvent(TThrowEvent value) {
        return new JAXBElement<TThrowEvent>(_ThrowEvent_QNAME, TThrowEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TItemDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "itemDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TItemDefinition> createItemDefinition(TItemDefinition value) {
        return new JAXBElement<TItemDefinition>(_ItemDefinition_QNAME, TItemDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TAdHocSubProcess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "adHocSubProcess", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TAdHocSubProcess> createAdHocSubProcess(TAdHocSubProcess value) {
        return new JAXBElement<TAdHocSubProcess>(_AdHocSubProcess_QNAME, TAdHocSubProcess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalUserTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalUserTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TGlobalUserTask> createGlobalUserTask(TGlobalUserTask value) {
        return new JAXBElement<TGlobalUserTask>(_GlobalUserTask_QNAME, TGlobalUserTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCategory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "category", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TCategory> createCategory(TCategory value) {
        return new JAXBElement<TCategory>(_Category_QNAME, TCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TStartEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "startEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TStartEvent> createStartEvent(TStartEvent value) {
        return new JAXBElement<TStartEvent>(_StartEvent_QNAME, TStartEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TPerformer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "performer", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "resourceRole")
    public JAXBElement<TPerformer> createPerformer(TPerformer value) {
        return new JAXBElement<TPerformer>(_Performer_QNAME, TPerformer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFormalExpression }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "formalExpression", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "expression")
    public JAXBElement<TFormalExpression> createFormalExpression(TFormalExpression value) {
        return new JAXBElement<TFormalExpression>(_FormalExpression_QNAME, TFormalExpression.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TMessageEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "messageEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TMessageEventDefinition> createMessageEventDefinition(TMessageEventDefinition value) {
        return new JAXBElement<TMessageEventDefinition>(_MessageEventDefinition_QNAME, TMessageEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCatchEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "catchEvent")
    public JAXBElement<TCatchEvent> createCatchEvent(TCatchEvent value) {
        return new JAXBElement<TCatchEvent>(_CatchEvent_QNAME, TCatchEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDataObjectReference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataObjectReference", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TDataObjectReference> createDataObjectReference(TDataObjectReference value) {
        return new JAXBElement<TDataObjectReference>(_DataObjectReference_QNAME, TDataObjectReference.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TBoundaryEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "boundaryEvent", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TBoundaryEvent> createBoundaryEvent(TBoundaryEvent value) {
        return new JAXBElement<TBoundaryEvent>(_BoundaryEvent_QNAME, TBoundaryEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSendTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "sendTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TSendTask> createSendTask(TSendTask value) {
        return new JAXBElement<TSendTask>(_SendTask_QNAME, TSendTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TChoreography }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "choreography", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "collaboration")
    public JAXBElement<TChoreography> createChoreography(TChoreography value) {
        return new JAXBElement<TChoreography>(_Choreography_QNAME, TChoreography.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCallChoreography }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "callChoreography", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TCallChoreography> createCallChoreography(TCallChoreography value) {
        return new JAXBElement<TCallChoreography>(_CallChoreography_QNAME, TCallChoreography.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalChoreographyTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalChoreographyTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "choreography")
    public JAXBElement<TGlobalChoreographyTask> createGlobalChoreographyTask(TGlobalChoreographyTask value) {
        return new JAXBElement<TGlobalChoreographyTask>(_GlobalChoreographyTask_QNAME, TGlobalChoreographyTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TMultiInstanceLoopCharacteristics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "multiInstanceLoopCharacteristics", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "loopCharacteristics")
    public JAXBElement<TMultiInstanceLoopCharacteristics> createMultiInstanceLoopCharacteristics(TMultiInstanceLoopCharacteristics value) {
        return new JAXBElement<TMultiInstanceLoopCharacteristics>(_MultiInstanceLoopCharacteristics_QNAME, TMultiInstanceLoopCharacteristics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TPotentialOwner }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "potentialOwner", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "performer")
    public JAXBElement<TPotentialOwner> createPotentialOwner(TPotentialOwner value) {
        return new JAXBElement<TPotentialOwner>(_PotentialOwner_QNAME, TPotentialOwner.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TUserTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "userTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TUserTask> createUserTask(TUserTask value) {
        return new JAXBElement<TUserTask>(_UserTask_QNAME, TUserTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSignal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "signal", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TSignal> createSignal(TSignal value) {
        return new JAXBElement<TSignal>(_Signal_QNAME, TSignal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TParallelGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "parallelGateway", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TParallelGateway> createParallelGateway(TParallelGateway value) {
        return new JAXBElement<TParallelGateway>(_ParallelGateway_QNAME, TParallelGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSubConversation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "subConversation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "conversationNode")
    public JAXBElement<TSubConversation> createSubConversation(TSubConversation value) {
        return new JAXBElement<TSubConversation>(_SubConversation_QNAME, TSubConversation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TBaseElementWithMixedContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "baseElementWithMixedContent")
    public JAXBElement<TBaseElementWithMixedContent> createBaseElementWithMixedContent(TBaseElementWithMixedContent value) {
        return new JAXBElement<TBaseElementWithMixedContent>(_BaseElementWithMixedContent_QNAME, TBaseElementWithMixedContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSignalEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "signalEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TSignalEventDefinition> createSignalEventDefinition(TSignalEventDefinition value) {
        return new JAXBElement<TSignalEventDefinition>(_SignalEventDefinition_QNAME, TSignalEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDataStore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataStore", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TDataStore> createDataStore(TDataStore value) {
        return new JAXBElement<TDataStore>(_DataStore_QNAME, TDataStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TRootElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "rootElement")
    public JAXBElement<TRootElement> createRootElement(TRootElement value) {
        return new JAXBElement<TRootElement>(_RootElement_QNAME, TRootElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "activity")
    public JAXBElement<TActivity> createActivity(TActivity value) {
        return new JAXBElement<TActivity>(_Activity_QNAME, TActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TGlobalTask> createGlobalTask(TGlobalTask value) {
        return new JAXBElement<TGlobalTask>(_GlobalTask_QNAME, TGlobalTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "error", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TError> createError(TError value) {
        return new JAXBElement<TError>(_Error_QNAME, TError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "task", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TTask> createTask(TTask value) {
        return new JAXBElement<TTask>(_Task_QNAME, TTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "resource", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TResource> createResource(TResource value) {
        return new JAXBElement<TResource>(_Resource_QNAME, TResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TInterface }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "interface", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TInterface> createInterface(TInterface value) {
        return new JAXBElement<TInterface>(_Interface_QNAME, TInterface.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCorrelationProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "correlationProperty", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TCorrelationProperty> createCorrelationProperty(TCorrelationProperty value) {
        return new JAXBElement<TCorrelationProperty>(_CorrelationProperty_QNAME, TCorrelationProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TExclusiveGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "exclusiveGateway", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TExclusiveGateway> createExclusiveGateway(TExclusiveGateway value) {
        return new JAXBElement<TExclusiveGateway>(_ExclusiveGateway_QNAME, TExclusiveGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "message", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TMessage> createMessage(TMessage value) {
        return new JAXBElement<TMessage>(_Message_QNAME, TMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDataStoreReference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataStoreReference", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TDataStoreReference> createDataStoreReference(TDataStoreReference value) {
        return new JAXBElement<TDataStoreReference>(_DataStoreReference_QNAME, TDataStoreReference.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGlobalManualTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "globalManualTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TGlobalManualTask> createGlobalManualTask(TGlobalManualTask value) {
        return new JAXBElement<TGlobalManualTask>(_GlobalManualTask_QNAME, TGlobalManualTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCallActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "callActivity", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TCallActivity> createCallActivity(TCallActivity value) {
        return new JAXBElement<TCallActivity>(_CallActivity_QNAME, TCallActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEscalation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "escalation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TEscalation> createEscalation(TEscalation value) {
        return new JAXBElement<TEscalation>(_Escalation_QNAME, TEscalation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTextAnnotation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "textAnnotation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "artifact")
    public JAXBElement<TTextAnnotation> createTextAnnotation(TTextAnnotation value) {
        return new JAXBElement<TTextAnnotation>(_TextAnnotation_QNAME, TTextAnnotation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "group", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "artifact")
    public JAXBElement<TGroup> createGroup(TGroup value) {
        return new JAXBElement<TGroup>(_Group_QNAME, TGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TExpression }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "expression")
    public JAXBElement<TExpression> createExpression(TExpression value) {
        return new JAXBElement<TExpression>(_Expression_QNAME, TExpression.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "transaction", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TTransaction> createTransaction(TTransaction value) {
        return new JAXBElement<TTransaction>(_Transaction_QNAME, TTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TChoreographyTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "choreographyTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TChoreographyTask> createChoreographyTask(TChoreographyTask value) {
        return new JAXBElement<TChoreographyTask>(_ChoreographyTask_QNAME, TChoreographyTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGateway }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "gateway")
    public JAXBElement<TGateway> createGateway(TGateway value) {
        return new JAXBElement<TGateway>(_Gateway_QNAME, TGateway.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TResourceRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "resourceRole")
    public JAXBElement<TResourceRole> createResourceRole(TResourceRole value) {
        return new JAXBElement<TResourceRole>(_ResourceRole_QNAME, TResourceRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TPartnerEntity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "partnerEntity", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TPartnerEntity> createPartnerEntity(TPartnerEntity value) {
        return new JAXBElement<TPartnerEntity>(_PartnerEntity_QNAME, TPartnerEntity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TPartnerRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "partnerRole", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "rootElement")
    public JAXBElement<TPartnerRole> createPartnerRole(TPartnerRole value) {
        return new JAXBElement<TPartnerRole>(_PartnerRole_QNAME, TPartnerRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TBusinessRuleTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "businessRuleTask", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TBusinessRuleTask> createBusinessRuleTask(TBusinessRuleTask value) {
        return new JAXBElement<TBusinessRuleTask>(_BusinessRuleTask_QNAME, TBusinessRuleTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFlowElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "flowElement")
    public JAXBElement<TFlowElement> createFlowElement(TFlowElement value) {
        return new JAXBElement<TFlowElement>(_FlowElement_QNAME, TFlowElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDataObject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataObject", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "flowElement")
    public JAXBElement<TDataObject> createDataObject(TDataObject value) {
        return new JAXBElement<TDataObject>(_DataObject_QNAME, TDataObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLinkEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "linkEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TLinkEventDefinition> createLinkEventDefinition(TLinkEventDefinition value) {
        return new JAXBElement<TLinkEventDefinition>(_LinkEventDefinition_QNAME, TLinkEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "association", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "artifact")
    public JAXBElement<TAssociation> createAssociation(TAssociation value) {
        return new JAXBElement<TAssociation>(_Association_QNAME, TAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCallConversation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "callConversation", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "conversationNode")
    public JAXBElement<TCallConversation> createCallConversation(TCallConversation value) {
        return new JAXBElement<TCallConversation>(_CallConversation_QNAME, TCallConversation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEscalationEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "escalationEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TEscalationEventDefinition> createEscalationEventDefinition(TEscalationEventDefinition value) {
        return new JAXBElement<TEscalationEventDefinition>(_EscalationEventDefinition_QNAME, TEscalationEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TConversationNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "conversationNode")
    public JAXBElement<TConversationNode> createConversationNode(TConversationNode value) {
        return new JAXBElement<TConversationNode>(_ConversationNode_QNAME, TConversationNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCompensateEventDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "compensateEventDefinition", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "eventDefinition")
    public JAXBElement<TCompensateEventDefinition> createCompensateEventDefinition(TCompensateEventDefinition value) {
        return new JAXBElement<TCompensateEventDefinition>(_CompensateEventDefinition_QNAME, TCompensateEventDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TStandardLoopCharacteristics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "standardLoopCharacteristics", substitutionHeadNamespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", substitutionHeadName = "loopCharacteristics")
    public JAXBElement<TStandardLoopCharacteristics> createStandardLoopCharacteristics(TStandardLoopCharacteristics value) {
        return new JAXBElement<TStandardLoopCharacteristics>(_StandardLoopCharacteristics_QNAME, TStandardLoopCharacteristics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "sourceRef", scope = TDataAssociation.class)
    @XmlIDREF
    public JAXBElement<Object> createTDataAssociationSourceRef(Object value) {
        return new JAXBElement<Object>(_TDataAssociationSourceRef_QNAME, Object.class, TDataAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataOutputRefs", scope = OutputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createOutputSetDataOutputRefs(Object value) {
        return new JAXBElement<Object>(_OutputSetDataOutputRefs_QNAME, Object.class, OutputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "optionalOutputRefs", scope = OutputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createOutputSetOptionalOutputRefs(Object value) {
        return new JAXBElement<Object>(_OutputSetOptionalOutputRefs_QNAME, Object.class, OutputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "whileExecutingOutputRefs", scope = OutputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createOutputSetWhileExecutingOutputRefs(Object value) {
        return new JAXBElement<Object>(_OutputSetWhileExecutingOutputRefs_QNAME, Object.class, OutputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "inputSetRefs", scope = OutputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createOutputSetInputSetRefs(Object value) {
        return new JAXBElement<Object>(_OutputSetInputSetRefs_QNAME, Object.class, OutputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "whileExecutingInputRefs", scope = InputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createInputSetWhileExecutingInputRefs(Object value) {
        return new JAXBElement<Object>(_InputSetWhileExecutingInputRefs_QNAME, Object.class, InputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "outputSetRefs", scope = InputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createInputSetOutputSetRefs(Object value) {
        return new JAXBElement<Object>(_InputSetOutputSetRefs_QNAME, Object.class, InputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "optionalInputRefs", scope = InputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createInputSetOptionalInputRefs(Object value) {
        return new JAXBElement<Object>(_InputSetOptionalInputRefs_QNAME, Object.class, InputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "dataInputRefs", scope = InputSet.class)
    @XmlIDREF
    public JAXBElement<Object> createInputSetDataInputRefs(Object value) {
        return new JAXBElement<Object>(_InputSetDataInputRefs_QNAME, Object.class, InputSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", name = "flowNodeRef", scope = Lane.class)
    @XmlIDREF
    public JAXBElement<Object> createLaneFlowNodeRef(Object value) {
        return new JAXBElement<Object>(_LaneFlowNodeRef_QNAME, Object.class, Lane.class, value);
    }

}
