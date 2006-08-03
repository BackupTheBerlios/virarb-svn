package Server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ClientProxy;
import java.io.*;
import GUI.Ip;


public class FileServer {
	private final int CHUNKSIZE=1024*1024;
	private String wanIp;
	private String lanIp;
	
	public FileServer() throws Exception {
		wanIp = new String();
		lanIp = new String();
		try {
			wanIp = Ip.getWanIp(); 
			lanIp = Ip.getLanIp();
			Remote.config(lanIp, 1235, wanIp, 1235);
		    ItemServer.bind(this, "VirArbFileServer");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("FileServer gestartet unter lokaler Ip " + lanIp );
		System.out.println("FileServer gestartet unter öffentlicher Ip " + wanIp );
	}
	
   public Remote getCp() throws Exception {
	   ClientProxy cp = new ClientProxy();
	   return cp.remoteThis;
   }
	public long getNumChunks(File f){
		return f.length()/CHUNKSIZE;
		
	}
   
	public long[] getNumChunksRemote(File f){
		 long[] a = {f.length()/CHUNKSIZE};
		 return a;
	}
	
	public byte[] readChunk(File f,int[] nextChunkNumber){
		int nextChunk = nextChunkNumber[0];
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
	public String getLanIp() {
		return lanIp;
	}
	public void setLanIp(String lanIp) {
		this.lanIp = lanIp;
	}
	public String getWanIp() {
		return wanIp;
	}
	public void setWanIp(String wanIp) {
		this.wanIp = wanIp;
	}

}
