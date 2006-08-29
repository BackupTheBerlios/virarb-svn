package GUI;

import java.awt.Color;
import java.io.Serializable;

/**
 * NamedColor besteht aus einer Farbe und einem Namen.
 * @author Daniel Meurer
 */
public class NamedColor implements Serializable {
	private Color color;
	private String name;
	
	/**
	 * Konstruktor
	 * @param color die Farbe
	 * @param name der Name der Farbe
	 */
	public NamedColor(Color color, String name) {
		super();
		this.color = color;
		this.name = name;
	}

	/**
	 * Gibt die Farbe zurück
	 * @return die Farbe
	 */
	public Color getColor() {
		return color;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return name;
	}	
}