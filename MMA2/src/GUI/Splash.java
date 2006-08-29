package GUI;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

/**
 * Die Klasse Spash öffnet beim Aufruf einen Screen auf dem ein Logo für 4 sec angezeigt wird.
 * @author Daniel Langer, Daniel Meurer
 */
public class Splash {
	private static JWindow splash=null;
	
	/**
	 * Diese Methode ist für den Splash Screen beim Programmaufruf
	 * verantwortlich
	 */
	public static void main(String[] args) {
		new Splash();
	}
		
	/**
	 * Konstruktor
	 */
	private Splash(){
		showSplash(true);
		try {
			Thread.sleep(1000 * 4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		showSplash(false);
		Login inst = new Login();
		inst.setVisible(true);		
	}
	
	/**
	 * Gibt an ob der Screen angezeigt wird oder nicht
	 * @param show ja oder nein?
	 */
	private void showSplash(boolean show)
    {
        splash = getSplash();
        if (show) splash.setVisible(show);
        else
        {
            splash.setVisible(show);
            splash.dispose();
            splash = null;
        }
    }
	
	/**
	 * Gibt das fertige JWindow mit dem Logo zurück
	 * @return Das JWindow mit dem Logo
	 */
	private  JWindow getSplash() {
    
		if (splash == null)
    	{
            splash= new JWindow();
            JLabel jl_splash = new JLabel(new ImageIcon(this.getClass().getResource("icons/va.jpg")));

            jl_splash.setBorder(BorderFactory.createRaisedBevelBorder());
            splash.setSize(365, 265);       
            splash.getContentPane().add(jl_splash, BorderLayout.NORTH);            
            JProgressBar pbar = new JProgressBar();
            pbar.setSize(365,10);
            pbar.setIndeterminate(true);
            splash.getContentPane().add(pbar, BorderLayout.SOUTH);                       
            splash.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - splash.getSize().width / 2, 
            		(Toolkit.getDefaultToolkit().getScreenSize().height / 2) - splash.getSize().height / 2);            
    	}
        return splash;
    }
}
