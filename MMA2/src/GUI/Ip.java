package GUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;


public  class Ip {

	public static String getWanIp() {
	       	BufferedReader reader = null;
	       	String ip = new String();
	       	try {
	            URL url = new URL("http://checkip.dyndns.org/");
	            URLConnection con = url.openConnection();
	            con.connect();
	            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String line = reader.readLine();
	            ip = line.substring(76,line.length()-14);	          	
	            reader.close(); 
	       	} 
     		catch (Exception ioe)
     		{
     			ioe.printStackTrace();
     		}
		  return ip;
	 }
	
	public static String getLanIp() {
		String ip = new String();
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	public static int getMyPort(){
		int port = 1234;
		File f = new File("VirArb.cfg");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader reader = new BufferedReader(fr);
			port = Integer.parseInt(reader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
        return port;
	}

	public static int getServerPort(){
		int port = 1234;
		File f = new File("VirArb.cfg");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader reader = new BufferedReader(fr);
			reader.readLine();
			port = Integer.parseInt(reader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
        return port;
	}
}
