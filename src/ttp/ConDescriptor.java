package ttp;



public class ConDescriptor {
	private String srcaddr;
	private String dstaddr;
	private short srcport;
	private short dstport;
	private int SYN;
	private Timer timer;
	
	public ConDescriptor(String srcaddr, String dstaddr, short srcport, short dstport, int SYN){
		this.srcaddr = srcaddr;
		this.dstaddr = dstaddr;
		this.srcport = srcport;
		this.dstport = dstport;
		this.SYN = SYN;
		
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

	public int getSYN() {
		return SYN;
	}

	public void setSYN(int sYN) {
		SYN = sYN;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public void killTimer() {
		if (timer != null && timer.getSendCount() > 0) {
			timer.interrupt();
		}
	}
}
