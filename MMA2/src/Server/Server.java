package Server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ClientProxy;
import java.awt.Color;
import java.io.File;
import java.net.InetAddress;
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

	private List sessions = new ArrayList();
	private List files=new ArrayList();
	private DefaultListModel values;
	private Vector lines = new Vector();
	private int count=0;
	private static Color[] colortable = { Color.RED, Color.CYAN,Color.MAGENTA, Color.ORANGE, Color.PINK, Color.GREEN };
	
	public static void main(String[] args){
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Konstruktor
	 * @throws Exception
	 */
	public Server() throws Exception {
		String wanIp = new String();
		String lanIp = new String();
		try {
			wanIp = Ip.getWanIp(); 
			lanIp = InetAddress.getLocalHost().getHostAddress();
			Remote.config(lanIp, 1234, wanIp, 1234);
		    ItemServer.bind(this, "VirArbServer");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lines.addElement(new String("malen"));
		values=new DefaultListModel();	

		System.out.println("Server gestartet unter lokaler Ip " + lanIp );
		System.out.println("Server gestartet unter öffentlicher Ip " + wanIp );
		}
	  
	   public Remote getCp(String name) throws Exception {
	      ClientProxy cp = new ClientProxy();
	      Participant p = new Participant(name, cp);
	      sessions.add(p);
	      System.out.println("User "+name+" joined!");
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
		for (int i = 0; i < sessions.size(); i++) {
			p = (Participant) sessions.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveMessage", message);
			} catch (Exception ex) {
				removeSession(p);
				i--; 
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));

			}
		}
	}


	/* (non-Javadoc)
	 * @see Server.ChatServer#removeSession(Server.ChatSession)
	 */
	public void removeSession(Participant p) {
		sessions.remove(p);
	}

	/* (non-Javadoc)
	 * @see Server.ChatServer#addFile(byte[], java.lang.String)
	 */
	public void addFile(File f,String name,String lanIp, String wanIp){
		Object[] temp={lanIp, wanIp,f};
		files.add(temp);	
		values.addElement(name);
		Participant p;
		for (int i = 0; i < sessions.size(); i++) {
			p = (Participant) sessions.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveNewFile", name);	
			} catch (Exception e) {
				e.printStackTrace();
				removeSession(p);
				i--;
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
			}
		}
	}
	/* (non-Javadoc)
	 * @see Server.ChatServer#getFile(int)
	 */
	public Object[] getFile(int[] index){
		return (Object[])files.get(index[0]);
	}
	

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
		for (int i = 0; i < sessions.size(); i++) {
			p = (Participant) sessions.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);	
			} catch (Exception e) {
				e.printStackTrace();
				removeSession(p);
				i--;
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));

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
		for (int i = 0; i < sessions.size(); i++) {
			p = (Participant) sessions.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);
			} catch (Exception e) {
				e.printStackTrace();
				removeSession(p);
				i--;
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
			}
		}
		lines.setElementAt(new String("malen"),0);
	}
	
	 /* (non-Javadoc)
	 * @see Server.ChatServer#setStatus(java.lang.String)
	 */
	public void setStatus(String status){
		 Participant p;
			for (int i = 0; i < sessions.size(); i++) {
				p = (Participant) sessions.get(i);
				try {
					Remote.invoke(p.getCp(), "setStatus", status);
				} catch (Exception ex) {					
					removeSession(p);
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
		Participant p=(Participant)sessions.get(0);
		try {
			return (Document) Remote.invoke(p.getCp(),"getChat",null);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendDummy() throws Exception {
		// wir machen gar nix
		System.out.println("Dummy");
	}
}
