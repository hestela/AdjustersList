package dbConnection;

public class QueryData{
	private String company, name, phone, fax;
	
	public QueryData(String company,String name,String phone,String fax){
		this.company 	= company;
		this.name 		= name;
		this.phone 		= phone;
		this.fax 		= fax;
	}
	
	public QueryData(String[] data){
		this.company 	= data[0];
		this.name 		= data[1];
		this.phone 		= data[2];
		this.fax 		= data[3];
	}

	public String[] getObjArray(){
		return new String[] {company, name, phone, fax};
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

}
