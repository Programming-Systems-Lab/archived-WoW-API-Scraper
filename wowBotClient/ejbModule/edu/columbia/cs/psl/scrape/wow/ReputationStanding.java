
package edu.columbia.cs.psl.scrape.wow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reputationStanding complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reputationStanding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sink" type="{http://wow.scrape.psl.cs.columbia.edu/}reputationSink" minOccurs="0"/>
 *         &lt;element name="standing" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reputationStanding", propOrder = {
    "id",
    "max",
    "sink",
    "standing",
    "value"
})
public class ReputationStanding {

    protected int id;
    protected int max;
    protected ReputationSink sink;
    protected int standing;
    protected int value;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the max property.
     * 
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     */
    public void setMax(int value) {
        this.max = value;
    }

    /**
     * Gets the value of the sink property.
     * 
     * @return
     *     possible object is
     *     {@link ReputationSink }
     *     
     */
    public ReputationSink getSink() {
        return sink;
    }

    /**
     * Sets the value of the sink property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReputationSink }
     *     
     */
    public void setSink(ReputationSink value) {
        this.sink = value;
    }

    /**
     * Gets the value of the standing property.
     * 
     */
    public int getStanding() {
        return standing;
    }

    /**
     * Sets the value of the standing property.
     * 
     */
    public void setStanding(int value) {
        this.standing = value;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(int value) {
        this.value = value;
    }

}
