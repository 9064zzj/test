package test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zhongjiezheng
 */

import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;

public class DialogFactory extends JDialog {
	JLabel firstNameLabel = new JLabel("First Name: ");
	JTextField firstNameField = new JTextField(10);
	JLabel lastNameLabel = new JLabel("Last Name: ");
	JTextField lastNameField = new JTextField(10);
	JLabel addressLabel = new JLabel("Address: ");
	JTextField addressField = new JTextField(10);
	JLabel phoneLabel = new JLabel("Phone: ");
	JTextField phoneField = new JTextField(10);
	JLabel bookIDLabel = new JLabel("Book ID: ");
	JTextField bookIDField = new JTextField(10);
	JLabel branchIDLabel = new JLabel("Branch ID: ");
	JTextField branchIDField = new JTextField(10);
	JLabel cardNoLabel = new JLabel("Card No: ");
	JTextField cardNoField = new JTextField(10);
	JLabel borrowerLabel = new JLabel("Borrower: ");
	JTextField borrowerField = new JTextField(20);
	JLabel titleLabel = new JLabel("Title: ");
	JTextField titleField = new JTextField(20);
	JLabel authorLabel = new JLabel("Author: ");
	JTextField authorField = new JTextField(20);
	JLabel copiesLabel = new JLabel("Copies: ");
	JTextField copiesField = new JTextField(20);
	JTable resultTable = new JTable();
	JPanel tablePanel = new JPanel();
	
