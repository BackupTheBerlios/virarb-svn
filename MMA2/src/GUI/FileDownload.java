package GUI;
import java.awt.dnd.DragGestureEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import Server.*;


public class FileDownload implements Runnable {
	ChatSession session;
	Object[] fileinfo;
	File tempfile;

	public FileDownload(ChatSession session,Object[] fileinfo,File tempfile){
		this.session=session;
		this.fileinfo=fileinfo;
		this.tempfile=tempfile;

	}
	
	public void run() {
			FileServer tempfserver=null;
			String ip=(String)fileinfo[0];
			File  f=(File)fileinfo[1];
			
			try {
		   		tempfserver = (FileServer)Naming.lookup("file-server");
		   		
		   	} catch (Exception e) {
				e.printStackTrace();
			}
			
		   	try {
				long numChunks=tempfserver.getNumChunks(f);
				BufferedOutputStream bout=new BufferedOutputStream(new FileOutputStream(tempfile));
				
				for(int i=0;i<=numChunks;i++){
					byte[]  buffer=tempfserver.readChunk(f,i);
					bout.write(buffer,0,buffer.length);
					buffer=null;
					bout.flush();
				}
				bout.close();

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

}