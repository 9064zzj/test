/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thread2;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.*;
/**
 *
 * @author zhongjiezheng
 */
public class Thread2 {
     //   Other variables declaration
    private static int[] MovieSeat=new int[5];
    private static final int TOTAL_CUSTOMER=300;
    
    private static String MovieName[] = new String[5];
    
    //   Semaphore declaration
//    private static Semaphore mutex1= new Semaphore(1);
    private static Semaphore mutex2= new Semaphore(1);
    private static Semaphore mutex3= new Semaphore(1);
    private static Semaphore mutex4= new Semaphore(1);
    
    private static Semaphore box= new Semaphore(0);
    private static Semaphore ticket_check= new Semaphore(0); 
    private static Semaphore ticket_check_done = new Semaphore(0);
    private static Semaphore want_to_buy_snack = new Semaphore(0);
    private static Semaphore ready_buy_snack= new Semaphore(0);
    private static Semaphore buy_snacks = new Semaphore(0);
    private static Semaphore snack_ready = new Semaphore(0);
    
    private static Semaphore Movie0= new Semaphore(1);
    private static Semaphore Movie1= new Semaphore(1);
    private static Semaphore Movie2= new Semaphore(1);
    private static Semaphore Movie3= new Semaphore(1);
    private static Semaphore Movie4= new Semaphore(1);
   
    
    //   Quene are here
    private static LinkedList<Customer> BoxLine = new LinkedList();
    private static LinkedList<Customer> TicketLine = new LinkedList();
    private static LinkedList<Customer> SnackLine = new LinkedList();
    private static Customer arrayCustomer [] = new Customer[TOTAL_CUSTOMER];
    
    //   Thread 
    
    
    //   Costumer
    private static class Customer extends Thread{
        private String movie=new String();
        private String Snack=new String();
        private int CustomerNumber;
        public int SnackType;
        Random Rand = new Random();
        public int MovieNumber;
        public boolean Sold_out;
        
        
        private static Semaphore BO_ready= new Semaphore(0);
        private static Semaphore Decision_ready= new Semaphore(0);
        private static Semaphore Availability= new Semaphore(0);
        private static Semaphore Ticket_ready= new Semaphore(0);
//        private static Semaphore ChosenMovie;
        //Constructor
        public Customer(String s, int num)
      {
    		super(s);
         CustomerNumber = num;
   //      System.out.println(getName() + " created");
         
    	}
        
        // The method & function used out thread
        public int getCustomerNumber()
      {
         return CustomerNumber;
      }
        
        public void BO_Ready_for()
      {
            BO_ready.release();
        }
        
        public void Decision_ready()
      {
            try{
                Decision_ready.acquire();
            }
            catch(InterruptedException ex){}
        }
        
        public void Availability()
      {
            Availability.release();
        }        
        
        public void Ticket_ready()
      {
            Ticket_ready.release();
        }
        //The method & function used in thread
        public void ChooseMovie()
      {
            
           MovieNumber=Rand.nextInt(5);
            
           switch(MovieNumber){
               case 0: 
                       movie=MovieName[0];
                       break;
               case 1:
                       movie=MovieName[1];
                       break;
               case 2: 
                       movie=MovieName[2];
                       break;
               case 3:
                       movie=MovieName[3];
                       break;
               case 4: 
                       movie=MovieName[4];
                       break;
           }
                   
        }
        
        public boolean BuySnack()
      {
            int temp=Rand.nextInt(2);
            if(temp==1)
                return true;
            else
                return false;
        }
        
        public void ChooseSnack()
      {
            SnackType=Rand.nextInt(3);
            switch(SnackType){
                case 0:
                       Snack = "Popcorn";
                       break;
                case 1:
                       Snack= "Soda";
                       break;
                case 2:
                       Snack= "Popcorn and Soda";
                       break;
            }
        }
        
