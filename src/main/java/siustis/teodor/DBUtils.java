package siustis.teodor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	public static void close(Connection connection){
		if(connection != null){
			try{
				connection.close();
			}catch(SQLException e){
				
			}
		}
	}
	public static void close(Statement statement){
		if(statement != null){
			try{
				statement.close();
			}catch(SQLException e){
				
			}
		}
	}
}
