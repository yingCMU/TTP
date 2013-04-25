package FTP;

/*FTP Server*/

import java.net.*;


public class FTPServer
{
 public static void main(String args[]) throws Exception
 {
     ServerSocket soc=new ServerSocket(5217);
     System.out.println("FTP Server Started on Port Number 5217");
     while(true){
    	 System.out.println("thread counts >>"+Thread.activeCount());
         System.out.println("Waiting for Connection ...");
         TransferfileServer t=new TransferfileServer(soc.accept());
         
     }
 }
}

