package FTP;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TransferfileClient
{
 Socket ClientSoc;

 DataInputStream din;
 DataOutputStream dout;
 BufferedReader stdinBR;
 TransferfileClient(Socket soc)
 {
     try
     {
         ClientSoc=soc;
         din=new DataInputStream(ClientSoc.getInputStream());
         dout=new DataOutputStream(ClientSoc.getOutputStream());
         stdinBR=new BufferedReader(new InputStreamReader(System.in));
     }
     catch(Exception ex)
     {
     }        
 }
 void postFile(String filename) throws Exception
 {        
     
     
         
     File f=new File(filename);
     System.out.println("client posting file :"+filename);
     if(!f.exists())
     {
         System.out.println("File not Exists...");
         dout.writeUTF("File not found");
         return;
     }
     
    
     
     String msgFromServer=din.readUTF();
     if(msgFromServer.compareTo("File Already Exists")==0)
     {
         String Option;
         System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
         Option=stdinBR.readLine();            
         if(Option.equals("Y") || Option.equals("y"))    
         {
             dout.writeUTF("Y");
         }
         else 
         {
             dout.writeUTF("N");
             return;
         }
     }
     
     System.out.println("Sending File ...");
     FileInputStream fin=new FileInputStream(f);
     int ch;
     do
     {
         ch=fin.read();
         System.out.println(">> ch :"+ch);
         dout.writeUTF(String.valueOf(ch));
     }
     while(ch!=-1);
     fin.close();
     System.out.println(din.readUTF());
     
 }
 
 void getFile(String fileName) throws Exception
 {
	 System.out.println("in client getFIle "+fileName);
     //dout.writeUTF(fileName);
	 
     String msgFromServer=din.readUTF();
     System.out.println("msgFromServer "+msgFromServer);
     if(msgFromServer.compareTo("File Not Found")==0)
     {
         System.out.println("File not found on Server ...");
         return;
     }
     else  if(msgFromServer.compareTo("READY")==0)
     {
         System.out.println("Receiving File ...");
         
         File f=new File(fileName);
         FileOutputStream fout=new FileOutputStream(f);
         int ch;
         String temp;
         do
         {
             temp=din.readUTF();
             ch=Integer.parseInt(temp);
             if(ch!=-1)
             {
                 fout.write(ch);                    
             }
         }while(ch!=-1);
         fout.close();
         System.out.println(din.readUTF());
             
     }
     
     
 }

 public void displayMenu() throws Exception {
	 printCommands();
     while(true)
     {    
    	 
         System.out.print("740ftp>");
         String input = stdinBR.readLine();
         String[] command = input.split(" ");
         //System.out.println(command[0]);
         //System.out.println(command[1]);
         if(command[0].equals("quit"))
         {
        	 
        	 dout.writeUTF("quit");
        	 ClientSoc.close();
             System.exit(1);
             
         }
         else if(command[0].equals("get"))  {
        	 
        	 File f=new File(command[1]);
        	 if(f.exists())
             {
                 String Option = null;
                 System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
                 Option=stdinBR.readLine();            
                 if(Option.equals("N") || Option.equals("n"))    
                 {
                     dout.flush();
                     continue;    
                 }
                 else if (!Option.equals("Y") && !(Option.equals("y"))){
                	 System.out.println("invalid choice");
                	 dout.flush();
                	 continue;
                 }
             }
        	 
        	 
             dout.writeUTF(input);
             getFile(command[1]);
         }
         else if(command[0].equals("post"))
         {
        	 dout.writeUTF(input);
             postFile(command[1]);
         }
         else {
        	 System.out.println("\n  !!! invalid commands \n  ");
        	 printCommands();
         }
         	
     }
 }
 public void printCommands(){
	 System.out.println("******* commands ******");
     System.out.println("1. Send File: post");
     System.out.println("2. Receive File: get");
     System.out.println("3. Exit: quit");
     System.out.println("\nEnter Choice :");
     System.out.println("**********************");
 }
}
