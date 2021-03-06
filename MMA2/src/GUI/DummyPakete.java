package GUI;

import gnu.cajo.invoke.Remote;

/**
 * Klasse DummyPakete bietet einen Thread,
 * der alle 5 sekunden ein Dummypaket an den Server
 * schickt, damit die Verbindung bei Inaktivitšt
 * nicht gekappt wird.
 * @author Daniel Meurer
 */
public class DummyPakete implements Runnable {
	private Object server;
	private String name;

	/**
	 * Konstruktor
	 * @param server
	 */
	public DummyPakete(Object server, String name){
		this.server = server;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean x = true;
		while(x){
			try {
				Thread.sleep(1000*5);
				Remote.invoke(server, "sendDummy", name);				
			} catch (Exception e) {
				x = false;
			}			
		}		
	}
}
