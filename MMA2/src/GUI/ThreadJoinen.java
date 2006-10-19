package GUI;

import javax.swing.JFrame;
import Server.Server;

public class ThreadJoinen implements Runnable {
	private String username, ip;
	private JFrame owner;
	
	public ThreadJoinen(JFrame owner, String username, String ip) {
		super();
		this.ip = ip;
		this.owner = owner;
		this.username = username;
	}

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
