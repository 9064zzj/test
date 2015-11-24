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
import java.util.*;
import java.util.concurrent.Semaphore;
class MovieServer{

class ClientWorker implements Runnable 
{
   private Socket client;
   
   ClientWorker(Socket client) 
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
      	 
	 // Receive choice from client
	 String choice = in.readLine();
	 
	 switch( choice ){
	 	case "A":
		case "a":
		System.out.println("Client");//
		line = "[a movie list will be added here]";//line = movie_names[0] + movie_names[1];
		break;


		case "B":
		case "b":
                String moviechoose=in.readLine();
                String NumTicket=in.readLine();
                
                
                if(CheckTicket(moviechoose,NumTicket)){
                    out.println("true");
                    String confirm=in.readLine();
                    if(TicketConfirm(confirm,NumTicket,moviechoose)){
                        out.println("1");
                    }
                    else
                        out.println("2");
                }
                else
                    out.print("false");

		break;


		case "C":
		case "c":
		line = "end";// This will tell Client thread to end
		System.out.println( "Client exists system" );
		break;
		default:
		line = "Invalid choice, please choose from menu";
	 }
	 // Send response back to client
	 
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

//class MovieServer{
   ServerSocket server = null;
    String movie[] = new String[5];
   int total_ticket[]= new int[5];
   int cust_Num = 0;
   public static Semaphore[] MovieMutex= new Semaphore[5];
   
   
   MovieServer(){
       SemaphoreInitial();
   }
      
   
   public void SemaphoreInitial(){
       for(int i=0;i<MovieMutex.length;i++){
           MovieMutex[i]=new Semaphore(1);
       }
   }
   
   public boolean TicketConfirm(String confirm, String NumTicket,String moviechoose){
       
       int movieNum=Integer.parseInt(moviechoose.trim().substring(0,1))-1;
       int NumTick=Integer.parseInt(NumTicket.trim().substring(0,2));
       int Y_N=Integer.parseInt(confirm.trim().substring(0, 1));
       if(Y_N==1){
            return true;   //do nothing, confirm buy
       }
       else{  //back ticket to pool, invoid buy
           try{
               MovieMutex[movieNum].acquire();
               total_ticket[movieNum]+=NumTick;
               MovieMutex[movieNum].release();
           }
           catch(InterruptedException ex){
               
           }
           return false;
       }
   }
   
   public boolean CheckTicket(String moviechoose, String NumTicket){
       moviechoose=moviechoose.trim();
       NumTicket=NumTicket.trim();
       
       int NumTick=Integer.parseInt(NumTicket.substring(0,2));
       int movieNum=Integer.parseInt(moviechoose.substring(0,1))-1;
       boolean success=false;

       
       try{
           MovieMutex[movieNum].acquire();
           if(total_ticket[movieNum]-NumTick<0)
               success=false;
           else{
               total_ticket[movieNum]=total_ticket[movieNum]-NumTick;
               success=true;
           }
           MovieMutex[movieNum].release();
       }
       catch(InterruptedException ex){
           System.out.println("Error:"+ex.getMessage());
       }
       
       return success;
   }
   
   
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
      	 
         ClientWorker[] w = new ClientWorker[10];// There could be 10 clients at maxium
         Thread t[] = new Thread[10];// There could be 10 clients at maximum
	 try
         {
            w[cust_Num] = new ClientWorker(server.accept());
            t[cust_Num] = new Thread(w[cust_Num]);
            t[cust_Num].start();
	    System.out.println( "Client " + cust_Num + " enters system" );//
	    cust_Num++;

	    //end process
	    try{
	    	for( int i = cust_Num; i < 0; i--){
	    	t[i].join();
	    	System.out.println( "Client " + cust_Num + " exits system" );//
	    }
	    }
	    catch(Exception e){
	    
	    }
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
      	 System.out.println("Server closes");//
         server.close();
      } 
      catch (IOException e) 
      {
         System.out.println("Could not close socket");
         System.exit(-1);
      }
   }

   void readFile( String fileName ){
   	try{
		File file = new File( fileName );
      		Scanner sc = new Scanner( file );
      		int i = 0;

		System.out.println("hello1");//This line shows in console
		//while( sc.hasNext() ){
      	 		String line = sc.nextLine();
      
     			String part[] = line.split("\t");
			System.out.println( movie.length );//This line doesnt show in console
      			movie[1] = part[0];
			System.out.println(part[0]);// This line doesnt show in console
		 	//total_ticket[i] = Integer.valueOf(part[1]);
			System.out.println("her");// This line doesnt show in console
			System.out.println("total ticket: ");//+total_ticket[i]);// This line doesnt show in console
			i++;
      		//}
	}
	catch(Exception e){
	
	}
   } 

   public static void main(String[] args)
   {	
      
      if (args.length != 2)
      {
         System.out.println("Usage: java MovieServer [port] [fileName]");
	 System.exit(1);
      }

      MovieServer server = new MovieServer();
      int port = Integer.valueOf(args[0]);
      
      //below is to read file
      server.readFile( args[1] );

      
	
      server.listenSocket(port);
   }
}
