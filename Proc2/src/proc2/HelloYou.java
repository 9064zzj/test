/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proc2;

import java.util.Scanner;

/**
 *
 * @author zhongjiezheng
 */
public class HelloYou {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         
   
      Scanner sc = new Scanner(System.in);
 
      String name = null;
      if (sc.hasNext())
         name = sc.nextLine(); 

      System.out.println("Hello " + name + "!");
   
    }
}
