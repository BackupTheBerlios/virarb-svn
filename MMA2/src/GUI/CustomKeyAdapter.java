package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Die Klasse CustomKeyAdapter erweitert die Klasse KeyAdapter.
 * Sie wird verwendet um Tastaturbefehle abzufangen, in diesem Falle das Drücken der 
 * Entertaste.
 * Daraufhin wird in dem angegebenen ActionListener die angegebene Event
 * gefeuert.
 * @author Daniel Meurer
 */
public class CustomKeyAdapter extends KeyAdapter{
	private String command;
	private ActionListener al;
	
	/**
	 * Konstruktor
	 * @param command String Befehl
	 * @param al zu nutzender ActionListener
	 */
	public CustomKeyAdapter(String command, ActionListener al){
		this.command = command;
		this.al = al;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			al.actionPerformed(new ActionEvent(arg0.getComponent(),0,command));
		}
	}
}
