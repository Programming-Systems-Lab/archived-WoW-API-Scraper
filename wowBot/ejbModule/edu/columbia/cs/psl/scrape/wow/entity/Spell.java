package edu.columbia.cs.psl.scrape.wow.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Spell {
	private int id;
	private String name;
	private boolean mount;
	private boolean companion;
	
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
	public boolean isMount() {
		return mount;
	}
	public void setMount(boolean mount) {
		this.mount = mount;
	}
	public boolean isCompanion() {
		return companion;
	}
	public void setCompanion(boolean companion) {
		this.companion = companion;
	}
}
