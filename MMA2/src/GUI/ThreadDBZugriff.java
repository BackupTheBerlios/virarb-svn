package GUI;

import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ThreadDBZugriff implements Runnable {
	private String SQLbefehl;
	private JFrame ownerframe;
	private JDialog ownerdialog;
	
	public ThreadDBZugriff(JFrame owner, String lbefehl) {
		super();
		this.ownerframe = owner;
		SQLbefehl = lbefehl;
	}
	
	public ThreadDBZugriff(JDialog owner, String lbefehl) {
		super();
		this.ownerdialog = owner;
		SQLbefehl = lbefehl;
	}

	public void run() {
		Waitdialog wait;
		if(ownerdialog == null){
			wait = new Waitdialog(ownerframe, "Bitte Warten!");
		}
		else{
			wait = new Waitdialog(ownerdialog, "Bitte Warten!");
		}
		wait.setVisible(true);
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");				
			Statement statement = connection.createStatement();	
			statement.execute(SQLbefehl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		wait.dispose();
	}
}
