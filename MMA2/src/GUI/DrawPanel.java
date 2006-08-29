package GUI;

import gnu.cajo.invoke.Remote;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;

/**
 * Die Klasse DrawPanel erstellt ein Panel, in dem mit Hilfe der Maus
 * gezeichnet werden kann.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class DrawPanel extends Panel implements MouseListener, MouseMotionListener {
	private Object server;
	private Color myColor;
	private Vector lines;
	private int x1, y1;
	
	/**
	 * @param session Die ChatSession, über die die Daten transferiert werden.
	 * @param myColor Die Farbe der Zeichnung
	 */
	public DrawPanel(Object server,Color myColor) {
		this.server=server;
		this.myColor=myColor;
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this);	
		try {			
			lines=(Vector)Remote.invoke(server, "getLines", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		e.consume();
		try {
			Remote.invoke(server, "addElement", new ColorLine(x1,y1,e.getX(), e.getY(),myColor));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		x1 = e.getX();
		y1 = e.getY();
		repaint();	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		e.consume();
		x1 = e.getX();
		y1 = e.getY();
		try {
			Remote.invoke(server, "addElement", new ColorLine(e.getX(), e.getY(), -1, -1,myColor));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		x1 = e.getX();
		y1 = e.getY();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}
	
	/**
	 * @param x Vector, der gezeichnet werden soll
	 */
	public void draw(Vector x){
		lines=x;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		int np = lines.size();
		if(np>0 && ((String)lines.get(0)).equals("loeschen")){
			g.setColor(Color.WHITE);
			g.fillRect(0,0,300,300);			
		}
		else if(np>0){
			for (int i = 1; i < np; i++) {
				ColorLine p = (ColorLine) lines.elementAt(i);
				g.setColor(p.getColor());
				if (p.width != -1) {
					g.drawLine(p.x, p.y, p.width, p.height);
				} else {
					g.drawLine(p.x, p.y, p.x, p.y);
				}
			}
		}
	}

	/**
	 * @return die aktuelle Farbe
	 */
	public Color getMyColor() {
		return myColor;
	}

	/**
	 * @param myColor neue Farbe
	 */
	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}
}

