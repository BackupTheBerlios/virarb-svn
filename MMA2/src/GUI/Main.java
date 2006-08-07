package GUI;

import gnu.cajo.invoke.Remote;
import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ItemProxy;
import gnu.cajo.utils.extra.Xfile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.*;
import Server.*;

public class Main extends javax.swing.JFrame{

	private JTextPane ta_chat;
	private DrawPanel malfenster;
	private DnDText filetable;
	private JTextField tf_status,tf_chateingabe;
	private String username="User";
	private Color myColor=Color.BLACK;
	private SimpleAttributeSet set;
	private Object server;
//	private ClientProxy cp;
//	private ItemProxy proxy;
//	private ItemProxy fproxy;
	private Xfile xfile = new Xfile(64 * 1024);
	private boolean serverAvailable = true;

	
	/**
	 * Konsruktor
	 * @param username Der Name des Users
	 **/
	public Main(String username){	
		super();
		this.username=username;
		String[] args = {username, Ip.getLanIp(), Ip.getWanIp()};
		try {
	   		server = Remote.getItem("//"+Ip.getLanIp()+":1234/VirArbServer");
	   		RemoteInvoke cp = (RemoteInvoke)Remote.invoke(server, "getCp", args);
	   		new ItemProxy(cp, this);
	   		xfile.remoteInvoke = true;
	   		ItemServer.bind(xfile, "xfile");
	   		myColor = (Color) Remote.invoke(server, "getMyColor", null);
	   	} catch (Exception e) {
			e.printStackTrace();
		}
		initGUI();
		try {
			Remote.invoke(server, "setStatus", "Server gestartet.");
			sendMessage(new Chatmessage(Color.BLACK,"Server gestartet von '"+username+"' unter der IP: "+Ip.getLanIp(),new Date(),"System"));	
			sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' ist der Sitzung beigetreten.",new Date(),"System"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Konstruktor
	 * @param ip Die IP-Adresse des Servers.
	 * @param username Der Name des Users.
	 */
	public Main(String ip,String username) throws Exception{
		super();
		this.username=username;
		String[] args = {username, Ip.getLanIp(), Ip.getWanIp()};
		String lanIp = ip;
		String wanIp = ip;
		try {
			Class.forName("com.mysql.jdbc.Driver");		
			Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");		
			Statement statement = connection.createStatement();	
			
			String abfrage="SELECT LanIp,WanIp FROM UserOnline WHERE Nickname='"+ip+"';";
			ResultSet x = statement.executeQuery(abfrage);
			if(!x.next()){
				throw new Exception("Kein Server gefunden!");
			}
			else{
				lanIp = x.getString(1);
				wanIp = x.getString(2);
			}
		} catch (Exception e2) {
			throw new Exception("Keine Verbindung zur Datenbank");
//			e2.printStackTrace();
		}
		
		try {
			server = Remote.getItem("//"+lanIp+":1234/VirArbServer");
	   		RemoteInvoke cp = (RemoteInvoke)Remote.invoke(server, "getCp", args);
	   		new ItemProxy(cp, this); 	
	   		ItemServer.bind(xfile, "xfile");
	   		myColor = (Color) Remote.invoke(server, "getMyColor", null);
	   	} catch (Exception e) {
//	   	   System.out.println("Server im lokalen Netz nicht gefunden. Versuch �ber WanIp");
	   	   if(!lanIp.equals(wanIp)){
		   	   try {
			   		server = Remote.getItem("//"+wanIp+":1234/VirArbServer");
			   		RemoteInvoke cp = (RemoteInvoke)Remote.invoke(server, "getCp", args);
			   		new ItemProxy(cp, this);
			   		ItemServer.bind(xfile, "xfile");
			   		myColor = (Color) Remote.invoke(server, "getMyColor", null);
		   	   }
		   	   catch(Exception e1){
		   		   throw new Exception("Kein Server gefunden!");
		   	   }
	   	   }
	   	   else{
	   		   throw new Exception("Kein Server gefunden!");
 
	   	   }
		}
	   	initGUI();
		try {
			ta_chat.setDocument((Document)Remote.invoke(server, "getChat", null));
			Remote.invoke(server, "setStatus", "User '"+username+"' ist der Sitzung beigetreten.");
			sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' ist der Sitzung beigetreten.",new Date(),"System"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * initialiert und l�dt das Userinterface.
	 */
	private void initGUI() {
		JPanel pa_left,pa_right,pa_file,pa_paint;
		JScrollPane scroll_chat;
		JButton button_chat;
		
		set=new SimpleAttributeSet();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Border lineborder = BorderFactory.createLineBorder(Color.black);
		Border lowerededge = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		
		Main_action al=new Main_action(this);
		
		JMenuBar mbar = new JMenuBar();
		
		JMenu aktionen = new JMenu("Menu");
		JMenuItem aktionen1 = new JMenuItem("Arbeitsraum verlassen");
		aktionen1.addActionListener(al);
		aktionen.add(aktionen1);
		JMenuItem aktionen2 = new JMenuItem("Programm schlie�en");
		aktionen2.addActionListener(al);
		aktionen.add(aktionen2);	
		
		JMenu hilfe = new JMenu("Help");
		JMenuItem hilfe1 = new JMenuItem("Hilfe");
		hilfe1.addActionListener(al);
		hilfe.add(hilfe1);	
		JMenuItem hilfe2 = new JMenuItem("Info");
		hilfe2.addActionListener(al);
		hilfe.add(hilfe2);
		
		mbar.add(aktionen);
		mbar.add(hilfe);
		
		this.setJMenuBar(mbar);
		
		try {
			this.getContentPane().setLayout(new BorderLayout());
			{
				
				pa_left = new JPanel();
				pa_left.setLayout(new BorderLayout());
				pa_left.setPreferredSize(new Dimension(400, 300));

				JPanel pa_eingaben=new JPanel();
				pa_eingaben.setLayout(new BorderLayout());
				pa_eingaben.setPreferredSize(new Dimension(300,30));
				
				tf_chateingabe = new JTextField();
				tf_chateingabe.setForeground(myColor);
				tf_chateingabe.setText("hier text einf�gen");
				tf_chateingabe.setPreferredSize(new Dimension(250, 25));
				tf_chateingabe.setMinimumSize(new Dimension(280,25));
//				tf_chateingabe.addActionListener(new chateingabe_senden());
				pa_eingaben.add(tf_chateingabe);
				
				button_chat = new JButton();
				button_chat.setText("Senden");
				button_chat.setPreferredSize(new Dimension(90, 25));
				button_chat.addActionListener(al);
				pa_eingaben.add(button_chat,BorderLayout.EAST);			

				
				pa_left.add(pa_eingaben,BorderLayout.SOUTH);
				
				ta_chat = new JTextPane();
				ta_chat.setEditable(false);
				ta_chat.setFocusable(false);
				ta_chat.setAutoscrolls(true);
				ta_chat.setCharacterAttributes(set,true);		
				
				
				scroll_chat = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scroll_chat.getViewport().setAutoscrolls(true);
				scroll_chat.getViewport().add(ta_chat);			
//				scroll_chat.setPreferredSize(new Dimension(300,150));
				pa_left.add(scroll_chat);
			
			}
			
			{
				pa_right = new JPanel();
				pa_right.setPreferredSize(new java.awt.Dimension(320, 300));
				pa_right.setBackground(Color.LIGHT_GRAY);
				pa_right.setLayout(new BorderLayout());
				

				{
					pa_paint = new JPanel();
					pa_paint.setLayout(new BorderLayout());
				
					JButton button_loeschen=new JButton("Zeichnung loeschen");
					button_loeschen.addActionListener(al);
					pa_paint.add(button_loeschen,BorderLayout.SOUTH);
			
					
				
					JPanel pa_malfenster =new JPanel();
					
					malfenster = new DrawPanel(server, myColor);
					malfenster.setPreferredSize(new Dimension(300, 300));
					malfenster.setBackground(new Color(255, 255, 255));
					
					pa_malfenster.add(malfenster);
					pa_malfenster.setBorder(lineborder);
					pa_paint.add(pa_malfenster,BorderLayout.CENTER);
					
					TitledBorder title = BorderFactory.createTitledBorder(lowerededge, null);
					title.setTitlePosition(TitledBorder.ABOVE_TOP);
					title.setTitlePosition(TitledBorder.BELOW_TOP);
					title.setTitle("Skizzenblatt - Mit der Maus malen.");
					pa_paint.setBorder(title);
					pa_right.add(pa_paint, BorderLayout.NORTH);
						
				}
				{
					
					pa_file = new JPanel();
					pa_file.setLayout(new BorderLayout());
					
					filetable = new DnDText(server, username);
					filetable.setBackground(new Color(200, 200, 200));
					filetable.setPreferredSize(new Dimension(290, 250));
					filetable.setAutoscrolls(true);
					pa_file.add(filetable);
					TitledBorder title = BorderFactory.createTitledBorder(lowerededge, null);
					title.setTitlePosition(TitledBorder.ABOVE_TOP);
					title.setTitlePosition(TitledBorder.BELOW_TOP);
					title.setTitle("FileTable - Files per Drag&Drop einf�gen!");
					pa_file.setBorder(title);
					pa_right.add(pa_file);
				}
				
				tf_status=new JTextField("status");
				tf_status.setEditable(false);
				tf_status.setBorder(lineborder);
				tf_status.setFont(new Font(null,Font.ITALIC,10));
				
				
				this.add(tf_status,BorderLayout.SOUTH);
				pa_left.setBorder(lowerededge);
				this.getContentPane().add(pa_left);
//				pa_right.setBorder(loweredbevel);
				this.getContentPane().add(pa_right,BorderLayout.EAST);
			}

			pack();
//			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			this.getRootPane().setDefaultButton(button_chat);
			this.setSize(800, 600);
			this.setLocationRelativeTo(null);
			this.setTitle("Virtueller Arbeitsraum 0.8   [" + username + "]");
			this.addWindowListener(al);
			malfenster.draw((Vector)Remote.invoke(server, "getLines", null));
			
			
//			Dummys senden damit verbindung nicht gekappt wird
			Thread t1 =new Thread(new DummyPakete(server));
			t1.start();
			
			Thread t2 = new Thread(new ServerChecker());
			t2.start();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * Die Methode empf�ngt Nachrichten vom Server und stellt diese im Chatfenster dar.
	 * @param rein Die empfangene Chatmessage.
	 */
	public void receiveMessage(Chatmessage rein) {
			StyleConstants.setForeground(set,rein.getColor());
			write("\n"+rein.getUser()+" ["+rein.getTime()+"]: "+rein.getMessage());
			ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

	}

	
	

	
	/**
	 * Die Methode sendMessage() schickt eine Chatmessage an den Server, wo sie dann verteilt wird.
	 * @param raus Die Chatmessage, die gesendet werden soll.
	 * @throws RemoteException
	 */
	public void sendMessage(Chatmessage raus) throws Exception {
			Remote.invoke(server, "postMessage", raus);
	}
	
	//string in chatfeld ausgeben
	/**
	 * �ber diese Methode wird ein String im Chatfenster ausgegeben.
	 * @param x Der auszugebende Text.
	 */
	public void write(String x){
		Document temp=ta_chat.getDocument();
		try {
			temp.insertString(temp.getLength(),x,set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Gibt das Malfenster des Mainfensters zur�ck
	 * @return das Malfenster
	 */
	public DrawPanel getMalfenster() {
		return malfenster;
	}
	
	public void draw(Vector lines){
		malfenster.draw(lines);
	}
	
	
	
	//FILETRANSFER--------------------->

	/**
	 * �ber diese Methode wird ein neuer File(name) im Filetable eingetragen.
	 * @param filename Der Name der Datei.
	 */
	public void receiveNewFile(String filename) {
		filetable.addElement(filename);
	}
		
	//Statusleiste
	/**
	 * �ber diese Methode wird ein neuer Text in der Statusleiste angezeigt
	 * @param status Die auszugebende Nachricht
	 */
	public void setStatus (String status){
		this.tf_status.setText(status);
	}
	
	
	/**
	 * Gibt das Chatdocument zur�ck, um einem neuen User, den Zugriff auf vorher geschriebenes zu erm�glichen
	 * @return das Chatdocument
	 */
	public Document getChat(){
		return ta_chat.getDocument();
	}
	

	public void setServerUnavailable() {
		this.serverAvailable = false;
	}
	
	
	public void leaveOnYourOwn(){
		try {
			Remote.invoke(server, "removeParticipant", username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Error err = new Error("Info","Sie haben den Arbeitsraum verlassen.",this);
		err.setVisible(true);
		Auswahl a=new Auswahl(username);
		a.setVisible(true);
		this.dispose();		
	}
	
	public void leaveByServer(){
		Error err = new Error("Info","Der Arbeitsraum wurde geschlossen.",this);
		err.setVisible(true);
		Auswahl a=new Auswahl(username);
		a.setVisible(true);
		this.dispose();		
	}
	
	/**
	 * DIe Klasse stellt den ActionListener f�r die Klasse Main zur Verf�gung.
	 * @author Klassen,Kokoschka,Langer,Meurer
	 *
	 */
	public class Main_action implements ActionListener,WindowListener{
		
		private Main owner;
		
		/**
		 * Konstruktor
		 * @param owner Der zu dem Listener geh�rende JFrame
		 */
		public Main_action(Main owner){
			this.owner=owner;
		}
		
		public void actionPerformed(ActionEvent e){
			
			if(e.getActionCommand().equals("Senden")){
				String inp=tf_chateingabe.getText();
				if(inp.length()>0){
					Chatmessage raus=new Chatmessage(myColor,inp,new Date(),username);				
					try {
						sendMessage(raus);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				tf_chateingabe.setText("");
			}
			else if(e.getActionCommand().equals("Zeichnung loeschen")){
						
				try {
					Remote.invoke(server, "skizze_loeschen", null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getActionCommand().equals("Arbeitsraum verlassen")){
				try {
					if(((String)Remote.invoke(server, "getStarter", null)).equals(username)){
						Remote.invoke(owner.server, "shutDown", null);
					}			
					else{
						leaveOnYourOwn();
					}
				} catch (Exception e1) {					
					e1.printStackTrace();
					leaveOnYourOwn();
				}
			}
			else if(e.getActionCommand().equals("Programm schlie�en")){
				try {
					Class.forName("com.mysql.jdbc.Driver");			
					Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
					Statement statement = connection.createStatement();	
					statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+username+"';");				
					if(((String)Remote.invoke(server, "getStarter", null)).equals(username)){
						Remote.invoke(owner.server, "shutDown", null);
					}	
					else{
						Remote.invoke(server, "removeParticipant", username);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.exit(0);	
			}
			else if(e.getActionCommand().equals("Hilfe")){
				Error help = new Error("Hilfe","Hier gibts irgendwann mal Hilfe",owner);
				help.setVisible(true);
			}
			else if(e.getActionCommand().equals("Info")){
				Error info = new Error("Info","'Virtueller Arbeitsraum'\n2006\nKlassen,Kokoschka,Meurer",owner);
				info.setVisible(true);
			}

		}
		

		public void windowActivated(WindowEvent arg0) {
		}
		
		public void windowClosed(WindowEvent arg0) {
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
		 */
		public void windowClosing(WindowEvent arg0) {
			try {
				Class.forName("com.mysql.jdbc.Driver");			
				Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
				Statement statement = connection.createStatement();	
				statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+username+"';");
//				session.setStatus("User "+session.getNickname()+ " hat die Sitzung verlassen");
				sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' hat die Sitzung verlassen",new Date(),"System"));
				if(((String)Remote.invoke(server, "getStarter", null)).equals(username)){
					Remote.invoke(owner.server, "shutDown", null);
				}			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);	
		}
		public void windowDeactivated(WindowEvent arg0) {
		}

		public void windowDeiconified(WindowEvent arg0) {
		}

		public void windowIconified(WindowEvent arg0) {
		}

		public void windowOpened(WindowEvent arg0) {
		}
		
	}
	
	public class ServerChecker implements Runnable{
		public void run() {		
			while(serverAvailable == true){
//				System.out.println("Check server");
				try {
					Thread.sleep(1000 * 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			leaveByServer();
		}
	}



}






