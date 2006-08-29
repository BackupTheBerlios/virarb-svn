package GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * Die Klasse Ip stellt statische Methoden zur Findung
 * von Ip-Adressen zur Verfügung
 * @author Daniel Meurer
 */
public  class Ip {

	/**
	 * Die WanIp ist die im Internet sichtbare Ip des Rechners
	 * @return WanIp
	 */
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
	
	/**
	 * Die LanIp ist die Ip des Rechners im lokalen Netz.
	 * @return LanIp
	 */
	public static String getLanIp() {
		String ip = new String();
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	/**
	 * der Clientport ist der Port des Clients.
	 * Die Callbacks des Servers laufen hierüber.d
	 * @return den ClientPort
	 */
	public static int getMyPort(){
		String x = Fileausgabe.getProperty("Port");
        if(x.equals("") || x == null ){
        	return 1234;
        }
        else{
        	return Integer.parseInt(x);
        }
	}

	/**
	 * der Serverport ist der Port zu dem verbunden wird.
	 * @return den ServerPort
	 */
	public static int getServerPort(){
		String x = Fileausgabe.getProperty("ServerPort");
        if(x.equals("") || x == null ){
        	return 1234;
        }
        else{
        	return Integer.parseInt(x);
        }
	}
}
	

