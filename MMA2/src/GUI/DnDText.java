package GUI;

import gnu.cajo.invoke.Remote;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import Server.FileServer;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
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
	private FileServer fserver;
	Thread t;
	
	
	/**
	 * Konstruktor
	 * @param session Die ChatSession, über die die Files transferiert werden.
	 */
	public DnDText(Object server) {
		this.server=server;			 
		try {
			this.values=(DefaultListModel)Remote.invoke(server, "getValues", null);
			target=new JList(values);
//			FileServer starten
			fserver=new FileServer();
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
	 * Ein Element (nur den Namen!!!)  in die Liste einfügen
	 * @param filename der Name des Files
	 */
	public void addElement(String filename){
		this.values.addElement(filename);
	}

	public void dragEnter(DropTargetDragEvent dEvent) {
	}

	public void dragOver(DropTargetDragEvent dEvent) {
	}

	public void dragExit(DropTargetEvent dEvent) {
	}

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
//						URI uri = file.toURI();
//						URL url = file.toURL();
						String name=file.getName();
						Remote.invoke(server, "setStatus", "Neuer File "+name+" wird bereitgestellt.");
//						session.setStatus("Neuer File "+name+" wird von User "+session.getNickname()+" bereitgestellt.");
						try {
							Remote.invoke(server, "setStatus", "Ladevorgang abgeschlossen. File '"+name+ "' kann jetzt heruntergeladen werden");						
//							session.setStatus("Ladevorgang abgeschlossen. File '"+name+ "' kann jetzt heruntergeladen werden");			
							Object[] args = {file,name,fserver.getLanIp(), fserver.getWanIp()};
							Remote.invoke(server, "addFile",args); 
//							session.addFile(file,name,InetAddress.getLocalHost().getHostAddress());
						} catch (Exception e1) {		
//							session.setStatus("File "+name+" konnte leider nicht geladen werden.");
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

	
	public void dragGestureRecognized(DragGestureEvent dge) {
		if(target.getSelectedIndex()>=0){
			int index=target.getSelectedIndex();
			System.out.println("DropEvent ausgelöst");
			File tempfile;

			try {
				tempfile=new File(values.get(index).toString());
				tempfile.createNewFile();
				tempfile.deleteOnExit();
				
				int[] i = {index};
				Object[] fileinfo=(Object[])Remote.invoke(server, "getFile", i);	
				t=new Thread(new FileDownload(fileinfo,tempfile,dge,this));
				t.start();
				Trans temp=new Trans(tempfile);
				dge.startDrag(null,null,null,temp,this);
						
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {
		if(target.getSelectedIndex()>=0){
			if(me.getClickCount()>=2){
				int index=target.getSelectedIndex();
				System.out.println("DoubleClick ausgelöst");
				File tempfile;
				try {
//					byte[] data=session.getFile(index);	
					tempfile=new File(values.get(index).toString());
					tempfile.createNewFile();
//					FileOutputStream out = new FileOutputStream(tempfile);
//					out.write(data);
//					out.close( );
//					Runtime.getRuntime().exec("start "+tempfile.getAbsolutePath());
					tempfile.deleteOnExit();
					
					String osName = System.getProperty("os.name" );
//			        String[] cmd = new String[3];
		            if( osName.equals( "Windows XP" ) )
		            {
		                Runtime.getRuntime().exec("cmd.exe /C start "+tempfile.getAbsolutePath());
		            }
//  		           if(OS=MAC/LINUX)?????
		            
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void dragDropEnd(DragSourceDropEvent arg0) {
		//t.start(); // starte download
	}

	public void dragEnter(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DragSourceEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
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
	private File temp;

	/**
	 * Konstruktor
	 * @param temp Die Datei, die verschickt werden soll
	 * @throws IOException
	 */
	public Trans(File temp) throws IOException {
		this.temp = temp;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor) {
		ArrayList list = new ArrayList( );
		list.add(temp);
		return list;
	}	

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors( ) {
		DataFlavor[] df = new DataFlavor[1];
		df[0] = DataFlavor.javaFileListFlavor;
		return df;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if(flavor == DataFlavor.javaFileListFlavor) {
			return true;
		}
		return false;

	} 
}















