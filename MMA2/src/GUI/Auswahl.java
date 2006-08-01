package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import Server.Server;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import javax.swing.*;



/**
 * 
 * Die Klasse Auswahl ist ein Frame, in dem ausgewählt werden kann ob
 * eine Session gehostet wird oder sich einer laufenden Sitzung 
 * angeschlossen wird.
 * @author Klassen,Kokoschka,Langer,Meurer
 *
 */
public class Auswahl extends JFrame {

	//private static String myWanIP=Ip.getWanIp(); 
	private JTextField tf_ip;
	private String username="user";

	
//	Mainmethode nicht mehr notwendig
	
	public static void main(String[] args) {
		Auswahl inst = new Auswahl();
		inst.setVisible(true);
	}
	
	
	
	/**
	 * Konstruktor
	 */
	public Auswahl() {
		super();
		//System.setProperty("java.rmi.server.hostname",myWanIP);
		initGUI();
	}
	
	/**
	 * Konstruktor.
	 * @param name Der Name des Users.
	 */
	public Auswahl(String name) {
		super();
		this.username=name;
		//System.setProperty("java.rmi.server.hostname",myWanIP);
		initGUI();
	}
	
	/**
	 * Initialisiert die GUI
	 */
	private void initGUI() {
		JButton button_startserver;
		JButton button_join;
		JButton button_warten;
		Auswahl_action al=new Auswahl_action(this);
		this.addWindowListener(al);
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				tf_ip = new JTextField();
				this.getContentPane().add(tf_ip, new AnchorConstraint(163,437, 285, 42, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				tf_ip.setText("Enter IP");
				tf_ip.setPreferredSize(new java.awt.Dimension(155, 30));
			}
			{
				button_join = new JButton();
				this.getContentPane().add(button_join, new AnchorConstraint(163,940, 283, 501, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				button_join.setText("Join Session");
				button_join.setPreferredSize(new java.awt.Dimension(173, 32));
				button_join.addActionListener(al);
			}
			{
				button_warten = new JButton();
				this.getContentPane().add(button_warten, new AnchorConstraint(667,940, 791, 496, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				button_warten.setText("Warte auf Einladung");
				button_warten.setPreferredSize(new java.awt.Dimension(174, 33));
				button_warten.setSelected(true);
				button_warten.addActionListener(al);
			}
			{
				button_startserver = new JButton();
				this.getContentPane().add(button_startserver, new AnchorConstraint(419,940, 539, 501,AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				button_startserver.setText("Hoste Session");
				button_startserver.setPreferredSize(new java.awt.Dimension(175, 32));
				button_startserver.addActionListener(al);
			}
			this.getRootPane().setDefaultButton(button_join);
			this.setTitle("Virtueller Arbeitsraum 0.8   [" + username + "]");
			pack();
			setSize(400, 300);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Die Klasse stellt den ActionListener für die Kalsse Auswahl zur Verfügung
	 * @author Daniel Meuer
	 */
	public class Auswahl_action implements ActionListener, WindowListener{
		
		private JFrame owner;
		/**
		 * Konstruktor
		 * @param owner Der zu dem Listener gehörende JFrame
		 */
		public Auswahl_action(JFrame owner){
			this.owner=owner;
		}
		public void actionPerformed(ActionEvent e){
			System.out.println(e.getActionCommand());
			if(e.getActionCommand().equals("Hoste Session")){ //HOSTEN
				try {
					new Server(username);
					System.out.println("Server gestartet");
					Main main=new Main(username);
					main.setVisible(true);
					owner.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					Error err=new Error("Fehler","\n\nServer konnte nicht erstellt werden.",owner);					
					err.setVisible(true);
	
				}
	
			}
			else if(e.getActionCommand().equals("Join Session")){ //JOIN IP
					try {
						String ip=tf_ip.getText();
						Main main=new Main(ip,username);
						main.setVisible(true);
						owner.dispose();
					} catch (Exception e1) {
						e1.printStackTrace();
						Error err=new Error("Fehler","\n\nBitte überprüfen Sie die IP-Adresse.",owner);					
						err.setVisible(true);
	
					}
	
			}
		}
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowClosing(WindowEvent arg0) {
			try {
				Class.forName("com.mysql.jdbc.Driver");			
				Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
				Statement statement = connection.createStatement();	
				statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+username+"';");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
