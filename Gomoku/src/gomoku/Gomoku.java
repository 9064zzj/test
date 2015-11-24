/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;
import javax.swing.JFrame;
/**
 *
 * @author zhongjiezheng
 */
public class Gomoku {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int size = 19;
	if (args.length > 0)
	    size = Integer.parseInt(args[0]);

	JFrame frame = new JFrame();
	
	final int FRAME_WIDTH = 600;
	final int FRAME_HEIGHT = 650;
	frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	frame.setTitle("Gomoku");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	Panel panel = new Panel(size);
	frame.add(panel);
	
	frame.setVisible(true);
    }
}
