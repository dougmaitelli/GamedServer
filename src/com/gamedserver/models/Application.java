package com.gamedserver.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.avaje.ebean.Ebean;
import com.gamedserver.exceptions.InvalidApplicationException;
import com.gamedserver.exceptions.UnauthorizedException;
import com.gamedserver.util.Util;

@Entity  
@Table(name="applications")  
public class Application {
	
	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String appId;
	
	@XmlTransient
	@Column
	private String apiKey;
	
	@Column
	private boolean requiresUserAuthentication;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public boolean isRequiresUserAuthentication() {
		return requiresUserAuthentication;
	}
	
	public void setRequiresUserAuthentication(boolean requiresUserAuthentication) {
		this.requiresUserAuthentication = requiresUserAuthentication;
	}
	
	public static Session initApplication(String appId, String apiKey) throws InvalidApplicationException {
		Application application = Ebean.find(Application.class).where().eq("appId", appId).eq("apiKey", apiKey).findUnique();
		
		if (application == null) {
			throw new InvalidApplicationException();
		}
		
		Session session = new Session();
		session.setApplication(application);
		session.setSessionId(Util.getRandomIdentifier());
		session.setToken(Util.getRandomIdentifier());
		session.setRevalidateToken(Util.getRandomIdentifier());
		session.setDate(new Date());
		
		Ebean.save(session);
		
		return session;
	}
	
	public static Session validateSession(String sessionId, String token) throws UnauthorizedException {
		Session session = Ebean.find(Session.class).where().eq("sessionId", sessionId).eq("token", token).findUnique();
		
		if (session == null || session.getApplication() == null) {
			throw new UnauthorizedException();
		}
		
		return session;
	}
	
	public static Application getApplication(String appId) {
		Application application = Ebean.find(Application.class).where().eq("appId", appId).findUnique();
		
		return application;
	}
	
	public static void endApplication(Session session) {
		Ebean.delete(session);
	}

}
