package GUI;

import java.io.File;
import java.io.Serializable;

public class ListEntry implements Serializable{
	public boolean IsLocal = false;
	private String owner;
	private File file;

	public ListEntry(File file, String owner) {
		super();
		this.file = file;
		this.owner = owner;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String toString(){
		if(IsLocal){
			return "<html><font color='Green'>"+file.getName()+" ["+owner+"]"+"</font></html>";
		}
		else{
			return "<html><i>"+file.getName()+" ["+owner+"]"+"</i></html>";
		}
	}

	public File getFile() {
		return file;
	}

	public String getOwner() {
		return owner;
	}
}
