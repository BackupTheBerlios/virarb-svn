package GUI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;


public  class Ip {

	public static  String getWanIp() {
	       	BufferedReader reader = null;
	       	String ip = new String();
	       	try {
	            URL url = new URL("http://checkip.dyndns.org/");
	            URLConnection con = url.openConnection();
	            con.connect();
	            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String line = reader.readLine();
	            ip = line.substring(76,line.length()-14);	          	
//	            System.out.println(ip);
	            reader.close(); 
	       	} 
     		catch (Exception ioe)
     		{
     			ioe.printStackTrace();
     		}
		  return ip;
	 }
	
	public static  String getLanIp() {
		String ip = new String();
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
