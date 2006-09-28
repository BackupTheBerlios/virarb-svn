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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object x){
		if(x instanceof NamedColor){
			NamedColor nc = (NamedColor)x;
			return this.toString().equals(nc.toString());
		}
		else if(x instanceof String){
			String s = (String) x;
			return this.toString().equals(s);
		}
		else if(x instanceof Color){
			Color c = (Color) x;
			return this.getColor().equals(c);
		}
		return false;
	}
	
}