package ttp;

public class ClientOnServer {
	private int SYN;
	private int ACK;
	private ConDescriptor dp;
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
	public ConDescriptor getDp() {
		return dp;
	}
	public void setDp(ConDescriptor dp) {
		this.dp = dp;
	}
}
