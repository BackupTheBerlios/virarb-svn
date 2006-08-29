package GUI;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Das IntegerDocument ist ein Document, welches nur
 * Eingaben von bis zu fünfstelligen Zahlen zulässt.
 * Wird genutz zur Eingabe von Portnummern.
 * @author Daniel Meurer
 */
public class IntegerDocument extends PlainDocument
{	
 /* (non-Javadoc)
 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
 */
public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException
    {
	  if(offset >= 5 ||  this.getLength()== 5){
		  Toolkit.getDefaultToolkit().beep(); //macht ein DÜT
	  }
	  else{
	      try{
	            Integer.parseInt(s);
	         }
	         catch(Exception ex)   //only allow integer values
	         {
	            Toolkit.getDefaultToolkit().beep(); //macht ein DÜT
	            return ;
	         }
	      super.insertString(offset,s, attributeSet);
	  }
    }
}