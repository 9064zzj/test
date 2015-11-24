/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc01;

import java.util.*;
import java.lang.*;
import java.io.*;
/**
 *
 * @author zhongjiezheng
 */
public class CC01 {

     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        while(true){
        CC01 test = new CC01();
        Scanner input = new Scanner(System.in);
        String a = new String();
        a = input.nextLine().toUpperCase();
        System.out.println(test.unique(a));
        }
    }
    
    
    public boolean unique(String a){
        StringBuffer temp = new StringBuffer(a);
        int length = temp.length();
        ArrayList index = new ArrayList();
        
        for (int i=0; i < length ; i++){
            char CharOfString = temp.charAt(i);
            if(CharOfString>64 && CharOfString<133 && !index.contains(CharOfString))
                index.add(CharOfString);
        }
        
        if(index.size() == 26)
            return true;
        else
            return false;
    }


//    public int index(char a){
//        int index;
//        switch(a){
//            case 'a': 
//                index = 0;
//                break;
//            case 'A':
//                index = 0;
//                break;
//            case 'b': 
//                index = 1;
//                break;
//            case 'B':
//                index = 1;
//                break;
//            case 'c': 
//                index = 2;
//                break;
//            case 'C':
//                index = 2;
//                break;
//                
//        }
//        
//    }
}
