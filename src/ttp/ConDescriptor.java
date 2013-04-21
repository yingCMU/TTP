package ttp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConDescriptor {
	private String srcaddr;
	private String dstaddr;
	private short srcport;
	private short dstport;
	public ConDescriptor(String srcaddr, String dstaddr, short srcport, short dstport){
		this.srcaddr = srcaddr;
		this.dstaddr = dstaddr;
		this.srcport = srcport;
		this.dstport = dstport;
		
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
}
