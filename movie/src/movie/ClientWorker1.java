/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movie;

/**
 *
 * @author zhongjiezheng
 */
import java.io.*;
import java.net.*;

class ClientWorker1 implements Runnable 
{
   private Socket client;
   
   ClientWorker1(Socket client) 
   {
      this.client = client;
   }

   public void run()
   {
      String line;
      BufferedReader in = null;
      PrintWriter out = null;
      try 
      {
	 in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	 out = new PrintWriter(client.getOutputStream(), true);
      } 
      catch (IOException e) 
      {
	 System.out.println("in or out failed");
	 System.exit(-1);
      }

      try 
      {
	 // Receive text from client
	 line = in.readLine();
	 
	 // Send response back to client
	 line = "Hello " + line;
	 out.println(line);
      } 
      catch (IOException e) 
      {
	 System.out.println("Read failed");
	 System.exit(-1);
      }

      try 
      {
	 client.close();
      } 
      catch (IOException e) 
      {
	 System.out.println("Close failed");
	 System.exit(-1);
      }
   }
}

