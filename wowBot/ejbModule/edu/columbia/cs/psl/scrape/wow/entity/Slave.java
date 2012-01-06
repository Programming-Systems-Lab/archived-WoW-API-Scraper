package edu.columbia.cs.psl.scrape.wow.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Slave {
	private long ip;
	private String name;
	private int requestsToday;
	private Date requestsTodayDate;
	private Date lastUpdated;
	
	@Id
	public long getIp() {
		return ip;
	}
	public void setIp(long ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRequestsToday() {
		return requestsToday;
	}
	public void setRequestsToday(int requestsToday) {
		this.requestsToday = requestsToday;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRequestsTodayDate() {
		return requestsTodayDate;
	}
	public void setRequestsTodayDate(Date requestsTodayDate) {
		this.requestsTodayDate = requestsTodayDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
