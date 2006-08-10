package GUI;

import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import layout.AnchorConstraint;
import layout.AnchorLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import java.net.*;

/**
 * Die Klasse Reg erstellt ein JFrame mit 5 textfeldern, die dazu genutzt
 * werden einen neuen User in der Datenbank zu registrieren.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class Reg extends JFrame{

	private JLabel label_mail;
	private JLabel label_nachname;
	private JLabel label_name;
	private JTextField tf_vorname;
	private JTextField tf_nick;
	private JTextField tf_email;
	private JTextField tf_nachname;
	private JLabel label_header;
	private JButton button_absenden;
	private JLabel label_nick;
	private JButton button_abbrechen;
	private JLabel label_passwort;
	private JLabel label_passwort2;
	private JPasswordField pw_passwort;
	private JPasswordField pw_passwort2;


	/**
	 * Konstruktor
	 */
	public Reg() {
		super();
		initGUI();
	}

	/**
	 * Initialisiert und lädt die GUI
	 */
	private void initGUI() {
		try {
			this.getContentPane().setLayout(null);
			Reg_action al=new Reg_action(this);
	

			
			label_header = new JLabel();
			label_header.setBounds(10, 10, 300, 25);
			label_header.setText("Bitte folgende Felder zur Registrierung ausfüllen");
			this.getContentPane().add(label_header);
			
			label_name = new JLabel();
			label_name.setBounds(30, 40, 100, 25);
			label_name.setText("Vorname:");
			this.getContentPane().add(label_name);
			
			tf_vorname = new JTextField();
			tf_vorname.setBounds(200, 40, 150, 25);
			tf_vorname.setPreferredSize(new java.awt.Dimension(134, 26));
			this.getContentPane().add(tf_vorname);

			label_nachname = new JLabel();
			label_nachname.setBounds(30, 70, 100, 25);
			label_nachname.setText("Nachname:");
			this.getContentPane().add(label_nachname);

			tf_nachname = new JTextField();
			tf_nachname.setBounds(200, 70, 150, 25);
			tf_nachname.setText("");
			this.getContentPane().add(tf_nachname);
			
			label_mail = new JLabel();
			label_mail.setBounds(30, 100, 150, 25);
			label_mail.setText("Email:");
			this.getContentPane().add(label_mail);

			tf_email = new JTextField();
			tf_email.setBounds(200, 100, 150, 25);
			this.getContentPane().add(tf_email);

			label_nick = new JLabel();
			label_nick.setBounds(30, 130, 100, 25);
			label_nick.setText("Nickname:");
			this.getContentPane().add(label_nick);

			tf_nick = new JTextField();
			tf_nick.setBounds(200, 130, 150, 25);
			this.getContentPane().add(tf_nick);

			label_passwort = new JLabel();
			label_passwort.setBounds(30, 160, 100, 25);
			label_passwort.setText("Passwort:");
			this.getContentPane().add(label_passwort);
			
			pw_passwort = new JPasswordField();
			pw_passwort.setBounds(200, 160, 150, 25);
			this.getContentPane().add(pw_passwort);
		
			label_passwort2 = new JLabel();
			label_passwort2.setBounds(30, 190, 150, 25);
			label_passwort2.setText("Passwort wiederholen:");
			this.getContentPane().add(label_passwort2);
			
			pw_passwort2 = new JPasswordField();
			pw_passwort2.setBounds(200, 190, 150, 25);
			this.getContentPane().add(pw_passwort2);

			button_abbrechen = new JButton();
			button_abbrechen.setBounds(280, 230, 100, 30);
			button_abbrechen.setText("Abbrechen");
			button_abbrechen.addActionListener(al);
			this.getContentPane().add(button_abbrechen);
			
			button_absenden = new JButton();
			button_absenden.setBounds(390, 230, 100, 30);
			button_absenden.setText("Absenden");
			button_absenden.setToolTipText("Daten Abschicken und zum Login zurückkehren.");
			button_absenden.addActionListener(al);
			this.getContentPane().add(button_absenden);

			this.getRootPane().setDefaultButton(button_absenden);
			this.setSize(500, 300);
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			this.setLocationRelativeTo(null);
			this.setTitle("Virtueller Arbeitsplatz");
		
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Die Klasse Reg_action stellt den ActionListener für die Klasse Reg.
	 * @author Daniel Meurer
	 */
	public class Reg_action  implements ActionListener {
		private JFrame owner;
		public Reg_action(JFrame owner){
			super();
			this.owner=owner;
		}
		
		public void actionPerformed(ActionEvent e) {
			String test = e.getActionCommand();
			if (test.equals("Beenden"))
				System.exit(0);
			else if(test.equals("Abbrechen")){
				Login inst = new Login();
				inst.setVisible(true);
				dispose();
			}
			else{
				String Vorn = tf_vorname.getText();
				String Lastname = tf_nachname.getText();
				String Mail = tf_email.getText();
				String Nickname = tf_nick.getText();
				String pw = pw_passwort.getText();
				String pw2 = pw_passwort2.getText();
				
				if(Vorn.equals("") || Lastname.equals("") || Mail.equals("") || Nickname.equals("")|| pw.equals("")){
					Error err=new Error("Fehler","Eingaben nicht vollständig",owner);
					err.setVisible(true);
				}
				else if(!pw2.equals(pw)){
					Error err=new Error("Fehler","Passwörter stimmen nicht überein.",owner);
					err.setVisible(true);
					pw_passwort.setText("");
					pw_passwort2.setText("");
				}
				else{
					System.out.println(Vorn + "\n" + Lastname + "\n" + Mail + "\n" + Nickname + "\n" + pw);
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");
						Statement statement = connection.createStatement();
						
						InetAddress inet=InetAddress.getLocalHost();
						statement.executeUpdate("INSERT INTO UserData(Nickname,Vorname,Nachname,Email,Passwort) VALUES ('"+Nickname+"', '"+Vorn+"','"+Lastname+"', '"+Mail+"', '"+pw+"')");
						Error err2=new Error("Glückwunsch","Du hast dich erfolgreich angemeldet.\nDu kannst dich jetzt einloggen.",owner);
						err2.setModal(true);
						err2.setVisible(true);
						statement.close();
						connection.close();
						Fileausgabe.setProperty("DefaultNick", Nickname);
						Fileausgabe.setProperty("DefaultPw", pw);
						Login inst = new Login();
						inst.setVisible(true);
						dispose();						
					} catch (Exception e1) {
						System.out.println("ERROR:" + e1.getMessage());
					}
				}
			}
		}
	}
}
