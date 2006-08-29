package GUI;

import gnu.cajo.invoke.Remote;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Die Klasse DndText erstellt einen JScrollpane, in dem sich eine Liste befinden,
 * in die Dateien per Drag&Drop geladen und genauso wieder herausgenommen werden
 * können. 
 * @author Klassen,Kokoschka,Langer,Meurer
 **/
public class DnDText extends JScrollPane implements DropTargetListener,DragGestureListener,MouseListener,DragSourceListener{
	private Object server;
	private JList target = null;
	private DefaultListModel values;
	private DragSource dragSource;
	private String username;
	private JProgressBar pbar;
	Thread t;
	
	/**
	 * Konstruktor
	 * @param session Die ChatSession, über die die Files transferiert werden.
	 */
	public DnDText(Object server, String username, JProgressBar pbar) {
		this.server = server;			 
		this.username = username;
		this.pbar = pbar;
		try {
			this.values = (DefaultListModel)Remote.invoke(server, "getValues", null);
			target = new JList(values);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(target,DnDConstants.ACTION_COPY,this);				
		getViewport().add(target);
		DropTarget dt = new DropTarget();
		dt.setComponent(target);
		try {
			dt.addDropTargetListener(this);
		} catch (TooManyListenersException e) {
			System.out.println("Es kann nur EIN Listener bei " + "der Komponente registriert werden");
		}
		target.addMouseListener(this);
	}
	
	/**
	 * Ein Element des Typs ListEntry in die Liste einfügen
	 * @param entry der Listeneintrag
	 */
	public void addElement(ListEntry entry){
		this.values.addElement(entry);
	}

	/**
	 * Ein Element aus der Liste entfernen
	 * @param entry der Eintrag
	 */
	public void removeElement(ListEntry entry){
		this.values.removeElement(entry);
	}
	
	/**
	 * Ein remote-Objekt wird auf die lokale Festplatte kopiert
	 * und somit local verfügbar gemacht.
	 * @param index der Index der Datei in der Liste
	 */
	public void makeLocallyAvailable(int index){
		ListEntry entry = (ListEntry)values.getElementAt(index);
		int[] i = {index};
		try {
			Object[] xf=(Object[])Remote.invoke(server, "getFile", i);	
			Object x = xf[0];			
			File f = (File)xf[1];
			File tempfile=new File(f.getName());
			tempfile.createNewFile();
			tempfile.deleteOnExit();								
			t=new Thread(new FileDownload(this, entry, f, tempfile, x));
			t.start();				
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}							
	}
	
	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dEvent) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dEvent) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dEvent) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dEvent) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dEvent) {
		if ((dEvent.getSourceActions() & DnDConstants.ACTION_COPY) == 0)
			dEvent.rejectDrop();
		else {
			dEvent.acceptDrop(DnDConstants.ACTION_COPY);
			Transferable trans = dEvent.getTransferable();
			DataFlavor[] currentFlavors = dEvent.getCurrentDataFlavors();
			DataFlavor selectedFlavor = null;
			for (int i = 0; i < currentFlavors.length; i++) {
				if (DataFlavor.javaFileListFlavor.equals(currentFlavors[i])) {
					selectedFlavor = currentFlavors[i];
					break;					
				}
			}
			if (selectedFlavor != null) {
				try {
					Iterator dateien = ((java.util.List) trans.getTransferData(selectedFlavor)).iterator();
					while (dateien.hasNext()) {
						File file =(File) dateien.next();	
						String name=file.getName();
						Remote.invoke(server, "setStatus", "Neuer File "+name+" wird bereitgestellt.");
						try {
							Remote.invoke(server, "setStatus", "Ladevorgang abgeschlossen. File '"+name+ "' kann jetzt heruntergeladen werden");						
							Object[] args = {username, file};
							Remote.invoke(server, "addFile", args); 
						} catch (Exception e1) {		
							Remote.invoke(server, "setStatus", "File "+name+" konnte leider nicht geladen werden.");
							e1.printStackTrace();
						}
					}				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		if(target.getSelectedIndex()>=0){
			int index=target.getSelectedIndex();
			ListEntry entry = (ListEntry)this.values.getElementAt(index);
			System.out.println("DropEvent ausgelöst");
			if(entry.IsLocal){
				try {
					File f = entry.getFile();
					Trans temp= new Trans(f);
					dge.startDrag(null,null,null,temp,this);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			else{
				System.out.println("File noch nicht local!");
			}
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {
		int index=target.getSelectedIndex();
		if(index>=0){
			ListEntry entry = (ListEntry)values.getElementAt(index);
	
			if(!entry.IsLocal && me.getClickCount() == 2){				
				makeLocallyAvailable(index);
			}		
			else if(entry.IsLocal && me.getClickCount()>=2){
				File tempfile = entry.getFile();
				try {
					String osName = System.getProperty("os.name" );
		            if( osName.equals( "Windows XP" ) )
		            {
		                Runtime.getRuntime().exec("cmd.exe /C start "+tempfile.getPath());
		            }
	//  		        if(OS=MAC/LINUX)?????					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {	
		if(e.getButton() == MouseEvent.BUTTON3 && !values.isEmpty() && target.getSelectedIndex()>=0){		
			JPopupMenu popup = new JPopupMenu();
			JMenuItem aktionen0 = new JMenuItem("Datei verfügbar machen.");
			aktionen0.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					makeLocallyAvailable(target.getSelectedIndex());			
				}				
			});
			popup.add(aktionen0);
			JMenuItem aktionen1 = new JMenuItem("Datei löschen");
			aktionen1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						ListEntry entry = (ListEntry)values.getElementAt(target.getSelectedIndex());
						Remote.invoke(server, "removeFile", entry);
					} catch (Exception e) {
							e.printStackTrace();
					}	
				}				
			});
			popup.add(aktionen1);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}		
	}

	
	
	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent arg0) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent arg0) {
	}

	/**
	 * @return JList
	 */
	public JList getTarget() {
		return target;
	}

	/**
	 * @return DefaultListModel mit den Dateien
	 */
	public DefaultListModel getValues() {
		return values;
	}

	/**
	 * @return die Progressbar
	 */
	public JProgressBar getPbar() {
		return pbar;
	}
}


/**
 * Die Klasse Trans stellt ein Transferable Object zur Verfügung,
 * welche es erlaubt Dateien über RMI zu verschicken.
 * @author Daniel Meurer
 **/
class Trans implements Transferable { 
	/**
	 * Die Datei
	 */
	private File f;
	private boolean gotData = false;
	private ArrayList list = new ArrayList( );
	private static final DataFlavor javaFileListFlavor = DataFlavor.javaFileListFlavor;
	private static final DataFlavor[] flavors = {javaFileListFlavor};
	private static final List flavorList = Arrays.asList( flavors );
	
	/**
	 * Konstruktor
	 * @param temp Die Datei, die verschickt werden soll
	 * @throws IOException
	 */
	public Trans(File f) throws IOException {
		this.f = f;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor) {
		if(!gotData){
			list.clear();
			list.add(f);
			gotData = true;
		}
		return list;	
	}	

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors( ) {
		return flavors;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		  return (flavorList.contains(flavor));
	} 
}
