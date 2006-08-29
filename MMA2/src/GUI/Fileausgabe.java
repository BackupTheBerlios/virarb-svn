package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Die Klasse Fileausgabe erledigt statisch
 * die Ausgaben in die Konfigurationsdatei.
 * Es können beliebige Properties mit zugehörigen Werten gespeichert und
 * wieder geladen werden.
 * @author Daniel Meurer
 */
public class Fileausgabe {
	private static File f = new File("VirArb.cfg");
	
	/**
	 * Läd ein Property aus der Datei
	 * @param propertyName der Name des Properties
	 * @return den Wert des Properties
	 */
	public static String getProperty(String propertyName){
		String value = new String();		
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			RandomAccessFile file = new RandomAccessFile (f, "rw");
			String line;
			while((line = file.readLine()) != null){
				if(line.contains(propertyName)){
					value = line.substring(propertyName.length()+3,line.length());				
					break;
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return value;
	}
	
	/**
	 * Ein Property speichern.
	 * @param propertyName der Name des Properties
	 * @param value den Wert des Properties
	 */
	public static void setProperty(String propertyName, String value){	
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			File tf = new File("VirArb.tmp");
			tf.createNewFile();
			RandomAccessFile file = new RandomAccessFile (f, "rw");
			RandomAccessFile tempfile = new RandomAccessFile(tf, "rw");
			String line;
			while((line = file.readLine()) != null){
				if(!line.contains(propertyName)){
					tempfile.writeBytes(line+"\r\n");
				}
			}
			tempfile.writeBytes("["+propertyName+"] "+value+"\r\n");
			tempfile.close();
			file.close();
			f.delete();
			tf.renameTo(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
}