	protected DialogFactory(GUI father, String type){
		super(father, type, true);
		setResizable(false);
		tablePanel.setLayout(new GridLayout(1, 0));
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		switch(type){
			case "New Borrower":{
				this.setLayout(new GridLayout(5,1,5,5));
				add(firstNameLabel);
				add(firstNameField);
				add(lastNameLabel);
				add(lastNameField);
				add(addressLabel);
				add(addressField);
				add(phoneLabel);
				add(phoneField);
				add(okButton);
				add(cancelButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String firstName = firstNameField.getText();
						String lastName = lastNameField.getText();
						String address = addressField.getText();
						String phone = phoneField.getText();
						if(firstName.length() == 0 || lastName.length() == 0 || address.length() == 0){
							JOptionPane.showMessageDialog(null, "Must Fill these fields!!");
						}
						else{
							StringBuffer sqlBuffer = new StringBuffer();
							sqlBuffer.append("Select Fname, Lname, Address From borrower Where");
							sqlBuffer.append(" Fname='" + firstName + "' AND Lname='" + lastName + "' AND Address='" + address + "';");
							String sql = sqlBuffer.toString();
							try{
								Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								Statement stmt = conn.createStatement();
								stmt.executeQuery("use library;");
								ResultSet rs = stmt.executeQuery(sql);
								
								int count = 0;
								while(rs.next()){
									count++;
								}
								rs.close();
								conn.close();
								
								if(count == 0){
									//System.out.println("OYeah!!!!!!!!!");
									sql = new String("Select card_no From borrower Order By card_no DESC;");
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									rs = stmt.executeQuery(sql);
									//if(rs.next()){
									rs.next();
									String highestCardNoString = rs.getString(1);
									rs.close();
									conn.close();
									
									long highestCardNo = (new Long(highestCardNoString)).longValue();
									String card_no = (new Long(highestCardNo + 1)).toString();
									
									sqlBuffer = new StringBuffer();
									
									if(phone.length() != 0){
										sqlBuffer.append("Insert Into Borrower (card_no, Fname, Lname, Address, Phone)");
										sqlBuffer.append(" Values ('" + card_no + "', '" + firstName + "', '" + lastName + "', '" 
													+ address + "', '" + phone + "');");	
									}
									else{
										sqlBuffer.append("Insert Into Borrower (card_no, Fname, Lname, Address)");
										sqlBuffer.append(" Values ('" + card_no + "', '" + firstName + "', '" + lastName + "', '" 
													+ address + "');");
									}
									sql = sqlBuffer.toString();
									System.out.println(sql);
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									stmt.executeUpdate(sql);
									//rs.close();
									conn.close();
									//}
									//else{
									//	
									//}
									dispose();
								}
								else{
									JOptionPane.showMessageDialog(null, "This person has registered!!");
								}
							} catch(SQLException ex){
								ex.printStackTrace();
							}
						}
					}
				});
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
				pack();
				setVisible(true);
				break;	
			}
			case "Check Out":{
				this.setLayout(new GridLayout(4,1,5,5));
				add(bookIDLabel);
				add(bookIDField);
				add(branchIDLabel);
				add(branchIDField);
				add(cardNoLabel);
				add(cardNoField);
				add(okButton);
				add(cancelButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String bookID = bookIDField.getText();
						String branchID = branchIDField.getText();
						String cardNo = cardNoField.getText();
						if(bookID.length() == 0 || branchID.length() == 0 || cardNo.length() == 0){
							JOptionPane.showMessageDialog(null, "Must Fill these fields!!");
						}
						else{
							try{
								StringBuffer sqlBuffer = new StringBuffer();
								sqlBuffer.append("Select * From borrower where card_no='" + cardNo + "';");
								String sql = sqlBuffer.toString();
								Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								Statement stmt = conn.createStatement();
								stmt.executeQuery("use library;");
								ResultSet rs = stmt.executeQuery(sql);
								if(!rs.next()){
									rs.close();
									conn.close();
									JOptionPane.showMessageDialog(null, "No Such Borrower!!");
								}
								else{
									rs.close();
									conn.close();
									sqlBuffer = new StringBuffer();
									sqlBuffer.append("Select count(*) From book_loans");
									sqlBuffer.append(" Where card_no='" + cardNo + "' Group By card_no;");
									sql = sqlBuffer.toString();
									//System.out.println(sql);
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									rs = stmt.executeQuery(sql);
									int count;
									if(!rs.next()){
										count = 0;
									}
									else{
										String countString = rs.getString(1);
										count = (new Integer(countString)).intValue();
									}
									//int count = 0;
									/*while(rs.next()){
										count++;
										String bookAvailableString = rs.getString(1);
										System.out.println(bookAvailableString);
									}*/
									rs.close();
									conn.close();
									if(count >= 3){
										JOptionPane.showMessageDialog(null, "This person has already borrowed three books!!");
									}
									else{
										sqlBuffer = new StringBuffer();
										sqlBuffer.append("Select * from book where book_id='" + bookID + "';");
										sql = sqlBuffer.toString();
										conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
										stmt = conn.createStatement();
										stmt.executeQuery("use library;");
										rs = stmt.executeQuery(sql);
										if(!rs.next()){
											rs.close();
											conn.close();
											JOptionPane.showMessageDialog(null, "No Such Book!!");
										}
										else{
											rs.close();
											conn.close();
											sqlBuffer = new StringBuffer();
											sqlBuffer.append("Select * from library_branch where branch_id='" + branchID + "';");
											sql = sqlBuffer.toString();
											conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
											stmt = conn.createStatement();
											stmt.executeQuery("use library;");
											rs = stmt.executeQuery(sql);
											if(!rs.next()){
												rs.close();
												conn.close();
												JOptionPane.showMessageDialog(null, "No Such Branch!!");
											}
											else{
												sqlBuffer = new StringBuffer();
												sqlBuffer.append("Select book_copies.no_of_copies-count(card_no) From book_copies left join book_loans on book_copies.book_id=book_loans.book_id");
												sqlBuffer.append(" Where book_copies.book_id='" + bookID + "' AND book_copies.branch_id='" + branchID + "'");
												sqlBuffer.append(" Group By book_copies.book_id, book_copies.branch_id");
												sql = sqlBuffer.toString();
												conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
												stmt = conn.createStatement();
												stmt.executeQuery("use library;");
												rs = stmt.executeQuery(sql);
												int bookAvailable;
												if(!rs.next()){
													bookAvailable = 0;
												}
												else{
													String bookAvailableString = rs.getString(1);
													bookAvailable = (new Integer(bookAvailableString)).intValue();
												}
												//System.out.println("Book Available: " + bookAvailable);
												rs.close();
												conn.close();
												if(bookAvailable == 0){
													JOptionPane.showMessageDialog(null, "There's no book available in this branch!!");
												}
												else{
													// Current Date and Due Date
													SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
													Calendar dateOut = Calendar.getInstance();
													Date out = dateOut.getTime();
													dateOut.set(Calendar.DATE, (dateOut.get(Calendar.DATE) + 14));
													Date due = dateOut.getTime();
													String outString = dateFormat.format(out).toString();
													String dueString = dateFormat.format(due).toString();
													
													sqlBuffer = new StringBuffer();
													sqlBuffer.append("Insert Into Book_Loans (book_id, branch_id, card_no, date_out, due_date)");
													sqlBuffer.append(" Values ('" + bookID + "', '" + branchID + "', '" + cardNo + "', '" + outString + "', '" + dueString + "');");
													sql = sqlBuffer.toString();
													//System.out.println(sql);
													conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
													stmt = conn.createStatement();
													stmt.executeQuery("use library;");
													stmt.executeUpdate(sql);
													conn.close();
													dispose();
												}
											}
										}
									}
								}
							} catch(SQLException ex){
								ex.printStackTrace();
							}
						}
					}
				});
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
				pack();
				setVisible(true);
				break;
			}
			case "Check In":{
				this.setLayout(new BorderLayout());
				JButton searchButton = new JButton("Search");
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new GridLayout(5,1,5,5));
				inputPanel.add(bookIDLabel);
				inputPanel.add(bookIDField);
				inputPanel.add(cardNoLabel);
				inputPanel.add(cardNoField);
				inputPanel.add(borrowerLabel);
				inputPanel.add(borrowerField);
				inputPanel.add(searchButton);
				//add(okButton);
				inputPanel.add(cancelButton);
				add(inputPanel, BorderLayout.NORTH);
				add(tablePanel, BorderLayout.CENTER);
				searchButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(((JButton)e.getSource()).getText().equals("Search")){
							String bookID = bookIDField.getText();
							String borrower = borrowerField.getText();
							String cardNo = cardNoField.getText();
							if(bookID.length() == 0 && cardNo.length() == 0 && borrower.length() == 0){
								JOptionPane.showMessageDialog(null, "Must Fill these fields!!");
							}
							else{
								try{
									Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									Statement stmt = conn.createStatement();
									StringBuffer sqlBuffer = new StringBuffer();
									String sql = new String();
									if(cardNo.length() == 0){
										if(borrower.length() == 0){
											sqlBuffer.append("Select * From Book_loans");
											sqlBuffer.append(" Where book_id='" + bookID + "';");
										}
										else{
											if(borrower.contains(" ")){
												String[] borrowerArrays = borrower.split(" ");
												String lName = borrowerArrays[1];
												String fName = borrowerArrays[0];
												sqlBuffer.append("Select Card_No From Borrower");
												sqlBuffer.append(" Where fname='" + fName + "'");
												sqlBuffer.append(" AND lname='" + lName + "';");
											}
											else{
												sqlBuffer.append("Select Card_No From Borrower");
												sqlBuffer.append(" Where fname='" + borrower + "'");
												sqlBuffer.append(" OR lname='" + borrower + "';");
											}
											sql = sqlBuffer.toString();
											conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
											stmt = conn.createStatement();
											stmt.executeQuery("use library;");
											ResultSet rs = stmt.executeQuery(sql);
											int count = 1;
											sqlBuffer = new StringBuffer();
											sqlBuffer.append("Select * From book_loans");
											while(rs.next()){
												cardNo = rs.getString(1);
												System.out.println(cardNo);
												if(count == 1){
													if(bookID.length() != 0){
														sqlBuffer.append(" Where book_id='" + bookID + "'");
														sqlBuffer.append(" AND (card_no='" + cardNo + "'");
													}
													else{
														sqlBuffer.append(" Where (card_no='" + cardNo + "'");
													}
													
												}
												else{
													sqlBuffer.append(" or card_no='" + cardNo + "'");
												}
												count++;
											}
											rs.close();
											conn.close();
											if(count == 1){
												sqlBuffer = new StringBuffer();
												JOptionPane.showMessageDialog(null, "No such Borrowers!!");
												dispose();
											}
											else{
												sqlBuffer.append(");");
											}
										}
									}
									else{
										sqlBuffer.append("Select * From Book_loans");
										if(bookID.length() != 0){
											sqlBuffer.append(" Where book_id='" + bookID + "'");
											sqlBuffer.append(" AND cardNo='" + cardNo + "';");
										}
										else{
											sqlBuffer.append(" Where cardNo='" + cardNo + "';");
										}
									}
									//System.out.println(sqlBuffer);
									sql = sqlBuffer.toString();
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									ResultSet rs = stmt.executeQuery(sql);
									Vector<Vector<String>> result = new Vector<Vector<String>>();
									Vector<String> tempResult = new Vector<String>();
									if(rs.next()){
										rs.previous();
										Vector<String >columnName = new Vector<String>();
										columnName.add("BookID");
										columnName.add("BranchID");
										columnName.add("CardNo");
										columnName.add("DateOut");
										columnName.add("DueDate");
										while(rs.next()){
											String book_id = rs.getString(1);
											String branch_id = rs.getString(2);
											String cardNumber = rs.getString(3);
											String dateOut = rs.getString(4);
											String dueDate = rs.getString(5);
											
											tempResult = new Vector<String>(Arrays.asList(book_id, branch_id, cardNumber, dateOut, dueDate));
											System.out.println(tempResult);
											result.add(tempResult);
										}
										resultTable = new JTable(result, columnName);
										Dimension d = getSize();
										resultTable.setPreferredScrollableViewportSize(new Dimension(d.width, 150));
										resultTable.setRowSelectionAllowed(true);
										resultTable.setFillsViewportHeight(true);
										resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
										JScrollPane tableScrollPane = new JScrollPane(resultTable);
										tablePanel.removeAll();
										tablePanel.add(tableScrollPane);
										tablePanel.validate();
										//tablePanel.validate();
										pack();
										repaint();
										
										rs.close();
										conn.close();
										((JButton)e.getSource()).setText("OK");
									}
									else{
										JOptionPane.showMessageDialog(null, "There's no such record!! Please check again!!");
									}
								} catch(SQLException ex){
									ex.printStackTrace();
								}	
							}
							//dispose();
						}
						else{
							if(((JButton)e.getSource()).getText().equals("OK")){
								int selected = resultTable.getSelectedRow();
								String bookID;
								String branchID;
								String cardNo;
								String dateOutString;
								String dueDateString;
								System.out.println(selected);
								if(selected < 0){
									selected = 0;
								}
								else{
									
								}
								bookID = (String)resultTable.getValueAt(selected, 0);
								branchID = (String)resultTable.getValueAt(selected, 1);
								cardNo = (String)resultTable.getValueAt(selected, 2);
								dateOutString = (String)resultTable.getValueAt(selected, 3);
								dueDateString = (String)resultTable.getValueAt(selected, 4);
								StringBuffer sqlBuffer = new StringBuffer();
								sqlBuffer.append("Delete from book_loans");
								sqlBuffer.append(" where book_id='" + bookID +"'");
								sqlBuffer.append(" AND branch_id='" + branchID + "'");
								sqlBuffer.append(" AND card_no='" + cardNo + "'");
								sqlBuffer.append(" AND date_out='" + dateOutString + "'");
								sqlBuffer.append(" AND due_date='" + dueDateString + "';");
								String sql = sqlBuffer.toString();
								try{
									Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									Statement stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									stmt.executeUpdate(sql);
									conn.close();
									dispose();
								} catch(SQLException ex){
									ex.printStackTrace();
								}
							}
							else{
								
							}
						}
					}
				});
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
				pack();
				setVisible(true);
				break;
			}
			case "Delete Borrower":{
				this.setLayout(new FlowLayout());
				add(cardNoLabel);
				add(cardNoField);
				add(okButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String cardNo = cardNoField.getText();
						StringBuffer sqlBuffer = new StringBuffer();
						sqlBuffer.append("Select * From Book_loans");
						sqlBuffer.append(" Where card_no='" + cardNo + "';");
						String sql = sqlBuffer.toString();
						try{
							Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
							Statement stmt = conn.createStatement();
							stmt.executeQuery("use library;");
							ResultSet rs = stmt.executeQuery(sql);
							if(rs.next()){
								JOptionPane.showMessageDialog(null, "This person needs to return books he borrowed!");
							}
							else{
								rs.close();
								conn.close();
								sqlBuffer = new StringBuffer();
								sqlBuffer.append("Delete From borrower Where card_no='" + cardNo +  "';");
								sql = sqlBuffer.toString();
								conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								stmt = conn.createStatement();
								stmt.executeQuery("use library;");
								stmt.executeUpdate(sql);
								conn.close();
								dispose();
							}
						} catch(SQLException ex){
							ex.printStackTrace();
						}
					}
				});
				pack();
				setVisible(true);
				break;
			}
			case "Update Borrower":{
				this.setLayout(new GridLayout(4,1,5,5));
				add(cardNoLabel);
				add(cardNoField);
				add(addressLabel);
				add(addressField);
				add(phoneLabel);
				add(phoneField);
				add(okButton);
				add(cancelButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String cardNo = cardNoField.getText();
						String address = addressField.getText();
						String phone = phoneField.getText();
						if(cardNo.length() == 0 || (address.length() == 0 && phone.length() == 0)){
							JOptionPane.showMessageDialog(null, "Must fill the Card No and any other fields!");
						}
						else{
							StringBuffer sqlBuffer = new StringBuffer();
							String sql;
							sqlBuffer.append("select * from borrower where card_no='" + cardNo + "';");
							sql = sqlBuffer.toString();
							try{
								Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								Statement stmt = conn.createStatement();
								stmt.executeQuery("use library;");
								ResultSet rs = stmt.executeQuery(sql);
								if(!rs.next()){
									rs.close();
									conn.close();
									JOptionPane.showMessageDialog(null, "No such borrower!");
								}
								else{
									rs.close();
									conn.close();
									sqlBuffer = new StringBuffer();
									sqlBuffer.append("Update borrower Set");
									if(address.length() != 0){
										sqlBuffer.append(" Address='" + address + "'");
										if(phone.length() != 0){
											sqlBuffer.append(",Phone='" + phone + "'");
										}
										else{
											
										}
									}
									else{
										if(phone.length() != 0){
											sqlBuffer.append(" Phone='" + phone + "'");
										}
										else{
											
										}
									}
									sqlBuffer.append(" Where card_no='" + cardNo + "';");
									sql = sqlBuffer.toString();
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									stmt.executeUpdate(sql);
									conn.close();
								}								
							} catch(SQLException ex){
								ex.printStackTrace();
							}
							dispose();
						}
					}
				});
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
				pack();
				setVisible(true);
				break;
			}
			case "Add Book":{
				this.setLayout(new GridLayout(6,1,5,5));
				add(bookIDLabel);
				add(bookIDField);
				add(titleLabel);
				add(titleField);
				add(authorLabel);
				add(authorField);
				add(branchIDLabel);
				add(branchIDField);
				add(copiesLabel);
				add(copiesField);
				add(okButton);
				add(cancelButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String bookID = bookIDField.getText();
						String title = titleField.getText();
						String author = authorField.getText();
						String branchID = branchIDField.getText();
						String copies = copiesField.getText();
						if(bookID.length() == 0 || title.length() == 0 || author.length() == 0 || branchID.length() == 0 || copies.length() == 0){
							JOptionPane.showMessageDialog(null, "Must fill all the fields!");
						}
						else{
							StringBuffer sqlBuffer = new StringBuffer();
							try{
								sqlBuffer.append("select * from book where book_id='" + bookID +"';");
								String sql = sqlBuffer.toString();
								Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
								Statement stmt = conn.createStatement();
								stmt.executeQuery("use library;");
								ResultSet rs = stmt.executeQuery(sql);
								if(rs.next()){
									rs.close();
									conn.close();
									JOptionPane.showMessageDialog(null, "Duplicate Book ID!");
								}
								else{
									rs.close();
									conn.close();
									sqlBuffer = new StringBuffer();
									sqlBuffer.append("select * from library_branch where branch_id='" + branchID + "';");
									sql = sqlBuffer.toString();
									conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
									stmt = conn.createStatement();
									stmt.executeQuery("use library;");
									rs = stmt.executeQuery(sql);
									if(!rs.next()){
										rs.close();
										conn.close();
										JOptionPane.showMessageDialog(null, "There's no such branch!");
									}
									else{
										rs.close();
										conn.close();
										sqlBuffer = new StringBuffer();
										sqlBuffer.append("Select * From book_authors Where book_id='" + bookID + "' AND author_name='" + author + "';");
										sql = sqlBuffer.toString();
										conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
										stmt = conn.createStatement();
										stmt.executeQuery("use library;");
										rs = stmt.executeQuery(sql);
										if(rs.next()){
											rs.close();
											conn.close();
											JOptionPane.showMessageDialog(null, "Duplicate Book ID and Author!");
										}
										else{
											rs.close();
											conn.close();
											sqlBuffer = new StringBuffer();
											sqlBuffer.append("Insert book VALUES ('" + bookID + "', '" + title + "');");
											sql = sqlBuffer.toString();
											conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
											stmt = conn.createStatement();
											stmt.executeQuery("use library;");
											stmt.executeUpdate(sql);
											conn.close();
											sqlBuffer = new StringBuffer();
											sqlBuffer.append("Insert book_authors VALUES ('" + bookID + "', '" + author + "');");
											sql = sqlBuffer.toString();
											conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
											stmt = conn.createStatement();
											stmt.executeQuery("use library;");
											stmt.executeUpdate(sql);
											conn.close();
											sqlBuffer = new StringBuffer();
											sqlBuffer.append("Insert book_copies VALUES ('" + bookID + "', '" + branchID + "', '" + copies + "');");
											sql = sqlBuffer.toString();
											conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "GLOBEFISH@110205");
											stmt = conn.createStatement();
											stmt.executeQuery("use library;");
											stmt.executeUpdate(sql);
											conn.close();
											dispose();
										}
									}
								}
							} catch(SQLException ex){
								ex.printStackTrace();
							}
						}
					}
				});
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
				pack();
				setVisible(true);
				break;
			}
			default:{
				JOptionPane.showMessageDialog(null, "Wrong Dialog Message!");
			}
		}
		
	}
}
