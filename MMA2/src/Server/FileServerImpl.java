package Server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import java.io.*;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.DefaultListModel;
import GUI.Ip;


public class FileServer {
	private final int CHUNKSIZE=1024*1024;
	
	
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
	public long getNumChunks(File f){
		return f.length()/CHUNKSIZE;
	}
	
	public byte[] readChunk(File f,int nextChunk){
		byte[] data=null;
		if(f.length() < CHUNKSIZE)
		{
			data = new byte[(int)(f.length())];
		}
		else if (nextChunk!=getNumChunks(f))
		{
			data = new byte[CHUNKSIZE];
		}
		else
		{
			data = new byte[(int)f.length()%CHUNKSIZE];
		}	
		
		try {
			BufferedInputStream bin=new BufferedInputStream(new FileInputStream(f));
			bin.skip(CHUNKSIZE*nextChunk);
			bin.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	

	
	
//	public 


}
