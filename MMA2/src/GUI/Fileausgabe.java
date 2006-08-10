package GUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Fileausgabe {
	private static File f = new File("VirArb.cfg");
	
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
