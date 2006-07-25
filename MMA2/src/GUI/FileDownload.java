package GUI;
import gnu.cajo.invoke.Remote;
import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ItemProxy;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class FileDownload implements Runnable {
	//ChatSession session;
	Object[] fileinfo;
	File tempfile;
	ItemProxy proxy;

	public FileDownload(Object[] fileinfo,File tempfile){
		//this.session=session;
		this.fileinfo=fileinfo;
		this.tempfile=tempfile;
	}
	
	// noch an die möglichkeit von zugriffen übers internet konfigurieren
	public void run() {
			Object tempfserver=null;
			String lanIp=(String)fileinfo[0];
			String wanIp=(String)fileinfo[1];
			File  f=(File)fileinfo[2];
			
			try {
		   		tempfserver = (Object)Remote.getItem("//"+lanIp+":1234/VirArbFileServer");
		   		RemoteInvoke cp = (RemoteInvoke)Remote.invoke(tempfserver, "getCp", null);
		   		proxy = new ItemProxy(cp, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		   	try {
				long numChunks=((long[])Remote.invoke(tempfserver, "getNumChunksRemote",f))[0];
				BufferedOutputStream bout=new BufferedOutputStream(new FileOutputStream(tempfile));
				
				for(int i=0;i<=numChunks;i++){
					int[] ii = {i};
					Object[] args = {f, ii};
					byte[]  buffer=(byte[])Remote.invoke(tempfserver, "readChunk", args);
					bout.write(buffer,0,buffer.length);
					buffer=null;
					bout.flush();
				}
				bout.close();

			} catch (Exception e) {	
				e.printStackTrace();
			} 
			
		}

}