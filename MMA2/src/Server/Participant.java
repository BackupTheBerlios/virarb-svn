package Server;

import java.io.Serializable;
import gnu.cajo.utils.extra.ClientProxy;

public class Participant {
	private ClientProxy cp;
	private String name;
	private String lanIp;
	private String wanIp;
	private int port = 1234;
	
	public Participant( String name) {
		this.name = name;
		this.cp = null;
		this.lanIp = null;
		this.wanIp = null;
	}
	
	public Participant(String username, String lanIp, String wanIp, int port, ClientProxy cp) {
		super();
		this.cp = cp;
		this.name =username;
		this.lanIp = lanIp;
		this.wanIp =wanIp;
	}
	
	public ClientProxy getCp() {
		return cp;
	}
	public void setCp(ClientProxy cp) {
		this.cp = cp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
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

	public String getLanIp() {
		return lanIp;
	}

	public String getWanIp() {
		return wanIp;
	}

	public int getPort() {
		return port;
	}
	
}
