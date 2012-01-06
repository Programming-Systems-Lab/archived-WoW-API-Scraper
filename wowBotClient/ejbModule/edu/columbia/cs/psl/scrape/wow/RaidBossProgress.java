
package edu.columbia.cs.psl.scrape.wow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for raidBossProgress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="raidBossProgress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="heroic" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="normal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="raidBoss" type="{http://wow.scrape.psl.cs.columbia.edu/}raidBoss" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "raidBossProgress", propOrder = {
    "heroic",
    "id",
    "normal",
    "raidBoss"
})
public class RaidBossProgress {

    protected int heroic;
    protected int id;
    protected int normal;
    protected RaidBoss raidBoss;

    /**
     * Gets the value of the heroic property.
     * 
     */
    public int getHeroic() {
        return heroic;
    }

    /**
     * Sets the value of the heroic property.
     * 
     */
    public void setHeroic(int value) {
        this.heroic = value;
    }

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
     * Gets the value of the normal property.
     * 
     */
    public int getNormal() {
        return normal;
    }

    /**
     * Sets the value of the normal property.
     * 
     */
    public void setNormal(int value) {
        this.normal = value;
    }

    /**
     * Gets the value of the raidBoss property.
     * 
     * @return
     *     possible object is
     *     {@link RaidBoss }
     *     
     */
    public RaidBoss getRaidBoss() {
        return raidBoss;
    }

    /**
     * Sets the value of the raidBoss property.
     * 
     * @param value
     *     allowed object is
     *     {@link RaidBoss }
     *     
     */
    public void setRaidBoss(RaidBoss value) {
        this.raidBoss = value;
    }

}
