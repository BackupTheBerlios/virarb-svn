package Server;
import java.awt.Color;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.text.Document;
import GUI.ColorLine;


/**
 * Das Interface für einen Chatserver
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public interface ChatServer extends Remote {

 /**
 *Erstellt eine neue Instanz der Klasse ChatSession auf dem Server und
 *gibt diese zurück.
 * @param nickname 	Der Name des Clients
 * @param handle	Der Handle des Clients
 * @return Gibt die Instanz von Chatsession zurück
 * @throws RemoteException
 */
  public ChatSession createSession(String nickname, ClientHandle handle)
                                        throws RemoteException;
  /**
   * Liefert eine Farbe zurück, die bis zu einer gewissen Anzahl an Nutzern einzigartig ist.
 * @return die Farbe
 * @throws RemoteException
 */
public Color getMyColor() throws RemoteException;
  /**
   * Fügt eine neue Datei in der Liste des Servers ein
 * @param data die Datei
 * @param name der Name der Datei
 * @throws RemoteException
 */
public void addFile(File f,String name,String ip) throws RemoteException;
  /**
   * Gibt den File an dem bestimmten index zurück
 * @param index Der Index, wo die Datei zu finden ist
 * @return die Datei
 * @throws RemoteException
 */
//public  FileInputStream getFile(int index) throws RemoteException;
  /**
   * gibt die Liste der Dateinamen zurück, um diese am Client darzustellen
 * @return Die Listeneinträge
 * @throws RemoteException
 */
public DefaultListModel getValues() throws RemoteException;
  /**
   * Fügt ein neues Element in das Malfenster ein
 * @param x Das Element der Klasse ColorLine
 * @see GUI.ColorLine
 * @throws RemoteException
 *  */
public void addElement(ColorLine x) throws RemoteException;
  /**
   * Der Aufruf der Funktion löscht auf allen Clients den Inhalt des Malfensters
   *  @throws RemoteException
 */
public void skizze_loeschen() throws RemoteException;
  /**
   * Mit Hilfe der Funktíon wird die Statuszeile auf allen CLients aktualisiert bzw der 
   * angegebene Text wird dort ausgegeben.
 * @param status Der Text, der ausgegeben werden soll
 * @throws RemoteException
 */
public void setStatus(String status)  throws RemoteException;
  /**
   * Gibt einen Vector mit den Inhalten (ColorLines) des Malfensters zurück.
   * @return den Vector
 * @throws RemoteException
 */
public Vector getLines() throws RemoteException;
  /**
   * Gibt das ChatDocument des ersten Clients zurück, in dem alle bisherigen Nachrichten enthalten sind.
 * @return das Document
 * @throws RemoteException
 */
public Document getChat() throws RemoteException;
  /**
   * Löscht eine Session aus der Liste des Servers.
 * @param session die zu löschende Session
 * @throws RemoteException
 */
public void removeSession(ChatSession session) throws RemoteException;

public void sendDummy()throws RemoteException;

 }