package Server;

import java.awt.Color;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.text.Document;
import GUI.Chatmessage;
import GUI.ColorLine;


/**
 * 
 * Die Klasse stellt den Server dar, über den die Kommunikation zwischen den Clients bzw den Sessions abläuft.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
	private List sessions = new ArrayList();
	private List files=new ArrayList();
	private DefaultListModel values;
	private Vector lines = new Vector();
	private int count=0;
	private static Color[] colortable = { Color.RED, Color.CYAN,Color.MAGENTA, Color.ORANGE, Color.PINK, Color.GREEN };

	
//	private static String myIP=new String("81.173.229.84");
	
	
	
	public static void main(String[] args){
		try {
			new ChatServerImpl();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Konstruktor
	 * @throws RemoteException
	 */
	public ChatServerImpl() throws RemoteException {
//////////////////
//		System.setProperty("java.rmi.server.logCalls","true");
//		System.setProperty("java.rmi.server.hostname",myIP);
		
//		System.setProperty("");
//////////////////	/
		try {
//			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			LocateRegistry.createRegistry(1099);
			Naming.rebind("chat-server", this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lines.addElement(new String("malen"));
		values=new DefaultListModel();	

		System.out.println("Server gestartet unter Ip "+ System.getProperty("java.rmi.server.hostname") );

		
	}


	/* (non-Javadoc)
	 * @see Server.ChatServer#createSession(java.lang.String, Server.ClientHandle)
	 */
	public ChatSession createSession(String nickname, ClientHandle handle)
			throws RemoteException {
		System.out.println("create session: " + nickname);
		ChatSession s = new ChatSessionImpl(this, nickname, handle);
		sessions.add(s);

		return s;
	}
	
	/* (non-Javadoc)
	 * @see Server.ChatServer#getMyColor()
	 */
	public Color getMyColor() {
		return colortable[count++ % colortable.length];
	}
	
	public void postMessage(Chatmessage message) {
		ChatSessionImpl tmp;
		message.setTime(new Date());
//		chat=chat+("\n"+message.getUser()+" ["+message.getTime()+"]: "+message.getMessage());
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			try {
				tmp.getClientHandle().receiveMessage(message);
			} catch (RemoteException ex) {
				removeSession(tmp);
				i--; 
				setStatus("Die Verbindung zu User '"+tmp.getNickname()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+tmp.getNickname()+"' hat die Sitzung verlassen",new Date(),"System"));

			}
		}
	}


	/* (non-Javadoc)
	 * @see Server.ChatServer#removeSession(Server.ChatSession)
	 */
	public void removeSession(ChatSession session) {
		sessions.remove(session);
	}

	/* (non-Javadoc)
	 * @see Server.ChatServer#addFile(byte[], java.lang.String)
	 */
	public void addFile(File f,String name,String ip){
		Object[] temp={ip,f};
		files.add(temp);	
		values.addElement(name);
		ChatSessionImpl tmp;
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			try {
				tmp.getClientHandle().receiveNewFile(name);	

			} catch (Exception e) {
				e.printStackTrace();
				removeSession(tmp);
				i--;
				setStatus("Die Verbindung zu User '"+tmp.getNickname()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+tmp.getNickname()+"' hat die Sitzung verlassen",new Date(),"System"));
			}
		}
	}
	/* (non-Javadoc)
	 * @see Server.ChatServer#getFile(int)
	 */
	public Object[] getFile(int index){
		return (Object[])files.get(index);
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
	public void addElement(ColorLine x) throws RemoteException{
		lines.add(x);
		ChatSessionImpl tmp;
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			try {
				tmp.getClientHandle().draw(lines);	
			} catch (Exception e) {
				e.printStackTrace();
				removeSession(tmp);
				i--;
				setStatus("Die Verbindung zu User '"+tmp.getNickname()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+tmp.getNickname()+"' hat die Sitzung verlassen",new Date(),"System"));

			}
		}
	}
	
	/* (non-Javadoc)
	 * @see Server.ChatServer#skizze_loeschen()
	 */
	public void skizze_loeschen(){
		ChatSessionImpl tmp;
		lines.removeAllElements();
		lines.addElement(new String("loeschen"));
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			try {
				tmp.getClientHandle().draw(lines);	
			} catch (Exception e) {
				e.printStackTrace();
				removeSession(tmp);
				i--;
				setStatus("Die Verbindung zu User '"+tmp.getNickname()+"' ist leider abgerissen. Session wurde gelöscht");
				postMessage(new Chatmessage(Color.BLACK,"User '"+tmp.getNickname()+"' hat die Sitzung verlassen",new Date(),"System"));
			}
		}
		lines.setElementAt(new String("malen"),0);
	}
	
	 /* (non-Javadoc)
	 * @see Server.ChatServer#setStatus(java.lang.String)
	 */
	public void setStatus(String status){
		 ChatSessionImpl tmp;
			for (int i = 0; i < sessions.size(); i++) {
				tmp = (ChatSessionImpl) sessions.get(i);
				try {
					tmp.getClientHandle().setStatus(status);
				} catch (RemoteException ex) {
					
					removeSession(tmp);
					i--; 
					setStatus("Die Verbindung zu User '"+tmp.getNickname()+"' ist leider abgerissen. Session wurde gelöscht");
					postMessage(new Chatmessage(Color.BLACK,"User '"+tmp.getNickname()+"' hat die Sitzung verlassen",new Date(),"System"));

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
		ChatSessionImpl tmp=(ChatSessionImpl)sessions.get(0);
		try {
			return tmp.getClientHandle().getChat();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendDummy() throws RemoteException {
		// wir machen gar nix
		System.out.println("Dummy");
	}
	
	

}