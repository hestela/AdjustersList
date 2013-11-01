package dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class Help extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Help dialog = new Help();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Help() {
		setBounds(100, 100, 454, 260);
		getContentPane().setLayout(null);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(174, 187, 89, 23);
		getContentPane().add(btnOk);

		JLabel lblHelp = new JLabel("<html><h1>Help</h1><p>Click add adjuster to add an adjuster to the list</p>"
				+ "<ul dir=\"ltr\"><li>Then you can click Ok to add the new adjuster or click cancel</li></ul>"
				+ "<p dir=\"ltr\">To remove adjusters select them and then click remove adjuster</p>"
				+ "<p dir=\"ltr\">To seach for an adjuster click on search and then type in the new text box</p>"
				+ "<ul dir=\"ltr\"><li>Click cancel when done searching</li></ul>"
				+ "<p dir=\"ltr\">To print the adjusters click on print</p>"
				+ "<p dir=\"ltr\">Columns can be sorted by clicking just to the right of them</p></html>");
		lblHelp.setBounds(0, -15, 435, 202);
		getContentPane().add(lblHelp);

	}

}
