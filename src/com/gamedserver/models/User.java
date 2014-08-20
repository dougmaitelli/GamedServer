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
import javax.xml.bind.annotation.XmlTransient;

import com.avaje.ebean.Ebean;
import com.gamedserver.exceptions.InvalidUserException;
import com.gamedserver.exceptions.UnauthorizedException;
import com.gamedserver.util.Util;

@Entity  
@Table(name="users")  
public class User {
	
	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "application_id")
	private Application application;
	
	@Column
	private String user;
	
	@XmlTransient
	@Column
	private String password;
	
	@Column
	private String email;
	
	@Column
	private boolean emailVerified;
	
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
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public static Session loginUser(String loginUser, String password) throws UnauthorizedException, InvalidUserException {
		User user = Ebean.find(User.class).where().eq("user", loginUser).eq("password", password).findUnique();
		
		if (user == null) {
			throw new InvalidUserException();
		}
		
		Session session = new Session();
		session.setUser(user);
		session.setSessionId(Util.getRandomIdentifier());
		session.setToken(Util.getRandomIdentifier());
		session.setRevalidateToken(Util.getRandomIdentifier());
		session.setDate(new Date());
		
		Ebean.save(session);
		
		return session;
	}
	
	public static Session validateUser(String sessionId, String token) throws UnauthorizedException {
		Session session = Ebean.find(Session.class).where().eq("sessionId", sessionId).eq("token", token).findUnique();
		
		if (session == null || session.getUser() == null) {
			throw new UnauthorizedException();
		}
		
		return session;
	}
	
	public static User getUser(String loginUser) {
		User user = Ebean.find(User.class).where().eq("user", loginUser).findUnique();
		
		return user;
	}
	
	public static void logoutUser(Session session) {
		Ebean.delete(session);
	}
}
