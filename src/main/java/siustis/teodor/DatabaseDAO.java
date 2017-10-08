package siustis.teodor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import java.sql.PreparedStatement;

public class DatabaseDAO {
	private Connection connection = null;
	private PreparedStatement ptmt = null;
	ResultSet resultSet = null;
	public DatabaseDAO(){}

	public static Connection getConnection() throws SQLException{
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	

	
	public void add(PacientBean p,String fileName) {
		try {
			String queryString = "INSERT INTO pacients " + "(Document,Name,Disease,Symptom,Matching) VALUES (?,?,?,?,?)";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, fileName);
			ptmt.setString(2, p.getName());
			ptmt.setString(3, p.getDiagnostic());
			ptmt.setString(4, p.getSymptoms());
			ptmt.setString(5, p.getMatching());
			ptmt.executeUpdate();
			System.out.println("Data Added Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
	
	
	public void UpdateDatabase(PacientBean p,String fileName,int i) throws SQLException{
		try{
	
		connection = getConnection();
		
		String sql = "Update pacients SET Document = ?, Name =?,Disease = ?, Symptom =?, Matching =? ";

		ptmt = connection.prepareStatement(sql);
		ptmt.setString(1, fileName);
		ptmt.setString(2, p.getName());
		ptmt.setString(3, p.getDiagnostic());
		ptmt.setString(4, p.getSymptoms());
		ptmt.setString(5, p.getMatching());

		
		ptmt.executeUpdate();
		}catch(SQLException e){
				e.printStackTrace();

	      
		} finally{
	          //finally block used to close resources
	          try{
	             if(ptmt!=null)
	               ptmt.close();
	          }catch(SQLException se){
	          }// do nothing
	          try{
	             if(connection!=null)
	                connection.close();
	          }catch(SQLException se){
	             se.printStackTrace();
	          }//end finally try
	       }
	}
	public void findAll(PacientBean p) {
		try {
			String queryString = "SELECT * FROM pacients "  ;
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				System.out.println("ID " + resultSet.getInt("ID")
						+ ", Name " + resultSet.getString("Name") + ", Disease "
						+ resultSet.getString("Course") + ", Symptoms "
						+ resultSet.getString("Symptom"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
		
	
}

