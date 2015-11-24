/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 *
 * @author zhongjiezheng
 */
public class Panel extends JPanel{
    private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;

    private static int size = 19;
    private GomokuState state;
    
    Panel(){
        this(19);
    }
    
    Panel(int size){
        super();
	this.size = size;
	state = new GomokuState(size);
	addMouseListener(new GomokuListener());
    }
    
    class GomokuListener extends MouseAdapter 
    {
	public void mouseReleased(MouseEvent e) 
	{
	    double panelWidth = getWidth();
	    double panelHeight = getHeight();
	    double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
	    double squareWidth = boardWidth / size;
	    double pieceDiameter = PIECE_FRAC * squareWidth;
	    double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
	    double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
	    int col = (int) Math.round((e.getX() - xLeft) / squareWidth - 0.5);
	    int row = (int) Math.round((e.getY() - yTop) / squareWidth - 0.5);
	    if (row >= 0 && row < size && col >= 0 && col < size
		&& state.getPiece(new Move(row, col)) == GomokuState.NONE
		&& state.getWinner() == GomokuState.NONE) {
		state.playPiece(row, col, GomokuState.BLACK); 
		repaint();
		int winner = state.getWinner();
		if (winner != GomokuState.NONE)
		    JOptionPane.showMessageDialog(null,(winner == GomokuState.BLACK) ? "Black wins!" : "White wins!");
                else{
                    AI AIplayer = new AI(GomokuState.WHITE); 
                    Move aiMove = AIplayer.makeMove(state);
                    state.playPiece(aiMove.row, aiMove.col, GomokuState.WHITE);
                    repaint();
                    winner = state.getWinner();
		    if (winner != GomokuState.NONE)
		        JOptionPane.showMessageDialog(null,(winner == GomokuState.BLACK) ? "Black wins!" : "White wins!");
                }
	    }
	}    
    }
    
    public void paintComponent(Graphics g) 
    {
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	
	double panelWidth = getWidth();
	double panelHeight = getHeight();

	g2.setColor(new Color(0.925f, 0.670f, 0.34f)); // light wood
	g2.fill(new Rectangle2D.Double(0, 0, panelWidth, panelHeight));

	
	double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
	double squareWidth = boardWidth / size;
	double gridWidth = (size - 1) * squareWidth;
	double pieceDiameter = PIECE_FRAC * squareWidth;
	boardWidth -= pieceDiameter;
	double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
	double yTop = (panelHeight - boardWidth) / 2 + MARGIN;

	g2.setColor(Color.BLACK);
	for (int i = 0; i < size; i++) {
	    double offset = i * squareWidth;
	    g2.draw(new Line2D.Double(xLeft, yTop + offset, 
				      xLeft + gridWidth, yTop + offset));
	    g2.draw(new Line2D.Double(xLeft + offset, yTop,
				      xLeft + offset, yTop + gridWidth));
	}
	
	for (int row = 0; row < size; row++) 
	    for (int col = 0; col < size; col++) {
		int piece = state.getPiece(new Move(row, col));
		if (piece != GomokuState.NONE) {
		    Color c = (piece == GomokuState.BLACK) ? Color.BLACK : Color.WHITE;
		    g2.setColor(c);
		    double xCenter = xLeft + col * squareWidth;
		    double yCenter = yTop + row * squareWidth;
		    Ellipse2D.Double circle
			= new Ellipse2D.Double(xCenter - pieceDiameter / 2,
					       yCenter - pieceDiameter / 2,
					       pieceDiameter,
					       pieceDiameter);
		    g2.fill(circle);
		    g2.setColor(Color.black);
		    g2.draw(circle);
		}
	    }
    }


}
