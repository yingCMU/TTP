package FTP;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;


class TransferfileServer extends Thread
{
 Socket clientSocket;
 boolean live = true;
 DataInputStream din;
 DataOutputStream dout;
 
 public TransferfileServer(Socket socket)
 {
     try {
         clientSocket=socket;                        
         din=new DataInputStream(clientSocket.getInputStream());
         dout=new DataOutputStream(clientSocket.getOutputStream());
         System.out.println("FTP Client Connected ...");
         start();
         
         
     }
     catch(Exception ex) {
    	 ex.printStackTrace();
     }        
 }
 void SendFile(String filename) throws Exception {        
    // String filename=din.readUTF();
     File f=new File(filename);
     System.out.println("server sending file :"+filename);
     if(!f.exists()){
         dout.writeUTF("File Not Found");
         return;
     }
     else  {
    	 System.out.println("READY");
         dout.writeUTF("READY");
         FileInputStream fin=new FileInputStream(f);
         int ch;
         do {
        	 
             ch=fin.read();
             System.out.println("ch >> "+ch);
             dout.writeUTF(String.valueOf(ch));
         }
         while(ch!=-1);    
         fin.close();    
         dout.writeUTF("File Receive Successfully");   
         return;
     }
 }
 
 void ReceiveFile(String filename) throws Exception
 {
	 System.out.println("server receive file :" +filename);
     if(filename.compareTo("File not found")==0)     {
         return;
     }
     File f=new File(filename);
     String option;
     
     if(f.exists())     {
         dout.writeUTF("File Already Exists");
         option=din.readUTF();
         System.out.println("option "+option);
     }
     else     {
         dout.writeUTF("SendFile");
         option="Y";
         
     }
         
         if(option.compareTo("Y")==0)
         {
             FileOutputStream fout=new FileOutputStream(f);
             int ch;
             String temp;
             do
             {
                 temp=din.readUTF();
                 System.out.println("temp>>"+temp);
                 if(temp.equals("File not found"))
                	 return;
                 ch=Integer.parseInt(temp);
                 if(ch!=-1)
                 {
                     fout.write(ch);                    
                 }
             }while(ch!=-1);
             fout.close();
             dout.writeUTF("File Send Successfully");
         }
         else
         {
             return;
         }
         
 }


	 public void run() {
	     while(live){
	         try   {
	        	 
	        	 System.out.println("current thread"+Thread.currentThread()+"active>>"+Thread.activeCount());
		         System.out.println("Waiting for Command ...");
		         //String[] command = input.split(" ");
		        // BufferedReader br=new BufferedReader(new InputStreamReader(din));
		         String Commands=din.readUTF();
		         String[] command = Commands.split(" " );
		         System.out.println(command[0]);
		         if(command[0].equals("get"))  {
		             System.out.println("\tget Command Received ...");
		             SendFile(command[1]);
		             continue;
		         }
		         else if(command[0].equals("post")) {
		             System.out.println("\tpost Command Receiced ...");                
		             ReceiveFile(command[1]);
		             continue;
		         }
		         else if(command[0].equals("quit")) {
		             System.out.println("quit Command Received ...");
		             clientSocket.close();
		             
		             //live = false;
		            
		         }
		         else
		        	 System.out.println("invalid command");
	         }
	         catch(SocketException e){
	        	 try {
					clientSocket.close();
					din.close();
					live = false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	 
	         }
	         catch(Exception e) {
	        	 e.printStackTrace();
	         }
	     }
	    }
}