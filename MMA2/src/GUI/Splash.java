package GUI;

import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Die Klasse Spash öffnet beim Aufruf einen Screen auf dem ein Logo für 4 sec angezeigt wird.
 * @author Daniel Langer
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
			Thread.sleep(4000);
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
 
            JLabel jl_splash =new JLabel(new ImageIcon(this.getClass().getResource("icons/va.jpg")));

            jl_splash.setBorder(BorderFactory.createRaisedBevelBorder());
            splash.setSize(jl_splash.getPreferredSize());
            jl_splash.setName("splash");
            splash.getContentPane().add(jl_splash);
            splash.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - splash.getSize().width / 2, 
            		(Toolkit.getDefaultToolkit().getScreenSize().height / 2) - splash.getSize().height / 2);
        }
        return splash;
    }

}
