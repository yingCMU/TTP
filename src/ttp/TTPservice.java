package ttp;

import java.io.IOException;

import datatypes.Datagram;

import services.DatagramService;


public abstract class TTPservice {
	static DatagramService dataService;
	 
	 String srcaddr;
	
	 short srcport;
	
	 short MSS = 563;
	 final  short win = 128;// go back n window size
		
		
	
	/*
	  * to do
	  * read received ttp payload, pass data to upper layer, update ACK and SYN
	  */
		 Object readTTP(TTP ttp) {
			// TODO Auto-generated method stub  update ack and syn
			/*hisSYN = ttp.getSYN();
			hisACK = hisSYN + ttp.getLength()+1;
			mySYN = ttp.getACK();
			
			return ttp.getData();*/
			return null;
		}
		 Datagram constructPacket(TTP ttp, String dstaddr, short dstport ){
			short checksum = ttp.getCheckSum();
			short size = 0;// size of datagram, to do 
			/*
			 * Datagram(String srcaddr, String dstaddr, short srcport,
				short dstport, short size, short checksum, Object data)
			 */
			Datagram gram = new Datagram(srcaddr,dstaddr,srcport,dstport,size,checksum,ttp);
			
			return gram;
			
		}
		 
		 
}
