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
	
	public void addDataList(ArrayList<QueryResult> tableData){
		// Add data to the able from ArrayList
		for(QueryResult entry : tableData){
			this.addRow(entry.getObjArray());
		}
	}
	
	public void addRow(QueryResult newRow){
		this.addRow(newRow.getObjArray());
	}
	
	public void addRow(String company, String name, String phone, String fax){
		this.addRow(new String[] {company, name, phone, fax});
	}
	
/*	public void removeRow(int row){
		this.removeRow(dataTable.convertRowIndexToModel(row));
	}
	*/
}
