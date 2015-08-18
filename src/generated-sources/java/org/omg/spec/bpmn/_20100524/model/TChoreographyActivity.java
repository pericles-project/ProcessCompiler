
package org.omg.spec.bpmn._20100524.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tChoreographyActivity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tChoreographyActivity">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tFlowNode">
 *       &lt;sequence>
 *         &lt;element name="participantRef" type="{http://www.w3.org/2001/XMLSchema}QName" maxOccurs="unbounded" minOccurs="2"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}correlationKey" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="initiatingParticipantRef" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="loopType" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tChoreographyLoopType" default="None" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tChoreographyActivity", propOrder = {
    "participantReves",
    "correlationKeies"
})
@XmlSeeAlso({
    TSubChoreography.class,
    TChoreographyTask.class,
    TCallChoreography.class
})
public abstract class TChoreographyActivity
    extends TFlowNode
{

    @XmlElement(name = "participantRef", required = true)
    protected List<QName> participantReves;
    @XmlElement(name = "correlationKey")
    protected List<CorrelationKey> correlationKeies;
    @XmlAttribute(name = "initiatingParticipantRef", required = true)
    protected QName initiatingParticipantRef;
    @XmlAttribute(name = "loopType")
    protected TChoreographyLoopType loopType;

    /**
     * Gets the value of the participantReves property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantReves property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantReves().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QName }
     * 
     * 
     */
    public List<QName> getParticipantReves() {
        if (participantReves == null) {
            participantReves = new ArrayList<QName>();
        }
        return this.participantReves;
    }

    /**
     * Gets the value of the correlationKeies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correlationKeies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrelationKeies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrelationKey }
     * 
     * 
     */
    public List<CorrelationKey> getCorrelationKeies() {
        if (correlationKeies == null) {
            correlationKeies = new ArrayList<CorrelationKey>();
        }
        return this.correlationKeies;
    }

    /**
     * Gets the value of the initiatingParticipantRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getInitiatingParticipantRef() {
        return initiatingParticipantRef;
    }

    /**
     * Sets the value of the initiatingParticipantRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setInitiatingParticipantRef(QName value) {
        this.initiatingParticipantRef = value;
    }

    /**
     * Gets the value of the loopType property.
     * 
     * @return
     *     possible object is
     *     {@link TChoreographyLoopType }
     *     
     */
    public TChoreographyLoopType getLoopType() {
        if (loopType == null) {
            return TChoreographyLoopType.NONE;
        } else {
            return loopType;
        }
    }

    /**
     * Sets the value of the loopType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TChoreographyLoopType }
     *     
     */
    public void setLoopType(TChoreographyLoopType value) {
        this.loopType = value;
    }

}
