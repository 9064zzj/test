/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author zhongjiezheng
 */
public class Test2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String paid = null;
        
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "q6egmoB1");
            
            Statement PayCheck = conn.createStatement();
            ResultSet PayResult=PayCheck.executeQuery(
                    " SELECT paid AS A" +
                    " FROM FINES" +
                    " WHERE loan_id='9';");
            PayResult.next();
            paid=PayResult.getString("A");
            PayCheck.close();
            conn.close();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error in connection: "+ ex.getMessage(), "WARNING", JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.print(paid);
    }
}
