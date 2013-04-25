/*
 * A sample server that uses DatagramService
 */
package TTPApplication;

import ttp.ServerTTPService;


public class TTPserver {

	private static ServerTTPService service;
	
	public static void main(String[] args){

		if(args.length != 1) {
			printUsage();
		}
		
		System.out.println("Starting Server ...");
		
		int port = Integer.parseInt(args[0]);
		service = new ServerTTPService("localhost", (short)port);
		while(true){
			service.serverListen();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: server <port>");
		System.exit(-1);
	}
}
