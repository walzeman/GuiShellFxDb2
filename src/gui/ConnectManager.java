package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectManager {
	public enum DB {
		PROD, ACCT;
	}
	
	private static final String DB_URL_PROD 
		= "jdbc:mysql:///ProductsDb";
	private static final String DB_URL_ACCT 
		= "jdbc:mysql:///AccountsDb";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	public static Connection getConnection(DB db) throws SQLException {	
		Connection conn = null;
		switch(db) {
			case PROD:
				conn = DriverManager.getConnection(DB_URL_PROD, USERNAME,
						PASSWORD);
				break;
			case ACCT:
				conn = DriverManager.getConnection(DB_URL_ACCT, USERNAME,
						PASSWORD);
				break;
			default:
				//do nothing
		}
		
		System.out.println("Got connection...");
		return conn;
	}
	
}
