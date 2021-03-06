package edu.columbia.cs.psl.scrape.wow.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class RaidBossProgress {
	private int id;
	private RaidBoss raidBoss;
	private Toon toon;
	private int normal;
	private int heroic;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public RaidBoss getRaidBoss() {
		return raidBoss;
	}
	public void setRaidBoss(RaidBoss raidBoss) {
		this.raidBoss = raidBoss;
	}
	@XmlTransient
	@ManyToOne
	public Toon getToon() {
		return toon;
	}
	public void setToon(Toon toon) {
		this.toon = toon;
	}
	public int getNormal() {
		return normal;
	}
	public void setNormal(int normal) {
		this.normal = normal;
	}
	public int getHeroic() {
		return heroic;
	}
	public void setHeroic(int heroic) {
		this.heroic = heroic;
	}
	
}
