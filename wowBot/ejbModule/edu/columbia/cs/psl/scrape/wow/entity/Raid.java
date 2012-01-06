package edu.columbia.cs.psl.scrape.wow.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Raid {
	private int id;
	private String name;
	private List<RaidBoss> bosses;
	
	@Id
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
	@XmlTransient
	@OneToMany(mappedBy="raid")
	public List<RaidBoss> getBosses() {
		return bosses;
	}
	public void setBosses(List<RaidBoss> bosses) {
		this.bosses = bosses;
	}
}
