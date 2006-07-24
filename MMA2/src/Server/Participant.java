package Server;

import gnu.cajo.utils.extra.ClientProxy;

public class Participant{
	ClientProxy cp;
	String name;
	
	public Participant( String name, ClientProxy cp) {
		super();
		this.cp = cp;
		this.name = name;
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
	
}
