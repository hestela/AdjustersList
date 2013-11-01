package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteJDBC {
	
	private Connection connection;
	
	/**
	 * Default constructor
	 */
	public SQLiteJDBC(){
		this.connection = null;
	}
	
	/**
	 * Attempts to open a connection to the database
	 * @throws SQLException 
	 * @return true if connection good, false if error
	 */
	public boolean initConnection()
	{
		// Check if connection already open
		if (connection != null){
			// No need to reopen the connection
			return true;
		}
		else{
			try{	
				// Attempt to connect to the database 
	  			Class.forName("org.sqlite.JDBC");
			    connection = DriverManager.getConnection("jdbc:sqlite:AdjustersDB.db");

				// Make sure we have the table we need
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(10); 
				statement.executeUpdate(makeQueryString(null, QueryType.CREATE, null));
			}
			catch(SQLException e){
				e.printStackTrace();
				
				// R.I.P, this connection
				connection = null;
				
		    	// Tell Houston there is a problem
		    	return false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			
			// Since we must have gotten a good connection, return true
			return true;
		}
	}

	public ArrayList<QueryData> getDbData(){
		
		try{
			// Make a result ArrayList
			ArrayList<QueryData> result = new ArrayList<QueryData>();
			
			// Ask the DB for all of its data in the adjusters table
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			ResultSet rs = statement.executeQuery(makeQueryString(null, QueryType.SELECT, null));
			
			while(rs.next()){
				// Make a new QueryResult and place it in the result ArrayList
				result.add(new QueryData(
								rs.getString("company"), 
								rs.getString("name"), 
								rs.getString("phone"), 
								rs.getString("fax") ));
			}			
			return result;			
		}
		catch(SQLException e){
			// Got a bad result from our query. going to assume/ hope this never happens
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertData(String company,String name,String phone,String fax){
		try{
			// Setup query
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			// Attempt to get data
			String queryData = "('" + company + "','" + name + "','" + phone + "','" + fax + "')";
			statement.executeUpdate(makeQueryString(null, QueryType.INSERT, queryData));
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void updateRow(QueryData updateQuery, QueryType type){
		try{
			// Setup query
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			// Execute appropriate query
			switch (type){
				case COMPANY:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.COMPANY, null));
					break;				
				case NAME:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.NAME, null));
					break;				
				case PHONE:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.PHONE, null));
					break;				
				case FAX:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.FAX, null));
					break;
			default:
				break;
			}	
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void deleteRow(QueryData row){
		
		Statement statement;
		try {
			// Setup query
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			// Attempt to delete row
			statement.executeUpdate(makeQueryString(row, QueryType.DELETE, null));				
		} catch (SQLException e) {
			// Tried to delete invalid data?
			e.printStackTrace();
		}		
	}
	
	private String makeQueryString(QueryData query, QueryType type, String newData){
		String queryData = "";
		
		// Create appropriate query string
		switch (type){
		case COMPANY:
			queryData = "UPDATE adjusters SET company='" + query.getCompany() 
			+ "' WHERE name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;	
		case NAME:
			queryData = "UPDATE adjusters SET name='" + query.getName() 
			+ "' WHERE company='" + query.getCompany()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;	
		case PHONE:
			queryData = "UPDATE adjusters SET phone='" + query.getPhone() 
			+ "' WHERE company='" + query.getCompany()
			+ "' AND name='" + query.getName()
			+ "' AND fax='" + query.getFax() + "'";
			break;
		case FAX:
			queryData = "UPDATE adjusters SET fax='" + query.getFax() 
			+ "' WHERE company='" + query.getCompany()
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone() + "'";
			break;
		case DELETE:
			queryData = "DELETE FROM adjusters WHERE company='" + query.getCompany() 
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;
		case CREATE:
			queryData = "CREATE TABLE IF NOT EXISTS adjusters (company TEXT, name TEXT, phone TEXT, fax TEXT)";
			break;
		case INSERT:
			queryData = "INSERT into adjusters values " + newData;
			break;
		case SELECT:
			queryData = "SELECT * FROM adjusters";
			break;
		}
		return queryData;
	}	
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
