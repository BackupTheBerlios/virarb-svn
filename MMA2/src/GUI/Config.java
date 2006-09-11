package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
 * Die Klasse Config ist ein JDialog, der
 * dem Nutzer die Möglichkeit gibt den Virtuellen
 * Arbeitsraum zu konfigurieren
 * @author Daniel Meurer
 */
public class Config extends JDialog {
	private String port;// serverport;
	private File f = new File("VirArb.cfg");
	private JTextField tf_port;// tf_serverport;
	
	/**
	 * Konstruktor
	 * @throws Exception
	 */
	public Config() throws Exception{
		super();
		if(!f.exists()){
			Fileausgabe.setProperty("Port","1234");
			Fileausgabe.setProperty("ServerPort","1234");
		}
		port = Fileausgabe.getProperty("Port");
//        serverport =Fileausgabe.getProperty("ServerPort");
 		initGUI();
	}
	
	/**
	 * Initialiert das UserInterface
	 */
	private void initGUI() {
		try {
			JButton button_save, button_cancel;
			JPanel panel_network;
			JLabel label_port, label_serverport;	
			Border lowerededge = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Config_action al = new Config_action(this);
			
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setModal(true);
			
			this.getContentPane().setLayout(null);
			
			button_save = new JButton();
			this.getContentPane().add(button_save);
			button_save.setText("Speichern");
			button_save.setPreferredSize(new java.awt.Dimension(158, 31));
			button_save.setBounds(330, 230, 150, 30);
			button_save.addActionListener(al);
			button_save.setActionCommand("save");
			
			button_cancel = new JButton();
			this.getContentPane().add(button_cancel);
			button_cancel.setText("Abbrechen");
			button_cancel.setBounds(170, 230, 150, 30);
			button_cancel.addActionListener(al);
			button_cancel.setActionCommand("cancel");
			
			panel_network = new JPanel();
			this.getContentPane().add(panel_network);
			panel_network.setBounds(20, 10, 460, 100);
			panel_network.setLayout(null);
			
			TitledBorder title2 = BorderFactory.createTitledBorder(lowerededge, null);
			title2.setTitlePosition(TitledBorder.ABOVE_TOP);
			title2.setTitle("Netzwerkeinstellungen");
			panel_network.setBorder(title2);
				
			tf_port = new JTextField(5);
			tf_port.setDocument(new IntegerDocument());			
			tf_port.setBounds(370, 30, 80, 25);		

			tf_port.setText(port);
			panel_network.add(tf_port);
			
			label_port = new JLabel();
			label_port.setText("Port");
			label_port.setBounds(250, 30, 100, 25);
			panel_network.add(label_port);		
			
//			tf_serverport = new JTextField(5);
//			tf_serverport.setDocument(new IntegerDocument());			
//			tf_serverport.setBounds(370, 60, 80, 25);		
//
//			tf_serverport.setText(serverport);
//			panel_network.add(tf_serverport);
//			
//			label_serverport = new JLabel();
//			label_serverport.setText("Server Port");
//			label_serverport.setBounds(250, 60, 100, 25);
//			panel_network.add(label_serverport);		

			this.setTitle("Virtueller Arbeitsraum - Konfiguration");
			this.setResizable(false);
			this.getRootPane().setDefaultButton(button_save);
			this.setLocationRelativeTo(null);
			this.setSize(500, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return den eingestellten Port des Clients
	 */
	public String getPort(){
		port = tf_port.getText();
		return port;
	}
	
	/**
	 * @return den ConfigFile VirArb.cfg auf dem lokalen Rechner
	 */
	public File getFile() {
		return f;
	}

//	/**
//	 * @return den Port des Servers
//	 */
//	public String getServerport() {
//		serverport = tf_serverport.getText();
//		return serverport;
//	}
}

/**
 * Die Klasse Config_Action beinhaltet alle zu Klasse
 * Config gehörigen Aktionen
 * @author Dani1
 */
class Config_action implements ActionListener{
	private Config owner;
	
	/**
	 * Konstruktor
	 * @param owner der aufrufende Dialog
	 */
	public Config_action(Config owner) {
		super();
		this.owner = owner;
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("save")){
			Fileausgabe.setProperty("Port",owner.getPort());
//			Fileausgabe.setProperty("ServerPort",owner.getServerport());
			try {
				Class.forName("com.mysql.jdbc.Driver");			
				Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
				Statement statement = connection.createStatement();	
				System.out.print("UPDATE UserOnline SET Port='"+Ip.getMyPort()+"' WHERE WanIp='"+Ip.getWanIp()+" AND LanIp='"+Ip.getLanIp()+"';");

				statement.executeUpdate("UPDATE UserOnline SET Port='"+Ip.getMyPort()+"' WHERE WanIp='"+Ip.getWanIp()+"' AND LanIp='"+Ip.getLanIp()+"';");
			}
			catch(Exception e1){
				e1.printStackTrace();
			}			
			owner.dispose();
		}
		else if(e.getActionCommand().equals("cancel")){
			owner.dispose();
		}		
	}	
}

