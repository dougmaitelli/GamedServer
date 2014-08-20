package com.gamedserver.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.avaje.ebean.Ebean;

@Entity  
@Table(name="achievements")
public class Badge {

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "application_id")
	private Application application;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column
	private Integer reqPoints;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
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

	public Integer getReqPoints() {
		return reqPoints;
	}
	
	public void setReqPoints(Integer reqPoints) {
		this.reqPoints = reqPoints;
	}
	
	public static Badge getBadge(String appId, int badgeId) {
		Badge badge = Ebean.find(Badge.class).where().eq("application.appId", appId).eq("id", badgeId).findUnique();
		
		return badge;
	}
	
	public static List<Badge> getAllBadges(String appId) {
		List<Badge> badges = Ebean.find(Badge.class).where().eq("application.appId", appId).findList();
		
		return badges;
	}
}
