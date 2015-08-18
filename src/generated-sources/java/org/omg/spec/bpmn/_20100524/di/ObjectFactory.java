
package org.omg.spec.bpmn._20100524.di;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.omg.spec.bpmn._20100524.di package. 
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

    private final static QName _BPMNEdge_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNEdge");
    private final static QName _BPMNShape_QNAME = new QName("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNShape");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.omg.spec.bpmn._20100524.di
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BPMNDiagram }
     * 
     */
    public BPMNDiagram createBPMNDiagram() {
        return new BPMNDiagram();
    }

    /**
     * Create an instance of {@link BPMNPlane }
     * 
     */
    public BPMNPlane createBPMNPlane() {
        return new BPMNPlane();
    }

    /**
     * Create an instance of {@link BPMNLabelStyle }
     * 
     */
    public BPMNLabelStyle createBPMNLabelStyle() {
        return new BPMNLabelStyle();
    }

    /**
     * Create an instance of {@link BPMNLabel }
     * 
     */
    public BPMNLabel createBPMNLabel() {
        return new BPMNLabel();
    }

    /**
     * Create an instance of {@link BPMNEdge }
     * 
     */
    public BPMNEdge createBPMNEdge() {
        return new BPMNEdge();
    }

    /**
     * Create an instance of {@link BPMNShape }
     * 
     */
    public BPMNShape createBPMNShape() {
        return new BPMNShape();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BPMNEdge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/DI", name = "BPMNEdge", substitutionHeadNamespace = "http://www.omg.org/spec/DD/20100524/DI", substitutionHeadName = "DiagramElement")
    public JAXBElement<BPMNEdge> createBPMNEdge(BPMNEdge value) {
        return new JAXBElement<BPMNEdge>(_BPMNEdge_QNAME, BPMNEdge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BPMNShape }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.omg.org/spec/BPMN/20100524/DI", name = "BPMNShape", substitutionHeadNamespace = "http://www.omg.org/spec/DD/20100524/DI", substitutionHeadName = "DiagramElement")
    public JAXBElement<BPMNShape> createBPMNShape(BPMNShape value) {
        return new JAXBElement<BPMNShape>(_BPMNShape_QNAME, BPMNShape.class, null, value);
    }

}
