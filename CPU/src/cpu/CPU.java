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
public class CPU {

    //Default no Operation
   private static final int OP_NO_OP = 0;
   
   //Load value in the next line
   private static final int OP_LOAD_VALUE = 1;
   
   //Load value in memory at the address into AC
   private static final int OP_LOAD_ADDR = 2;
   
   //Store value from AC into the memory address
   private static final int OP_STORE_ADDR = 3;
   
   //Add value in X into AC
   private static final int OP_ADD_X = 4;
   
   //Add value in Y into AC
   private static final int OP_ADD_Y = 5;
   
   //Substract value of X from AC
   private static final int OP_SUB_X = 6;
   
   //Substract value of Y from AC
   private static final int OP_SUB_Y = 7;
   
   //Get random int from 1 to 100
   private static final int OP_GET = 8;
   
   //Put port (1 = write AC as int, 2 = write AC as char)
   private static final int OP_PUT_PORT = 9;
   
   //Copy value of AC to X
   private static final int OP_COPY_TO_X = 10;
   
   //Copy value of AC to Y
   private static final int OP_COPY_TO_Y = 11;
   
   //Copy from X to AC
   private static final int OP_COPY_FROM_X = 12;
   
   //Copy from Y to AC
   private static final int OP_COPY_FROM_Y = 13;
   
   //Jump to specified address
   private static final int OP_JUMP_ADDR = 14;
   
   //Jump to specified address if AC == 0
   private static final int OP_JUMP_IF_EQUAL_ADDR = 15;
   
   //Jump to specified address if AC != 0
   private static final int OP_JUMP_IF_NOT_EQUAL_ADDR = 16;
   
   //Put return address to the stack, jump to the address
   private static final int OP_CALL_ADDR = 17;
   
   //Pop return address from stack, jump to the address
   private static final int OP_RET = 18;
   
   //Increment X by 1
   private static final int OP_INC_X = 19;
   
   //Decrement X by 1
   private static final int OP_DEC_X = 20;
   
   //Load the value of (address + X)
   private static final int OP_LOAD_IND_X_ADDR = 21;
   
   //Load the value of (address + Y)
   private static final int OP_LOAD_IND_Y_ADDR = 22;
   
   //Push AC to stack
   private static final int OP_PUSH = 23;
   
   //Pop from stack to AC
   private static final int OP_POP = 24;
   
   //............
   private static final int OP_INT = 25;
   
   //...............
   private static final int OP_IRET = 26;
   
   //End execution
   private static final int OP_END = 50;
   
