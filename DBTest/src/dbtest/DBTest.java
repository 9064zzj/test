/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbtest;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author zhongjiezheng
 */
public class DBTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Vector<Vector<String>> result=new Vector<Vector<String>>();
    Vector<String>  Bookcolumn = new Vector<String>();
               result = new Vector<Vector<String>>();
        Connection conn = null;
        Vector<String> temp = new Vector<String>();
        
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            Statement BookSearch = conn.createStatement();
            ResultSet BookResult= BookSearch.executeQuery(
            "SELECT book.book_id, book.title, book_authors.author_name, fname,minit,lname, BOOK_COPIES.branch_id, BOOK_COPIES.no_of_copies-count(card_no) AS available_copies"+
            " FROM BOOK NATURAL JOIN BOOK_AUTHORS NATURAL JOIN BOOK_COPIES LEFT JOIN book_loans ON book_copies.book_id=book_loans.book_id" +
            " WHERE author_name LIKE '%"+"%' AND book.book_id LIKE '%"+""+"%' AND title LIKE '%"+"%'"+
            " GROUP BY book.book_id, book_copies.branch_id;");
            while(BookResult.next()){

                String book_id= BookResult.getString("book_id");
                String title = BookResult.getString("title");
                String authorName =BookResult.getString("author_name");
                String fname = BookResult.getString("fname");
                String minit = BookResult.getString("minit");
                String lname = BookResult.getString("lname");
                String branch_id = BookResult.getString("branch_id");
                String available = BookResult.getString("available_copies");
                
//                System.out.print(book_id + ".\t");
//				System.out.print(title + "\t");
//				System.out.print(authorName + "\t");
//				System.out.print(fname + "\t");
//				System.out.print(minit + "\t");
//                                System.out.print(lname + "\t");
//                                System.out.print(branch_id + "\t");
//                                System.out.print(available + "\t");
//                                
//				System.out.println();
                
                
                
                

                temp= new Vector<String>(Arrays.asList(book_id,title,authorName,fname,minit,lname,branch_id,available));
                
                 result.add(temp);
            }
            
            
            BookResult.close();
            conn.close();
            
            System.out.println(result.toString());
        }
        catch(SQLException ex) {
		System.out.println("Error in connection: " + ex.getMessage());
    }
        Bookcolumn= new Vector<String>();
        Bookcolumn.add("Book ID");
        Bookcolumn.add("Title");
        Bookcolumn.add("Author");
        Bookcolumn.add("Fname");
        Bookcolumn.add("Minit");
        Bookcolumn.add("Lname");
        Bookcolumn.add("Branch ID");
        Bookcolumn.add("Available Copies");
        JTable ResultTable = new JTable(result, Bookcolumn);
        JFrame jf =new JFrame();
       jf.add(ResultTable);
       jf.setVisible(true);
        
								
    }
}
