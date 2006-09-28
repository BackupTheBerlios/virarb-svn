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

/**
 * Die Klasse Server gibt im Programm Virtueller Arbeitsraum
 * den Server, bzw stellt einen offenen Arbeitsraum dar.
 * @author Daniel Meurer
 */
/**
 * @author Dani1
 *
 */
/**
 * @author Dani1
 *
 */
public class Server {
	private List participantList = new ArrayList();
	private DefaultListModel values;
	private Vector lines = new Vector();
	private int count=0;
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
		System.out.println("Server gestartet unter öffentlicher Ip " + wanIp+":"+port );
		}
	  
	 /**
	 * Erstellt eine Session, einen Teilnehmer
	 * @param username der Name des Teilnehmers
	 * @param lanIp die lokale Ip des Teilnehmers
	 * @param wanIp die externe Ip des Teilnehmers
	 * @param port der Port des Teilnehmers
	 * @return RemoteObject des Servers
	 * @throws Exception
	 */
	public Remote getCp(String username, String lanIp, String wanIp, String port) throws Exception {
	      ClientProxy cp = new ClientProxy();
	      Participant p = new Participant(username, lanIp, wanIp, Integer.parseInt(port), cp);
	      participantList.add(p);
	      System.out.println("User "+username+" joined!");
	      return cp.remoteThis;
	   }


	/**
	 * Gibt eine, soweit möglich, eindeutige Farbe aus der Farbentabelle zurück.
	 * @return Color Die zugewiesene Farbe
	 */
	public Color getMyColor() {
		return ((NamedColor)colortable.get(count++ % colortable.size())).getColor();
	}
	
	/**
	 * Gibt die möglichen Farben als ColorVector zurück
	 * @return Colorvector Farben
	 */
	public ColorVector getColortable(){
//		System.out.print(colortable.toString());
		return colortable;
	}
	
	/**
	 * Eine Message verteilen
	 * @param message Die Nachricht
	 */
	public void postMessage(Chatmessage message) {
		Participant p;
		message.setTime(new Date());
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "receiveMessage", message);
			} catch(Exception e){
//				ex.printStackTrace();
//				System.out.println((new Date().getTime() - p.getLastDummy().getTime()));
				if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
					removeParticipant(p);
					i--; 
				}
			}
		}
	}

	/**
	 * Einen Teilnehmer löschen (zB wenn er die Sitzung verlässt)
	 * @param name der Name des Teilnehmers
	 */
	public void removeParticipant(String name) {
		removeParticipant(new Participant(name));
	}
	
	/* (non-Javadoc)
	 * @see Server.ChatServer#removeSession(Server.ChatSession)
	 */
	public void removeParticipant(Participant p) {
		System.out.println("Lösche User "+p.getName());
		try {
			participantList.remove(p);
			setStatus("User "+p.getName()+ " hat die Sitzung verlassen");
			postMessage(new Chatmessage(Color.BLACK,"User '"+p.getName()+"' hat die Sitzung verlassen",new Date(),"System"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Eine Datei dem FileTable hinzufügen
	 * @param name der Name des Files
	 * @param f die Datei
	 */
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
//				e.printStackTrace();
//				removeParticipant(p);
//				i--;
			}
		}
	}
	
	/**
	 * Gibt den File an der Stelle des index zurück
	 * @param index
	 * @return ein Obejctarray mit RemoteItem und Dateipfad
	 */
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
	
	/**
	 * Löscht eine Dateieintrag aus dem FileTable
	 * @param entry der Eintrag
	 */
	public void removeFile(ListEntry entry ){	
		values.removeElement(entry);
		Participant p;
		for (int i = 0; i < participantList.size(); i++) 
		{
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "removeFile", entry);	
			} catch (Exception e) {
//				e.printStackTrace();
//				removeParticipant(p);
//				i--;
			}
		}
		setStatus("Die Datei '"+entry.getFile().getName()+"' wurde erfolgreich gelöscht.");
	}

	
	/**
	 * Gibt die Liste aller bis hierher hochgeladenen Dateien zurück.
	 * @return DefaultListModel Die Dateienliste
	 */
	public DefaultListModel getValues(){
		return values;
	}
	
	
