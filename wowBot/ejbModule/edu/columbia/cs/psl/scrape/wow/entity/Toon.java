package edu.columbia.cs.psl.scrape.wow.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Toon {
	private int id;
	private String name;
	private Date updated;
	private Server server;
	private Guild guild;
	private int pClass;
	private int pRace;
	private int pGender;
	private int pLevel;
	private int gRank;
	private int source;
	private int explored;
	private List<AchievementProgress> progress;
	private List<Achievement> achievements;
	private List<Quest> questsDone;
	private List<ReputationStanding> reputation;
	private List<Title> titles;
	private Title selectedTitle;
	private Profession prof1;
	private Profession prof2;
	private int firstAidLevel;
	private int archaelogyLevel;
	private int fishingLevel;
	private int cookingLevel;
	private List<Spell> mounts;
	private List<Spell> companions;
	private int achievementPoints;
	private int prof1Level;
	private int prof2Level;
	private List<RaidProgress> raidProgress;
	private List<RaidBossProgress> raidBossProgress;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public Guild getGuild() {
		return guild;
	}
	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	public int getpClass() {
		return pClass;
	}
	public void setpClass(int pClass) {
		this.pClass = pClass;
	}
	public int getpRace() {
		return pRace;
	}
	public void setpRace(int pRace) {
		this.pRace = pRace;
	}
	public int getpGender() {
		return pGender;
	}
	public void setpGender(int pGender) {
		this.pGender = pGender;
	}
	public int getpLevel() {
		return pLevel;
	}
	public void setpLevel(int pLevel) {
		this.pLevel = pLevel;
	}
	public int getgRank() {
		return gRank;
	}
	public void setgRank(int gRank) {
		this.gRank = gRank;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	@OneToMany(mappedBy="toon")
	public List<AchievementProgress> getProgress() {
		return progress;
	}
	public void setProgress(List<AchievementProgress> progress) {
		this.progress = progress;
	}
	@ManyToMany
	public List<Achievement> getAchievements() {
		return achievements;
	}
	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}
	@ManyToMany
	public List<Quest> getQuestsDone() {
		return questsDone;
	}
	public void setQuestsDone(List<Quest> questsDone) {
		this.questsDone = questsDone;
	}
	@OneToMany(mappedBy="toon")
	public List<ReputationStanding> getReputation() {
		return reputation;
	}
	public void setReputation(List<ReputationStanding> reputation) {
		this.reputation = reputation;
	}
	@OneToMany(mappedBy="toon")
	public List<RaidBossProgress> getRaidBossProgress() {
		return raidBossProgress;
	}
	public void setRaidBossProgress(List<RaidBossProgress> raidBossProgress) {
		this.raidBossProgress = raidBossProgress;
	}
	@OneToMany(mappedBy="toon")
	public List<RaidProgress> getRaidProgress() {
		return raidProgress;
	}
	public void setRaidProgress(List<RaidProgress> raidProgress) {
		this.raidProgress = raidProgress;
	}
	public List<Title> getTitles() {
		return titles;
	}
	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}
	public Title getSelectedTitle() {
		return selectedTitle;
	}
	public void setSelectedTitle(Title selectedTitle) {
		this.selectedTitle = selectedTitle;
	}
	public Profession getProf1() {
		return prof1;
	}
	public void setProf1(Profession prof1) {
		this.prof1 = prof1;
	}
	public Profession getProf2() {
		return prof2;
	}
	public void setProf2(Profession prof2) {
		this.prof2 = prof2;
	}
	public int getFirstAidLevel() {
		return firstAidLevel;
	}
	public void setFirstAidLevel(int firstAidLevel) {
		this.firstAidLevel = firstAidLevel;
	}
	public int getArchaelogyLevel() {
		return archaelogyLevel;
	}
	public void setArchaelogyLevel(int archaelogyLevel) {
		this.archaelogyLevel = archaelogyLevel;
	}
	public int getFishingLevel() {
		return fishingLevel;
	}
	public void setFishingLevel(int fishingLevel) {
		this.fishingLevel = fishingLevel;
	}
	public int getCookingLevel() {
		return cookingLevel;
	}
	public void setCookingLevel(int cookingLevel) {
		this.cookingLevel = cookingLevel;
	}
	public int getExplored() {
		return explored;
	}
	public void setExplored(int explored) {
		this.explored = explored;
	}
	
	@OneToMany
	public List<Spell> getMounts() {
		return mounts;
	}
	public void setMounts(List<Spell> mounts) {
		this.mounts = mounts;
	}
	
	@OneToMany
	public List<Spell> getCompanions() {
		return companions;
	}
	public void setCompanions(List<Spell> companions) {
		this.companions = companions;
	}
	public int getAchievementPoints() {
		return achievementPoints;
	}
	public void setAchievementPoints(int achievementPoints) {
		this.achievementPoints = achievementPoints;
	}
	public int getProf1Level() {
		return prof1Level;
	}
	public void setProf1Level(int prof1Level) {
		this.prof1Level = prof1Level;
	}
	public int getProf2Level() {
		return prof2Level;
	}
	public void setProf2Level(int prof2Level) {
		this.prof2Level = prof2Level;
	}
}
