/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbinitial;
import java.io.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author zhongjiezheng
 */
public class DBInitial {
    
    public String Book_copies_path = new String();
    public String Book_author_path = new String();
    public String Borrower_Path = new String();
    public String library_branch_path = new String();
    public static Connection conn=null;
    
    DBInitial(){
        
    }
    
    public void initial(){
          InitialBook();
          InitialBranch();
          InitialBorrower();
          InitialAuthor();
          InitialBookCopy();
    }
    
    public void InitialAuthor(){
        try{
            FileInputStream fstream = new FileInputStream(Book_author_path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp,temp2;
            int i=0;
            int length=0,AuthorNum;
            
            while((temp=br.readLine())!=null){
                if(i>0){
                    LinkedList<String> name;
                    String arrs[]=temp.split("\t");   ///split book id and author name book title
                    
                    if(arrs.length==2){                 //if book have no author
                        arrs[0]=BookIdRegular(arrs[0]);
                        AuthorImport(arrs[0],"null","null","null","null");
                    }
                    
                    if(arrs[0]!=null && arrs[1]!=null){     //if book has
                        arrs[0]=BookIdRegular(arrs[0]);   //arrs[0] is book id;
                        name=NameRegular(arrs[1]);        //name contain mutiple author
                        AuthorNum=(name.size())/4;
                        for(int j=0;j<AuthorNum;j++){
                            AuthorImport(arrs[0],name.get(j*4),name.get(j*4+1),name.get(j*4+2),name.get(j*4+3));
                        }
                    }  
                    i++;
                    System.out.println("INSERT Book Auhtors Success in Row "+i); 
                }
                else
                    i++;  
            }
            
        }
        catch(IOException e){
            
        }
        
    }
    
    public LinkedList<String> NameRegular(String Name){
        LinkedList<String> total =new LinkedList();
        String author[];
        String NameComp[];
        int NameNum;
        
        Name=Name.trim();
        author=Name.split(",");                   //if there are multiple author
        NameNum=author.length;
        for(int i=0; i< NameNum; i++){            // Now process each author, slipt each name into fname lname,minit
            if(author[i].contains("Museum") && author[i].contains("Various")){             //if the book author is a group
                total.offer(author[i]);
                total.offer("null");
                total.offer("null");
                total.offer("null");
            }

            else{                                   //if not a group
                String temp[]=null;
                String PureName;
                if(author[i].contains("(")){
                    temp=author[i].split("\\(");         //if there are case like "(editor)", trucate what after "("
                    PureName=temp[0];
                }
                else
                    PureName=author[i];         //if not, get the author name
                
                PureName=PureName.trim();
                NameComp=PureName.split(" ");      // split lname fanme minit(if applicable)
                if(NameComp.length==1){              //if only have fname
                    total.offer(author[i]);
                    total.offer(NameComp[0]);
                    total.offer("null");
                    total.offer("null");
                }
                else if(NameComp.length==2)        //if only have fname lname
                {
      //             total.append(author[i]+" "+NameComp[0]+" null "+NameComp[1]+" ");    //if only fname lname
                    total.offer(author[i]);
                    total.offer(NameComp[0]);
                    total.offer("null");
                    total.offer(NameComp[1]);
                }
                else{
           //        total.append(author[i]+" "+NameComp[0]+" "+NameComp[1].charAt(0)+" "+NameComp[NameComp.length-1]+" "); //if have middle lname fname
                    total.offer(author[i]);
                    total.offer(NameComp[0]);
                    total.offer(NameComp[1].substring(0, 1));
                    total.offer(NameComp[NameComp.length-1]);
                }
            } 
        }
      
        return total;
}
    
    public void AuthorImport(String BookId, String Author, String fname, String minit, String lname){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO BOOK_AUTHORS VALUES ("+"\'"+BookId+"\',"+affix(Author)+","+affix(fname)+","+affix(minit)+","+affix(lname)+");");
            stmt.close();
            conn.close();
        }
        catch(SQLException ex) {
			System.out.println("Error in connection: " + ex.getMessage());
    }
 //       System.out.println("INSERT INTO BOOK_AUTHORS VALUES ("+"\'"+BookId+"\',"+affix(Author)+","+affix(fname)+","+affix(minit)+","+affix(lname)+");");
        
    }
    
    public String affix(String origin){
        origin=origin.trim();
        if(origin.equals("null"))
            return origin;
        else
            return "\'"+origin+"\'";
    }

///////////////////////////////////////////////Book Copy////////////////////////////////////////////////
    public void InitialBookCopy(){
        try{
            FileInputStream fstream = new FileInputStream(Book_copies_path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp;
            int i=0;
            int branch_id,no_of_copy;
            
            while((temp=br.readLine())!=null){
                if(i>0){
                    String arrs[]=temp.split("\t");   ///split book id, branch id, no of copy
                    if(arrs!=null)
                        arrs[0]=BookIdRegular(arrs[0]);
                    branch_id=Integer.parseInt(arrs[1]);   //arrs[1] is branch id;
                    no_of_copy=Integer.parseInt(arrs[2]);
                    BookCopyImport(arrs[0],branch_id,no_of_copy);
                    i++;
                    System.out.println("INSERT BOOK_COPIES Table Success in Row "+i); 
                }
                else
                    i++;  
            }
        }
        catch(IOException e){
            
        }
        
    }
    
    public void BookCopyImport(String book_id, int branch_id,int copy){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO BOOK_COPIES VALUES ("+"\'"+book_id+"\',\'"+branch_id+"\',\'"+copy+"\');");
            stmt.close();
            conn.close();
        }
        catch(SQLException ex) {
		System.out.println("Error in connection: " + ex.getMessage());
    }
        
    }
    
///////////////////////////////////////////////Borrower////////////////////////////////////////////////
    public void InitialBorrower(){
        try{
            FileInputStream fstream = new FileInputStream(Borrower_Path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp;
            int i=0;
            int card_no;
            
            while((temp=br.readLine())!=null){
                if(i>0){
                    String arrs[]=temp.split("\t");   ///split branch id, name, address
                    card_no=Integer.parseInt(arrs[0]);   //arrs[0] is branch id;
                    BorrowerImport(card_no,arrs[1],arrs[2],arrs[3],arrs[4],arrs[5],arrs[6]);
                    i++;
                    System.out.println("INSERT Borrower Table Success in Row "+i); 
                }
                else
                    i++;  
            }
        }
        catch(IOException e){
            
        }
    }
    
    public void BorrowerImport(int card_no,String fname,String lname, String address,String city, String state, String phone ){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO BORROWER VALUES ("+"\'"+card_no+"\',\'"+fname+"\',\'"+lname+"\',\'"+address+" "+city+" "+state+"\',\'"+phone+"\');");
            stmt.close();
            conn.close();
        }
        catch(SQLException ex) {
		System.out.println("Error in connection: " + ex.getMessage());
    }
        
    }
    
///////////////////////////////////////////////Branch////////////////////////////////////////////////
    public void InitialBranch(){
        try{
            FileInputStream fstream = new FileInputStream(library_branch_path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp;
            int i=0;
            int branch_id;
            
            while((temp=br.readLine())!=null){
                if(i>0){
                    String arrs[]=temp.split("\t");   ///split branch id, name, address
                    branch_id=Integer.parseInt(arrs[0]);   //arrs[0] is branch id;
                    BranchImport(branch_id,arrs[1],arrs[2]);    //
                    i++;
                    System.out.println("INSERT Book_Branch Table Success in Row "+i); 
                }
                else
                    i++;  
            }
        }
        catch(IOException e){
            
        }
        
    }
        
    public void BranchImport(int branch_id,String Name, String Address){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO LIBRARY_BRANCH VALUES ("+"\'"+branch_id+"\',\'"+Name+"\',\'"+Address+"\');");
            stmt.close();
            conn.close();
        }
        catch(SQLException ex) {
		System.out.println("Error in connection: " + ex.getMessage());
    }
        
    }
    
 ///////////////////////////////////////////////BOOK////////////////////////////////////////////////
    public void InitialBook(){
        try{
            FileInputStream fstream = new FileInputStream(Book_author_path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp;
            int i=0;
            int length=0;
            
            while((temp=br.readLine())!=null){
                if(i>0){
                    String arrs[]=temp.split("\t");   ///split book id and author name book title
                    if(arrs[0]!=null)
                        arrs[0]=BookIdRegular(arrs[0]);   //arrs[0] is book id;
                    length=arrs.length;
                    arrs[length-1]=BookTitleRegular(arrs[length-1]);  //arrs[length-1] is the title
                    BookImport(arrs[0],arrs[length-1]);    //bookid,booktitle
                    i++;
        //            System.out.println("INSERT INTO BOOK VALUES ("+"\'"+arrs[0]+"\',\'"+arrs[length-1]+"\');");
                    System.out.println("INSERT Book Table Success in Row "+i); 
                    
                    
                }
                else
                    i++;  
            }
            
        }
        catch(IOException e){
            
        }
    }
    
    public String BookIdRegular(String bookid){
        bookid=bookid.trim();
        int Num=10-bookid.length();
        StringBuffer newid = new StringBuffer();
        for(int i=0; i<Num; i++){
            newid.append("0");
        }
        newid.append(bookid);
        return newid.toString();
    }
    
    public String BookTitleRegular(String title){
        title=title.trim();
        if(title.contains("\'")){
            title=title.replaceAll("\'", "\'\'");
            return title;
        }
        else
            return title;
    }

    public void BookImport(String BookId, String BookTitle){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO BOOK VALUES ("+"\'"+BookId+"\',\'"+BookTitle+"\');");
            stmt.close();
            conn.close();
        }
        catch(SQLException ex) {
			System.out.println("Error in connection: " + ex.getMessage());
    }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner Input = new Scanner(System.in);
        System.out.println("This program is going to insert data in your database, if the data already there, this may cause problem, do you want countine? Y/N");
        String D=Input.nextLine();
        D=D.trim();
        if(D.contains("N"));{
           DBInitial A= new DBInitial();
           A.Book_copies_path="/Users/zhongjiezheng/Desktop/14S/Database Design/Project/Library_data/books_authors.csv";
           A.library_branch_path="/Users/zhongjiezheng/Desktop/14S/Database Design/Project/Library_data/library_branch.csv";
           A.Borrower_Path="/Users/zhongjiezheng/Desktop/14S/Database Design/Project/Library_data/borrowers.csv";
           A.Book_copies_path="/Users/zhongjiezheng/Desktop/14S/Database Design/Project/Library_data/book_copies.csv";
           A.Book_author_path="/Users/zhongjiezheng/Desktop/14S/Database Design/Project/Library_data/books_authors.csv";
           A.initial();
    }
        
        
    }
}
