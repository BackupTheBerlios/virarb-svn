package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;

public class ThreadCheckLogin implements Runnable {
	private String name;
	private String pw;
	private JFrame owner;
	
	public ThreadCheckLogin(JFrame owner, String name, String pw) {
		super();
		this.name = name;
		this.owner = owner;
		this.pw = pw;
	}

	public void run() {
		Waitdialog wait = new Waitdialog(owner, "Bitte Warten! Ihre Daten werden überprüft.");
		wait.setVisible(true);
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://server8.cyon.ch/medienin_danieldb", "medienin_daniWeb", "web");
			Statement statement = connection.createStatement();	
			String abfrage="SELECT Passwort, Nickname FROM UserData WHERE Nickname='"+name+"';";
			ResultSet x = statement.executeQuery(abfrage);
			if(!x.next()){
				wait.dispose();
				Error err=new Error("Fehler","Bitte Username und Passwort eingeben \noder registrieren wenn noch kein gültiger \nAccount vorliegt!",owner);					
				err.setVisible(true);
			}
			else{					
				if(pw.equals(x.getString(1))){
					String nickname = x.getString(2);
					String lanIp = Ip.getLanIp();
					String wanIp = Ip.getWanIp();							
					try {
						statement = connection.createStatement();	
						statement.executeUpdate("INSERT INTO UserOnline(Nickname,LanIp,WanIp,Port) VALUES ('"+nickname+"','"+lanIp+"','"+wanIp+"','"+Ip.getMyPort()+"');");
					} catch (Exception e1) {
						statement.executeUpdate("UPDATE UserOnline SET LanIp='"+lanIp+"',WanIp='"+wanIp+"',Port='"+Ip.getMyPort()+"' WHERE Nickname='"+nickname+"';");
					}
					Fileausgabe.setProperty("DefaultNick", name);
					Fileausgabe.setProperty("DefaultPw", pw);
					wait.dispose();
					Error err=new Error("Glückwunsch","Sie haben sich erfolgreich eingeloggt.",owner);					
					err.setVisible(true);
					Auswahl aw=new Auswahl(name);
					aw.setVisible(true);
					owner.dispose();
				}
				else{
					wait.dispose();
					Error err=new Error("Warnung","Sorry, Username und Password passen nicht.",owner);									
					err.setVisible(true);
				}
			}
			statement.close();
			connection.close();		
		} catch (Exception e1) {
			wait.dispose();
			Error err=new Error("Fehler","\n\nBitte überprüfen Sie ihre Internetverbindung.",owner);					
			err.setVisible(true);
			System.out.println("ERROR:" + e1.getMessage());
		}		
	}
}
