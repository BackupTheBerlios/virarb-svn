package Server;

import java.util.Date;
import gnu.cajo.utils.extra.ClientProxy;

/**
 * Die Klasse Participant stellt einen Teilnehmer am virtuellen Arbeitsraum dar.
 * @author Daniel Meurer
 */
public class Participant {
	private ClientProxy cp;
	private String name;
	private String lanIp;
	private String wanIp;
	private int port;
	private Date lastDummy = new Date();
	
	/**
	 * Konstruktor
	 * @param name der Name des Teilnehmers
	 */
	public Participant( String name) {
		this.name = name;
		this.cp = null;
		this.lanIp = null;
		this.wanIp = null;
	}
	
	/**
	 * Konstruktor
	 * @param username der Name des Teilnehmers
	 * @param lanIp die Lokale IP des Teilnehmers
	 * @param wanIp die Internet Ip des Teilnehmers
	 * @param port der port des Teilnehmers
	 * @param cp der Proxy des Teilnehmers
	 */
	public Participant(String username, String lanIp, String wanIp, int port, ClientProxy cp) {
		super();
		this.cp = cp;
		this.name =username;
		this.lanIp = lanIp;
		this.wanIp =wanIp;
		this.port = port;
	}
	
	/**
	 * Gibt den Proxy des Teilnehmers zurück
	 * @return den CLientProxy des Teilnehmers
	 */
	public ClientProxy getCp() {
		return cp;
	}
	/**
	 * Setzt den Proxy des Teilnehmers
	 * @param cp den ClientProxy des Teilnehmers
	 */
	public void setCp(ClientProxy cp) {
		this.cp = cp;
	}
	/**
	 * Gibt den Namen des Teilnehmers zurück
	 * @return den Name  des Teilnehmers
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setze den Namen des Teilnehmers
	 * @param name den Name des Teilnehmers
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(o instanceof String){
			if(o.equals(this.name)){
				return true;
			}
			else{
				return false;
			}	
		}
		else if(o instanceof Participant)
			if(((Participant)o).name.equals(this.name)){
				return true;
			}
			else{
				return false;
			}	
		else
			return o == this;
	}

	/**
	 * Gibt die lokale Ip des Teilnehmers zurück
	 * @return die lokale Ip des Teilnehmers
	 */
	public String getLanIp() {
		return lanIp;
	}

	/**
	 * Gibt die internet Ip des Teilnehmers zurück
	 * @return die Internet Ip des Teilnehmers
	 */
	public String getWanIp() {
		return wanIp;
	}

	/**
	 * Gibt den Port des Teilnehmers zurück
	 * @return den Port des Teilnehmers
	 */
	public int getPort() {
		return port;
	}

	public Date getLastDummy() {
		return lastDummy;
	}

	public void setLastDummy(Date lastDummy) {
		this.lastDummy = lastDummy;
	}
	
	
	
}
