package GUI;

import java.awt.Color;
import java.io.Serializable;

public class NamedColor implements Serializable {
	private Color color;
	private String name;
	
	public NamedColor(Color color, String name) {
		super();
		this.color = color;
		this.name = name;
	}

	public Color getColor() {
		return color;
	}
	
	public String toString(){
		return name;
	}
	
}
