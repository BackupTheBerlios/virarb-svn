package GUI;
import gnu.cajo.invoke.Remote;
public class DummyPakete implements Runnable {
	Object server;
	
	
	public DummyPakete(Object server){
		this.server=server;
	}
	
	public void run() {
		while(true){
			try {
				Thread.sleep(1000*90);
				Remote.invoke(server, "sendDummy", null);				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
