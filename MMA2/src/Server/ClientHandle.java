package Server;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.text.Document;
import GUI.Chatmessage;

/**
 * Das Interface für den ClientHandle.
 * Stellt die Verbindung clientseitig sicher.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public interface ClientHandle extends Remote {

  /**
   * Empfängt eine Nachricht vom Server und gibt diese am Client aus
 * @param message Die übermittelte Nachricht
 * @throws RemoteException
 */
public void receiveMessage(Chatmessage message) throws RemoteException;
  /**
   * empfängt eine neuen Dateinamen vom Server und gibt diesen am Client aus
 * @param filename der Name der Datei
 * @throws RemoteException
 */
public void receiveNewFile(String filename) throws RemoteException;
  /**
   * Empfängt einen Vector von Linien die dann am Malfenster des Clients ausgegeben werden
 * @param x Der Vector mit den ColorLines
 * @throws RemoteException
 */
public void draw(Vector x) throws RemoteException;
  /**
 * Empfängt einen neuen Text für die Statuszeile und gibt sie aus
 * @param status der neue Text
 * @throws RemoteException
 */
public void setStatus(String status) throws RemoteException;
  /**
   * Gibt das Chatdocument des Clients zurück
 * @return das Chatdocument
 * @throws RemoteException
 */
public Document getChat() throws RemoteException;



}

