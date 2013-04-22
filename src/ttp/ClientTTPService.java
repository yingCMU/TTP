package ttp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import datatypes.Datagram;

import services.DatagramService;

public class ClientTTPService extends TTPservice implements Runnable{
	
	
	private static DatagramService clientService;
	private final int LISTENER_MAX = 10; 
	int timer;//time out
	private int data_length;
	private int serverSYN;// current syn
	private int serverACK;// ack I should reply to him now
	private int clientSYN;
	private int clientACK;// my coming ack is everything is good
	
	
	private ConcurrentLinkedQueue<Datagram> request_queue;
	private ConcurrentLinkedQueue<Datagram> data_queue;
	
	private char ID;
	private int maximum_buffer = 10 ;
	private HashMap sending_buffer = new HashMap();
	
	private short win;
	//private HashMap rec_buffer = new HashMao();// using go back n, not needed
	
	public ClientTTPService(short srcport, short dstport, String dstaddr) throws SocketException{
		clientService = new DatagramService(srcport, 10);
		this.dstaddr = dstaddr;
		this.dstport = dstport;
		try
        {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
           // System.out.println(addr.getHostAddress());
            //System.out.println(hostname);
            this.srcport = srcport;
    		this.srcaddr = addr.getHostAddress();
        }catch(UnknownHostException e)
        {
             e.printStackTrace();
        }
		
		request_queue = new ConcurrentLinkedQueue<Datagram>();
		data_queue = new ConcurrentLinkedQueue<Datagram>();
		
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
	public void clientCon( int ACK,  Object data, short length){
		TTP ttpSYN = new TTP(ACK, 1, data, length);// a SYN packet
		short size = 0;//size of datagram , to do
		
		try {
			
			/*
			 * Datagram(String srcaddr, String dstaddr, short srcport,
			short dstport, short size, short checksum, Object data)
			 */
			
			Datagram datagram = constructPacket(ttpSYN);
			System.out.println("sending SYN...");
			clientService.sendDatagram(datagram);
			System.out.println(" SYN . sent  next receive syn+ack");
			datagram = clientService.receiveDatagram();//a new thread to handle? incoming queue?
			if( ( (TTP) datagram.getData()).isACK() ){
			System.out.println("3-way  ACK from server ip "+ datagram.getSrcaddr() + ":" 
					+ datagram.getSrcport() + " Data: " + datagram.getData());
		    
			}
			else{
				System.out.println("clientcon: received invalid packet from ip "+ datagram.getSrcaddr() + ":" 
						+ datagram.getSrcport());	
			}
			TTP ttpACK = new TTP(1, 0, data, length);
			datagram = constructPacket(ttpACK);
			clientService.sendDatagram(datagram);
			System.out.println("3-way finished");
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			//clientService.close();
		}
	}
	
	
	/*
	 * send data over a datagram service
	 */
	public void sendData( Object data, short dataLength) {
		
		TTP ttp = new TTP(serverACK,clientSYN, data, dataLength);
		Datagram datagram = constructPacket(ttp);
		
		try {
			clientService.sendDatagram(datagram);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * receive data
	 * if in order, remove corresponding data from sending_buffer, to do 
	 */
	public void recData() {
		Datagram datagram;
		try {
			System.out.println("client rec data");
			datagram = clientService.receiveDatagram();
			System.out.println("client rec data 2");
			TTP ttp = (TTP)datagram.getData();
			clientSYN = ttp.getACK();
			serverACK = ttp.getSYN()+ttp.getLength();
			readTTP(ttp);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	/*
	 * to be changed
	 * construct response ack
	 */
	private Datagram constructACK(int ACK, int SYN) {
		Datagram ack = new Datagram();
		ack.setSrcaddr(srcaddr);
		ack.setSrcport(srcport);
		ack.setDstaddr(dstaddr);
		ack.setDstport(dstport);
		ack.setData("ACK");
		return ack;
	}
	
	
	
	/*
	private TTP construct_ttpPacket(Object data, short dataLength){
		TTP ttpPacket = new TTP();;
		ttpPacket.setData(data);
		ttpPacket.setFragmentLength(dataLength);
		
		ttpPacket.setSYN(SYN);
		SYN += dataLength;
		ttpPacket.setACK(ACK);
		
		ttpPacket.setOffset((char)0);
		ttpPacket.setID((char)0);
		ttpPacket.setFlag((char)0);
		return ttpPacket;
	}
	*/
	

	/*
	 * send data over a datagram service
	 */

	
	
	/*
	 * to handle multiple connection simultaneously
	 */
	public void mulCon(){
		
	}
	/*
	 * if out of order or time out, get data from send buffer and retransmit
	 * 
	 */
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
	/*
	 * close datagram services
	 */
	public void closeService(){
		clientService.close();
		//dataService.close();
		System.out.println("ttp server service closed");
	}

	
	/* multithread for application layer
	private class Acceptor implements Runnable {

		public void run() {
			try {
				Socket s = serverSocket.accept();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}
	*/
}
