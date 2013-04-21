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
	//private Pseudo Pseudo_Header;
	private short checkSum;
	private Object data;//?
	private char offset;//for fragment in unit of 8
	private char ID;
	short fragment_length;// for each fragmented message 
	private short length;// for each fragmented message 
	private char flag;//indicate whether has more fragment
	
	public TTP() {
		offset = 0;
		ID = 0;
		flag = 0;
		fragment_length = 0;
	}
	/*
	 * construct response ttp packet to be sent, update ack and syn
	 */
	public TTP(int ACK, int SYN, Object data,short length){
		this.ACK = ACK;
		this.length = length;
		this.SYN = SYN;
		this.data = data;
		
		offset = 0;
		ID = 0;
		flag = 0;
		fragment_length = 0;
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
		return fragment_length;
	}
	
	public void setFragmentLength(short fragmentLength) {
		this.fragment_length = fragmentLength;
	}
	
	public char getFlag() {
		return flag;
	}
	
	public void setFlag(char flag) {
		this.flag = flag;
	}
	private short getFragLength() {
		// TODO Auto-generated method stub
		return fragment_length;
	}
	
	public short getCheckSum(){
		//using int to prevent overflow, will get the lower 16 bits
		int checkSum = 0; 
		checkSum += (short) getSYN() + (short)(getSYN() >> 16);
		checkSum += (short) getACK() + (short)(getACK() >> 16);
		checkSum += (short) getOffset();
		checkSum += (short) getID();
		checkSum += (short) getFragLength();
		checkSum += (short) getFlag();
		return (short)checkSum;
		
		
		
	}

	public boolean isSYN() {
	
		return (SYN == 1);
	}
	public boolean isACK() {
		
		return (ACK == 1);
	}
	
	public boolean isData(){
		if ((flag ==0))
			return true;
		else 
			return false;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}
	
}
