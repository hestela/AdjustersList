package dbConnection;

public class QueryResult implements Comparable<QueryResult>{
	private String company, name, phone, fax;
	
	public QueryResult(String company,String name,String phone,String fax){
		this.company 	= company;
		this.name 		= name;
		this.phone 		= phone;
		this.fax 		= fax;
	}

	public String getCompany() {
		return company;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getFax() {
		return fax;
	}

	@Override
	public int compareTo(QueryResult rhs) {
		return this.name.compareTo(rhs.getName());
	}

	public Object getPriority() {
		// TODO Auto-generated method stub
		return null;
	}

}
