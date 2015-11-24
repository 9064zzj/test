/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc03;
import java.util.*;
/**
 *
 * @author zhongjiezheng
 */
public class CC03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CC03 test = new CC03();
        while(true){
            System.out.println("Please type same string:");
            Scanner input = new Scanner(System.in);
            StringBuffer a = new StringBuffer();
            a.append(input.nextLine());  //got the string in a
            String output=test.NoDuplicate(a);
            System.out.println(output);
            System.out.println(test.Test(new StringBuffer(output)));
        }
 
    }
    
    public String NoDuplicate(StringBuffer a){
        ArrayList duplicateChar = new ArrayList();
        int length = a.length();
        for(int i=0; i<length; i++){
            if(!duplicateChar.contains(a.charAt(i)))
                duplicateChar.add(a.charAt(i));
            else{
                a.deleteCharAt(i);
                i--;
                length--;
            }
        }
        return a.toString();
    }
    
    public boolean Test(StringBuffer a){
        boolean duplicated = false;
        ArrayList duplication = new ArrayList();
        int length = a.length();
        for(int i = 0; i<length; i++){
            if(duplication.contains(a.charAt(i))){
                duplicated =true;
                break;
            }
            else{
                duplication.add(a.charAt(i));
            }  
            
        }
        return duplicated;
    }
}
