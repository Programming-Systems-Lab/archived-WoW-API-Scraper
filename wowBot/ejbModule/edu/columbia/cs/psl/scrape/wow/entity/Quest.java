package edu.columbia.cs.psl.scrape.wow.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Quest {
	private int id;
	private boolean haveData;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isHaveData() {
		return haveData;
	}
	public void setHaveData(boolean haveData) {
		this.haveData = haveData;
	}
}
