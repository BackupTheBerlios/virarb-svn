package GUI;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Waitdialog extends JDialog{
	private String text;
	private Window owner;
	
	/**
	 * Konstrukter mit Jframe als aufrufende Klasse
	 * @param owner Der aufrufende JFrame
	 * @param text Text, der über dem Ladebalken gezeigt wird
	 */
	public Waitdialog(JFrame owner, String text){
		super(owner,false);
		this.owner = owner;
		this.text = text;
		initGUI();
	}
	
	/**
	 * Konstrukter mit JDialog als aufrufende Klasse
	 * @param owner Der aufrufende JDialog
	 * @param text Text, der über dem Ladebalken gezeigt wird
	 */
	public Waitdialog(JDialog owner, String text){
		super(owner,false);
		this.owner = owner;
		this.text = text;
		initGUI();
	}
	
	/**
	 * Initialisiert und läd die Gui
	 */
	private void initGUI(){
		try {
			this.getContentPane().setLayout(new BorderLayout());
			JLabel textlabel = new JLabel(text);
			this.add("North", textlabel);	
			JProgressBar pbar = new JProgressBar();
	        pbar.setSize(280,10);
	        pbar.setIndeterminate(true);
	        this.add("South", pbar);
			this.setAlwaysOnTop(true);
			this.setTitle("Bitte wartern.");
			this.setSize(300, 100);
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			this.setLocation(owner.getLocation().x+owner.getSize().width/2-this.getSize().width/2,owner.getLocation().y+owner.getSize().height/2-this.getSize().height/2);
			this.setResizable(false);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}
