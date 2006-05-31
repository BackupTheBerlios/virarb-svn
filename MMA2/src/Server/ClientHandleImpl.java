package Server;


import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import javax.swing.text.Document;
import GUI.Chatmessage;
import GUI.Main;


/**
 * Implementiert das Interface ClientHandle.
 * @author Klassen,Kokoschka,Langer,Meurer
 *
 */
public class ClientHandleImpl extends UnicastRemoteObject implements ClientHandle {
  Main client;

  /**
   * Konstruktor
 * @param client Der Client zu dem verbunden wird
 * @throws RemoteException
 */
  public ClientHandleImpl(Main client) throws RemoteException {
	  this.client = client;
  }

  /**
   * Konstruktor
 * @throws RemoteException
 */
	public ClientHandleImpl() throws RemoteException {
  }

  public void receiveMessage(Chatmessage message) {
    client.receiveMessage(message);
  }

  public void receiveNewFile(String filename) {
	  client.receiveNewFile(filename);
  }
  
  public void draw(Vector x){
	  client.getMalfenster().draw(x);
  }
  public void setStatus(String status){
	  client.setStatus(status);
  }
  
  public Document getChat(){
	  return client.getChat();
  }
  
	public FileInputStream getFile(File f) {
		FileInputStream in=null;
		try {
			in= new FileInputStream((f));
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return in;
	}

  
}
