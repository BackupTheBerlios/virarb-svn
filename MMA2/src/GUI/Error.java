package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import layout.AnchorConstraint;
import layout.AnchorLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;

/** 
 * Die Klasse Error ist ein Popup-Fenster, welches genutzt wird,
 * um Fehlermeldungen auszugeben.
 * @author Klassen,Kokoschka,Langer,Meurer
 */
public class Error extends JDialog{
	private JFrame owner;
	private String messagetext,headertext;
	private JTextArea ta_message;
	private JButton button_ok;

	/**
	 * @param headertext Überschrift
	 * Der Text, der im Kopf des Fensters dargestellt wird.
	 * @param messagetext Fehlertext
	 * Der Text, der in der Mitte des Fenster ausgegeben wird und dern Fehler näher beschreibt.
	 * @param owner Aufrufender Frame
	 * Das Fenster, in welchem der Fehler aufgetreten ist und wohin nachher zurückgekehrt wird.
	 */
	public Error(String headertext,String messagetext,JFrame owner) {
		super(owner,false);
		this.owner=owner;
		this.headertext=headertext;
		this.messagetext=messagetext;
		initGUI();
	}
	
	/**
	 * initialiert das UserInterface und stellt es dar.
	 */
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.getContentPane().setLayout(thisLayout);
			Error_action al=new Error_action(this);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				ta_message = new JTextArea();
				ta_message.setText(messagetext);
				this.getContentPane().add(ta_message, new AnchorConstraint(207,919, 719, 80, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				ta_message.setPreferredSize(new java.awt.Dimension(245, 85));
				ta_message.setEditable(false);
				ta_message.setOpaque(false);
			}
			{
				button_ok = new JButton();
				this.getContentPane().add(button_ok, new AnchorConstraint(792,627, 948, 361, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				button_ok.setText("OK");
				button_ok.setPreferredSize(new java.awt.Dimension(50, 25));
				button_ok.addActionListener(al);	
			}
			pack();
			this.getRootPane().setDefaultButton(button_ok);
			this.setTitle(headertext);
			this.setModal(true);
			this.setSize(300, 200);
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			this.setLocation(owner.getLocation().x+owner.getSize().width/2-this.getSize().width/2,owner.getLocation().y+owner.getSize().height/2-this.getSize().height/2);
			this.setResizable(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param e KeyEvent
	 */
	public void keyTyped(KeyEvent e){}	
	
	/**
	 * @param e KeyEvent
	 */
	public void keyReleased(KeyEvent e){}
	
	/**
	 * Enter getippt?
	 * @param e KeyEvent
	 */
	public void keyPressed(KeyEvent e){
		System.out.println("KeyEvent!"); 
		if(e.getKeyCode()==10){
			this.dispose();
		}
	}
	
	/**
	* Die Klasse stellt den ActionListener für die Kalsse Auswahl zur Verfügung
	* @author Daniel Meuer
	*/
	public class Error_action implements ActionListener{
		private JDialog owner;
		/**
		 * Konstruktor
		 * @param owner Der zu dem Listener gehörende JFrame
		 */
		public Error_action(JDialog owner){
			this.owner=owner;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e){			
			if(e.getActionCommand().equals("OK")){
				owner.dispose();
			}
		}
	}
}
