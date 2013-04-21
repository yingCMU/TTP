/*
 * A sample client that uses DatagramService
 */

package TTPApplication;

import java.io.IOException;
import java.net.SocketException;

import services.DatagramService;
import ttp.ClientTTPService;
import ttp.TTP;
import ttp.ServerTTPService;
import datatypes.Datagram;

public class TTPclient {

	//private static DatagramService ds;
	private static ClientTTPService ttps;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if(args.length != 2) {
			printUsage();
		}
		
		System.out.println("Starting client ...");
		
		int srcport = Integer.parseInt(args[0]);
		
		//ds = new DatagramService(srcport, 10);
		
		Datagram datagram = new Datagram();
		TTP ttp = new TTP();
		String data = "hello world" ;
		ttp.setData(data);
		ttp.setLength((short) data.length());
		datagram.setData(ttp);
		datagram.setSrcaddr("localhost");
		datagram.setDstaddr("localhost");
		datagram.setDstport((short)Integer.parseInt(args[1]));
		datagram.setSrcport((short)srcport);
		
		 /* clientCon(int clientport, int ACK, int SYN, Object data, short length)
		 */
		int dstport = (short)Integer.parseInt(args[1]);
		
		/*
		 * TTPService(short srcport, short dstport, String dstaddr)
		 */
		System.out.println("ttp service dstport "+dstport);
		ttps = new ClientTTPService((short) srcport,(short)dstport, "localhost");
		ttps.clientCon(srcport, 1, ttp,ttp.getLength());
		System.out.println("Sent SYN");
		ttps.closeService();
		//datagram = ds.receiveDatagram();
		//System.out.println("Received " + datagram.getData());
		/*ds.sendDatagram(datagram);
		System.out.println("Sent datagram");
		
		datagram = ds.receiveDatagram();
		System.out.println("Received " + datagram.getData());
		*/
	}
	
	private static void printUsage() {
		System.out.println("Usage: server <localport> <serverport>\n");
		System.exit(-1);
	}
}
