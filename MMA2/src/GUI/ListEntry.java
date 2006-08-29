package GUI;

import java.io.File;
import java.io.Serializable;

/**
 * Ein Listentry ist ein Eintrag im FileTable.
 * Er besteht aus NAme und Pfad des Files und einem Flag, dass angibt
 * ob die Datei bereits local verfügbar gemacht wurde.
 * @author Daniel Meurer
 */
public class ListEntry implements Serializable{
	public boolean IsLocal = false;
	private String owner;
	private File file;

	/**
	 * Konstruktor
	 * @param file Die Datei
	 * @param owner Der Besitzer der Datei
	 */
	public ListEntry(File file, String owner) {
		super();
		this.file = file;
		this.owner = owner;
	}

	/**
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(IsLocal){
			return "<html><font color='Green'>"+file.getName()+" ["+owner+"]"+"</font></html>";
		}
		else{
			return "<html><i>"+file.getName()+" ["+owner+"]"+"</i></html>";
		}
	}

	/**
	 * @return die Datei
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return den Besitzer
	 */
	public String getOwner() {
		return owner;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object x){
		if(x instanceof ListEntry){
			ListEntry entry = (ListEntry)x;
			if(entry.getFile().getName().equals(this.file.getName()) && entry.getOwner().equals(this.owner)){
				return true;
			}
			return false;
		}
		return false;
	}
}
