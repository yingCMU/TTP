package ttp;



public class ConDescriptor {
	private String srcaddr;
	private String dstaddr;
	private short srcport;
	private short dstport;
	private int serverSYN;
	private int clientSYN;
	
	private Timer timer;
	
	public ConDescriptor(String srcaddr, String dstaddr, short srcport, short dstport, int serverSYN, int clientSYN){
		this.srcaddr = srcaddr;
		this.dstaddr = dstaddr;
		this.srcport = srcport;
		this.dstport = dstport;
		this.serverSYN = serverSYN;
		this.clientSYN = clientSYN;
		
	}
	
	public boolean equals(ConDescriptor b){
		if (srcaddr.equals(b.srcaddr) && dstaddr.equals(b.dstaddr) 
				&& srcport == b.srcport && dstport ==b.dstport)
			return true;
		else 
			return false;
	}
	
	public String getKey() {
		return dstaddr + dstport;
	}


	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public void killTimer() {
		if (timer != null && timer.getSendCount() > 0) {
			timer.interrupt();
		}
	}

	public int getClientSYN() {
		return clientSYN;
	}

	public void setClientSYN(int clientSYN) {
		this.clientSYN = clientSYN;
	}

	public int getServerSYN() {
		return serverSYN;
	}

	public void setServerSYN(int serverSYN) {
		this.serverSYN = serverSYN;
	}
}
