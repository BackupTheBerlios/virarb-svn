package GUI;

import gnu.cajo.utils.extra.Xfile;
import java.io.File;
import javax.swing.JProgressBar;

/**
 * Die Klasse FileDownload erledigt den Download von RemoteFiles
 * in das lokale File System 
 * @author Daniel Meurer
 */
public class FileDownload implements Runnable {
	private DnDText dnd;
	private File sourceFile, destFile;
	private Object x;
	private ListEntry entry;
	private JProgressBar pbar;	
	
	/**
	 * Konstruktor
	 * @param dnd der FileTable
	 * @param entry der Listeneintrag
	 * @param sourceFile SourceFile
	 * @param destFile ZielFile
	 * @param x der FileServer
	 */
	public FileDownload(DnDText dnd, ListEntry entry, File sourceFile, File destFile, Object x) {
		super();
		this.dnd = dnd;
		this.entry = entry;
		this.sourceFile = sourceFile;
		this.destFile = new File(destFile.getAbsolutePath());
		this.x = x;
		pbar = dnd.getPbar();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		pbar.setIndeterminate(true);
		try {
			Xfile.fetch(x, "file:"+sourceFile.getPath(), destFile.getName());
			entry.IsLocal = true;
			entry.setFile(destFile);
			dnd.getTarget().nextFocus();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		pbar.setIndeterminate(false);
	}
}