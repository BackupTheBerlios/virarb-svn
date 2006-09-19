package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class CustomKeyAdapter extends KeyAdapter{
	private String command;
	private ActionListener al;
	public CustomKeyAdapter(String command, ActionListener al){
		this.command = command;
		this.al = al;
	}
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			al.actionPerformed(new ActionEvent(arg0.getComponent(),0,command));
		}
	}
}
