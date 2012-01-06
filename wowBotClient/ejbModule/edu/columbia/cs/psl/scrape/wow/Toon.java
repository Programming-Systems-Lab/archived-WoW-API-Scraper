
package edu.columbia.cs.psl.scrape.wow;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for toon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="toon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="achievementPoints" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="achievements" type="{http://wow.scrape.psl.cs.columbia.edu/}achievement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="archaelogyLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="companions" type="{http://wow.scrape.psl.cs.columbia.edu/}spell" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cookingLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="explored" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="firstAidLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fishingLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="guild" type="{http://wow.scrape.psl.cs.columbia.edu/}guild" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mounts" type="{http://wow.scrape.psl.cs.columbia.edu/}spell" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prof1" type="{http://wow.scrape.psl.cs.columbia.edu/}profession" minOccurs="0"/>
 *         &lt;element name="prof1Level" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="prof2" type="{http://wow.scrape.psl.cs.columbia.edu/}profession" minOccurs="0"/>
 *         &lt;element name="prof2Level" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="progress" type="{http://wow.scrape.psl.cs.columbia.edu/}achievementProgress" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="questsDone" type="{http://wow.scrape.psl.cs.columbia.edu/}quest" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="raidBossProgress" type="{http://wow.scrape.psl.cs.columbia.edu/}raidBossProgress" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="raidProgress" type="{http://wow.scrape.psl.cs.columbia.edu/}raidProgress" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reputation" type="{http://wow.scrape.psl.cs.columbia.edu/}reputationStanding" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="selectedTitle" type="{http://wow.scrape.psl.cs.columbia.edu/}title" minOccurs="0"/>
 *         &lt;element name="server" type="{http://wow.scrape.psl.cs.columbia.edu/}server" minOccurs="0"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="titles" type="{http://wow.scrape.psl.cs.columbia.edu/}title" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="updated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="gRank" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pClass" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pGender" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pRace" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "toon", propOrder = {
    "achievementPoints",
    "achievements",
    "archaelogyLevel",
    "companions",
    "cookingLevel",
    "explored",
    "firstAidLevel",
    "fishingLevel",
    "guild",
    "id",
    "mounts",
    "name",
    "prof1",
    "prof1Level",
    "prof2",
    "prof2Level",
    "progress",
    "questsDone",
    "raidBossProgress",
    "raidProgress",
    "reputation",
    "selectedTitle",
    "server",
    "source",
    "titles",
    "updated",
    "gRank",
    "pClass",
    "pGender",
    "pLevel",
    "pRace"
})
public class Toon {

    protected int achievementPoints;
    @XmlElement(nillable = true)
    protected List<Achievement> achievements;
    protected int archaelogyLevel;
    @XmlElement(nillable = true)
    protected List<Spell> companions;
    protected int cookingLevel;
    protected int explored;
    protected int firstAidLevel;
    protected int fishingLevel;
    protected Guild guild;
    protected int id;
    @XmlElement(nillable = true)
    protected List<Spell> mounts;
    protected String name;
    protected Profession prof1;
    protected int prof1Level;
    protected Profession prof2;
    protected int prof2Level;
    @XmlElement(nillable = true)
    protected List<AchievementProgress> progress;
    @XmlElement(nillable = true)
    protected List<Quest> questsDone;
    @XmlElement(nillable = true)
    protected List<RaidBossProgress> raidBossProgress;
    @XmlElement(nillable = true)
    protected List<RaidProgress> raidProgress;
    @XmlElement(nillable = true)
    protected List<ReputationStanding> reputation;
    protected Title selectedTitle;
    protected Server server;
    protected int source;
    @XmlElement(nillable = true)
    protected List<Title> titles;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updated;
    protected int gRank;
    protected int pClass;
    protected int pGender;
    protected int pLevel;
    protected int pRace;

    /**
     * Gets the value of the achievementPoints property.
     * 
     */
    public int getAchievementPoints() {
        return achievementPoints;
    }

    /**
     * Sets the value of the achievementPoints property.
     * 
     */
    public void setAchievementPoints(int value) {
        this.achievementPoints = value;
    }

    /**
     * Gets the value of the achievements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the achievements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAchievements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Achievement }
     * 
     * 
     */
    public List<Achievement> getAchievements() {
        if (achievements == null) {
            achievements = new ArrayList<Achievement>();
        }
        return this.achievements;
    }

    /**
     * Gets the value of the archaelogyLevel property.
     * 
     */
    public int getArchaelogyLevel() {
        return archaelogyLevel;
    }

    /**
     * Sets the value of the archaelogyLevel property.
     * 
     */
    public void setArchaelogyLevel(int value) {
        this.archaelogyLevel = value;
    }

    /**
     * Gets the value of the companions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the companions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompanions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Spell }
     * 
     * 
     */
    public List<Spell> getCompanions() {
        if (companions == null) {
            companions = new ArrayList<Spell>();
        }
        return this.companions;
    }

    /**
     * Gets the value of the cookingLevel property.
     * 
     */
    public int getCookingLevel() {
        return cookingLevel;
    }

    /**
     * Sets the value of the cookingLevel property.
     * 
     */
    public void setCookingLevel(int value) {
        this.cookingLevel = value;
    }

    /**
     * Gets the value of the explored property.
     * 
     */
    public int getExplored() {
        return explored;
    }

    /**
     * Sets the value of the explored property.
     * 
     */
    public void setExplored(int value) {
        this.explored = value;
    }

    /**
     * Gets the value of the firstAidLevel property.
     * 
     */
    public int getFirstAidLevel() {
        return firstAidLevel;
    }

    /**
     * Sets the value of the firstAidLevel property.
     * 
     */
    public void setFirstAidLevel(int value) {
        this.firstAidLevel = value;
    }

    /**
     * Gets the value of the fishingLevel property.
     * 
     */
    public int getFishingLevel() {
        return fishingLevel;
    }

    /**
     * Sets the value of the fishingLevel property.
     * 
     */
    public void setFishingLevel(int value) {
        this.fishingLevel = value;
    }

    /**
     * Gets the value of the guild property.
     * 
     * @return
     *     possible object is
     *     {@link Guild }
     *     
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * Sets the value of the guild property.
     * 
     * @param value
     *     allowed object is
     *     {@link Guild }
     *     
     */
    public void setGuild(Guild value) {
        this.guild = value;
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
     * Gets the value of the mounts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mounts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMounts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Spell }
     * 
     * 
     */
    public List<Spell> getMounts() {
        if (mounts == null) {
            mounts = new ArrayList<Spell>();
        }
        return this.mounts;
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

    /**
     * Gets the value of the prof1 property.
     * 
     * @return
     *     possible object is
     *     {@link Profession }
     *     
     */
    public Profession getProf1() {
        return prof1;
    }

    /**
     * Sets the value of the prof1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Profession }
     *     
     */
    public void setProf1(Profession value) {
        this.prof1 = value;
    }

    /**
     * Gets the value of the prof1Level property.
     * 
     */
    public int getProf1Level() {
        return prof1Level;
    }

    /**
     * Sets the value of the prof1Level property.
     * 
     */
    public void setProf1Level(int value) {
        this.prof1Level = value;
    }

    /**
     * Gets the value of the prof2 property.
     * 
     * @return
     *     possible object is
     *     {@link Profession }
     *     
     */
    public Profession getProf2() {
        return prof2;
    }

    /**
     * Sets the value of the prof2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Profession }
     *     
     */
    public void setProf2(Profession value) {
        this.prof2 = value;
    }

    /**
     * Gets the value of the prof2Level property.
     * 
     */
    public int getProf2Level() {
        return prof2Level;
    }

    /**
     * Sets the value of the prof2Level property.
     * 
     */
    public void setProf2Level(int value) {
        this.prof2Level = value;
    }

    /**
     * Gets the value of the progress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the progress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AchievementProgress }
     * 
     * 
     */
    public List<AchievementProgress> getProgress() {
        if (progress == null) {
            progress = new ArrayList<AchievementProgress>();
        }
        return this.progress;
    }

    /**
     * Gets the value of the questsDone property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the questsDone property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestsDone().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Quest }
     * 
     * 
     */
    public List<Quest> getQuestsDone() {
        if (questsDone == null) {
            questsDone = new ArrayList<Quest>();
        }
        return this.questsDone;
    }

    /**
     * Gets the value of the raidBossProgress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the raidBossProgress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRaidBossProgress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RaidBossProgress }
     * 
     * 
     */
    public List<RaidBossProgress> getRaidBossProgress() {
        if (raidBossProgress == null) {
            raidBossProgress = new ArrayList<RaidBossProgress>();
        }
        return this.raidBossProgress;
    }

    /**
     * Gets the value of the raidProgress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the raidProgress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRaidProgress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RaidProgress }
     * 
     * 
     */
    public List<RaidProgress> getRaidProgress() {
        if (raidProgress == null) {
            raidProgress = new ArrayList<RaidProgress>();
        }
        return this.raidProgress;
    }

    /**
     * Gets the value of the reputation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reputation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReputation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReputationStanding }
     * 
     * 
     */
    public List<ReputationStanding> getReputation() {
        if (reputation == null) {
            reputation = new ArrayList<ReputationStanding>();
        }
        return this.reputation;
    }

    /**
     * Gets the value of the selectedTitle property.
     * 
     * @return
     *     possible object is
     *     {@link Title }
     *     
     */
    public Title getSelectedTitle() {
        return selectedTitle;
    }

    /**
     * Sets the value of the selectedTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Title }
     *     
     */
    public void setSelectedTitle(Title value) {
        this.selectedTitle = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link Server }
     *     
     */
    public Server getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link Server }
     *     
     */
    public void setServer(Server value) {
        this.server = value;
    }

    /**
     * Gets the value of the source property.
     * 
     */
    public int getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     */
    public void setSource(int value) {
        this.source = value;
    }

    /**
     * Gets the value of the titles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the titles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTitles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Title }
     * 
     * 
     */
    public List<Title> getTitles() {
        if (titles == null) {
            titles = new ArrayList<Title>();
        }
        return this.titles;
    }

    /**
     * Gets the value of the updated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdated(XMLGregorianCalendar value) {
        this.updated = value;
    }

    /**
     * Gets the value of the gRank property.
     * 
     */
    public int getGRank() {
        return gRank;
    }

    /**
     * Sets the value of the gRank property.
     * 
     */
    public void setGRank(int value) {
        this.gRank = value;
    }

    /**
     * Gets the value of the pClass property.
     * 
     */
    public int getPClass() {
        return pClass;
    }

    /**
     * Sets the value of the pClass property.
     * 
     */
    public void setPClass(int value) {
        this.pClass = value;
    }

    /**
     * Gets the value of the pGender property.
     * 
     */
    public int getPGender() {
        return pGender;
    }

    /**
     * Sets the value of the pGender property.
     * 
     */
    public void setPGender(int value) {
        this.pGender = value;
    }

    /**
     * Gets the value of the pLevel property.
     * 
     */
    public int getPLevel() {
        return pLevel;
    }

    /**
     * Sets the value of the pLevel property.
     * 
     */
    public void setPLevel(int value) {
        this.pLevel = value;
    }

    /**
     * Gets the value of the pRace property.
     * 
     */
    public int getPRace() {
        return pRace;
    }

    /**
     * Sets the value of the pRace property.
     * 
     */
    public void setPRace(int value) {
        this.pRace = value;
    }

}
