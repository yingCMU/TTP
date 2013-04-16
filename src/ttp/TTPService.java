package ttp;

import java.io.IOException;
import java.net.SocketException;

import datatypes.Datagram;

import services.DatagramService;

public class TTPService implements Runnable{
	DatagramService udpService;
	
	int timer;//time out
	private TTP ttpPacket;
	private Datagram datagram;
	private int SYN;
	private int ACK;
	private char ID;
	
	public TTPService() throws SocketException{
		
	}
	
	/*
	 * start a new connection
	 * 3-way handshakes
	 *  Host A sends a TCP SYNchronize packet to Host B
	 *	Host B receives A's SYN
	 *	Host B sends a SYNchronize-ACKnowledgement
	 *	Host A receives B's SYN-ACK
	 *	Host A sends ACKnowledge
 	 *	Host B receives ACK. 
	 *	TCP socket connection is ESTABLISHED.
	 */
	public void startCon(String srcIp, String dstIp, 
		short srcPort, short dstPort){
		datagram = new Datagram(srcIp, dstIp, srcPort, dstPort);
		SYN = 0;
		ACK = 0;
	}
	/*
	 * send data over a connection
	 */
	public void sendData(Object data, short dataLength) throws IOException{
		ttpPacket.setData(data);
		ttpPacket.setFragmentLength(dataLength);
		
		ttpPacket.setSYN(SYN);
		SYN += dataLength;
		ttpPacket.setACK(ACK);
		
		ttpPacket.setOffset((char)0);
		ttpPacket.setID((char)0);
		ttpPacket.setFlag((char)0);
		
		datagram.setData(ttpPacket);
		
	}
	/*
	 * receive data
	 */
	public void recData() {
		
	}
	/*
	 * close a connection
	 */
	public void closeCon(){
		
	}
	/*
	 * to handle multiple connection simultaneously
	 */
	public void mulCon(){
		
	}
	public void retransmit(int time){
		
	}
	/*
	 * for situations where transfer large datagrams
	 */
	static void split(){
		
	}
	static void segment(){
		
	}
	static void reassemble(){
		
	}
	/*
	 * Go-Back-N
	 */
	private static void goBackN(){
		
	}
	
	static void checkDup(TTP in){
		
	}
	static void checkSum(){
		
	}
	static void checkDelayed(TTP in){
		
	}
	static void checkDroped(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

}
