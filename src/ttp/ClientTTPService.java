package ttp;

import datatypes.Datagram;

public class ClientTTPService extends TTPservice{
	
	private String dstaddr;
	private short dstport;
	private int SYN;
	private int expectSYN;
	private int ACK;
	
	private boolean isConnected;
	
	private Timer timer;
	
	private String data;
	
	public ClientTTPService(String srcaddr, short srcPort){
		super(srcaddr, srcPort);
		SYN = 0;
		ACK = 0;
		isConnected = false;
		
		data = "Hello Server";
	}
	
	public void connect (String dstaddr, short dstPort) {
		System.out.println("Connecting......");
		this.dstaddr = dstaddr;
		this.dstport = dstPort;
		
		while(true) {
			System.out.println("Sending SYN data");
			clientSendData(null, (short)0, (char)0);//SYN
			
			Datagram datagram = receiveData();
			System.out.println("Receiving server ACK");
			
			timer.interrupt();
			
			TTP ttp = (TTP)datagram.getData();
			int ack = ttp.getACK();
			
			System.out.println("ACK: " + ack + " ExpectSYN: " + expectSYN);
			
			if (ttp.getCategory() == (char)1 && ack == expectSYN) {
				
				System.out.println("Sending SYN + ACK");
				
				ACK = ttp.getSYN() + ttp.getLength();
				SYN = expectSYN;
				
				clientSendData(null, (short)0, (char)2); //SYN + ACK
				isConnected = true;
				System.out.println("connectioin established");
				break;
			}
		}
		System.out.println("Client connecting end");
	}

	public void clientSendData(Object data, short dataLength, char category) {
		timer = sendData(ACK, SYN, dstaddr, dstport, data, dataLength, category);
		expectSYN = SYN + dataLength;
	}
	
	public void clientSendData(Object data, short dataLength) {
		timer = sendData(ACK, SYN, dstaddr, dstport, data, dataLength, (char)3);
		
		expectSYN = SYN + dataLength;
	}

	public void clientReceiveData() {
		if (isConnected) {
			Datagram datagram = receiveData();
			TTP ttp = (TTP)datagram.getData();
			int ack = ttp.getACK();
			
			timer.interrupt();
			System.out.println("Timer alive: " + timer.isAlive());
			
			if (ttp.getCategory() == (char)3 && ack == expectSYN) {

				ACK = ttp.getSYN() + ttp.getLength();
				SYN = expectSYN;
				
				clientSendData(null, (short)0, (char)3); //SYN + ACK
				
				System.out.println("connectioin established");
			} else {
				clientSendData(data, (short)data.length());
			}
		} else {
			System.out.println("Please establish the connection first");
		}
	}
}
