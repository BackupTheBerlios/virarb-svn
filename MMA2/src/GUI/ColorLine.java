package GUI;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Die Klasse ColorLine erweitert die Klasse Rectangle um einen weiteren Parameter,
 * nämlich der Farbe.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class ColorLine extends Rectangle{
	private Color color;
	
	/**
	 * Konstrultor
	 * @param x1 1.x-Coordinate
	 * @param y1 1.y-Coordinate
	 * @param x2 2.x-Coordinate
	 * @param y2 2.y-Coordinate
	 * @param color Die Farbe der Linie.
	 */
	public ColorLine(int x1,int y1,int x2,int y2,Color color){
		super(x1,y1,x2,y2);
		this.color=color;
	}
	
	/**
	 * @return Gibt die Farbe der Linie zurück
	 */
	public Color getColor(){
		return color;
	}
}
