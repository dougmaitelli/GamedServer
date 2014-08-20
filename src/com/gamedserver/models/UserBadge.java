package com.gamedserver.models;

import java.util.Date;
import java.util.List;

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
import com.avaje.ebean.Expr;

@Entity  
@Table(name="user_achievements")
public class UserBadge {
	
	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "achievement_id")
	private Badge badge;
	
	@Column
	private Integer progress;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Badge getBadge() {
		return badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void addProgress(int progress) {
		if (this.getBadge().getReqPoints() != null) {
			if (this.getProgress() == null) {
				this.setProgress(0);
			}
			
			this.setProgress(Math.min(this.getBadge().getReqPoints(), this.getProgress() + progress));
		}
	}
	
	public void subProgress(int progress) {
		if (this.getBadge().getReqPoints() != null) {
			if (this.getProgress() == null) {
				this.setProgress(0);
			}
			
			this.setProgress(Math.min(0, this.getProgress() - progress));
		}
	}

	public static boolean userHasBadgeProgress(String loginUser, int badgeId) {
		boolean has = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).findRowCount() > 0;
		
		return has;
	}
	
	public static void badgeProgress(String loginUser, int badgeId, int progress) {
		UserBadge userBadge = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).findUnique();
		
		userBadge.addProgress(progress);
		Ebean.save(userBadge);
	}
	
	public static void badgeUnprogress(String loginUser, int badgeId, int progress) {
		UserBadge userBadge = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).findUnique();
		
		userBadge.subProgress(progress);
		Ebean.save(userBadge);
	}
	
	public static boolean userHasBadge(String loginUser, int badgeId) {
		boolean has = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).or(Expr.isNull("badge.reqPoints"), Expr.ge("progress", "badge.reqPoints")).findRowCount() > 0;
		
		return has;
	}
	
	public static void award(String loginUser, int badgeId) {
		User user = Ebean.find(User.class).where().eq("user", loginUser).findUnique();
		Badge badge = Ebean.find(Badge.class, badgeId);
		
		UserBadge userBadge = new UserBadge();
		userBadge.setUser(user);
		userBadge.setBadge(badge);
		Ebean.save(userBadge);
	}
	
	public static void revoke(String loginUser, int badgeId) {
		UserBadge userBadge = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).findUnique();
		
		Ebean.delete(userBadge);
	}
	
	public static UserBadge getUserBadge(String loginUser, int badgeId) {
		UserBadge userBadge = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).eq("badge.id", badgeId).findUnique();
		
		return userBadge;
	}
	
	public static List<UserBadge> getUserBadges(String loginUser) {
		List<UserBadge> badges = Ebean.find(UserBadge.class).where().eq("user.user", loginUser).findList();
		
		return badges;
	}
}
