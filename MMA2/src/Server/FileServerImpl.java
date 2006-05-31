package Server;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;


public class FileServerImpl extends UnicastRemoteObject implements FileServer {
	private final int CHUNKSIZE=1024*1024;
	
	
	public FileServerImpl() throws RemoteException {
		try {
			LocateRegistry.createRegistry(1100);
			Naming.rebind("file-server", this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("FileServer gestartet unter Ip "+ System.getProperty("java.rmi.server.hostname") );

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
