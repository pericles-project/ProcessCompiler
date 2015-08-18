
package org.omg.spec.dd._20100524.di;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;


/**
 * <p>Java class for Plane complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Plane">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/DD/20100524/DI}Node">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/DD/20100524/DI}DiagramElement" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Plane", propOrder = {
    "diagramElements"
})
@XmlSeeAlso({
    BPMNPlane.class
})
public abstract class Plane
    extends Node
{

    @XmlElementRef(name = "DiagramElement", namespace = "http://www.omg.org/spec/DD/20100524/DI", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends DiagramElement>> diagramElements;

    /**
     * Gets the value of the diagramElements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diagramElements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiagramElements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link DiagramElement }{@code >}
     * {@link JAXBElement }{@code <}{@link BPMNEdge }{@code >}
     * {@link JAXBElement }{@code <}{@link BPMNShape }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends DiagramElement>> getDiagramElements() {
        if (diagramElements == null) {
            diagramElements = new ArrayList<JAXBElement<? extends DiagramElement>>();
        }
        return this.diagramElements;
    }

}
