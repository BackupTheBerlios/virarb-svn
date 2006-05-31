package Server;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.DefaultListModel;

import GUI.Chatmessage;
import GUI.ColorLine;

/**
 * 
 * Das Interface f�r eine ChatSession 
 * @author Klassen,Kokoschka,Langer,Meurer
 *
 */
public interface ChatSession extends Remote {

 /**
  * Sendet eine Chatmessage an der Server
 * @param message Die Nachricht, die gesendet werden soll.
 * @throws RemoteException
 */
public void sendMessage(Chatmessage message) throws RemoteException; 
  /**
   * F�gt dem Filetable einen neue Datei hinzu
 * @param data Die Datei
 * @param filename Der Name der Datei
 * @throws RemoteException
 */
public void addFile(File f,String filename,String ip) throws RemoteException;
  /**
   * Die Methode gibt den Namen des zu dieser Session geh�renden Clients zur�ck.
 * @return den Namen
 * @throws RemoteException
 */
public String getNickname() throws RemoteException;
  /**
   * Gibt eine Datei zur�ck, die sich an der Stelle index auf dem Server befindet.
 * @param index Der Index der Datei
 * @return die Datei
 * @throws RemoteException
 */
public Object[] getFile(int index) throws RemoteException;
  /**
   * Gibt eine Referenz auf den Server zur�ck
 * @return die Referenz
 * @throws RemoteException
 */
public ChatServerImpl getServer() throws RemoteException;
  /**
   * Die Methode getValues() gibt die Liste mit den Namen der Dateien, die auf dem Server liegen, zur�ck.
 * @return die Liste der Dateien
 * @throws RemoteException
 */
public DefaultListModel getValues() throws RemoteException;
/**
 * Gibt einen Vector mit den Inhalten (ColorLines) des Malfensters zur�ck.
 * @return den Vector
* @throws RemoteException
*/
public Vector getLines()throws RemoteException;
/**
 * F�gt ein neues Element in das Malfenster ein
* @param x Das Element der Klasse ColorLine
* @see GUI.ColorLine
* @throws RemoteException
*  */
public void addElement(ColorLine x) throws RemoteException;
/**
 * Mit Hilfe der Funkt�on wird die Statuszeile auf allen Clients aktualisiert bzw der 
 * angegebene Text wird dort ausgegeben.
* @param status Der Text, der ausgegeben werden soll
* @throws RemoteException
*/
public void setStatus(String status)  throws RemoteException;
  /**
   * L�scht die Session vom Server und kappt damit die Verbindung.
   * @throws RemoteException
 */
public void removeMe() throws RemoteException;

}