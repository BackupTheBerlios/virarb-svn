package GUI;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.extra.Xfile;
import java.io.File;
import java.io.IOException;
import javax.swing.JProgressBar;


public class FileDownload implements Runnable {
	DnDText dnd;
	File sourceFile, destFile;
	Object x;
	ListEntry entry;
	JProgressBar pbar;
	
	
	public FileDownload(DnDText dnd, ListEntry entry, File sourceFile, File destFile, Object x) {
		super();
		this.dnd = dnd;
		this.entry = entry;
		this.sourceFile = sourceFile;
		this.destFile = new File(destFile.getAbsolutePath());
		this.x = x;
		pbar = dnd.getPbar();
	}



	public void run() {
		pbar.setIndeterminate(true);
		try {
			Xfile.fetch(x, "file:"+sourceFile.getPath(), destFile.getName());
			entry.IsLocal = true;
			entry.setFile(destFile);
			dnd.getTarget().nextFocus();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pbar.setIndeterminate(false);
	}
}