package edu.columbia.cs.psl.scrape.wow.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class ReputationStanding {
	private int id;
	private Toon toon;
	private ReputationSink sink;
	private int standing;
	private int value;
	private int max;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@XmlTransient
	@ManyToOne
	public Toon getToon() {
		return toon;
	}
	public void setToon(Toon toon) {
		this.toon = toon;
	}
	public ReputationSink getSink() {
		return sink;
	}
	public void setSink(ReputationSink sink) {
		this.sink = sink;
	}
	public int getStanding() {
		return standing;
	}
	public void setStanding(int standing) {
		this.standing = standing;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
}
