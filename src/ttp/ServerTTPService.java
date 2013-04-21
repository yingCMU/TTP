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

public class ServerTTPService implements Runnable{
	private static DatagramService listenService;
	private static DatagramService dataService;
	private static DatagramService clientService;
	private final int LISTENER_MAX = 10; 
	int timer;//time out
	private int data_length;
	private Datagram datagram;
	private int hisSYN;// current syn
	private int hisACK;// ack I should reply to him now
	private ConcurrentLinkedQueue<Datagram> request_queue;
	private ConcurrentLinkedQueue<Datagram> data_queue;
	private HashMap<Integer,ConDescriptor> active_connections;//record for active connections
	private int mySYN;
	private int myACK;// my coming ack is everything is good
	private char ID;
	private int maximum_buffer = 10 ,MSS;
	private HashMap sending_buffer = new HashMap();
	private String srcaddr;
	private String dstaddr;
	private short srcport;
	private short dstport;
	private short win;
	//private HashMap rec_buffer = new HashMao();// using go back n, not needed
	public ServerTTPService(short port) throws SocketException{
		//udpService = new DatagramService(port, 10);
		request_queue = new ConcurrentLinkedQueue<Datagram>();
		data_queue = new ConcurrentLinkedQueue<Datagram>();
		
	}
	public ServerTTPService(short srcport, short dstport, String dstaddr) throws SocketException{
		//udpService = new DatagramService(port, 10);
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
	 
	public void startCon(String srcIp, String dstIp, 
		short srcPort, short dstPort){
		datagram = new Datagram(srcIp, dstIp, srcPort, dstPort);
		
	}
	*/
	
	/*
	 * server open port to queue incoming requests , race condition?
	 */
	public void serverListen(int port){
		try{
			listenService = new DatagramService(port, 10);
			System.out.println("server listening on port  "+port );
			while(true){
				
				datagram = listenService.receiveDatagram();//a new thread to handle? incoming queue?
				if( ( (TTP) datagram.getData()).isSYN() ){
				System.out.println("add to SYN queue: received SYN  from ip "+ datagram.getSrcaddr() + ":" + datagram.getSrcport() + " Data: " + datagram.getData());
			    request_queue.add(datagram);
				}
				else {
					
					System.out.println("add to data queue: received data from ip "+ datagram.getSrcaddr() + ":" + datagram.getSrcport() + " Data: " + datagram.getData());
				    data_queue.add(datagram);
				}
					
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		finally{
			listenService.close();
		}
		
	}
	/*
	 * client initialize a connection
	 */
	public void clientCon(int clientport, int ACK,  Object data, short length){
		TTP ttpSYN = new TTP(ACK, 1, data, length);// a SYN packet
		short size = 0;//size of datagram , to do
		short checksum = ttpSYN.getCheckSum();
		try {
			clientService = new DatagramService(clientport, 10);
			/*
			 * Datagram(String srcaddr, String dstaddr, short srcport,
			short dstport, short size, short checksum, Object data)
			 */
			
			Datagram datagram = new Datagram(srcaddr,dstaddr,srcport,dstport,size,checksum,ttpSYN);
			
			clientService.sendDatagram(datagram);
			
			datagram = clientService.receiveDatagram();//a new thread to handle? incoming queue?
			if( ( (TTP) datagram.getData()).isACK() ){
			System.out.println("3-way finished: received ACK from ip "+ datagram.getSrcaddr() + ":" 
					+ datagram.getSrcport() + " Data: " + datagram.getData());
		    
			}
			else{
				System.out.println("clientcon: received invalid packet from ip "+ datagram.getSrcaddr() + ":" 
						+ datagram.getSrcport());	
			}
			
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
			clientService.close();
		}
	}
	
	
	/*
	 * server side open a socket to accept clients' connections
	 * it should handle multiple coming request, queue them, has a limit of queue size
	 * which limit the number of connection queued , after which the socket refuse to queue, when this happens
	 * tcp ignore coming requests, queued request are made available when calling accept
	 * In other words, accept returns a new socket through which the server can communicate with the newly connected client. The old socket (on which accept was called)
	 *  stays open, on the same port, listening for new connections
	 * to do: queue?? avoid race// add lock new thread to do this?
	 */
	public Datagram serverAccept() throws IOException, ClassNotFoundException{
		    if(request_queue.isEmpty())
		    	return null;
	        Datagram datagram = request_queue.peek();//should not be removed before success
	        
	        // assume all in order, to change for error handling
			
			
			TTP ttp =(TTP) datagram.getData();
			hisSYN = ttp.getSYN();//error handling , to do
			hisACK += hisSYN +1 ; // next expected syn is ack
			mySYN = 0;//random syn , to do
			Datagram ack = constructACK(hisACK, mySYN);// return SYN + ACK, to do
			listenService.sendDatagram(ack);// send SYN+ACK
			datagram = listenService.receiveDatagram();// ideal no error handling, time out, what if droped, to do
			System.out.println("received ACK request from ip "+ datagram.getSrcaddr() + ":" + datagram.getSrcport() + " Data: " + datagram.getData());
			ttp =(TTP) datagram.getData();
			// open a new socket to processTTP and release the listen socket for other requests
			myACK = ttp.getACK();
			request_queue.remove(); // can remove head from queue now
			return datagram;
		//}
	}
 /*
  * to do
  * read received ttp payload, pass data to upper layer, update ACK and SYN
  */
	private Object readTTP(TTP ttp) {
		// TODO Auto-generated method stub  update ack and syn
		hisSYN = ttp.getSYN();
		hisACK = hisSYN + ttp.getLength()+1;
		mySYN = ttp.getACK();
		
		return ttp.getData();
	}

	/*
	 * close datagram services
	 */
	public void closeService(){
		listenService.close();
		dataService.close();
		System.out.println("ttp service closed");
	}
	/*
	 * to be changed
	 * construct response ack
	 */
	private Datagram constructACK(int ACK, int SYN) {
		Datagram ack = new Datagram();
		ack.setSrcaddr(datagram.getDstaddr());
		ack.setSrcport(datagram.getDstport());
		ack.setDstaddr(datagram.getSrcaddr());
		ack.setDstport(datagram.getSrcport());
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
	private Datagram constructPacket(TTP ttp, String srcaddr, Short srcport, String destaddr, Short destport ){
		Datagram gram = new Datagram();
		gram.setData(ttp);
		gram.setSrcaddr(srcaddr);
		gram.setDstaddr(destaddr);
		gram.setDstport(destport);
		gram.setSrcport(srcport);
		return gram;
		
	}

	/*
	 * send data over a datagram service
	 */
	public void sendData(int ACK, int SYN, Object data, short dataLength) throws IOException{
		TTP ttp = new TTP(ACK,SYN, data, dataLength);
		datagram.setData(ttp);
		dataService.sendDatagram(datagram);
	}
	/*
	 * receive data
	 * if in order, remove corresponding data from sending_buffer, to do 
	 */
	public void recData(TTP ttp) {
		readTTP(ttp);
	}
	
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