        @Override
        public void run(){
            try{
                //Wait Mutual exculsion to get in line
                mutex2.acquire();
                //Enquene customer, Let costumer in that line
                BoxLine.offer(this);
                //Signal box i'm ready to meet BO
                box.release();
                //Signal mutex2;
                mutex2.release();
                //wait for BO response
                BO_ready.acquire();
                //Then choose the movie I want
                ChooseMovie();
                System.out.println("Customer"+CustomerNumber+" buying "+movie);
                //signal BO choose done, I'm ready to buy
                Decision_ready.release();
                //wait BO finish checking availablity
                Availability.acquire();
                //Now to see the if the moive is available
                if(Sold_out){
                    //Accept the fact,take the semaphore
                    Ticket_ready.acquire();
                    System.out.println("Customer"+CustomerNumber+" leave with disappointment");
                }
                else{
                    //Get the ticket, BO finish his job here
                    Ticket_ready.acquire();
                    //Go to line for ticket taker, use mutex3 to wait line
                    mutex3.acquire();
                    //Enter the quene, and print
                    TicketLine.offer(this);
                    System.out.println("Customer"+CustomerNumber+" in line to see ticket taker");
                    //signal ticket check I'm ready
                    ticket_check.release();
                    //Quit the mutual exclusion
                    mutex3.release();
                    //Wait for ticket check, and then ticket taker finish his job
                    ticket_check_done.acquire();
                    //Now decide if to buy some snack
                    if(BuySnack()){
                        //choose one snack
                        ChooseSnack();
                        System.out.println("Customer"+CustomerNumber+" in line to buy"+Snack);
                        //mutex to enter the line buy snack
                        mutex4.acquire();
                        //get in the line to buy snack
                        SnackLine.offer(this);
                        //Signal the seller
                        want_to_buy_snack.release();
                        //Quit the mutex4
                        mutex4.release();
                        //wait the signal from seller
                        ready_buy_snack.acquire();
                        //signal I'm ready to buy
                        buy_snacks.release();
                        //wait order ready
                        snack_ready.acquire();
                        System.out.println(getName()+getCustomerNumber()+" receives "+Snack);
                    }
                    System.out.println("Customer"+CustomerNumber+" enter theater to see "+movie);
                }//End if movie sold out
                
            }
            catch(InterruptedException ex){}
        }//End run
    }//End Customer
    
    
    private static class BoxOffice extends Thread{
        Customer customer;
        String movieName=new String();
        Semaphore movie;
        
       // this.setDaemon(true);
        public BoxOffice(String s)
        {
            super(s);
            System.out.println(s+" created");
        }
        public void getMovie(){
            switch(customer.MovieNumber)
            {
                case 0:movie = Movie0;
                       movieName=MovieName[0];
                       break;
                case 1:movie = Movie1;
                       movieName=MovieName[1];
                       break;
                case 2:movie = Movie2;   
                       movieName=MovieName[2];
                       break;
                case 3:movie = Movie3;   
                       movieName=MovieName[3];
                       break;
                case 4:movie = Movie4;   
                       movieName=MovieName[4];
                       break;
            }
              
        }
        
        public boolean MovieSoldOut()
        {
            switch(customer.MovieNumber)
            {
                case 0: if(MovieSeat[0]==0)
                          return true;
                        else{
                           MovieSeat[0]--;
                           return false;
                        }
  
                case 1:if(MovieSeat[1]==0)
                          return true;
                        else{
                           MovieSeat[1]--;
                           return false;
                        }
                    
                case 2:if(MovieSeat[2]==0)
                          return true;
                        else{
                           MovieSeat[2]--;
                           return false;
                        }
                    
                case 3:if(MovieSeat[3]==0)
                          return true;
                        else{
                           MovieSeat[3]--;
                           return false;
                        }
                    
                case 4:if(MovieSeat[4]==0)
                          return true;
                        else{
                           MovieSeat[4]--;
                           return false;
                        }
                default:return true;
            }
        }
        
        @Override
        public void run()
        {
   //         setDaemon(true);
            while(true)
            {
                try
                {
                    //wait for customer ready
                    box.acquire();
                    //wait mutex to enter depueue mutex
                    mutex2.acquire();
                    //dequeue from the boxline
                    customer=BoxLine.removeFirst();
                    System.out.println(this.getName()+" serving "+customer.getName()+customer.getCustomerNumber());
                    //call customer I'm ready
                    customer.BO_Ready_for();
                    //quit the mutual exclusion
                    mutex2.release();
                    
                    //wait for customer make decision
                    customer.Decision_ready();
                    //See which movie customer choose
                    getMovie();
                    //Enter movie mutex
                    movie.acquire();
                    //Count moive seat and told customer if it's sold out
                    if(MovieSoldOut())   //sold out
                        customer.Sold_out=true;
                    else{//Still available
                        customer.Sold_out=false;
                        //Print sold
                        System.out.println(this.getName()+" sold ticket for "+movieName+" to "+customer.getName()+customer.getCustomerNumber());
                    }
                    //quit movie mutex
                    movie.release();
                    //sleep 1.5s to "sell ticket"
                    sleep(1500);
                    //told customer could see if it's sold out
                    customer.Availability();
                    //ticket ready(No matter customer buy or not buy ticket)
                    customer.Ticket_ready();
                }catch(InterruptedException ex){};
            }//End while
            
        }//End Run
    }//End BO
     
    
    private static class Ticket_taker extends Thread{
        Customer customer;
        
