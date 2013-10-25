package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

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
				statement.setQueryTimeout(30); 
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS adjusters (company TEXT, name TEXT, phone TEXT, fax TEXT)");
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

	public EventList<QueryResult> getDbData(){
		
		try{
			// Make a result ArrayList
			EventList<QueryResult> result = new BasicEventList<QueryResult>();
			
			// Ask the DB for all of its data in the adjusters table
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = statement.executeQuery("select * from adjusters");
			
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
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			String queryData = "('" + company + "','" + name + "','" + phone + "','" + fax + "')";
			statement.executeUpdate("insert into adjusters values" + queryData);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void updateRow(int rowID, UpdateType type, String newData){
		try{
			// Setup query
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			String queryData;
			
			// Execute appropriate query
			switch (type){
				case COMPANY:
					queryData = "company='" + newData + "' WHERE rowid=" + rowID;
					statement.executeUpdate("UPDATE adjusters SET " + queryData);
					break;
					
				case NAME:
					queryData = "name='" + newData + "' WHERE rowid=" + rowID;
					statement.executeUpdate("UPDATE adjusters SET " + queryData);
					break;
					
				case PHONE:
					queryData = "phone='" + newData + "' WHERE rowid=" + rowID;
					statement.executeUpdate("UPDATE adjusters SET " + queryData);
					break;
					
				case FAX:
					queryData = "fax='" + newData + "' WHERE rowid=" + rowID;
					statement.executeUpdate("UPDATE adjusters SET " + queryData);
					break;
			}	
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
