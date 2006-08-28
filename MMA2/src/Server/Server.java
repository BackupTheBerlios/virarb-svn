package Server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ClientProxy;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.text.Document;
import GUI.Chatmessage;
import GUI.ColorLine;
import GUI.ColorVector;
import GUI.Ip;
import GUI.ListEntry;
import GUI.NamedColor;

public class Server {

	private List participantList = new ArrayList();
	private DefaultListModel values;
	private Vector lines = new Vector();
	private int count=0;
	//private static Color[] colortable = { Color.RED, Color.CYAN,Color.MAGENTA, Color.ORANGE, Color.PINK, Color.GREEN };
	private static ColorVector colortable = new ColorVector();
	private String starter;
	private Remote remoteRef;
	private final int port = Ip.getMyPort();

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
			Remote.config(lanIp, port, wanIp, port);
			remoteRef =  ItemServer.bind(this, "VirArbServer");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lines.addElement(new String("malen"));
		values=new DefaultListModel();	

		System.out.println("Server gestartet unter lokaler Ip " + lanIp +":"+port);
		System.out.println("Server gestartet unter �ffentlicher Ip " + wanIp+":"+port );
		}
	  
	   public Remote getCp(String username, String lanIp, String wanIp, String port) throws Exception {
	      ClientProxy cp = new ClientProxy();
	      Participant p = new Participant(username, lanIp, wanIp, Integer.parseInt(port), cp);
	      participantList.add(p);
	      System.out.println("User "+username+" joined!");
	      return cp.remoteThis;
	   }

	/* (non-Javadoc)
	 * @see Server.ChatServer#getMyColor()
	 */
	public Color getMyColor() {
//		return colortable[count++ % colortable.length];
		return ((NamedColor)colortable.get(count++ % colortable.size())).getColor();
	}
	
	public ColorVector getColortable(){
		System.out.print(colortable.toString());
		return colortable;
	}
	
	public void postMessage(Chatmessage message) {
		Participant p;
		message.setTime(new Date());
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveMessage", message);
			} catch (Exception ex) {
				removeParticipant(p);
				i--; 
				setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gel�scht");
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
			participantList.remove(p);
			setStatus("User "+p.getName()+ " hat die Sitzung verlassen");
			postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFile(String name, File f ){	
		ListEntry entry = new ListEntry(f, name);
		values.addElement(entry);
		Participant p;
		for (int i = 0; i < participantList.size(); i++) 
		{
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveNewFile", entry);	
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
			}
		}
	}
	
	public Object[] getFile(int[] index){	
		ListEntry e = (ListEntry)values.get(index[0]);
		Participant p1 = (Participant)participantList.get(participantList.indexOf(new Participant(e.getOwner())));
		Object[] xf = new Object[2];
		xf[1] = e.getFile();
		try {
			xf[0] = Remote.getItem("//"+p1.getLanIp()+":"+p1.getPort()+"/xfile");	
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				xf[0] = Remote.getItem("//"+p1.getWanIp()+":"+p1.getPort()+"/xfile");	
			} catch (Exception e2) {
				e2.printStackTrace();				
			}	
		}	
		return xf;
	}
	
	public void removeFile(ListEntry entry ){	
		values.removeElement(entry);
		Participant p;
		for (int i = 0; i < participantList.size(); i++) 
		{
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "removeFile", entry);	
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
			}
		}
		setStatus("Die Datei '"+entry.getFile().getName()+"' wurde erfolgreich gel�scht.");
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
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);	
			} catch (Exception e) {
				e.printStackTrace();
				removeParticipant(p);
				i--;
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
					setStatus("Die Verbindung zu User '"+p.getName()+"' ist leider abgerissen. Session wurde gel�scht");
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
