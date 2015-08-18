
package org.omg.spec.bpmn._20100524.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tInputOutputSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tInputOutputSpecification">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tBaseElement">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}dataInput" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}dataOutput" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}inputSet" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}outputSet" maxOccurs="unbounded"/>
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
@XmlType(name = "tInputOutputSpecification", propOrder = {
    "dataInputs",
    "dataOutputs",
    "inputSets",
    "outputSets"
})
@XmlRootElement(name = "ioSpecification")
public class IoSpecification
    extends TBaseElement
{

    @XmlElement(name = "dataInput")
    protected List<DataInput> dataInputs;
    @XmlElement(name = "dataOutput")
    protected List<DataOutput> dataOutputs;
    @XmlElement(name = "inputSet", required = true)
    protected List<InputSet> inputSets;
    @XmlElement(name = "outputSet", required = true)
    protected List<OutputSet> outputSets;

    /**
     * Gets the value of the dataInputs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataInputs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataInputs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataInput }
     * 
     * 
     */
    public List<DataInput> getDataInputs() {
        if (dataInputs == null) {
            dataInputs = new ArrayList<DataInput>();
        }
        return this.dataInputs;
    }

    /**
     * Gets the value of the dataOutputs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataOutputs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataOutputs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataOutput }
     * 
     * 
     */
    public List<DataOutput> getDataOutputs() {
        if (dataOutputs == null) {
            dataOutputs = new ArrayList<DataOutput>();
        }
        return this.dataOutputs;
    }

    /**
     * Gets the value of the inputSets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inputSets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInputSets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputSet }
     * 
     * 
     */
    public List<InputSet> getInputSets() {
        if (inputSets == null) {
            inputSets = new ArrayList<InputSet>();
        }
        return this.inputSets;
    }

    /**
     * Gets the value of the outputSets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outputSets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutputSets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutputSet }
     * 
     * 
     */
    public List<OutputSet> getOutputSets() {
        if (outputSets == null) {
            outputSets = new ArrayList<OutputSet>();
        }
        return this.outputSets;
    }

}
