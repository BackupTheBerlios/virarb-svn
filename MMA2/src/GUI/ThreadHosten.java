package GUI;

import javax.swing.JFrame;
import Server.Server;

public class ThreadHosten implements Runnable {
	private String username;
	private JFrame owner;
	
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
