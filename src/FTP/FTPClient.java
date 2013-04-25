package FTP;
//FTP Client

import java.net.*;
import java.io.*;
import java.util.*;


class FTPClient
{
 public static void main(String args[]) throws Exception
 {
     Socket soc=new Socket("127.0.0.1",5217);
     TransferfileClient t=new TransferfileClient(soc);
     t.displayMenu();
     
 }
}
