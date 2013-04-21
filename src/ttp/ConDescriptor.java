package ttp;



public class ConDescriptor {
	private String srcaddr;
	private String dstaddr;
	private short srcport;
	private short dstport;
	private int SYN;
	private int ACK;
	public ConDescriptor(String srcaddr, String dstaddr, short srcport, short dstport, int SYN, int ACK){
		this.srcaddr = srcaddr;
		this.dstaddr = dstaddr;
		this.srcport = srcport;
		this.dstport = dstport;
		this.ACK = ACK;
		this.SYN = SYN;
		
	}
	
	public boolean equals(ConDescriptor b){
		if (srcaddr.equals(b.srcaddr) && dstaddr.equals(b.dstaddr) 
				&& srcport == b.srcport && dstport ==b.dstport)
			return true;
		else 
			return false;
	}
	/*
	public static void main(String[] args){
		ConDescriptor a = new ConDescriptor("1","2", (short)3,(short)4);
		ConDescriptor b= new ConDescriptor("1","2", (short)3,(short)4);
		System.out.println(a.equals(b));
		
		
		
	}*/

	public int getSYN() {
		return SYN;
	}

	public void setSYN(int sYN) {
		SYN = sYN;
	}

	public int getACK() {
		return ACK;
	}

	public void setACK(int aCK) {
		ACK = aCK;
	}
}
