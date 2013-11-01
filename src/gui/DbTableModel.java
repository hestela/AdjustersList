package gui;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import dbConnection.*;

public class DbTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DbTableModel(){
		// Add column names
		this.addColumn("Company");
		this.addColumn("Name");
		this.addColumn("Phone");
		this.addColumn("Fax");	
	}
	
	public void addDataList(ArrayList<QueryData> tableData){
		// Add data to the able from ArrayList
		for(QueryData entry : tableData){
			this.addRow(entry.getObjArray());
		}
	}
	
	public void addRow(QueryData newRow){
		this.addRow(newRow.getObjArray());
	}
	
	public void addRow(String company, String name, String phone, String fax){
		this.addRow(new String[] {company, name, phone, fax});
	}
	
	public String[] getRowAt(int row) {
	     String[] result = new String[4];

	     for (int colIndex = 0; colIndex < 4; colIndex++) {
	         result[colIndex] = (String) this.getValueAt(row, colIndex);
	     }

	     return result;
	}

}
