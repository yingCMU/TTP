package ttp;

import java.util.Hashtable;
import datatypes.Datagram;

public class ServerTTPService extends TTPservice{
	private Hashtable<String, ConDescriptor> clientList;
	private String data;
	
	public ServerTTPService(String srcaddr, short srcPort) {
		super(srcaddr, srcPort);
		
		clientList = new Hashtable<String, ConDescriptor>();
		
		data = "Hello Client";
	
	}
	
	public void serverListen(){
		Datagram datagram = receiveData();
		
		
		TTP ttp = (TTP)datagram.getData();
		System.out.println("Server Receive from client, Category: " + (int)ttp.getCategory());
		
		if (ttp.getCategory() == (char)0) { //syn packet
			String dstaddr = datagram.getSrcaddr();
			short dstPort = datagram.getSrcport();
			
			ConDescriptor client = new ConDescriptor(srcaddr, dstaddr,
										srcPort, dstPort, ttp.getSYN());
			clientList.put(client.getKey(), client);
			
			serverSendData(client, null, (short)0, (char)1);
			
		} else if (ttp.getCategory() == (char)2) { // last hand shake
			if (clientList.containsKey(datagram.getSrcaddr() + datagram.getSrcport())) {
				System.out.println("Hand shake finishes");
				ConDescriptor client = clientList.get(datagram.getSrcaddr() 
														 + datagram.getSrcport());
				client.setConnected(true);
				client.killTimer();
				
				//send data
				serverSendData(client, data, (short)data.length(), (char)3);
			}
		} else {
			if (clientList.containsKey(datagram.getSrcaddr() + datagram.getSrcport())) {
				System.out.println("Receive Data From Client");
				ConDescriptor client = clientList.get(datagram.getSrcaddr() 
														 + datagram.getSrcport());
				client.killTimer();
				if (client.isConnected()) {
					int ack = ttp.getACK();
					
					if (ack == client.getExpectSYN()) { //right
						client.setServerSYN(ack);
						client.setACK(ttp.getSYN()+ttp.getLength()) ;
						client.setServerSYN(ack);
					} else {							//wrong
						//resend data
						serverSendData(client, data, (short)data.length(), (char)3);
					}
				}

			}
		}
	}
	
	public int serverSendData(ConDescriptor client, Object data, short dataLength, char category) {
		Timer timer = super.sendData(client.getACK(), client.getServerSYN(), client.getClientAddr(), 
					   client.getClientPort(), data, dataLength, category);
		client.setTimer(timer);
		client.setExpectSYN(client.getServerSYN() + dataLength);
		
		return 0;
	}
}
