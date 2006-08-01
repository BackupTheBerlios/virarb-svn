package GUI;
import gnu.cajo.invoke.Remote;
import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ItemProxy;
import java.awt.dnd.DragGestureEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class FileDownload implements Runnable {
	private Object[] fileinfo;
	private File tempfile;
	private DragGestureEvent dge;
	private ItemProxy proxy;
	private DnDText x;

	public FileDownload(Object[] fileinfo,File tempfile,DragGestureEvent dge, DnDText x){
		this.fileinfo = fileinfo;
		this.tempfile = tempfile;
		this.dge = dge;
		this.x = x;
	}
	
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
				System.out.println("No Fileserver under lanIp");
				e.printStackTrace();
				try {
					tempfserver = (Object)Remote.getItem("//"+wanIp+":1234/VirArbFileServer");
			   		RemoteInvoke cp = (RemoteInvoke)Remote.invoke(tempfserver, "getCp", null);
			   		proxy = new ItemProxy(cp, this);
				}
				catch(Exception e1) {
					System.out.println("Can't reach any Fileserver");
					e.printStackTrace();
				}
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
				//Trans temp=new Trans(tempfile);
				//dge.startDrag(null,null,null,temp,x);
			} catch (Exception e) {	
				e.printStackTrace();
			} 		
		}
}