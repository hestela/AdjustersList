package dbConnection;

import ca.odell.glazedlists.gui.TableFormat;

public class QueryTableFormat implements TableFormat<QueryResult>{

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		switch (col){
			case 0:
				return "Company";
				
			case 1:
				return "Name";
				
			case 2:
				return "Phone";
				
			case 3:
				return "Fax";
		}
		return null;
	}

	@Override
	public Object getColumnValue(QueryResult result, int col) {
		switch (col){
		case 0:
			return result.getCompany();
			
		case 1:
			return result.getName();
			
		case 2:
			return result.getPhone();
			
		case 3:
			return result.getFax();
		default:
			throw new IllegalStateException();
		}
	}

}
