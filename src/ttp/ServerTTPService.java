package ttp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedQueue;

import datatypes.Datagram;

import services.DatagramService;

public class ServerTTPService extends TTPservice implements Runnable{
	private static DatagramService listenService;
	
	private static DatagramService clientService;
	private final int LISTENER_MAX = 10; 
	private int con_descriptor = 1;
	int timer;//time out
	private int data_length;
	private Datagram datagram;
	private int hisSYN;// current syn
	private int hisACK;// ack I should reply to him now
	private ConcurrentLinkedQueue<Datagram> request_queue;
	private ConcurrentLinkedQueue<Datagram> data_queue;
	private Hashtable<String, ConDescriptor> clientList;//record for active connections
	private int mySYN;
	private int myACK;// my coming ack is everything is good
	private char ID;
	private int maximum_buffer = 10 ,MSS;
	private HashMap sending_buffer = new HashMap();
	
	private short win;
	//private HashMap rec_buffer = new HashMao();// using go back n, not needed
	public ServerTTPService(short port) throws SocketException{
		
		this.srcport = port;
		listenService = new DatagramService(srcport, 10);
		try
        {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
           // System.out.println(addr.getHostAddress());
            //System.out.println(hostname);
            
    		this.srcaddr = addr.getHostAddress();
        }catch(UnknownHostException e)
        {
             e.printStackTrace();
        }
		clientList = new Hashtable<String, ConDescriptor>();
		request_queue = new ConcurrentLinkedQueue<Datagram>();
		data_queue = new ConcurrentLinkedQueue<Datagram>();
		
	}
	
	public ServerTTPService(short srcport, short dstport, String dstaddr) throws SocketException{
		//udpService = new DatagramService(port, 10);
		this.dstaddr = dstaddr;
		this.dstport = dstport;
		
		listenService = new DatagramService(srcport, 10);
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
		clientList = new Hashtable<String, ConDescriptor>();
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
	public ConDescriptor serverListen(){
		try{
			
			
			while(true){
				System.out.println("server listening on port  "+srcport );
				datagram = listenService.receiveDatagram();//a new thread to handle? incoming queue?
				if( ( (TTP) datagram.getData()).isSYN() ){
				System.out.println("add to SYN queue: received SYN  from ip "+ datagram.getSrcaddr() + ":" + datagram.getSrcport() + " Data: " + datagram.getData());
				
				request_queue.add(datagram);
				
				System.out.println("request queue size "+request_queue.size());
				ConDescriptor dp = serverAccept();
			    return dp;// return a connection descriptor , to do
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
			//listenService.close();
		}
		return null;
		
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
	public ConDescriptor serverAccept() throws IOException, ClassNotFoundException{
		System.out.println("server accepting........");
		    while(true){
		    	if (request_queue.isEmpty())
		    		continue;
	        Datagram datagram = request_queue.peek();//should not be removed before success
	        System.out.println("request_queue.peek  ...");
	        // assume all in order, to change for error handling
			
			
			TTP ttp =(TTP) datagram.getData();
			hisSYN = ttp.getSYN();//error handling , to do
			hisACK += hisSYN +1 ; // next expected syn is ack
			mySYN = 0;//random syn , to do
			TTP synack = new TTP(1, 1,"syn+ack" , (short)"syn+ack".length());// a SYN packet
			short size = 0;//size of datagram , to do
			short checksum = synack.getCheckSum();
			
				
				 datagram = new Datagram(srcaddr,datagram.getSrcaddr(),srcport,datagram.getSrcport(),size,checksum,synack);
			
			//Datagram ack = constructACK(hisACK, mySYN);// return SYN + ACK, to do
				// System.out.println("srcaddr :"+datagram.getSrcaddr()+" dstaddr "+datagram.getDstaddr() + "srcport "+datagram.getSrcport()+ "dstport "+datagram.getDstport());
			listenService.sendDatagram(datagram);// send SYN+ACK
			datagram = listenService.receiveDatagram();// ideal no error handling, time out, what if droped, to do
			System.out.println("received ACK request from ip "+ datagram.getSrcaddr() + ":" + datagram.getSrcport() + " Data: " + datagram.getData());
			ttp =(TTP) datagram.getData();
			// open a new socket to processTTP and release the listen socket for other requests
			myACK = ttp.getACK();
			request_queue.remove(); // can remove head from queue now
			System.out.println("3-way finished");
			System.out.println("request queue size "+request_queue.size());
			ConDescriptor oneClient= new ConDescriptor(srcaddr, datagram.getSrcaddr(), srcport, datagram.getSrcport(),1,0);
			clientList.put(oneClient.getKey(), oneClient);
			return oneClient;
		}
	}


	/*
	 * close datagram services
	 */
	public void closeService(){
		listenService.close();
		dataService.close();
		System.out.println("ttp server service closed");
	}

	
	/*
	 * send data over a datagram service
	 */
	public void sendData(int ACK, ConDescriptor dp, Object data, short dataLength) throws IOException{
		TTP ttp = new TTP(ACK, dp.getClientSYN(), data, dataLength);
		//datagram.setData(ttp);
		//TTP ttp = new TTP(serverACK,clientSYN, data, dataLength);
		 datagram = constructPacket(ttp);
		 System.out.println("server sending data :\nsrcaddr :"+datagram.getSrcaddr()+" dstaddr "+datagram.getDstaddr() + "srcport "+datagram.getSrcport()+ "dstport "+datagram.getDstport());
		Timer sendWithTimer = new Timer(20000, datagram, listenService);
		dp.setTimer(sendWithTimer);
		sendWithTimer.run();//no dest port addr
		
		
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
