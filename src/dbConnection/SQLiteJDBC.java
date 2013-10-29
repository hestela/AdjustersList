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

	public ArrayList<QueryResult> getDbData(){
		
		try{
			// Make a result ArrayList
			ArrayList<QueryResult> result = new ArrayList<QueryResult>();
			
			// Ask the DB for all of its data in the adjusters table
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			ResultSet rs = statement.executeQuery(makeQueryString(null, QueryType.SELECT, null));
			
			while(rs.next()){
				// Make a new QueryResult and place it in the result ArrayList
				result.add(new QueryResult(
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
	
	public void updateRow(QueryResult updateQuery, QueryType type, String newData){
		try{
			// Setup query
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			// Execute appropriate query
			switch (type){
				case COMPANY:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.COMPANY, newData));
					break;				
				case NAME:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.NAME, newData));
					break;				
				case PHONE:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.PHONE, newData));
					break;				
				case FAX:
					statement.executeUpdate(makeQueryString(updateQuery, QueryType.FAX, newData));
					break;
			default:
				break;
			}	
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void deleteRows(ArrayList<QueryResult> rows){
		
		Statement statement;
		try {
			// Setup query
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			// Attempt to delete all rows
			for(QueryResult row: rows)
			{
				statement.executeUpdate(makeQueryString(row, QueryType.DELETE, null));
			}
				
		} catch (SQLException e) {
			// Tried to delete invalid data?
			e.printStackTrace();
		}

		
	}
	
	private String makeQueryString(QueryResult query, QueryType type, String newData){
		String queryData = "";
		
		// Create appropriate query string
		switch (type){
		case COMPANY:
			queryData = "DELETE FROM adjusters WHERE company='" + query.getCompany() 
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;	
		case NAME:
			queryData = "DELETE FROM adjusters WHERE name='" + query.getCompany() 
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;	
		case PHONE:
			queryData = "DELETE FROM adjusters WHERE phone='" + query.getCompany() 
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;
		case FAX:
			queryData = "DELETE FROM adjusters WHERE fax='" + query.getCompany() 
			+ "' AND name='" + query.getName()
			+ "' AND phone='" + query.getPhone()
			+ "' AND fax='" + query.getFax() + "'";
			break;
		case DELETE:
			queryData = "DELETE FROM adjusters WHERE delete='" + query.getCompany() 
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
