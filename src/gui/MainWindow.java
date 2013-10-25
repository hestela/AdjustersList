package gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dbConnection.*;

import javax.swing.JLabel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow {

	private JFrame frame;
	private SQLiteJDBC connection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
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
				
		initialize();	
		
		// Close the DB connection before be close the JVM
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
        		// Close the DB connection
        		connection.closeConnection();	
                e.getWindow().dispose();
            }
        });
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Local variables
		//ArrayList<QueryResult> DbRecords;
		EventList<QueryResult> dbRecords;
		SortedList<QueryResult> sortedResults; 

		
		// Open this JFrame
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Open the DB connection
		connection = new SQLiteJDBC();
		
		if(!connection.initConnection())
		{
			// Failed in creating DB or got a corrupted one
			frame.setEnabled(false);
			JDialog error = new DBError();
			error.setLocationRelativeTo(frame);
			error.setVisible(true);
		}
		
		// Get all of the records from the DB
		dbRecords = connection.getDbData();
		Collections.sort(dbRecords);
		
		// Initialize the data container for the table
		AdvancedTableModel<QueryResult> issuesTableModel =
				GlazedListsSwing.eventTableModelWithThreadProxyList(dbRecords, new QueryTableFormat());
		JTable issuesJTable = new JTable(issuesTableModel);

		
		JScrollPane TableScrollPane = new JScrollPane(issuesJTable);
		
		frame.add(TableScrollPane);
	
	}

}
