package siustis.teodor;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

public class ConnectionFactory {
	private static ConnectionFactory connectionFactory=null;
	public final static String connectionUrl = "jdbc:mysql://localhost:3306/diseases"; //schema diseases tabel pacients
	public final static String dbUser = ""; //insert user name
			public final static String dbPass = ""; //insert pass
	public final static String DriverClassName = "com.mysql.jdbc.Driver";
	
	private ConnectionFactory() {
		try {
			Class.forName(DriverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	Connection getConnection()throws SQLException {
		java.sql.Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionUrl + "?useSSL=false", dbUser, dbPass);
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}
		return connection;
	}
	public static ConnectionFactory getInstance() {
		if(connectionFactory == null)
			connectionFactory = new ConnectionFactory();
		
		return connectionFactory;
	}
}
