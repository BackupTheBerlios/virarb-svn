package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Die Klasse ThreadDBZugriff erstellt einen eigenen Thread, der
 * den Zugriff auf die Datenbank regelt.
 * Nötig ist dies, damit die GUI nicht einfriert und ein Fortschrittsbalken
 * gezeigt werden kann, der dem User zeigt, dass das Programm weiterhin
 * läuft. 
 * @author Daniel Meurer
 */
public class ThreadDBZugriff implements Runnable {
	private String SQLbefehl;
	private JFrame ownerframe;
	private JDialog ownerdialog;
	
	/**
	 * Konstruktor mit JFrame als aufrufende Klasse.
	 * @param owner
	 * @param lbefehl Der SQL-Befehl
	 */
	public ThreadDBZugriff(JFrame owner, String lbefehl) {
		super();
		this.ownerframe = owner;
		SQLbefehl = lbefehl;
	}
	
	/**
	 * Konstruktor mit JDialog als aufrufende Klasse.
	 * @param owner
	 * @param lbefehl Der SQL-Befehl
	 */
	public ThreadDBZugriff(JDialog owner, String lbefehl) {
		super();
		this.ownerdialog = owner;
		SQLbefehl = lbefehl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
