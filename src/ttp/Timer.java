package ttp;

import java.io.IOException;

import services.DatagramService;
import datatypes.Datagram;

public class Timer extends Thread implements Runnable{
	
	private int time;
	private Datagram data;
	private DatagramService datagramService;
	private int sendCount;
	
	public Timer(int milliseconds, Datagram datagram, DatagramService dataService) {
		time = milliseconds;
		data = datagram;
		datagramService = dataService;
		sendCount = 5;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(sendCount > 0) {
			
			try {
				datagramService.sendDatagram(data);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Time Exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("sending Exception");
			}	
			
			sendCount--;
		}
	}
	
	public synchronized int getSendCount() {
		return sendCount;
	}

}
