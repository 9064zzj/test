/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movie;

/**
 *
 * @author zhongjiezheng
 */
///This is the 
import java.io.*;
import java.util.Scanner;
import java.net.*;
import java.util.concurrent.Semaphore;

public class MovieClient
{
    
   public Socket socket = null;
   PrintWriter out = null;
   BufferedReader in = null;
   
   public void communicate()
   {  
      Scanner sc = new Scanner(System.in);
      System.out.println("A. Display the list of movies\nB. Purchase tickets\nC. Exit\n\n Enter choice:");
      String choice = sc.nextLine();

      //Send data over socket
      out.println(choice);

      //Receive text from server
      try
      {
         String line = in.readLine();
	 
	 if( !line.equals("error") ){
	 	
         	System.out.println( line );
	 }
	 else{
	 	System.out.println("Invalid input.");
	 }
	 line = in.readLine();
	 System.out.println( line );
      } 
      catch (IOException e)
      {
         System.out.println("Read failed");
         System.exit(1);
      }
   }
  
   public void listenSocket(String host, int port)
   {
      //Create socket connection
      try
      {
	 socket = new Socket(host, port);
	 out = new PrintWriter(socket.getOutputStream(), true);
	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } 
      catch (UnknownHostException e) 
      {
	 System.out.println("Unknown host");
	 System.exit(1);
      } 
      catch (IOException e) 
      {
	 System.out.println("No I/O");
	 System.exit(1);
      }
   }

   public static void main(String[] args)
   {
      if (args.length != 2)
      {
         System.out.println("Usage:  client hostname port");
	 System.exit(1);
      }

      MovieClient client = new MovieClient();
      String host = args[0];
      int port = Integer.valueOf(args[1]);
      client.listenSocket(host, port);
      client.communicate();
   }
}

