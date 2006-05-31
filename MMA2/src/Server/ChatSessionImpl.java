package Server;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import javax.swing.DefaultListModel;
import GUI.Chatmessage;
import GUI.ColorLine;



/**
 * Die Klasse ChatSessionImpl implementiert das Interface ChatSession und stellt die Verbindung
 * zwischen Client und Server dar.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class ChatSessionImpl extends UnicastRemoteObject implements ChatSession {
  private ChatServerImpl server;
  private String nickname;
  private ClientHandle handle;
  
//  private static String myIP=new String("81.173.229.84");

  /**
 * Konstruktor
 * @throws RemoteException
 * 
 */
public ChatSessionImpl() throws RemoteException {
  }

  /**
   * Konstrukor
 * @param server der Server, zu dem verbunden wird
 * @param nickname der name des Clients
 * @param handle das handle des clients
 * @throws RemoteException
 */
public ChatSessionImpl(ChatServerImpl server, String nickname, ClientHandle handle) throws RemoteException {
//////////////////
//	System.setProperty("java.rmi.server.hostname",myIP);	
///////////////////
	this.server = server;
    this.nickname = nickname;
    this.handle = handle;
  }


  public ChatServerImpl getServer(){
	  return server;
  }
  
  public void sendMessage(Chatmessage message) {
    server.postMessage(message);
  }

  public ClientHandle getClientHandle() {
    return handle;
  }

  public String getNickname() {
    return nickname;
  }
   
  public void addFile(File f,String filename,String ip){
	  server.addFile(f,filename,ip);
  }

  public Object[] getFile(int index){
		return server.getFile(index);
  }
  
  public DefaultListModel getValues(){
	  return server.getValues();
  }
  
  public void addElement(ColorLine x) throws RemoteException{
	  server.addElement(x);
  }
  
  public Vector getLines(){
	  return server.getLines();
  }
  public void setStatus(String status) {
	  server.setStatus(status);
  }
  
  public void removeMe(){
	  server.removeSession(this);
  }
}