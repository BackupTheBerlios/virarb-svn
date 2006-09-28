package GUI;

import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;

/**
 * Die Klasse Colorvector stellt die verschieden
 * Farben (NamedColor) für chat und malfenster zur Verfügung
 * @author Daniel Meurer
 **/
public class ColorVector extends Vector implements Serializable {

	/**
	 * Konstruktor
	 * Füllt Vector automatisch
	 */
	public ColorVector() {
		super();
		generateContent();
	}
	
	/**
	 * Füllt den Vector
	 */
	private void generateContent(){
		this.add(new NamedColor(new Color(180,0,0),"Rot"));
		this.add(new NamedColor(new Color(0,180,0),"Grün"));
		this.add(new NamedColor(new Color(0,0,180),"Blau"));
		this.add(new NamedColor(new Color(200,0,200),"Magenta"));
		this.add(new NamedColor(new Color(0,200,200),"Cyan"));
		this.add(new NamedColor(new Color(120,0,120),"Lila"));
		this.add(new NamedColor(new Color(0,120,120),"Dunkelgrün"));
		this.add(new NamedColor(new Color(120,120,0),"Dschungelgrün"));
	}
}