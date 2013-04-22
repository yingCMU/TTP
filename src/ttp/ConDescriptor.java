package ttp;



public class ConDescriptor {
	private String serveraddr;
	 String clientaddr;
	private short serverport;
	 short clientport;
	private int serverSYN;
	private int clientSYN;
	
	private Timer timer;
	
	public ConDescriptor(String serveraddr, String clientaddr, short serverport, short clientport, int serverSYN, int clientSYN){
		this.serveraddr = serveraddr;
		this.clientaddr = clientaddr;
		this.serverport = serverport;
		this.clientport = clientport;
		this.serverSYN = serverSYN;
		this.clientSYN = clientSYN;
		
	}
	
	public boolean equals(ConDescriptor b){
		if (serveraddr.equals(b.serveraddr) && clientaddr.equals(b.clientaddr) 
				&& serverport == b.serverport && clientport ==b.clientport)
			return true;
		else 
			return false;
	}
	
	public String getKey() {
		return clientaddr + clientport;
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
