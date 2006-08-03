package GUI;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import layout.AnchorConstraint;
import layout.AnchorLayout;



/**
 * 
 * Die Klasse Login zeigt ein Fenster, in welchem man sich mit Username
 * und Passwort über eine Datenbank im System einloggen kann. * 
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class Login extends JFrame {

	private JLabel l_reg;
	private JButton button_reg;
	private JButton button_login;
	/**
	 *hier wird das passwort eingetragen 
	 */
	private JPasswordField pf_password;
	
	/**
	 *hier wird der name eingetragen 
	 */
	private JTextField tf_username;
	private JLabel l_password;
	private JLabel l_username;
	private JLabel l_header;

	
//	public static void main(String[] args) {
//		
//		Login inst = new Login();
//		inst.setVisible(true);
//	}
	
	/**
	 * Konstruktor
	 */
	public Login() {
		super();
		initGUI();
		
	}
	
	/**
	 * initialisiert und lädt das graphische Userinterface
	 */
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.getContentPane().setLayout(thisLayout);
			Login_action al=new Login_action(this);
			{
						
						{
							l_header = new JLabel();
							this.getContentPane().add(l_header, new AnchorConstraint(24,714, 173, 346, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							l_header.setText("Bitte mit Kennung einloggen");
							l_header.setPreferredSize(new java.awt.Dimension(176, 39));
						}
						{
							l_username = new JLabel();
							this.getContentPane().add(l_username,new AnchorConstraint(254,382,324,155,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							l_username.setText("Benutzername:");
							l_username.setPreferredSize(new java.awt.Dimension(103,14));
						}
						{
							l_password = new JLabel();
							this.getContentPane().add(l_password,new AnchorConstraint(344,287,492,155,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL));
							l_password.setText("Passwort:");
							l_password.setPreferredSize(new java.awt.Dimension (60,30));
						}
						{
							tf_username = new JTextField();
							this.getContentPane().add(tf_username,new AnchorConstraint(245,648,349,386,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL));
							tf_username.setPreferredSize(new java.awt.Dimension(119,21));
//							tf_username.addActionListener(this);
						}
						{
							pf_password = new JPasswordField();
							this.getContentPane().add(pf_password,new AnchorConstraint(383,648,487,386,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL));
							pf_password.setPreferredSize(new java.awt.Dimension(119,21));
//							pf_password.addActionListener(this);
						}
						{
							button_login = new JButton();
							this.getContentPane().add(button_login, new AnchorConstraint(382,837, 487, 689, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							button_login.setText("Login");
							button_login.setPreferredSize(new java.awt.Dimension(71, 25));
							button_login.addActionListener(al);
						}
						{
							l_reg = new JLabel();
							this.getContentPane().add(l_reg,new AnchorConstraint(641,794,789,223,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL,AnchorConstraint.ANCHOR_REL));
							l_reg.setText("Noch keine Kennung? Dann bitte registrieren");
							l_reg.setPreferredSize(new java.awt.Dimension(259,30));
						}
						{
							button_reg = new JButton();
							this.getContentPane().add(button_reg, new AnchorConstraint(817,641, 922, 377, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							button_reg.setText("Registrieren");
							button_reg.setPreferredSize(new java.awt.Dimension(126, 25));
							button_reg.addActionListener(al);
						}

					}


			this.getRootPane().setDefaultButton(button_login);
			this.setResizable(false);
			this.setSize(500, 300);
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			this.setLocationRelativeTo(null);
			this.setTitle("Virtueller Arbeitsplatz");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Diese Klasse definiert den zur Klasse Login gehörigen ActionListener
	 * @author Daniel Meurer
	 */
	public class Login_action implements ActionListener{
		private JFrame owner;
		
		/**
		 * Konstruktor
		 * @param owner Der zu dem Listener gehörende JFrame
		 */

		public Login_action(JFrame owner){
			this.owner=owner;
		}

		public void actionPerformed(ActionEvent e) {
	
			String test= e.getActionCommand();
	//		System.out.println(test);
			if(test.equals("Registrieren"))	{
	
				Reg inst = new Reg();
				inst.setVisible(true);
				dispose();
			}
			
			else if(test.equals("Beenden")){	
				//System.out.println("Beenden");
				System.exit(0);
			}
			else{	
				String name=tf_username.getText();
				String pw=pf_password.getText();
				try {
					Class.forName("com.mysql.jdbc.Driver");
				
					Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");
					
					Statement statement = connection.createStatement();	
					String abfrage="SELECT Passwort, Nickname FROM UserData WHERE Nickname='"+name+"';";
					ResultSet x = statement.executeQuery(abfrage);
					if(!x.next()){
						Error err=new Error("Fehler","Bitte Username und Passwort eingeben \noder registrieren wenn noch kein gültiger \nAccount vorliegt!",owner);					
						err.setVisible(true);
					}
					else{				
				
						if(pw.equals(x.getString(1))){
							String nickname = x.getString(2);
							String lanIp = Ip.getLanIp();
							String wanIp = Ip.getWanIp();
							
							try {
								statement = connection.createStatement();	
								statement.executeUpdate("INSERT INTO UserOnline(Nickname,LanIp,WanIp) VALUES ('"+nickname+"','"+lanIp+"','"+wanIp+"');");
							} catch (Exception e1) {
//									e1.printStackTrace();
									Error err=new Error("Warnung","Sorry, User "+nickname+" ist schon eingeloggt",owner);									
									err.setVisible(true);
									return;
							}
							Error err=new Error("Glückwunsch","Sie haben sich erfolgreich eingeloggt.",owner);					
							err.setVisible(true);
							Auswahl aw=new Auswahl(name);
							aw.setVisible(true);
							owner.dispose();
						}
						else{
							Error err=new Error("Warnung","Sorry, Username und Password passen nicht.",owner);									
							err.setVisible(true);
						}
					}
					statement.close();
					connection.close();
					
				} catch (Exception e1) {
					Error err=new Error("Fehler","\n\nBitte überprüfen Sie ihre Internetverbindung.",owner);					
					err.setVisible(true);
					System.out.println("ERROR:" + e1.getMessage());
				}
				
			}
		}
		
	}
	
}

	