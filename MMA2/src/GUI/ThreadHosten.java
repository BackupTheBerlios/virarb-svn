package GUI;

import javax.swing.JFrame;
import Server.Server;

/**
 * Die Klasse ThreadHosten erstellt einen eigenen Thread, der
 * einen Arbeitsraum erstellt und dann mit diesem verbindet.
 * Nötig ist dies, damit die GUI nicht einfriert und ein Fortschrittsbalken
 * gezeigt werden kann, der dem User zeigt, dass das Programm weiterhin
 * läuft.
 * @author Daniel Meurer
 **/
public class ThreadHosten implements Runnable {
	private String username;
	private JFrame owner;
	
	/**
	 * Konstruktor
	 * @param owner aufrufendes Fenster
	 * @param username Name des Users
	 */
	public ThreadHosten(JFrame owner, String username) {
		super();
		this.owner = owner;
		this.username = username;
	}

	public void run() {
		Waitdialog wait = new Waitdialog(owner, "Arbeitsraum wird erstellt.");
		wait.setVisible(true);
		try {
			new Server(username);
			System.out.println("Server gestartet");
			Main main=new Main(username);
			main.setVisible(true);
			wait.dispose();
			owner.dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
			wait.dispose();
			Error err=new Error("Fehler","\n\nEs konnte kein Arbeitsraum erstellt werden.",owner);					
			err.setVisible(true);
		}			
	}
}
