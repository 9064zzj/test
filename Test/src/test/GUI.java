/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author zhongjiezheng
 */


import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;

public class GUI extends JFrame implements ActionListener {
	//static Connection conn;
	//static Statement stmt;
	//static ResultSet rs;
	
	JMenuBar menuBar;
	JMenu function;
	JMenuItem newBorrower, checkOut, checkIn, deleteBorrower, updateBorrower, addBook;
	
	JPanel searchPanel;
	JPanel tablePanel;
	
	JLabel bookIDLabel, titleLabel, authorLabel;
	JTextField bookIDField, titleField, authorField;
	JButton searchButton;
	JTable resultTable;
	//ResultSetTableModel resultTableModel;
	JScrollPane tableScrollPane;
	
	Vector<Vector<String>> result;
	Vector<String> columnName;
	
	protected GUI(){
		setTitle("DataBase");
		//setSize(500, 150);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		newBorrower = new JMenuItem("New Borrower");
		newBorrower.addActionListener(this);
		checkOut = new JMenuItem("Check Out");
		checkOut.addActionListener(this);
		checkIn = new JMenuItem("Check In");
		checkIn.addActionListener(this);
		deleteBorrower = new JMenuItem("Delete Borrower");
		deleteBorrower.addActionListener(this);
		updateBorrower = new JMenuItem("Update Borrower");
		updateBorrower.addActionListener(this);
		addBook = new JMenuItem("Add Book");
		addBook.addActionListener(this);
		
		function = new JMenu("Function");
		function.add(newBorrower);
		function.add(checkOut);
		function.add(checkIn);
		function.add(deleteBorrower);
		function.add(updateBorrower);
		function.add(addBook);
		menuBar = new JMenuBar();
		menuBar.add(function);
		this.setJMenuBar(menuBar);
		
		searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		
		bookIDLabel = new JLabel("Book ID: ");
		bookIDField = new JTextField(10);
		titleLabel = new JLabel("Title: ");
		titleField = new JTextField(10);
		authorLabel = new JLabel("Author: ");
		authorField = new JTextField(10);
		searchButton = new JButton("Search");
		searchButton.addActionListener(this);
		
		searchPanel.add(bookIDLabel);
		searchPanel.add(bookIDField);
		searchPanel.add(titleLabel);
		searchPanel.add(titleField);
		searchPanel.add(authorLabel);
		searchPanel.add(authorField);
		searchPanel.add(searchButton);
		
		result = new Vector<Vector<String>>();
		Vector<String> tempResult = new Vector<String>();
		
		columnName = new Vector<String>();
		columnName.add("BookID");
		columnName.add("BranchID");
		columnName.add("Total");
		columnName.add("Available");
		//System.out.println(columnName);
		

		tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 0));
		
		
		setLayout(new BorderLayout());
		add(searchPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == newBorrower){
			DialogFactory dialog = new DialogFactory(this, "New Borrower");
		}
		else{
			if(event.getSource() == checkOut){
				DialogFactory dialog = new DialogFactory(this, "Check Out");
			}
			else{
				if(event.getSource() == checkIn){
					DialogFactory dialog = new DialogFactory(this, "Check In");
				}
				else{
					if(event.getSource() == searchButton){
						String bookID = bookIDField.getText();
						String title = titleField.getText();
						String author = authorField.getText();
						if(bookID.length() == 0 && title.length() == 0 && author.length() == 0){
							JOptionPane.showMessageDialog(null, "Must Fill at least one fields!!");
						}
						else{
							try{
								StringBuffer sqlBuffer = new StringBuffer();
								sqlBuffer.append("SELECT book.book_id, book_copies.branch_id, book_copies.no_of_copies, book_c"
										+ "opies.no_of_copies-count(card_no)");
								sqlBuffer.append(" FROM BOOK Natural Join BOOK_AUTHORS Natural Join BOOK_COPIES" + 
										" Left Join book_loans on book_copies.book_id=book_loans.book_id");
								sqlBuffer.append(" WHERE");
								if(bookID.length() != 0){
									sqlBuffer.append(" book.book_id LIKE'%" + bookID + "%'");
									if(title.length() != 0){
										sqlBuffer.append(" AND book.title LIKE'%" + title + "%'");
										if(author.length() != 0){
											sqlBuffer.append(" AND book_authors.author_name LIKE'%" + author + "%'");
											//sqlBuffer.append(" AND Author_name LIKE'%" + author + "%';");
										}
										else{
											//sqlBuffer.append(";");
										}
									}
									else{
										if(author.length() != 0){
											sqlBuffer.append(" AND book_authors.author_name LIKE'%" + author + "%'");
											//sqlBuffer.append(" AND Author_name LIKE'%" + author + "%';");
										}
										else{
											//sqlBuffer.append(";");
										}
									}
								}
								else{
									if(title.length() != 0){
										sqlBuffer.append(" book.title LIKE'%" + title + "%'");
										if(author.length() != 0){
											sqlBuffer.append(" AND book_authors.author_name LIKE'%" + author + "%'");
											//sqlBuffer.append(" AND Author_name LIKE'%" + author + "%';");
										}
										else{
											//sqlBuffer.append(";");
										}
									}
									else{
										if(author.length() != 0){
											sqlBuffer.append(" book_authors.author_name LIKE'%" + author + "%'");
											//sqlBuffer.append(" Author_name LIKE'%" + author + "%';");
										}
										else{
											//sqlBuffer.append(";");
										}
									}
								}
								sqlBuffer.append(" group by book.book_id, book_copies.branch_id;");
								String sql = sqlBuffer.toString();
								//System.out.println(sql);
								
								// Create a connection to the local MySQL server, with the "company" database selected.
								Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								Statement stmt = conn.createStatement();
								//stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
								stmt.executeQuery("use library;");
								ResultSet rs = stmt.executeQuery(sql);
								
							
								
								
								result = new Vector<Vector<String>>();
								Vector<String> tempResult = new Vector<String>();
								
								while (rs.next()){									
									String book_id = rs.getString(1);
									String branch_id = rs.getString(2);
									String total = rs.getString(3);
									String available = rs.getString(4);
									
									tempResult = new Vector<String>(Arrays.asList(book_id, branch_id, total, available));
									result.add(tempResult);

								}
								
								//System.out.println(result);
								
								columnName = new Vector<String>();
								columnName.add("BookID");
								columnName.add("BranchID");
								columnName.add("Total");
								columnName.add("Available");
								resultTable = new JTable(result, columnName);
								resultTable.setPreferredScrollableViewportSize(new Dimension(500, 150));
								resultTable.setFillsViewportHeight(true);
								resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
								tableScrollPane = new JScrollPane(resultTable);
								tablePanel.removeAll();
								tablePanel.add(tableScrollPane);
								tablePanel.validate();
								
								pack();
								repaint();
								
								rs.close();
								conn.close();
							} catch(SQLException ex){
								System.out.println("Error in connection: " + ex.getMessage());
							}
						}
					}
					else{
						if(event.getSource() == deleteBorrower){
							DialogFactory dialog = new DialogFactory(this, "Delete Borrower");
						}
						else{
							if(event.getSource() == updateBorrower){
								DialogFactory dialog = new DialogFactory(this, "Update Borrower");
							}
							else{
								if(event.getSource() == addBook){
									DialogFactory dialog = new DialogFactory(this, "Add Book");
								}
								else{
									
								}
							}
						}
					}
				}
			}
		}
	}
}
