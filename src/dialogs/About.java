package dialogs;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class About extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			About dialog = new About();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public About() {
		setBounds(100, 100, 388, 178);
		getContentPane().setLayout(null);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(141, 110, 89, 23);
		getContentPane().add(btnOk);
		
		JLabel lblText = new JLabel("<html><h1>AdjustersList Java Application</h1>"
				+ "<p>Lists the information of adjusters in a table from a database.<p>"
				+ "<strong>Author</strong>: Henry Estela</p>"
				+ "<p><strong>Email</strong>: henryestela@gmail.com</p>"
				+ "<p><strong>Github:&nbsp;</strong><a href=\"https://github.com/hestela/AdjustersList\">https://github.com/hestela/AdjustersList</a></html>");
		lblText.setBounds(10, -15, 414, 123);
		getContentPane().add(lblText);
	}
}
