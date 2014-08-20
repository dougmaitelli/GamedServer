package com.gamedserver.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import com.avaje.ebean.Ebean;

@Entity  
@Table(name="sessions")
public class Session {
	
	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "application_id")
	private Application application;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column
	private String sessionId;
	
	@Column
	private String token;
	
	@Column
	private String revalidateToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getRevalidateToken() {
		return revalidateToken;
	}

	public void setRevalidateToken(String revalidateToken) {
		this.revalidateToken = revalidateToken;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public static Session getSession(String sessionId) {
		Session session = Ebean.find(Session.class).where().eq("sessionId", sessionId).findUnique();
		
		return session;
	}
}
