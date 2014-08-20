package com.gamedserver;

import java.util.List;
import java.util.Scanner;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import com.gamedserver.exceptions.InvalidApplicationException;
import com.gamedserver.exceptions.InvalidUserException;
import com.gamedserver.exceptions.UnauthorizedException;
import com.gamedserver.models.Application;
import com.gamedserver.models.Badge;
import com.gamedserver.models.Session;
import com.gamedserver.models.User;
import com.gamedserver.models.UserBadge;
import com.gamedserver.util.DbUtil;

@WebService
public class GamedServer {
	
	@WebMethod
	public Session initApp(String appId, String apiKey) throws InvalidApplicationException {
		Session session = Application.initApplication(appId, apiKey);
		
		return session;
	}
	
	@WebMethod
	public void endApp(String appId, String sessionId, String token) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application.endApplication(session);
	}
	
	@WebMethod
	public Session loginUser(String appId, String sessionId, String token, String userId, String password) throws UnauthorizedException, InvalidUserException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (!app.isRequiresUserAuthentication()) {
			return null;
		}
		
		Session userSession = User.loginUser(userId, password);
		
		return userSession;
	}
	
	@WebMethod
	public User getUser(String appId, String sessionId, String token, String userId, String userSessionId, String userToken) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (!app.isRequiresUserAuthentication()) {
			return null;
		}
		
		Session userSession = User.validateUser(userSessionId, userToken);
		
		User user = userSession.getUser();
		
		return user;
	}
	
	@WebMethod
	public void logoutUser(String appId, String sessionId, String token, String userId, String userSessionId, String userToken) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (!app.isRequiresUserAuthentication()) {
			return;
		}
		
		Session userSession = User.validateUser(userSessionId, userToken);
		
		User.logoutUser(userSession);
	}
	
	@WebMethod
	public void badgeProgress(String appId, String sessionId, String token, String userId, String userSessionId, String userToken, int badgeId, int progress) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		if (UserBadge.userHasBadge(userId, badgeId)) {
			return;
		}
		
		UserBadge.badgeProgress(userId, badgeId, progress);
	}
	
	@WebMethod
	public void badgeUnprogress(String appId, String sessionId, String token, String userId, String userSessionId, String userToken, int badgeId, int progress) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		if (UserBadge.userHasBadgeProgress(userId, badgeId)) {
			UserBadge.badgeUnprogress(userId, badgeId, progress);
		}
	}

	@WebMethod
	public void awardBadge(String appId, String sessionId, String token, String userId, String userSessionId, String userToken, int badgeId) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		if (UserBadge.userHasBadge(userId, badgeId)) {
			return;
		}
		
		UserBadge.award(userId, badgeId);
	}
	
	@WebMethod
	public void revokeBadge(String appId, String sessionId, String token, String userId, String userSessionId, String userToken, int badgeId) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		if (UserBadge.userHasBadge(userId, badgeId)) {
			UserBadge.revoke(userId, badgeId);
		}
	}
	
	@WebMethod
	public Badge getBadge(String appId, String sessionId, String token, int badgeId) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		Badge badge = Badge.getBadge(app.getAppId(), badgeId);
		
		return badge;
	}
	
	@WebMethod
	public List<Badge> getAllBadges(String appId, String sessionId, String token) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		List<Badge> badges = Badge.getAllBadges(app.getAppId());
		
		return badges;
	}
	
	@WebMethod
	public UserBadge getUserBadge(String appId, String sessionId, String token, String userId, String userSessionId, String userToken, int badgeId) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		UserBadge userBadge = UserBadge.getUserBadge(userId, badgeId);
		
		return userBadge;
	}
	
	@WebMethod
	public List<UserBadge> getUserBadges(String appId, String sessionId, String token, String userId, String userSessionId, String userToken) throws UnauthorizedException {
		Session session = Application.validateSession(sessionId, token);
		
		Application app = session.getApplication();
		
		if (app.isRequiresUserAuthentication()) {
			Session userSession = User.validateUser(userSessionId, userToken);
			
			userId = userSession.getUser().getUser();
		}
		
		List<UserBadge> userBadges = UserBadge.getUserBadges(userId);
		
		return userBadges;
	}
	
	public static void main(String[] args) {
		DbUtil.initDatabase();
		
		Endpoint.publish("http://localhost:8080/GamedServer", new GamedServer());

        System.out.println("GamedServer service is started!");
        
        Scanner sc = new Scanner(System.in);
        do {
        	sc.next();
        	
        	System.out.println(UserBadge.userHasBadgeProgress("doug", 1));
        } while(true);
    }
}
