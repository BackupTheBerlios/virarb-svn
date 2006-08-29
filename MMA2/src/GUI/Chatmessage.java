package GUI;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 *  Chatmessage.
 *  Die Klasse Chatmessage stellt einen Container zu Verfügung
 *  der eine Textnachricht und einige zusätzliche Parameter enthält
 *  und serialisierbar ist. Sie kann also problemlos
 *  über einen Stream verschickt werden.	
 * @author Klassen,Kokoschka,Langer,Meurer
 *  
 */
public class Chatmessage implements Serializable
 {
	private Color color;
	private String user;
	private String time;
	private String message;
	
	/**
	 *   Konstruktor.
	 *   @param color Gibt die Farbe des Textes an.
	 *   @param message Enthält den Text der Nachricht.
	 *   @param time Enthält den Sendezeitpunkt der Nachricht.
	 *   @param user Gibt den Namen des Nachricht-Senders an
	 */
	public Chatmessage(Color color, String message, Date time, String user) {
		super();
		this.color = color;
		this.message = message;
		setTime(time);
		this.user = user;
	}
	
	/**
	 *   Ein ChatmessageObject in den Stream schreiben
	 *   @param out Gibt den Stream an, in der geschrieben werden soll.	
	 *   @exception IOException
	 */
	private void writeObject(ObjectOutputStream out)throws IOException{
		out.writeInt(color.getRed());
		out.writeInt(color.getGreen());
		out.writeInt(color.getBlue());
		out.writeUTF(message);
		out.writeUTF(time);
		out.writeUTF(user);
	}
	
	/**
	 *   Ein ChatmessageObject aus dem Stream lesen
	 *   @param in Gibt den Stream an, aus dem gelesen werden soll
	 *   @exception IOException
	 *   @exception ClassNotFoundException
	 */
    private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException{
		color=new Color(in.readInt(),in.readInt(),in.readInt());
		message=in.readUTF();
		time=in.readUTF();
		user=in.readUTF();
    }
	
	/**
	 * @return Gibt die Textfarbe der Nachricht zurück.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color Setzt die Textfarbe der Nachricht fest.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return Gibt den Text der Nachricht zurück.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message Setzt den Text der Nachricht fest.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Gibt die Sendezeit der Nachricht zurück.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time Setzt die Sendezeit der Nachreicht fest.
	 */
	public void setTime(Date time) {
		this.time="";
		if(time.getHours()<10){
			this.time="0";
		}
		this.time+=time.getHours()+":";
		if(time.getMinutes()<10){
			this.time+="0";
		}
		this.time+=time.getMinutes();
	}

	/**
	 * @return Gibt den Namen des Senders der Nachricht zurück.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @param user Setzt den Namen des Senders der Nachricht fest.
	 */
	public void setUser(String user) {
		this.user = user;
	}
}
