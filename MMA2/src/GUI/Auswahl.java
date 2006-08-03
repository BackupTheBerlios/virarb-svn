package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import Server.Server;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Auswahl extends JFrame {

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
		JButton button_startserver, button_join;
		JLabel label_ip, label_host1, label_host2;
		JPanel hostpanel, joinpanel;
		Auswahl_action al=new Auswahl_action(this);
		this.addWindowListener(al);
		try {
			
			JMenuBar mbar = new JMenuBar();
			
			JMenu aktionen = new JMenu("Menu");
			JMenuItem aktionen1 = new JMenuItem("Ausloggen");
			aktionen1.addActionListener(al);
			aktionen1.setActionCommand("logout");
			aktionen.add(aktionen1);
			JMenuItem aktionen2 = new JMenuItem("Programm schlie�en");
			aktionen2.setActionCommand("close");
			aktionen2.addActionListener(al);
			aktionen.add(aktionen2);	
			
			JMenu hilfe = new JMenu("Help");
			JMenuItem hilfe1 = new JMenuItem("Hilfe");
			hilfe1.addActionListener(al);
			hilfe1.setActionCommand("help");
			hilfe.add(hilfe1);	
			JMenuItem hilfe2 = new JMenuItem("Info");
			hilfe2.addActionListener(al);
			hilfe2.setActionCommand("info");
			hilfe.add(hilfe2);
			
			mbar.add(aktionen);
			mbar.add(hilfe);		
			this.setJMenuBar(mbar);
			
			this.getContentPane().setLayout(new BorderLayout(25,20));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			Border lowerededge = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

			joinpanel = new JPanel(null);
			joinpanel.setPreferredSize(new Dimension(490, 100));
			hostpanel = new JPanel(null);
			hostpanel.setPreferredSize(new Dimension(490, 100));		
		
			label_ip = new JLabel("Hier den Namen des Ausrichters eintragen : ");
			label_ip.setBounds(25, 40, 300, 20);
			joinpanel.add(label_ip);

			tf_ip = new JTextField();
			tf_ip.setText("Name");		
				
			joinpanel.add(tf_ip);
			tf_ip.setBounds(100, 60, 200, 30);

			button_join = new JButton();
			button_join.setText("Raum betreten!");
			button_join.setActionCommand("join");
			button_join.addActionListener(al);
			
			joinpanel.add(button_join);
			button_join.setBounds(330, 60, 150, 30);

			TitledBorder title2 = BorderFactory.createTitledBorder(lowerededge, null);
			title2.setTitlePosition(TitledBorder.ABOVE_TOP);
			title2.setTitle("Einen offenen Raum betreten.");
			joinpanel.setBorder(title2);
		
			label_host1 = new JLabel("Hier klicken um einen eigenen Arbeitsraum");
			label_host1.setBounds(25, 40, 300, 20);
			hostpanel.add(label_host1);
			label_host2 = new JLabel("unter dem Namen '"+this.username+"' zu �ffnen.");
			label_host2.setBounds(25, 55, 300, 20);
			hostpanel.add(label_host2);
			
			button_startserver = new JButton();
			button_startserver.setText("Raum erstellen.");
			button_startserver.setActionCommand("host");
			button_startserver.addActionListener(al);
			button_startserver.setBounds(330, 60, 150, 30);
			
			hostpanel.add(button_startserver);

			TitledBorder title3 = BorderFactory.createTitledBorder(lowerededge, null);
			title3.setTitlePosition(TitledBorder.ABOVE_TOP);
			title3.setTitle("Einen neuen Arbeitsraum erstellen.");
			hostpanel.setBorder(title3);
			
			this.getContentPane().add(new JLabel(""), BorderLayout.NORTH);
			this.getContentPane().add(joinpanel, BorderLayout.CENTER);
//			joinpanel.setPreferredSize(new java.awt.Dimension(492, 89));
			this.getContentPane().add(hostpanel, BorderLayout.SOUTH);
//			hostpanel.setPreferredSize(new java.awt.Dimension(492, 83));
			hostpanel.setLayout(null);

			this.getRootPane().setDefaultButton(button_join);
			this.setTitle("Virtueller Arbeitsraum 0.8   [" + username + "]");
//			pack();
			setSize(500, 300);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Die Klasse stellt den ActionListener f�r die Kalsse Auswahl zur Verf�gung
	 * @author Daniel Meuer
	 */
	public class Auswahl_action implements ActionListener, WindowListener{
		
		private JFrame owner;
		/**
		 * Konstruktor
		 * @param owner Der zu dem Listener geh�rende JFrame
		 */
		public Auswahl_action(JFrame owner){
			this.owner=owner;
		}
		public void actionPerformed(ActionEvent e){
			System.out.println(e.getActionCommand());
			if(e.getActionCommand().equals("host")){ //HOSTEN
				try {
					new Server(username);
					System.out.println("Server gestartet");
					Main main=new Main(username);
					main.setVisible(true);
					owner.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					Error err=new Error("Fehler","\n\nEs konnte kein Arbeitsraum erstellt werden.",owner);					
					err.setVisible(true);
	
				}	
			}
			else if(e.getActionCommand().equals("join")){ //JOIN IP
				String ip=tf_ip.getText();

				try {
					Main main=new Main(ip,username);
					main.setVisible(true);
					owner.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					Error err=new Error("Fehler","\n\nUnter dem Namen '"+ip+"' \nist kein Arbeitsraum ge�ffnet.",owner);					
					err.setVisible(true);

				}
			}
			else if(e.getActionCommand().equals("logout")){
				try {
					Class.forName("com.mysql.jdbc.Driver");			
					Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
					Statement statement = connection.createStatement();	
					statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+username+"';");				
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Login x = new Login();
				x.setVisible(true);
				owner.dispose();
			}
			else if(e.getActionCommand().equals("close")){
				try {
					Class.forName("com.mysql.jdbc.Driver");			
					Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
					Statement statement = connection.createStatement();	
					statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+username+"';");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.exit(0);	
			}
			else if(e.getActionCommand().equals("help")){
				Error help = new Error("Hilfe","Hier gibts irgendwann mal Hilfe",owner);
				help.setVisible(true);
			}
			else if(e.getActionCommand().equals("info")){
				Error info = new Error("Info","'Virtueller Arbeitsraum'\n2006\nLanger,Klassen,Kokoschka,Meurer",owner);
				info.setVisible(true);
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
