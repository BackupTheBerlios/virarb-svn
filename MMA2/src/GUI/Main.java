package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;
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
	private ChatSession session;
	private ChatServer server;

//	private static String myIP=new String("81.173.229.84");
	
	
	
//	/**
//	 * Konstruktor
//	 */
//	public Main(){
//		super();
////		try {
////			ip=InetAddress.getLocalHost().getHostAddress();
////		} catch (UnknownHostException e) {
////			e.printStackTrace();
////		}
//		initGUI();
//	}
	
	/**
	 * Konsruktor
	 * @param username Der Name des Users
	 * 	 */
	public Main(String username){	
		super();
//		System.setProperty("java.rmi.server.hostname","localhost");	
		String ip=new String();
		this.username=username;
	   	try {
	   		server = (ChatServer)Naming.lookup("rmi://localhost:1099/chat-server");
	   		ClientHandleImpl handle = new ClientHandleImpl(this);
	   		session = server.createSession(username, handle);
	   	
	   		myColor=server.getMyColor();
	   		ip=InetAddress.getLocalHost().getHostAddress();
	
	   	} catch (Exception e) {
			e.printStackTrace();
		}
		initGUI();
		try {
			session.setStatus("Server gestartet.");
			sendMessage(new Chatmessage(Color.BLACK,"Server gestartet von '"+username+"' unter der IP: "+ip,new Date(),"System"));	
			sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' ist der Sitzung beigetreten.",new Date(),"System"));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Konstruktor
	 * @param ip Die IP-Adresse des Servers.
	 * @param username Der Name des Users.
	 */
	public Main(String ip,String username){
		super();
//		System.setProperty("java.rmi.server.hostname",myIP);	
		
		this.username=username;

	   	try {
	   		server = (ChatServer)Naming.lookup("rmi://"+ip+":1099/chat-server");
	   		ClientHandleImpl handle = new ClientHandleImpl(this);
	   		session = server.createSession(username, handle);
	   		myColor=server.getMyColor();

	   	} catch (Exception e) {
			e.printStackTrace();
		}
		initGUI();
		try {
			ta_chat.setDocument(server.getChat());
			session.setStatus("User '"+username+"' ist der Sitzung beigetreten.");
			sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' ist der Sitzung beigetreten.",new Date(),"System"));

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * initialiert und lädt das Userinterface.
	 */
	private void initGUI() {
		JPanel pa_left,pa_right,pa_file,pa_paint;
		JScrollPane scroll_chat;
		JButton button_chat;
		
		set=new SimpleAttributeSet();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Border lineborder = BorderFactory.createLineBorder(Color.black);
		Border lowerededge = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		
		Main_action al=new Main_action();
		
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
				tf_chateingabe.setText("hier text einfügen");
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
					
					malfenster = new DrawPanel(session,myColor);
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
					
					filetable = new DnDText(session);
					filetable.setBackground(new Color(200, 200, 200));
					filetable.setPreferredSize(new Dimension(290, 250));
					filetable.setAutoscrolls(true);
					pa_file.add(filetable);
					TitledBorder title = BorderFactory.createTitledBorder(lowerededge, null);
					title.setTitlePosition(TitledBorder.ABOVE_TOP);
					title.setTitlePosition(TitledBorder.BELOW_TOP);
					title.setTitle("FileTable - Files per Drag&Drop einfügen!");
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
			malfenster.draw(server.getLines());
			
			
//			Dummys senden damit verbindung nicht gekappt wird
			Thread t =new Thread(new DummyPakete(server));
			t.start();
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * Die Methode empfängt Nachrichten vom Server und stellt diese im Chatfenster dar.
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
	public void sendMessage(Chatmessage raus) throws RemoteException {
			session.sendMessage(raus);
	}
	
	//string in chatfeld ausgeben
	/**
	 * Über diese Methode wird ein String im Chatfenster ausgegeben.
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
	 * Gibt das Malfenster des Mainfensters zurück
	 * @return das Malfenster
	 */
	public DrawPanel getMalfenster() {
		return malfenster;
	}
	
	
	
	//FILETRANSFER--------------------->

	/**
	 * Über diese Methode wird ein neuer File(name) im Filetable eingetragen.
	 * @param filename Der Name der Datei.
	 */
	public void receiveNewFile(String filename) {
		filetable.addElement(filename);
	}
	
	
	

	
	//Statusleiste
	/**
	 * Über diese Methode wird ein neuer Text in der Statusleiste angezeigt
	 * @param status Die auszugebende Nachricht
	 */
	public void setStatus (String status){
		this.tf_status.setText(status);
	}
	
	
	/**
	 * Gibt das Chatdocument zurück, um einem neuen User, den Zugriff auf vorher geschriebenes zu ermöglichen
	 * @return das Chatdocument
	 */
	public Document getChat(){
		return ta_chat.getDocument();
	}
	
	

	
	/**
	 * DIe Klasse stellt den ActionListener für die Klasse Main zur Verfügung.
	 * @author Klassen,Kokoschka,Langer,Meurer
	 *
	 */
	public class Main_action implements ActionListener,WindowListener{
		public void actionPerformed(ActionEvent e){
			
			if(e.getActionCommand().equals("Senden")){
				String inp=tf_chateingabe.getText();
				if(inp.length()>0){
					Chatmessage raus=new Chatmessage(myColor,inp,new Date(),username);				
					try {
						sendMessage(raus);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
				tf_chateingabe.setText("");
			}
			else if(e.getActionCommand().equals("Zeichnung loeschen")){
						
				try {
					server.skizze_loeschen();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
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
				session.setStatus("User "+session.getNickname()+ " hat die Sitzung verlassen");
				sendMessage(new Chatmessage(Color.BLACK,"User '"+username+"' hat die Sitzung verlassen",new Date(),"System"));
				session.removeMe();
			} catch (RemoteException e) {
				e.printStackTrace();
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




}






