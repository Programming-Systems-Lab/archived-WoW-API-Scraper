package edu.columbia.cs.psl.scrape.wow.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Achievement implements Serializable {

	private static final long serialVersionUID = -2196723062283400783L;
	private int id;
	private int category;
	private int points;
	private String title;
	private String description;
	private Item rewardItem;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Item getRewardItem() {
		return rewardItem;
	}
	public void setRewardItem(Item rewardItem) {
		this.rewardItem = rewardItem;
	}
	
}
