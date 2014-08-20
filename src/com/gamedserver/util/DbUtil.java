package com.gamedserver.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.MySqlPlatform;
import com.gamedserver.models.Application;
import com.gamedserver.models.Badge;
import com.gamedserver.models.Session;
import com.gamedserver.models.User;
import com.gamedserver.models.UserBadge;

public class DbUtil {
	
	private static EbeanServer server;

	public static void initDatabase() {
		ServerConfig config = new ServerConfig();  
		config.setName("database");  
		  
		// Define DataSource parameters  
		DataSourceConfig mysqlDb = new DataSourceConfig();  
		mysqlDb.setDriver("com.mysql.jdbc.Driver");  
		mysqlDb.setUsername("root");  
		mysqlDb.setPassword("");  
		mysqlDb.setUrl("jdbc:mysql://127.0.0.1:3306/gamed?characterEncoding=UTF-8");  
		mysqlDb.setHeartbeatSql("select count(*) from applications");  
		  
		config.setDataSourceConfig(mysqlDb);  
		  
		// automatically determine the DatabasePlatform  
		// using the jdbc driver   
		config.setDatabasePlatform(new MySqlPlatform());  
		
		config.setDefaultServer(true);
		  
		// specify the entity classes (and listeners etc)  
		// ... if these are not specified Ebean will search  
		// ... the classpath looking for entity classes.  
		config.addClass(Application.class);
		config.addClass(User.class);
		config.addClass(Session.class);
		config.addClass(Badge.class);
		config.addClass(UserBadge.class);
		  
		// create the EbeanServer instance  
		DbUtil.server = EbeanServerFactory.create(config); 
	}
}