   //Start main
   public static void main(String [] args)
   {
      //Variables
      int x = 0, y = 0;
      
      //Accumulator
      int ac = 0;
      
      //Program counter
      int pc = 0;
      
      //Instruction register
      int ir = 0;
      
      //Stack pointer
      int sp = 1000;
      
      int temp = 0;
      
      //Kernel
      boolean kernelMode = false;
      
      //Protocol
      String cmd = null;
      
      //reply memory
      String result = null;
      
      //tokens
      String [] tokens;
      
      Random rand = new Random();
      
      //Multi steps commands
      boolean lastSubCommand = false;
      
      try
      {
         Runtime rt = Runtime.getRuntime();
         
         //Create a process
         Process proc = rt.exec("java Memory");
         
         //Open outStream / inputStream to send/recive data to Memory
         OutputStream os = proc.getOutputStream();  
         InputStream is = proc.getInputStream();
         
         //Write to child
         PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(os));
                  
         //Read from child
         Scanner inReader = new Scanner(is);
         
         //We will keep executing instructions until either we see the
         //end of the commands or we run into access violation error
         while(true)
         {
            int cmdParameter = -1;
            
            //If kernelMode is off we cant access to interrupt handler area.
            if(!kernelMode && pc > 999)
            {
              System.out.println("Error: Access violation");
              cmdParameter = -1;
              ir = OP_END;
            }
            else
            {
               //Set up the command
               cmd = Memory.OPERATION_READ + " " + pc;
            
               //Send command to Memmory
               outWriter.println(cmd);
               outWriter.flush();
            
               //Read the data from memmory
               result = inReader.nextLine();
           
            
               tokens = result.split("[ ]+");
            
            
               if(ir == OP_NO_OP)
               {
                  //This must be the command
                  ir = Integer.parseInt(tokens[1]);
                  
               }//End if(ir == OP_NO_OP)
               else
               {
                  //This must be the parameter of the command we read perviously
                  cmdParameter = Integer.parseInt(tokens[1]);
               }
               
           }//End else
               
               //Start the switch
               switch(ir)
               {
                  //Load value in the next line to AC
                  case OP_LOAD_VALUE:
                     if(cmdParameter == -1)
                     {
                        //Break out of the switch statement, we will read the value
                        //in the next while loop. Note:we don't reset the ir variable
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        //We got the value
                        ac = cmdParameter;
                        ir = OP_NO_OP; // reset ir
                        pc++;
                     }
                     break;
                     
                  //Load the value at specific address into AC
                  case OP_LOAD_ADDR:
                     if(cmdParameter == -1)
                     {
                        //Need to read the next line to see 
                        //which address we need to read from
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        if(lastSubCommand)
                        {
                           //We got the value at the address
                           lastSubCommand = false;
                           //Load the value into AC
                           ac = cmdParameter;
                           //Set our program counter back to where we were
                           pc = temp;
                           temp = 0; //reset temp
                           ir = OP_NO_OP;
                           pc++;
                        }
                        else
                        {
                           //We got the address
                           //Now we need to change the program counter
                           //temporarily to the address so that we can read it value
                           temp = pc;
                           
                           //If kernelMode is off we cant access to interrupt handler area.
                           if(!kernelMode && cmdParameter > 999)
                           {
                              System.out.println("Error: Access violation");
                              cmdParameter = -1;
                              ir = OP_END;
                           }
                           
                           pc = cmdParameter;
                           lastSubCommand = true;
                        }
                     }
                     break;
                  
                  //Store valus of AC into address
                  case OP_STORE_ADDR:
                     if(cmdParameter == -1)
                     {
                        //we need to read the next line to see 
                        //which address we need to store the data
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        //If kernelMode if off we cant access to interrupt handler area.
                        if(!kernelMode && cmdParameter > 999)
                        {
                              System.out.println("Error: Access violation");
                              cmdParameter = -1;
                              ir = OP_END;
                        }
                        
                        //We have the address. Write AC to it
                        cmd = Memory.OPERATION_WRITE + " " + cmdParameter + " " + ac;
                        
                        //Send command to memory
                        outWriter.println(cmd);
                        outWriter.flush();
                        
                                                
                        ir = OP_NO_OP;
                        pc++;
                     }
                     break;
                  
                  //Add X to AC
                  case OP_ADD_X:
                     ac += x;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  //Add Y to AC
                  case OP_ADD_Y:
                     ac += y;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  //Subtract X from AC
                  case OP_SUB_X:
                     ac -= x;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  //Subtract Y from AC
                  case OP_SUB_Y:
                     ac -= y;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                     
                  //Get random number from 1 to 100
                  case OP_GET:
                     ac = rand.nextInt(100) + 1;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                     
                  //Put port (1 = write AC as int, 2 = write AC as char)
                  case OP_PUT_PORT:
                     if(cmdParameter == -1)
                     {
                        //We need to read the next line to see
                        //whether we need to print int or char
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        if(cmdParameter == 1)
                        {
                           //Print number
                           System.out.print(ac);
                        }
                        else if(cmdParameter == 2)
                        {
                           //Print number
                           System.out.print((char)ac);
                        }
                        ir = OP_NO_OP;
                        pc++;
                     }
                     break;
                     
                  case OP_COPY_TO_X:
                     x = ac;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  case OP_COPY_TO_Y:
                     y = ac;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  case OP_COPY_FROM_X:
                     ac = x;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                     
                  case OP_COPY_FROM_Y:
                     ac = y;
                     ir = OP_NO_OP;
                     pc++;
                     break;
                  
                  //Jump to the specified address
                  case OP_JUMP_ADDR:
                     if(cmdParameter == -1)
                     {
                        //We need to read the next line to see where we need to jump to
                        pc++;
                        break;
                     }
                     else
                     {
                        //If kernelMode if off we can't access to interrupt handler area.
                           if(!kernelMode && cmdParameter > 999)
                           {
                              System.out.println("Error: Access violation");
                              cmdParameter = -1;
                              ir = OP_END;
                           }
                           
                        //Set pc to the address
                        pc = cmdParameter;
                        ir = OP_NO_OP;
                     }
                     break;
                     
                  case OP_JUMP_IF_EQUAL_ADDR:
                     if(ac == 0)
                     {
                        //Set the command to jump to address then read the jump
                        //target in the next loop
                        ir = OP_JUMP_ADDR;
                        pc++;
                     }
                     else
                     {
                        //We don't care about the next line since we're not going
                        //to jump to it. Increment counter two times to skip it
                        pc++;
                        ir = OP_NO_OP;
                        pc++;
                     }
                     break;
                     
                  case OP_JUMP_IF_NOT_EQUAL_ADDR:
                     if(ac != 0)
                     {
                        //Set the command to jump to address then read the jump
                        //target in the next loop
                        ir = OP_JUMP_ADDR;
                        pc++;
                     }
                     else
                     {
                        //We don't care about the next line since we're not going
                        //to jump to it. Increment counter two times to skip it
                        pc++;
                        ir = OP_NO_OP;
                        pc++;
                     }
                     break;
                     
                  case OP_CALL_ADDR:
                     if(cmdParameter == -1)
                     {
                        //Need to read the next line to see 
                        //what we need to call
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        sp--;
                        //We get the address where to jump
                        //We write current PC in to array at 999
                        
                        cmd = Memory.OPERATION_WRITE + " " + sp + " " + pc;
                        
                        //Send cmd to memory
                        outWriter.println(cmd);
                        outWriter.flush();
                        
                        //If kernelMode if off we cant access to interrupt handler area.
                        if(!kernelMode && cmdParameter > 999)
                        {
                           System.out.println("Error: Access violation");
                           cmdParameter = -1;
                           ir = OP_END;
                        }                                                
                        pc = cmdParameter;
                        ir = OP_NO_OP;
                     }
                     break;
                  
                  case OP_RET:
                  
                     cmd = Memory.OPERATION_READ + " " + sp;
            
                     //Send command to Memmory
                     outWriter.println(cmd);
                     outWriter.flush();
                     
                     result = inReader.nextLine();
            
                     tokens = result.split("[ ]+");
                                         
                     //Jump back
                     pc = Integer.parseInt(tokens[1]);
                                          
                     sp++;
                                                           
                     ir = OP_NO_OP;
                     pc++;
                     
                     break;
                     
                  case OP_INC_X:
                     x++;
                     ir = OP_NO_OP;
                     pc++;
                     
                     break;
                     
                  case OP_DEC_X:
                     x--;
                     ir = OP_NO_OP;
                     pc++;
                     
                     break;
                     
                  case OP_LOAD_IND_X_ADDR:
                     if(cmdParameter == -1)
                     {
                        //Need to read the next line to see 
                        //the target address
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        if(lastSubCommand)
                        {
                           //We got the value at the address
                           lastSubCommand = false;
                           //Load the value into AC
                           ac = cmdParameter;
                           //Set our program counter back to where we were
                           pc = temp;
                           temp = 0; //reset temp
                           ir = OP_NO_OP;
                           //Go to next instruction
                           pc++;
                        }
                        else
                        {
                           //We got the address
                           //Now we need to change the program counter
                           //temporarily to the address + x so that we can read it value
                           temp = pc;
                           
                           //If kernelMode if off we cant access to interrupt handler area.
                        if(!kernelMode && (cmdParameter + x) > 999)
                        {
                              System.out.println("Error: Access violation");
                              cmdParameter = -1;
                              ir = OP_END;
                        }

                           pc = cmdParameter + x;
                           lastSubCommand = true;
                        }
                     }
                     break;
                     
                  case OP_LOAD_IND_Y_ADDR:
                     if(cmdParameter == -1)
                     {
                        //Need to read the next line to see 
                        //the target address
                        
                        pc++;
                        break;
                     }
                     else
                     {
                        if(lastSubCommand)
                        {
                           //We got the value at the address
                           lastSubCommand = false;
                           //Load the value into AC
                           ac = cmdParameter;
                           //Set our program counter back to where we were
                           pc = temp;
                           temp = 0; //reset temp
                           ir = OP_NO_OP;
                           //Go to next instruction
                           pc++;
                        }
                        else
                        {
                           //We got the address
                           //Now we need to change the program counter
                           //temporarily to the address + y so that we can read it value
                           temp = pc;
                           
                        //If kernelMode if off we cant access to interrupt handler area.
                        if(!kernelMode && (cmdParameter + y) > 999)
                        {
                              System.out.println("Error: Access violation");
                              cmdParameter = -1;
                              ir = OP_END;
                        }

                           pc = cmdParameter + y;
                           lastSubCommand = true;
                        }
                     }
                     break;
                     
                  case OP_PUSH:
                     //Move SP up to one
                     sp--;
                        //Set up the protocol 
                        cmd = Memory.OPERATION_WRITE + " " + sp + " " + ac;
                        
                        //Send cmd to memory
                        outWriter.println(cmd);
                        outWriter.flush();
                        
                        //Next instruction
                        pc++;
                      break;
                     
                  case OP_POP:
                    cmd = Memory.OPERATION_READ + " " + sp;
            
                     //Send command to Memmory
                     outWriter.println(cmd);
                     outWriter.flush();
                     
                     result = inReader.nextLine();
            
                     tokens = result.split("[ ]+");
                                         
                     //Pop value into AC
                     ac = Integer.parseInt(tokens[1]);
                                          
                     //After POP, bring sp down by 1
                     sp++;
                     
                     //Next Instruction
                     pc++;
                     break;
                     
                  case OP_INT:
                     /*Put PC into stack */
                     
                     //Move SP up to one
                     sp--;
                        //Set up the protocol 
                        cmd = Memory.OPERATION_WRITE + " " + sp + " " + pc;
                        
                        //Send cmd to memory
                        outWriter.println(cmd);
                        outWriter.flush();
                        
                    //Turn on Kernel mode
                    kernelMode =true;
                    
                    //Jump to interrupt handler
                    pc = 1000;
                     
                     break;
                     
                  case OP_IRET:
                     
                     cmd = Memory.OPERATION_READ + " " + sp;
            
                     //Send command to Memmory
                     outWriter.println(cmd);
                     outWriter.flush();
                     
                     result = inReader.nextLine();
            
                     tokens = result.split("[ ]+");
                                         
                     //Jump back
                     pc = Integer.parseInt(tokens[1]);
                                          
                     sp++;
                     
                     //Turn back to user mode
                     kernelMode = false;
                                                           
                     ir = OP_NO_OP;
                     
                     //Next instruction
                     pc++;
                     break;
                     
                  case OP_END:
                     proc.destroy();
                     proc.waitFor();
                     int exitVal = proc.exitValue();

                     System.out.println("No more instruction \n" + "Process exited: " + exitVal);
                     
                     System.exit(0);
                     
                     break;
                  
                  default:
                     System.out.println("Unknown command: "+ir+ " at line "+ (pc -1));
                     proc.destroy();
                     proc.waitFor();
               
                     exitVal = proc.exitValue();

                     System.out.println("Terminate program.\n" + "Process exited: " + exitVal);
                     
                     System.exit(1);
                     break;
                  
               }//End switch(ir)
                         
         }//End while
         
      }//End try
      catch(Throwable t)
      {
         t.printStackTrace();
      }
      
      
   }//End main
}
