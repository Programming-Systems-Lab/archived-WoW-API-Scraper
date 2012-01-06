
package edu.columbia.cs.psl.scrape.wow;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.columbia.cs.psl.scrape.wow package. 
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

    private final static QName _GetMyIP_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getMyIP");
    private final static QName _ProcessToon_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "processToon");
    private final static QName _UpdateGuildStatusResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateGuildStatusResponse");
    private final static QName _UpdateToonStatusResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateToonStatusResponse");
    private final static QName _UpdateSlaveCount_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateSlaveCount");
    private final static QName _ProcessGuild_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "processGuild");
    private final static QName _ProcessGuildResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "processGuildResponse");
    private final static QName _UpdateGuildStatus_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateGuildStatus");
    private final static QName _GetToonName_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getToonName");
    private final static QName _GetGuildToScrapeResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getGuildToScrapeResponse");
    private final static QName _Register_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "register");
    private final static QName _UpdateSlaveCountResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateSlaveCountResponse");
    private final static QName _UpdateToonStatus_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "updateToonStatus");
    private final static QName _GetGuildToScrape_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getGuildToScrape");
    private final static QName _RegisterResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "registerResponse");
    private final static QName _GetToonToScrapeResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getToonToScrapeResponse");
    private final static QName _ProcessToonResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "processToonResponse");
    private final static QName _GetMyIPResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getMyIPResponse");
    private final static QName _GetToonNameResponse_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getToonNameResponse");
    private final static QName _GetToonToScrape_QNAME = new QName("http://wow.scrape.psl.cs.columbia.edu/", "getToonToScrape");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.columbia.cs.psl.scrape.wow
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetToonName }
     * 
     */
    public GetToonName createGetToonName() {
        return new GetToonName();
    }

    /**
     * Create an instance of {@link GetToonNameResponse }
     * 
     */
    public GetToonNameResponse createGetToonNameResponse() {
        return new GetToonNameResponse();
    }

    /**
     * Create an instance of {@link RaidProgress }
     * 
     */
    public RaidProgress createRaidProgress() {
        return new RaidProgress();
    }

    /**
     * Create an instance of {@link Guild }
     * 
     */
    public Guild createGuild() {
        return new Guild();
    }

    /**
     * Create an instance of {@link UpdateGuildStatus }
     * 
     */
    public UpdateGuildStatus createUpdateGuildStatus() {
        return new UpdateGuildStatus();
    }

    /**
     * Create an instance of {@link Raid }
     * 
     */
    public Raid createRaid() {
        return new Raid();
    }

    /**
     * Create an instance of {@link RaidBoss }
     * 
     */
    public RaidBoss createRaidBoss() {
        return new RaidBoss();
    }

    /**
     * Create an instance of {@link Quest }
     * 
     */
    public Quest createQuest() {
        return new Quest();
    }

    /**
     * Create an instance of {@link GetToonToScrape }
     * 
     */
    public GetToonToScrape createGetToonToScrape() {
        return new GetToonToScrape();
    }

    /**
     * Create an instance of {@link UpdateGuildStatusResponse }
     * 
     */
    public UpdateGuildStatusResponse createUpdateGuildStatusResponse() {
        return new UpdateGuildStatusResponse();
    }

    /**
     * Create an instance of {@link Profession }
     * 
     */
    public Profession createProfession() {
        return new Profession();
    }

    /**
     * Create an instance of {@link ProcessGuild }
     * 
     */
    public ProcessGuild createProcessGuild() {
        return new ProcessGuild();
    }

    /**
     * Create an instance of {@link Toon }
     * 
     */
    public Toon createToon() {
        return new Toon();
    }

    /**
     * Create an instance of {@link Achievement }
     * 
     */
    public Achievement createAchievement() {
        return new Achievement();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link RaidBossProgress }
     * 
     */
    public RaidBossProgress createRaidBossProgress() {
        return new RaidBossProgress();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link Server }
     * 
     */
    public Server createServer() {
        return new Server();
    }

    /**
     * Create an instance of {@link GetMyIP }
     * 
     */
    public GetMyIP createGetMyIP() {
        return new GetMyIP();
    }

    /**
     * Create an instance of {@link AchievementProgress }
     * 
     */
    public AchievementProgress createAchievementProgress() {
        return new AchievementProgress();
    }

    /**
     * Create an instance of {@link ProcessToonResponse }
     * 
     */
    public ProcessToonResponse createProcessToonResponse() {
        return new ProcessToonResponse();
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link ReputationStanding }
     * 
     */
    public ReputationStanding createReputationStanding() {
        return new ReputationStanding();
    }

    /**
     * Create an instance of {@link ProcessToon }
     * 
     */
    public ProcessToon createProcessToon() {
        return new ProcessToon();
    }

    /**
     * Create an instance of {@link ProcessGuildResponse }
     * 
     */
    public ProcessGuildResponse createProcessGuildResponse() {
        return new ProcessGuildResponse();
    }

    /**
     * Create an instance of {@link GetGuildToScrapeResponse }
     * 
     */
    public GetGuildToScrapeResponse createGetGuildToScrapeResponse() {
        return new GetGuildToScrapeResponse();
    }

    /**
     * Create an instance of {@link UpdateSlaveCountResponse }
     * 
     */
    public UpdateSlaveCountResponse createUpdateSlaveCountResponse() {
        return new UpdateSlaveCountResponse();
    }

    /**
     * Create an instance of {@link UpdateToonStatusResponse }
     * 
     */
    public UpdateToonStatusResponse createUpdateToonStatusResponse() {
        return new UpdateToonStatusResponse();
    }

    /**
     * Create an instance of {@link GetGuildToScrape }
     * 
     */
    public GetGuildToScrape createGetGuildToScrape() {
        return new GetGuildToScrape();
    }

    /**
     * Create an instance of {@link GetToonToScrapeResponse }
     * 
     */
    public GetToonToScrapeResponse createGetToonToScrapeResponse() {
        return new GetToonToScrapeResponse();
    }

    /**
     * Create an instance of {@link ReputationSink }
     * 
     */
    public ReputationSink createReputationSink() {
        return new ReputationSink();
    }

    /**
     * Create an instance of {@link UpdateSlaveCount }
     * 
     */
    public UpdateSlaveCount createUpdateSlaveCount() {
        return new UpdateSlaveCount();
    }

    /**
     * Create an instance of {@link Spell }
     * 
     */
    public Spell createSpell() {
        return new Spell();
    }

    /**
     * Create an instance of {@link GetMyIPResponse }
     * 
     */
    public GetMyIPResponse createGetMyIPResponse() {
        return new GetMyIPResponse();
    }

    /**
     * Create an instance of {@link UpdateToonStatus }
     * 
     */
    public UpdateToonStatus createUpdateToonStatus() {
        return new UpdateToonStatus();
    }

    /**
     * Create an instance of {@link Title }
     * 
     */
    public Title createTitle() {
        return new Title();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyIP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getMyIP")
    public JAXBElement<GetMyIP> createGetMyIP(GetMyIP value) {
        return new JAXBElement<GetMyIP>(_GetMyIP_QNAME, GetMyIP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessToon }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "processToon")
    public JAXBElement<ProcessToon> createProcessToon(ProcessToon value) {
        return new JAXBElement<ProcessToon>(_ProcessToon_QNAME, ProcessToon.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateGuildStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateGuildStatusResponse")
    public JAXBElement<UpdateGuildStatusResponse> createUpdateGuildStatusResponse(UpdateGuildStatusResponse value) {
        return new JAXBElement<UpdateGuildStatusResponse>(_UpdateGuildStatusResponse_QNAME, UpdateGuildStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateToonStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateToonStatusResponse")
    public JAXBElement<UpdateToonStatusResponse> createUpdateToonStatusResponse(UpdateToonStatusResponse value) {
        return new JAXBElement<UpdateToonStatusResponse>(_UpdateToonStatusResponse_QNAME, UpdateToonStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSlaveCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateSlaveCount")
    public JAXBElement<UpdateSlaveCount> createUpdateSlaveCount(UpdateSlaveCount value) {
        return new JAXBElement<UpdateSlaveCount>(_UpdateSlaveCount_QNAME, UpdateSlaveCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessGuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "processGuild")
    public JAXBElement<ProcessGuild> createProcessGuild(ProcessGuild value) {
        return new JAXBElement<ProcessGuild>(_ProcessGuild_QNAME, ProcessGuild.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessGuildResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "processGuildResponse")
    public JAXBElement<ProcessGuildResponse> createProcessGuildResponse(ProcessGuildResponse value) {
        return new JAXBElement<ProcessGuildResponse>(_ProcessGuildResponse_QNAME, ProcessGuildResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateGuildStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateGuildStatus")
    public JAXBElement<UpdateGuildStatus> createUpdateGuildStatus(UpdateGuildStatus value) {
        return new JAXBElement<UpdateGuildStatus>(_UpdateGuildStatus_QNAME, UpdateGuildStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetToonName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getToonName")
    public JAXBElement<GetToonName> createGetToonName(GetToonName value) {
        return new JAXBElement<GetToonName>(_GetToonName_QNAME, GetToonName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGuildToScrapeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getGuildToScrapeResponse")
    public JAXBElement<GetGuildToScrapeResponse> createGetGuildToScrapeResponse(GetGuildToScrapeResponse value) {
        return new JAXBElement<GetGuildToScrapeResponse>(_GetGuildToScrapeResponse_QNAME, GetGuildToScrapeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<Register>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSlaveCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateSlaveCountResponse")
    public JAXBElement<UpdateSlaveCountResponse> createUpdateSlaveCountResponse(UpdateSlaveCountResponse value) {
        return new JAXBElement<UpdateSlaveCountResponse>(_UpdateSlaveCountResponse_QNAME, UpdateSlaveCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateToonStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "updateToonStatus")
    public JAXBElement<UpdateToonStatus> createUpdateToonStatus(UpdateToonStatus value) {
        return new JAXBElement<UpdateToonStatus>(_UpdateToonStatus_QNAME, UpdateToonStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGuildToScrape }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getGuildToScrape")
    public JAXBElement<GetGuildToScrape> createGetGuildToScrape(GetGuildToScrape value) {
        return new JAXBElement<GetGuildToScrape>(_GetGuildToScrape_QNAME, GetGuildToScrape.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<RegisterResponse>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetToonToScrapeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getToonToScrapeResponse")
    public JAXBElement<GetToonToScrapeResponse> createGetToonToScrapeResponse(GetToonToScrapeResponse value) {
        return new JAXBElement<GetToonToScrapeResponse>(_GetToonToScrapeResponse_QNAME, GetToonToScrapeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessToonResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "processToonResponse")
    public JAXBElement<ProcessToonResponse> createProcessToonResponse(ProcessToonResponse value) {
        return new JAXBElement<ProcessToonResponse>(_ProcessToonResponse_QNAME, ProcessToonResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyIPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getMyIPResponse")
    public JAXBElement<GetMyIPResponse> createGetMyIPResponse(GetMyIPResponse value) {
        return new JAXBElement<GetMyIPResponse>(_GetMyIPResponse_QNAME, GetMyIPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetToonNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getToonNameResponse")
    public JAXBElement<GetToonNameResponse> createGetToonNameResponse(GetToonNameResponse value) {
        return new JAXBElement<GetToonNameResponse>(_GetToonNameResponse_QNAME, GetToonNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetToonToScrape }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wow.scrape.psl.cs.columbia.edu/", name = "getToonToScrape")
    public JAXBElement<GetToonToScrape> createGetToonToScrape(GetToonToScrape value) {
        return new JAXBElement<GetToonToScrape>(_GetToonToScrape_QNAME, GetToonToScrape.class, null, value);
    }

}
