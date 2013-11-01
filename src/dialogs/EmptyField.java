package dialogs;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmptyField extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmptyField dialog = new EmptyField();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EmptyField() {
		setTitle("Error");
		setBounds(100, 100, 301, 115);
		getContentPane().setLayout(null);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnOk.setBounds(98, 47, 89, 23);
		getContentPane().add(btnOk);
		
		JLabel lblYouMustEnter = new JLabel("You must enter a value for company name");
		lblYouMustEnter.setFont(new Font("Arial", Font.PLAIN, 14));
		lblYouMustEnter.setBounds(5, 11, 274, 23);
		getContentPane().add(lblYouMustEnter);
	}
}
