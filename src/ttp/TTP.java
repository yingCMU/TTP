package ttp;

import java.io.Serializable;

/*
 * TTP layer  header and data which goes into the UDP layer payload
 * padding 
 * */
public class TTP implements Serializable{
	//size??
	//16 bit
	private short windowSize;
	//32 bit
	private int SYN;
	//32 bit
	private int ACK;
	private short checkSum;
	private Object data;//?
	private char offset;//for fragment in unit of 8
	private char ID;
	private short fragmentLength;// for each fragmented message 
	private char flag;//indicate whether has more fragment
	
	public TTP() {
		offset = 0;
		ID = 0;
		flag = 0;
		fragmentLength = 0;
	}
	
	public int getWSize() {
		return windowSize;
	}
	
	public void setWSize(short wsize) {
		this.windowSize = wsize;
	}
	
	public int getSYN(){
		return SYN;
	}
	
	public void setSYN(int syn) {
		this.SYN = syn;
	}
	
	public int getACK() {
		return ACK;
	}
	
	public void setACK(int ack) {
		this.ACK = ack;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public short getCheckSum() {
		return checkSum;
	}
	
	public void setCheckSum(short checkSum) {
		this.checkSum = checkSum;
	}
	
	public char getOffset() {
		return offset;
	}
	
	public void setOffset(char offset) {
		this.offset = offset;
	}
	
	public char getID() {
		return ID;
	}
	
	public void setID(char ID) {
		this.ID = ID;
	}
	public short getFragmentLength() {
		return fragmentLength;
	}
	
	public void setFragmentLength(short fragmentLength) {
		this.fragmentLength = fragmentLength;
	}
	
	public char getFlag() {
		return flag;
	}
	
	public void setFlag(char flag) {
		this.flag = flag;
	}
}