//	/**
//	 * Fügt dem Bild (DrawPanel) ein Element bzw eine Linie hinzu
//	 * (wird nicht mehr benötigt, da ersettz durch addElements(Vector))
//	 * @param x Colorline
//	 */
//	public void addElement(ColorLine x){
//		lines.add(x);
//		Participant p;
//		for (int i = 0; i < participantList.size(); i++) {
//			p = (Participant) participantList.get(i);
//			try {
//				Remote.invoke(p.getCp(), "draw", lines);	
//			} catch (Exception e) {			
//				if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
//					removeParticipant(p);
//					i--; 
//				}
//				//e.printStackTrace();
//				//removeParticipant(p);
//				//i--;
//			}
//		}
//	}
	

	/**
	 * Fügt dem DrawPanel einen Vector von Linien hinzu
	 * @param x Vector mit den Linien
	 */
	public void addElements(Vector x){
		for(int i = 0;i<x.size();i++){
			lines.add(x.get(i));
		}
		Participant p;
		for (int i = 0; i < participantList.size(); i++) {
			p = (Participant) participantList.get(i);
			try {
				Remote.invoke(p.getCp(), "draw", lines);	
			} catch (Exception e) {
				if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
					removeParticipant(p);
					i--; 
				}
			}
		}
	}
	
	/**
	 * Löscht das Bild im DrawPanel
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
				if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
					removeParticipant(p);
					i--; 
				}
			}
		}
		lines.setElementAt(new String("malen"),0);
	}
	
	
	/**
	 * Setzt den Status der Anwendung.
	 * Der Status wird in der Zeile unten in der ClientGui angezeigt.
	 * @param status Der auszugebende Status als String
	 */
	public void setStatus(String status){
		 Participant p;
			for (int i = 0; i < participantList.size(); i++) {
				p = (Participant) participantList.get(i);
				try {
					Remote.invoke(p.getCp(), "setStatus", status);
				} catch (Exception ex) {		
					if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
						removeParticipant(p);
						i--; 
					}
				}
			}
	 }

	/**
	 * Gibt den Inhalt des DrawPanels wieder.
	 * Wird genutzt wenn neue User dazu stoßen.
	 * @return Vector mit den Linien
	 */
	public Vector getLines() {
		return lines;
	}

	/**
	 * Gibt den kompletten Chatinhalt zurück.
	 * Wird genutzt wenn neue User dazu stoßen.
	 * @return Document
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
	
	/**
	 * Methode ist nur dafür da die Verbindung aufrecht
	 * zu halten. Um auch die Verbindung vom Server zu den Clients aufrecht zu halten
	 * wird auch ein Dummy an jeden Client geschickt.
	 */
	public void sendDummy(String name){
		((Participant)participantList.get(participantList.indexOf(new Participant(name)))).setLastDummy(new Date());
		System.out.println("Dummy");
		Participant p;
		for (int i = 0; i < participantList.size(); i++) {
				p = (Participant) participantList.get(i);
				try {
					Remote.invoke(p.getCp(), "sendDummy", null);
				} catch (Exception ex) {	
					if((new Date().getTime() - p.getLastDummy().getTime()) > (5*1000)){
						removeParticipant(p);
						i--; 
					}
				}
			}
	}

	/**
	 * Fährt der Server herunter und sendet diese Nachricht zuvor noch jedem Client,
	 * damit diese nicht einfrieren.
	 */
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

	/**
	 * Gibt den Namen des Clients zurück, der den Server gestartet hat
	 * @return den Namen des Starters
	 */
	public String getStarter() {
		return starter;
	}

}
