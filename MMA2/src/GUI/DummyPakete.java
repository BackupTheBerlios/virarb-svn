package GUI;

import gnu.cajo.invoke.Remote;

/**
 * Klasse DummyPakete bietet einen Thread,
 * der alle 90 sekunden ein Dummypaket an den Server
 * schickt, damit die Verbindung bei Inaktivität
 * nicht gekappt wird.
 * @author Daniel Meurer
 */
public class DummyPakete implements Runnable {
	Object server;

	/**
	 * Konstruktor
	 * @param server
	 */
	public DummyPakete(Object server){
		this.server=server;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean x = true;
		while(x){
			try {
				Thread.sleep(1000*90);
				Remote.invoke(server, "sendDummy", null);				
			} catch (Exception e) {
				x = false;
			}			
		}		
	}
}
