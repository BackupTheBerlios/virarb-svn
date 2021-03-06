package GUI;

import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;

/**
 * Die Klasse Colorvector stellt die verschieden
 * Farben (NamedColor) f�r chat und malfenster zur Verf�gung
 * @author Daniel Meurer
 **/
public class ColorVector extends Vector implements Serializable {

	/**
	 * Konstruktor
	 * F�llt Vector automatisch
	 */
	public ColorVector() {
		super();
		generateContent();
	}
	
	/**
	 * F�llt den Vector
	 */
	private void generateContent(){
		this.add(new NamedColor(new Color(180,0,0),"Rot"));
		this.add(new NamedColor(new Color(0,180,0),"Gr�n"));
		this.add(new NamedColor(new Color(0,0,180),"Blau"));
		this.add(new NamedColor(new Color(200,0,200),"Magenta"));
		this.add(new NamedColor(new Color(0,200,200),"Cyan"));
		this.add(new NamedColor(new Color(120,0,120),"Lila"));
		this.add(new NamedColor(new Color(0,120,120),"Dunkelgr�n"));
		this.add(new NamedColor(new Color(120,120,0),"Dschungelgr�n"));
	}
}