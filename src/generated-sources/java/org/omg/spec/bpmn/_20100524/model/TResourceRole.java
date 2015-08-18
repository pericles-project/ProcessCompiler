
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
 * <p>Java class for tResourceRole complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tResourceRole">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tBaseElement">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="resourceRef" type="{http://www.w3.org/2001/XMLSchema}QName"/>
 *           &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}resourceParameterBinding" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}resourceAssignmentExpression" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tResourceRole", propOrder = {
    "resourceAssignmentExpression",
    "resourceRef",
    "resourceParameterBindings"
})
@XmlSeeAlso({
    TPerformer.class
})
public class TResourceRole
    extends TBaseElement
{

    protected ResourceAssignmentExpression resourceAssignmentExpression;
    protected QName resourceRef;
    @XmlElement(name = "resourceParameterBinding")
    protected List<ResourceParameterBinding> resourceParameterBindings;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the resourceAssignmentExpression property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceAssignmentExpression }
     *     
     */
    public ResourceAssignmentExpression getResourceAssignmentExpression() {
        return resourceAssignmentExpression;
    }

    /**
     * Sets the value of the resourceAssignmentExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceAssignmentExpression }
     *     
     */
    public void setResourceAssignmentExpression(ResourceAssignmentExpression value) {
        this.resourceAssignmentExpression = value;
    }

    /**
     * Gets the value of the resourceRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getResourceRef() {
        return resourceRef;
    }

    /**
     * Sets the value of the resourceRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setResourceRef(QName value) {
        this.resourceRef = value;
    }

    /**
     * Gets the value of the resourceParameterBindings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceParameterBindings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceParameterBindings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceParameterBinding }
     * 
     * 
     */
    public List<ResourceParameterBinding> getResourceParameterBindings() {
        if (resourceParameterBindings == null) {
            resourceParameterBindings = new ArrayList<ResourceParameterBinding>();
        }
        return this.resourceParameterBindings;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
