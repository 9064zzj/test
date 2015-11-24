/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movie;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author zhongjiezheng
 */
class SocketThrdServer 
{
   ServerSocket server = null;

   public void listenSocket(int port)
   {
      try
      {
	 server = new ServerSocket(port); 
	 System.out.println("Server running on port " + port + 
	                     "," + " use ctrl-C to end");
      } 
      catch (IOException e) 
      {
	 System.out.println("Error creating socket");
	 System.exit(-1);
      }
      while(true)
      {
         ClientWorker1 w;
         try
         {
            w = new ClientWorker1(server.accept());
            Thread t = new Thread(w);
            t.start();
	 } 
	 catch (IOException e) 
	 {
	    System.out.println("Accept failed");
	    System.exit(-1);
         }
      }
   }

   protected void finalize()
   {
      try
      {
         server.close();
      } 
      catch (IOException e) 
      {
         System.out.println("Could not close socket");
         System.exit(-1);
      }
   }

   public static void main(String[] args)
   {
      if (args.length != 1)
      {
         System.out.println("Usage: java SocketThrdServer port");
	 System.exit(1);
      }

      SocketThrdServer server = new SocketThrdServer();
      int port = Integer.valueOf(args[0]);
      server.listenSocket(port);
   }
}

