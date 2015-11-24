/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proc2;
import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

/**
 *
 * @author zhongjiezheng
 */
public class Proc2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try
      {            
	 int x;
	 Runtime rt = Runtime.getRuntime();

	 Process proc = rt.exec("java HelloYou");

	 InputStream is = proc.getInputStream();
	 OutputStream os = proc.getOutputStream();

         PrintWriter pw = new PrintWriter(os);
	 pw.printf("Greg\n");
         pw.flush();

	 Scanner sc = new Scanner(is);
	 String line = sc.nextLine();

         for (int i=0; i<line.length(); i++)
	    System.out.println(line.charAt(i)); 
	      
	 proc.waitFor();

         int exitVal = proc.exitValue();

         System.out.println("Process exited: " + exitVal);

      } 
      catch (Throwable t)
      {
	 t.printStackTrace();
      }

    }
}
