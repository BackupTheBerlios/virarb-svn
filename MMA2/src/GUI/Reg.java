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

	private JLabel Email1;
	private JLabel Nachname1;
	private JLabel Name;
	private JTextField Vorname;
	private JTextField Nick;
	private JTextField Email;
	private JTextField Nachname;
	private JLabel jLabel1;
	private JButton Absenden;
	private JLabel Nick1;
	private JButton button_abbrechen;
	private JLabel Passwort1;
	private JPasswordField Passwort;


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
			AnchorLayout thisLayout = new AnchorLayout();
			this.getContentPane().setLayout(thisLayout);
			Reg_action al=new Reg_action(this);

			{
				
					{
						button_abbrechen = new JButton();
						this.getContentPane().add(button_abbrechen, new AnchorConstraint(629,919, 727, 712, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						button_abbrechen.setText("Abbrechen");
						button_abbrechen.setPreferredSize(new java.awt.Dimension(99, 25));
						button_abbrechen.addActionListener(al);
					}
					{
						Passwort1 = new JLabel();
						this.getContentPane().add(Passwort1, new AnchorConstraint(767,442, 893, 208, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Passwort1.setText("Passwort:");
						Passwort1.setPreferredSize(new java.awt.Dimension(112, 30));
					}
					{
						Passwort = new JPasswordField();
						this.getContentPane().add(Passwort, new AnchorConstraint(780,660, 880, 375, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Passwort.setPreferredSize(new java.awt.Dimension(136, 24));
//						Passwort.addActionListener(this);
					}
					{
						Absenden = new JButton();
						this.getContentPane().add(Absenden, new AnchorConstraint(778,917, 876, 712, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Absenden.setText("Absenden");
						Absenden.setPreferredSize(new java.awt.Dimension(98, 25));
						Absenden.setToolTipText("Daten Abschicken und zum Login zurückkehren.");
						Absenden.addActionListener(al);
					}
					{
						Nick1 = new JLabel();
						this.getContentPane().add(Nick1, new AnchorConstraint(616,390, 738, 210, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Nick1.setText("Nickname:");
						Nick1.setPreferredSize(new java.awt.Dimension(86, 32));
					}
					{
						Email1 = new JLabel();
						this.getContentPane().add(Email1, new AnchorConstraint(471,333, 593, 208, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Email1.setText("Email:");
						Email1.setPreferredSize(new java.awt.Dimension(60, 32));
					}
					{
						Nachname1 = new JLabel();
						this.getContentPane().add(Nachname1, new AnchorConstraint(326,390, 437, 208, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Nachname1.setText("Nachname:");
						Nachname1.setPreferredSize(new java.awt.Dimension(87, 29));
					}
					{
						Name = new JLabel();
						this.getContentPane().add(Name, new AnchorConstraint(173,335, 288, 210, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Name.setText("Vorname:");
						Name.setPreferredSize(new java.awt.Dimension(60, 30));
					}
					{
						Nachname = new JTextField();
						this.getContentPane().add(Nachname, new AnchorConstraint(341,660, 440, 379, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Nachname.setText("");
						Nachname.setPreferredSize(new java.awt.Dimension(134, 26));
//						Nachname.addActionListener(this);
					}
					{
						jLabel1 = new JLabel();
						this.getContentPane().add(jLabel1, new AnchorConstraint(17,844, 131, 212, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						jLabel1.setText("Bitte folgende Felder zur Registrierung ausfüllen");
						jLabel1.setPreferredSize(new java.awt.Dimension(302, 30));
					}
					{
						Email = new JTextField();
						this.getContentPane().add(Email, new AnchorConstraint(490,660, 589, 377, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Email.setPreferredSize(new java.awt.Dimension(135, 26));
//						Email.addActionListener(this);
					}
					{
						Nick = new JTextField();
						this.getContentPane().add(Nick, new AnchorConstraint(635,660, 734, 377, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Nick.setPreferredSize(new java.awt.Dimension(135, 26));
//						Nick.addActionListener(this);
					}
					{
						Vorname = new JTextField();
						this.getContentPane().add(Vorname, new AnchorConstraint(185,660, 284, 379, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Vorname.setPreferredSize(new java.awt.Dimension(134, 26));
//						Vorname.addActionListener(this);
					}

				}
				
			this.getRootPane().setDefaultButton(Absenden);
			this.setSize(486, 312);
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
				String Vorn = Vorname.getText();
				String Lastname = Nachname.getText();
				String Mail = Email.getText();
				String Nickname = Nick.getText();
				String pw= Passwort.getText();
				if(Vorn.equals("") || Lastname.equals("") || Mail.equals("") || Nickname.equals("")|| pw.equals("")){
					Error err=new Error("Fehler","Eingaben nicht vollständig",owner);
					err.setVisible(true);
				}
				else{
					System.out.println(Vorn + "\n" + Lastname + "\n" + Mail + "\n" + Nickname + "\n" + pw);
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");
						Statement statement = connection.createStatement();
						
						InetAddress inet=InetAddress.getLocalHost();
						statement.executeUpdate("INSERT INTO UserData(Nickname,Vorname,Nachname,Email,Passwort,IP) VALUES ('"+Nickname+"', '"+Vorn+"','"+Lastname+"', '"+Mail+"', '"+pw+"','"+inet.getHostAddress()+"')");
						Error err2=new Error("Glückwunsch","Du hast dich erfolgreich angemeldet.\nDu kannst dich jetzt einloggen.",owner);
						err2.setModal(true);
						err2.setVisible(true);
						statement.close();
						connection.close();
						
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
