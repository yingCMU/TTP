package ttp;

import java.io.IOException;

import datatypes.Datagram;

import services.DatagramService;


public abstract class TTPservice {
	static DatagramService dataService;
	 Datagram datagram;
	 String srcaddr;
	String dstaddr;
	 short srcport;
	 short dstport;
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
		 Datagram constructPacket(TTP ttp ){
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
