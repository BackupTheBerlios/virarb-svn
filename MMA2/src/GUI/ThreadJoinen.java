package GUI;

import javax.swing.JFrame;

/**
 * Die Klasse ThreadJoinen erstellt einen eigenen Thread, der
 * zu einem Arbeitsraum verbindet.
 * Nötig ist dies, damit die GUI nicht einfriert und ein Fortschrittsbalken
 * gezeigt werden kann, der dem User zeigt, dass das Programm weiterhin
 * läuft.
 * @author Daniel Meurer
 **/
public class ThreadJoinen implements Runnable {
	private String username, ip;
	private JFrame owner;
	
	/**
	 * @param owner Aufrufende Klasse
	 * @param username Name des Users
	 * @param ip Ip bzw Name, zu der verbunden wird.
	 */
	public ThreadJoinen(JFrame owner, String username, String ip) {
		super();
		this.ip = ip;
		this.owner = owner;
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Waitdialog wait = new Waitdialog(owner, "Verbindung zum Arbeitsraum wird hergestellt.");
		wait.setVisible(true);
		try {
			Main main=new Main(ip,username);
			main.setVisible(true);
			Fileausgabe.setProperty("lastServerName", ip);
			wait.dispose();
			owner.dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
			Error err=new Error("Fehler","\n\nUnter dem Namen '"+ip+"' \nist kein Arbeitsraum geöffnet.",owner);					
			wait.dispose();
			err.setVisible(true);
		}
	}	
}
