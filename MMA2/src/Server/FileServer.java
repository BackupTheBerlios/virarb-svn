package Server;

import java.io.File;
import java.rmi.*;

public interface FileServer extends Remote{
	
	public long getNumChunks(File f) throws RemoteException;
	public byte[] readChunk(File f,int nextChunk) throws RemoteException;
}
