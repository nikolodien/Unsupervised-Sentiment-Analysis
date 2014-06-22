package org.iitb.mtp.sair.poc.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	public static Connection getConnection(String database, String user,
			String passwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306";
		return DriverManager.getConnection(url + "/" + database, user, passwd);
	}
}