        public Ticket_taker(String s)
        {
            super(s);
        }
        
        @Override
        public void run(){
    //        setDaemon(true);
            while(true){
  
                try{
                    //wait for ticket check(someone want to check)
                    ticket_check.acquire();
                    //Enter the mutex to dequenue customer
                    mutex3.acquire();
                    //dequene customer
                    customer=TicketLine.remove();
                    //Exit mutex
                    mutex3.release();
                    //take and tear ticket
                    sleep(250);
                    System.out.println("Ticket taken from "+customer.getName()+customer.getCustomerNumber());
                    //Tell customer check done
                    ticket_check_done.release();
                    
                }catch(InterruptedException ex){};
                
            }//End while
        }//End run
        
    }
    
    private static class Concession_shop extends Thread{
        Customer customer;
        String SnackType=new String();
        
        public Concession_shop(String s){
            super(s);
        }
        
        public void TakeOrder(int x)
        {
            switch(x){
                case 0: SnackType="Popcorn";
                        break;
                case 1: SnackType="Soda";
                        break;
                case 2: SnackType="Popcorn and Soda";
                        break;
            } 
        }
        
        
        @Override
        public void run(){
  //          setDaemon(true);
            while(true){
                try{
                    //wait for anyone want buy
                    want_to_buy_snack.acquire();
                    //enter mutex4 to dequeue customer
                    mutex4.acquire();
                    //dequeue costumer
                    customer=SnackLine.remove();
                    //exit mutex4
                    mutex4.release();
                    //tell costumer I'm ready
                    ready_buy_snack.release();
                    //wait customer give order
                    buy_snacks.acquire();
                    //See customer order;
                    TakeOrder(customer.SnackType);
                    System.out.println("Order for "+SnackType+" taken from "+customer.getName()+customer.getCustomerNumber());
                    //Prepare order
                    sleep(3000);
                    //Order ready
                    System.out.println(SnackType+" given to "+customer.getName()+customer.getCustomerNumber());
                    //Signal customer order ready
                    snack_ready.release();
                    
                }catch(InterruptedException ex){};
            }//End while
        }//End run
    }//End class
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Read file
        
        String Path,trigger;
        System.out.println("Please input the file path and name");
        Scanner input =new Scanner(System.in);
        Path=input.nextLine();
        try {
          
          FileInputStream fstream = new FileInputStream(Path);     //Change the file addr. here
          BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
          String temp= null; 
          String arrs[];
          int i=0;
          while((temp = br.readLine()) != null){
              arrs=temp.split("\t");
              MovieName[i]=arrs[0];
              MovieSeat[i]=Integer.parseInt(arrs[1]);
              i++;
          }
          System.out.println("MoiveName"+"\t"+"Available Seat");
          
          for(int j=0;j<i;j++){
               System.out.println(MovieName[j]+"\t"+MovieSeat[j]);
           }
          
        }catch (IOException e) {
            System.out.println("error"+e.getMessage());
            System.exit(0);
        }
           
           
        System.out.println("Press any key to begin!");
        trigger=input.next();
        
        
        
        //Initial
        Concession_shop shop = new Concession_shop("Concession shop seller");
        Ticket_taker ticket_taker = new Ticket_taker("Ticket taker");
        BoxOffice BO1 = new BoxOffice("Box Office agent 0");
        BoxOffice BO2 = new BoxOffice("Box Office agent 1");
        BoxOffice BO3 = new BoxOffice("Box Office agent 2");
        shop.start();
        ticket_taker.start();
        BO1.start();
        BO2.start();
        BO3.start();
        
        for(int i=0;i<TOTAL_CUSTOMER;i++){
            Customer customer = new Customer("Customer",i);
            arrayCustomer[i]=customer;
            customer.start();
        }
        for(int i = 0; i < TOTAL_CUSTOMER; i++){
	     try
		{
		  arrayCustomer[i].join();
                  System.out.println("Joined Customer"+arrayCustomer[i].CustomerNumber);
				
		}catch(InterruptedException ex) { }
	}//End for
        System.exit(0);
    }
}
