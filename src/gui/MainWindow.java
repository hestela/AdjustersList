package gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dbConnection.*;
import dialogs.*;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;

public class MainWindow {
	// Declare JFrame
	private JFrame frmAdjustersList;
	
	// Declare DB connection
	private SQLiteJDBC connection;
	
	// Declare data table stuff
	private JTable dataTable;
	private TableRowSorter<DbTableModel> sorter;
	private DbTableModel tableModel;
	
	// Declare JTextFields
	private JTextField companyField;
	private JTextField nameField;
	private JTextField phoneField;
	private JTextField faxField;
	private JTextField searchField;
	
	// Declare JLables
	private JLabel lblCompany;	
	private JLabel lblName;	
	private JLabel lblPhone;	
	private JLabel lblFax;
	
	// Declare JButtons
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnSearch;	
	private JButton btnCancelSearch;
	
	// Declare JMenuItems
	private JMenuItem mntmHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmQuit;
	private JMenuItem menuItem_1;
	private JMenuItem menuItem_2;
	private JMenuItem menuItem_3;
	private JMenuItem menuItem_4;
	private JMenuItem menuItem_5;
	private JMenuItem menuItem_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmAdjustersList.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
				
		initElements();	
		initTable();
		
		// Close the DB connection before be close the JVM
		frmAdjustersList.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent wEvent)
            {
        		// Close the DB connection
        		connection.closeConnection();	
                wEvent.getWindow().dispose();
            }
        });
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initElements() {		
		// Open the JFrame
		frmAdjustersList = new JFrame();
		frmAdjustersList.setTitle("Adjusters List");
		frmAdjustersList.setBounds(0, 0, 600, 558);
		frmAdjustersList.setLocationRelativeTo(null);
		frmAdjustersList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdjustersList.setResizable(false);
		
		btnCancelSearch = new JButton("Cancel");
		btnCancelSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Clear search bar
				searchField.setText("");
				
				// Clear filter
				sorter.setRowFilter(new CaseInsensitiveFilter(""));	
				
				// Set main GUI state
				setGuiState(GuiState.MAIN);
			}
		});
		btnCancelSearch.setBounds(495, 445, 89, 23);
		frmAdjustersList.getContentPane().add(btnCancelSearch);
		
		btnAdd = new JButton("Add adjuster");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set add entry GUI state
				setGuiState(GuiState.ADD);
			}
		});
		btnAdd.setBounds(160, 445, 135, 23);
		frmAdjustersList.getContentPane().add(btnAdd);
		
		btnRemove = new JButton("Remove adjuster");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create an arraylist to sort the selected rows
				ArrayList<Integer> rowList = new ArrayList<Integer>();
				
				// Get row numbers
				int[] rows = dataTable.getSelectedRows();
				
				// Convert array to ArrayList
				for(int row: rows){
					rowList.add(dataTable.convertRowIndexToModel(row));
				}
				
				// We want to remove the highest order rows first so we don't go out of bounds due to changing indicies
				Collections.sort(rowList,Collections.reverseOrder());

				for(int row: rowList)
				{
					// Delete from DB
					connection.deleteRow(new QueryData(tableModel.getRowAt(row)));
					tableModel.getTableModelListeners();
					
					// Delete from table
					tableModel.removeRow(row);
				}

			}
		});
		btnRemove.setBounds(305, 445, 135, 23);
		frmAdjustersList.getContentPane().add(btnRemove);
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get data from text fields
				String company = companyField.getText();
				String name = nameField.getText();
				String phone = phoneField.getText();
				String fax = faxField.getText();
				
				// Check if company or name are filled with spaces
				String compCheck = company.replaceAll(" ", "");
				String nameCheck = name.replaceAll(" ", "");
				
				// Check for empty company name
				if (compCheck.equals(""))
				{
					// Display empty fields dialog
					JDialog empty = new EmptyField();
					empty.setLocationRelativeTo(frmAdjustersList);
					empty.setVisible(true);		
					
					// Check which string to clear
					if (compCheck.equals(""))
					{
						companyField.setText("");
					}					
				}
				else
				{
					// Check if name field empty
					if(nameCheck.equals(""))
					{
						name = "N/A";
					}
					// Check if phone field empty
					if(phone.equals(""))
					{
						phone = "N/A";
					}
					
					// Check if fax field is empty
					if(fax.equals(""))
					{
						fax = "N/A";
					}
					
					// Add entry to table
					tableModel.addRow(company, name, phone, fax);
					
					// Add entry to DB
					connection.insertData(company, name, phone, fax);
					
					// Clear text fields
					companyField.setText("");
					nameField.setText("");
					phoneField.setText("");
					faxField.setText("");

					// Set main GUI state
					setGuiState(GuiState.MAIN);
				}				
			}
		});
		btnOk.setBounds(495, 445, 89, 23);
		frmAdjustersList.getContentPane().add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Clear text fields
				companyField.setText("");
				nameField.setText("");
				phoneField.setText("");
				faxField.setText("");
				
				// Set main GUI state
				setGuiState(GuiState.MAIN);
			}
		});
		btnCancel.setBounds(495, 472, 89, 23);
		frmAdjustersList.getContentPane().add(btnCancel);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set search GUI state
				setGuiState(GuiState.SEARCH);
			}
		});
		btnSearch.setBounds(231, 479, 135, 23);
		frmAdjustersList.getContentPane().add(btnSearch);
		
		// Menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 600, 20);
		frmAdjustersList.getContentPane().add(menuBar);
		
		// Menu elements
		JMenuItem mntmPrint = new JMenuItem("Print");
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Open print dialog
				try {
					dataTable.print();
				} catch (PrinterException e) {
					// I'm not sure why this would happen
				}
			}
		});
		menuBar.add(mntmPrint);
		
		mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open help dialog
				JDialog help = new Help();
				help.setLocationRelativeTo(frmAdjustersList);
				help.setVisible(true);
			}
		});
		menuBar.add(mntmHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open about dialog
				JDialog about = new About();
				about.setLocationRelativeTo(frmAdjustersList);
				about.setVisible(true);
			}
		});
		menuBar.add(mntmAbout);
		
		mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Close program
				System.exit(0);
			}
		});
		menuBar.add(mntmQuit);
		
		// JLables
		lblCompany = new JLabel("Company");
		lblCompany.setBounds(62, 423, 60, 20);
		frmAdjustersList.getContentPane().add(lblCompany);
		
		lblName = new JLabel("Name");
		lblName.setBounds(62, 448, 60, 20);
		frmAdjustersList.getContentPane().add(lblName);
		
		lblPhone = new JLabel("Phone");
		lblPhone.setBounds(62, 473, 60, 20);
		frmAdjustersList.getContentPane().add(lblPhone);
		
		lblFax = new JLabel("Fax");
		lblFax.setBounds(62, 498, 60, 20);
		frmAdjustersList.getContentPane().add(lblFax);
		
		// JTextFields
		companyField = new JTextField();
		companyField.setBounds(122, 423, 350, 20);
		frmAdjustersList.getContentPane().add(companyField);
		companyField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(122, 448, 350, 20);
		frmAdjustersList.getContentPane().add(nameField);
		
		phoneField = new JTextField();
		phoneField.setColumns(10);
		phoneField.setBounds(122, 473, 350, 20);
		frmAdjustersList.getContentPane().add(phoneField);
		
		faxField = new JTextField();
		faxField.setColumns(10);
		faxField.setBounds(122, 498, 350, 20);
		frmAdjustersList.getContentPane().add(faxField);
		
		searchField = new JTextField();
		searchField.setToolTipText("Type text to search");
		searchField.setColumns(10);
		searchField.setBounds(30, 446, 350, 20);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			// Implementing all three methods so that when any key is added or removed the filter is applied
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// Get text from search field
				String search = searchField.getText();
				// Apply filter				
				sorter.setRowFilter(new CaseInsensitiveFilter(search));				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// Get text from search field
				String search = searchField.getText();
				// Apply filter
				sorter.setRowFilter(new CaseInsensitiveFilter(search));
				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// Get text from search field
				String search = searchField.getText();
				// Apply filter
				sorter.setRowFilter(new CaseInsensitiveFilter(search));
				
			}

		});
		frmAdjustersList.getContentPane().add(searchField);
		
		// Set main GUI state
		setGuiState(GuiState.MAIN);
		
		// Dummy buttons
		// TODO: Find a better way to make the other menu elements smaller		
		menuItem_1 = new JMenuItem("");
		menuBar.add(menuItem_1);
		
		menuItem_2 = new JMenuItem("");
		menuBar.add(menuItem_2);
		
		menuItem_3 = new JMenuItem("");
		menuBar.add(menuItem_3);
		
		menuItem_4 = new JMenuItem("");
		menuBar.add(menuItem_4);
		
		menuItem_5 = new JMenuItem("");
		menuBar.add(menuItem_5);
		
		menuItem_6 = new JMenuItem("");
		menuBar.add(menuItem_6);
	}
	
	private void initTable(){		
		// Local storage for records from DB
		ArrayList<QueryData> dbRecords;
		
		// Open the DB connection
		connection = new SQLiteJDBC();
		
		// Initialize DB connection
		if(!connection.initConnection())
		{
			// Failed in creating DB or got a corrupted one
			frmAdjustersList.setEnabled(false);
			JDialog error = new DBError();
			error.setLocationRelativeTo(frmAdjustersList);
			error.setVisible(true);
		}
		
		// Get all of the records from the DB
		dbRecords = connection.getDbData();
		
		// Create a table model for the table
		tableModel = new DbTableModel();		

		// Create JTable based on the model
		dataTable = new JTable(tableModel);
		
		// Set table attributes
		dataTable.setBounds(0, 20, 600, 400);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setCellSelectionEnabled(true);
		
		// Disable column dragging
		dataTable.getTableHeader().setReorderingAllowed(false);
		
		// Create a row sorter for the table
		sorter = new TableRowSorter<DbTableModel>(tableModel);
		
		// Add the sorter to the table
		dataTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);	
		
		// Add DB data to the table model
		tableModel.addDataList(dbRecords);
		frmAdjustersList.getContentPane().setLayout(null);
		
		// Place table inside of a scroll pane
		JScrollPane tableScrollPane = new JScrollPane(dataTable);
		tableScrollPane.setBounds(0, 20, 594, 400);
		
		// Add the scroll pane to the JFrame
		frmAdjustersList.getContentPane().add(tableScrollPane);
		
		tableModel.addTableModelListener(new TableModelListener(){

			@Override
			public void tableChanged(TableModelEvent e) {
				try
				{
					// Get the row and column that were edited
					int row = e.getFirstRow();
			        int column = e.getColumn();
			        
			        // Get the edited row data
			        String columnName = tableModel.getColumnName(column);
			        String[] data = tableModel.getRowAt(row);
			        
			        // Notify DB of row update
			        switch(columnName){
			        	case "Company":
			        		connection.updateRow(new QueryData(data), QueryType.COMPANY);
			        		break;
			        	case "Name":
			        		connection.updateRow(new QueryData(data), QueryType.NAME);
			        		break;
			        	case "Phone":
			        		connection.updateRow(new QueryData(data), QueryType.PHONE);
			        		break;
			        	case "Fax":
			        		connection.updateRow(new QueryData(data), QueryType.FAX);
			        		break;
			        }	
				}
				catch(IndexOutOfBoundsException ex)
				{
					// TODO: This is probably bad, but it works. Thanks java.
					// Without this try catch, java claims that an index like 5 is out of bounds because 5 <= 5. This only happens after a new entry is added
				}
			}			
		});
	}
	
	/**
	 * Sets the visibility for certain GUI elements
	 */
	private void setGuiState(GuiState state){
		switch(state){
			case MAIN:
				// Show or hide elements for main state
				lblCompany.setVisible(false);
				lblName.setVisible(false);
				lblPhone.setVisible(false);
				lblFax.setVisible(false);
				companyField.setVisible(false);
				nameField.setVisible(false);
				phoneField.setVisible(false);
				faxField.setVisible(false);
				searchField.setVisible(false);

				btnAdd.setVisible(true);
				btnRemove.setVisible(true);
				btnOk.setVisible(false);
				btnCancel.setVisible(false);
				btnSearch.setVisible(true);
				btnCancelSearch.setVisible(false);
				break;
			case ADD:
				// Show or hide elements for add state
				lblCompany.setVisible(true);
				lblName.setVisible(true);
				lblPhone.setVisible(true);
				lblFax.setVisible(true);
				companyField.setVisible(true);
				nameField.setVisible(true);
				phoneField.setVisible(true);
				faxField.setVisible(true);
				searchField.setVisible(false);

				btnAdd.setVisible(false);
				btnRemove.setVisible(false);
				btnOk.setVisible(true);
				btnCancel.setVisible(true);
				btnSearch.setVisible(false);
				btnCancelSearch.setVisible(false);
				break;
			case SEARCH:
				// Show or hide elements for search state
				lblCompany.setVisible(false);
				lblName.setVisible(false);
				lblPhone.setVisible(false);
				lblFax.setVisible(false);
				companyField.setVisible(false);
				nameField.setVisible(false);
				phoneField.setVisible(false);
				faxField.setVisible(false);
				searchField.setVisible(true);

				btnAdd.setVisible(false);
				btnRemove.setVisible(false);
				btnOk.setVisible(false);
				btnCancel.setVisible(false);
				btnSearch.setVisible(false);
				btnCancelSearch.setVisible(true);
				break;
		}

	}
}
