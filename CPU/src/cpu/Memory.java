/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;
import java.io.*;
import java.util.*;
import java.lang.Runtime;
/**
 *
 * @author zhongjiezheng
 */
public class Memory {
    public static final int OPERATION_READ  = 0;
   public static final int OPERATION_WRITE = 1;
   public static final int SUCCESS  = 1;
   public static final int FAIL     = -1;

   private static final int MAX_SIZE    = 2000;
   private static final int PROGRAM_MAX = 999;

   private int[] memory = new int[MAX_SIZE];

   /** Write data memory */
   public void write(int address, int data) throws Exception
   {
      if ((address >= 0) && (address <= PROGRAM_MAX))
      {
         memory[address] = data;
      }
      else
      {
         throw new Exception("Access violation");
      }
      
   }//End void write(int address, int data) throws Exception

   /** Read data from memory */
   public int read(int address) throws Exception
   {
      if ((address >= 0) && (address <= PROGRAM_MAX))
      {
         return memory[address];
      }
      else
      {
         throw new Exception("Access violation");
      }
      
   }//End int read(int address) throws Exception

   /** Fill in the memory with content of input files */
   public boolean initialize() 
   {
      try 
      {
         int index = 0;
         Scanner scanner = new Scanner(new java.io.File("sample1.txt"));
         while(scanner.hasNext())
         {
            String line = scanner.nextLine();
            String [] tokens = line.split(" ");
            write(index++, Integer.parseInt(tokens[0]));
         }
         // we're done with the input file
         scanner.close();
         return true;
      }
      catch (Exception e) 
      {
         e.printStackTrace();
         return false;
      }
      
   }//End boolean initialize()

   public static void main(String [] args)throws Exception
   {
      Memory memory = new Memory();
      // populate memory with content of the file
      memory.initialize();

      String cpuCommand = null;
      Scanner sc = new Scanner(System.in);

      // we run forever until parent terminates us
      while(true)
      {
         if(sc.hasNext())
         {
            cpuCommand = sc.nextLine();
            String [] tokens = cpuCommand.split("[ ]+");

            int operation = Integer.parseInt(tokens[0]);
            int address = Integer.parseInt(tokens[1]);
            int writeData = 0;
            if (tokens.length > 2) 
            {
               writeData = Integer.parseInt(tokens[2]);
            }

            String result = null;

            switch(operation)
            {
               case OPERATION_READ:
                  try {
                     int data = memory.read(address);
                     result = SUCCESS + " " + data;
                  }
                  catch (Exception e)
                  {
                     // this must be access violation
                     result = FAIL + "";
                  }
                  // send result back to parent process
                  System.out.println(result);
                  System.out.flush();
                  break;

               case OPERATION_WRITE:
                  try {
                     memory.write(address, writeData);
                     result = SUCCESS + "";
                  }
                  catch (Exception e)
                  {
                     // this must be access violation
                     result = FAIL+ "";
                  }
                  // send result back to parent process
                  System.out.println(result);
                  System.out.flush();
                  break;
                  
            }//End switch
            
         }//End if(sc.hasNext())
         
      }//End while
      
   }//End main
}
