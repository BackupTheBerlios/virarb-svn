package GUI;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class IntegerDocument extends PlainDocument
{	
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
	            //System.out.println("Keine Zahl!");
	            return ;
	         }
	      super.insertString(offset,s, attributeSet);
	  }
    }
}