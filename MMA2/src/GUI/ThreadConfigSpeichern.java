package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JDialog;

public class ThreadConfigSpeichern implements Runnable {
	private String sqlbefehl;
	private JDialog owner;
	private String port;
	
	public ThreadConfigSpeichern(JDialog owner, String sqlbefehl, String port) {
		super();
		this.owner = owner;
		this.sqlbefehl = sqlbefehl;
		this.port = port;
	}

	public void run() {
		Waitdialog wait;
		wait = new Waitdialog(owner, "Bitte Warten!");
		wait.setVisible(true);
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
			Statement statement = connection.createStatement();	
			statement.execute(sqlbefehl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		Fileausgabe.setProperty("Port", port);
		owner.dispose();
		wait.dispose();
	}
}
