
package org.omg.spec.bpmn._20100524.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tEventDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tEventDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tRootElement">
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEventDefinition")
@XmlSeeAlso({
    TTimerEventDefinition.class,
    TCancelEventDefinition.class,
    TMessageEventDefinition.class,
    TErrorEventDefinition.class,
    TConditionalEventDefinition.class,
    TTerminateEventDefinition.class,
    TLinkEventDefinition.class,
    TEscalationEventDefinition.class,
    TCompensateEventDefinition.class,
    TSignalEventDefinition.class
})
public abstract class TEventDefinition
    extends TRootElement
{


}
