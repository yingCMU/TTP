/*
 * A sample client that uses DatagramService
 */

package TTPApplication;

import java.io.IOException;
import ttp.ClientTTPService;
import ttp.TTP;
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
		ttps = new ClientTTPService("localhost", (short) srcport);
		
		ttps.connect("localhost", (short)dstport);
		ttps.receiveData();
		
	}
	
	private static void printUsage() {
		System.out.println("Usage: server <localport> <serverport>\n");
		System.exit(-1);
	}
}
