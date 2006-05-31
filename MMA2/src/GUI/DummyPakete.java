package GUI;
import Server.ChatServer;

public class DummyPakete implements Runnable {
	ChatServer server;
	
	
	public DummyPakete(ChatServer server){
		this.server=server;
	}
	
	public void run() {
		while(true){
			try {
				Thread.sleep(1000*90);
				server.sendDummy();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
