/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc04;
import java.util.*;
/**
 *
 * @author zhongjiezheng
 */
public class CC04 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CC04 test = new CC04();
        while(true){
            System.out.println("Please give a anagram string: (use space to separate them)");
            Scanner input = new Scanner(System.in);
            String a = input.nextLine();
            a = a.trim();
            String words[] = a.split(" ");
            if(test.anagram(words)==true)
                System.out.println(words[0]+" "+words[1]+"It is anagram");
            else
                System.out.println(words[0]+" "+words[1]+"It is NOT anagram");
        }
    }
    
    public boolean anagram(String[] words){
        if(words.length!=2){
//            System.out.println("jump out here");
            return false;
        }   
        else{
            String word1 = words[0];
            String word2 = words[1];
            
            if(word1.length()!=word2.length())
            {
//                System.out.println("jump out here");
                return false;
            }
            else{
                
                ArrayList<Character> charinword1 = new ArrayList();
                ArrayList<Integer> numofchar = new ArrayList();
                //collect the character info in the word1
                for(int i= 0; i<word1.length(); i++){
                    if(!charinword1.contains(word1.charAt(i))){
                        charinword1.add(word1.charAt(i));
                        numofchar.add(1);
                    }
                    else{
                        int index = charinword1.indexOf(word1.charAt(i));
                        int number = numofchar.get(index);
                        number++;
                        numofchar.remove(index);
                        numofchar.add(index,number);
                    }
                       
                }
                
                //see what word2 looks like
                for(int i=0; i<word2.length(); i++){
                    if(!charinword1.contains(word2.charAt(i)))
                        return false;
                    else{
                        int index = charinword1.indexOf(word2.charAt(i));
                        int number =numofchar.get(index);
                        number--;
                        numofchar.remove(index);
                        numofchar.add(index,number);
                    }
                    
                }
                for(int i =0; i< numofchar.size(); i++){
                    if(numofchar.get(i)!=0)
                        return false;
                }
                return true;
            }
        }
    }
       
}
