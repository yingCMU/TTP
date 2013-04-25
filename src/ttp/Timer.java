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
		while(sendCount > 0 && !Thread.interrupted()) {
			
			try {
				TTP ttp = (TTP)data.getData();
				int category = (int)ttp.getCategory();
				System.out.println("Sending data Category: " + category);
				datagramService.sendDatagram(data);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Timer Interupted");
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("sending Exception");
			}	
			
			sendCount--;
		}
		
		System.out.println("Thread killed");
		
	}
}
