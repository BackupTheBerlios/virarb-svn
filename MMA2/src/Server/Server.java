package Server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ClientProxy;
import gnu.cajo.utils.extra.Xfile;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.text.Document;
import GUI.Chatmessage;
import GUI.ColorLine;
import GUI.Ip;

public class Server {

	private List participantList = new ArrayList();
	private List files=new ArrayList();
	private DefaultListModel values;
	private Vector lines = new Vector();
	private int count=0;
	private static Color[] colortable = { Color.RED, Color.CYAN,Color.MAGENTA, Color.ORANGE, Color.PINK, Color.GREEN };
	private String starter;
	private Remote remoteRef;

	/**
	 * Konstruktor
	 * @throws Exception
	 */
	public Server(String starter) throws Exception {
		this.starter = starter;
		String wanIp = new String();
		String lanIp = new String();
		try {
			lanIp = Ip.getLanIp();
			wanIp = Ip.getWanIp(); 
			Remote.config(lanIp, 1234, wanIp, 1234);
			remoteRef =  ItemServer.bind(this, "VirArbServer");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lines.addElement(new String("malen"));
		values=new DefaultListModel();	

		System.out.println("Server gestartet unter lokaler Ip " + lanIp );
		System.out.println("Server gestartet unter öffentlicher Ip " + wanIp );
		}
	  
	   public Remote getCp(String username, String lanIp, String wanIp) throws Exception {
	      ClientProxy cp = new ClientProxy();
	      Participant p = new Participant(username, lanIp, wanIp, cp);
	      participantList.add(p);
	      System.out.println("User "+username+" joined!");
	      return cp.remoteThis;
	   }

	/* (non-Javadoc)
	 * @see Server.ChatServer#getMyColor()
	 */
	public Color getMyColor() {
		return colortable[count++ % colortable.length];
	}
	
	public void postMessage(Chatmessage message) {
		Participant p;
		message.setTime(new Date());
//		chat=chat+("\n"+message.getUser()+" ["+message.getTime()+"]: "+message.getMessage());
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveMessage", message);
			} catch (Exception ex) {
				removeParticipant(p);
				i--; 
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));

			}
		}
	}

	public void removeParticipant(String name) {
		removeParticipant(new Participant(name));
	}

	/* (non-Javadoc)
	 * @see Server.ChatServer#removeSession(Server.ChatSession)
	 */
	public void removeParticipant(Participant p) {
		try {
//			Class.forName("com.mysql.jdbc.Driver");			
//			Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
//			Statement statement = connection.createStatement();	
//			statement.execute("DELETE FROM `UserOnline` WHERE Nickname='"+p.name+"';");
			participantList.remove(p);
			setStatus("User "+p.getName()+ " hat die Sitzung verlassen");
			postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFile(String username, File f ){
		Object[] data = {username, f};
		files.add(data);
		String value = f.getName()+" ["+username+"]";
		values.addElement(value);
		Participant p;
		for (int i = 0; i < participantList.size(); i++) 
		{
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveNewFile", value);	
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
			}
		}
	}
	
	public Object[] getFile(int[] index){	
		Object[] o = (Object[])files.get(index[0]);
		String name = (String)o[0];
		Participant p1 = (Participant)participantList.get(participantList.indexOf(new Participant(name)));
		Object[] xf = new Object[2];
		xf[1] = o[1];
		try {
			xf[0] = Remote.getItem("//"+p1.getLanIp()+":1234/xfile");	
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				xf[0] = Remote.getItem("//"+p1.getWanIp()+":1234/xfile");	
			} catch (Exception e2) {
				e2.printStackTrace();				
			}	
		}	
		return xf;
	}

//	/* (non-Javadoc)
//	 * @see Server.ChatServer#addFile(byte[], java.lang.String)
//	 */
//	public void addFile(File f,String name,String lanIp, String wanIp){
//		Object[] temp={lanIp, wanIp,f};
//		files.add(temp);	
//		values.addElement(name);
//		Participant p;
//		for (int i = 0; i < participantList.size(); i++) {
//			p = (Participant) participantList.get(i);
//			try {
//				Remote.invoke(p.getCp(), "receiveNewFile", name);	
//			} catch (Exception e) {
//				e.printStackTrace();
//				removeParticipant(p);
//				i--;
////				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
////				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
//			}
//		}
//	}
	
//	/* (non-Javadoc)
//	 * @see Server.ChatServer#getFile(int)
//	 */
//	public Object[] getFile(int[] index){
//		return (Object[])files.get(index[0]);
//	}
	

	/* (non-Javadoc)
	 * @see Server.ChatServer#getValues()
	 */
	public DefaultListModel getValues(){
		return values;
	}
	
	
	/* (non-Javadoc)
	 * @see Server.ChatServer#addElement(GUI.ColorLine)
	 */
	public void addElement(ColorLine x) throws Exception{
		lines.add(x);
		Participant p;
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);	
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
//				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
//				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));

			}
		}
	}
	
	/* (non-Javadoc)
	 * @see Server.ChatServer#skizze_loeschen()
	 */
	public void skizze_loeschen(){
		Participant p;
		lines.removeAllElements();
		lines.addElement(new String("loeschen"));
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
//				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
//				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
			}
		}
		lines.setElementAt(new String("malen"),0);
	}
	
	 /* (non-Javadoc)
	 * @see Server.ChatServer#setStatus(java.lang.String)
	 */
	public void setStatus(String status){
		 Participant p;
			for (int i = 0; i < participantList.size(); i++) {
				p = (Participant) participantList.get(i);
				try {
					Remote.invoke(p.getCp(), "setStatus", status);
				} catch (Exception ex) {					
					removeParticipant(p);
					i--; 
					setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
					postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));

				}
			}
	 }

	/* (non-Javadoc)
	 * @see Server.ChatServer#getLines()
	 */
	public Vector getLines() {
		return lines;
	}

	/* (non-Javadoc)
	 * @see Server.ChatServer#getChat()
	 */
	public Document getChat() {		
		Participant p=(Participant)participantList.get(0);
		try {
			return (Document) Remote.invoke(p.getCp(),"getChat", null);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendDummy() throws Exception {
		// wir machen gar nix
//		System.out.println("Dummy");
	}

	public void shutDown(){
		Participant p;
		while (participantList.size() > 0) {
			p = (Participant) participantList.get(0);
			try {
				Remote.invoke(p.getCp(), "setServerUnavailable", null);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			participantList.remove(p);
		}	
		try {
			Remote.unexportObject(remoteRef, true);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public String getStarter() {
		return starter;
	}

}
