/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author zhongjiezheng
 */


import javax.swing.SwingUtilities;

public class DataBase2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
              GUI gui = new GUI();
              gui.setVisible(true);
            }	
		});
	}

}

