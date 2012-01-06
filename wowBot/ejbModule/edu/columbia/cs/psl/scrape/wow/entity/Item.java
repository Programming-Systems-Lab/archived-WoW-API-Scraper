package edu.columbia.cs.psl.scrape.wow.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item {
	private int id;
	private int iClass;
	private String name;
	private int quality;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getiClass() {
		return iClass;
	}
	public void setiClass(int iClass) {
		this.iClass = iClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	
}
