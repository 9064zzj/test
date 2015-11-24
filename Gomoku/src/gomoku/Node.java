/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.*;

public class Node {
    public Move move;  //possible move
    public List<Node> children;
    public Node parent;
    public GomokuState state;
    public int color;
    public boolean Leaf = false;
    public List<Strategy> StrategyBase;
    
    public Node(Move move, GomokuState state){
        this.move = move;
        this.state = state;
        children = new ArrayList<>();
    }
    
    public void switchColor(){
        if(color == GomokuState.BLACK)
            color = GomokuState.WHITE;
        else if(color == GomokuState.BLACK)
            color = GomokuState.BLACK;
    }
    
    public void calcScore(List<Strategy> strats) { //int player
        //System.out.println("CHECKING IF PRESENT FOR " + (color == GomokuState.BLACK ? "BLACK" : "WHITE"));
    	for (Strategy strat : strats) {
            if (strat instanceof StrategyWin && strat.isPresent(move, state, color)) { //switched owner to player
                move.score = strat.getValue();
                Leaf =true;;
                return;
            }

            if (strat.isPresent(move, state, color)) { 
                move.score = strat.getValue();
            }
        }
    }
    
    public void executeMove() {
        state.setPiece(move.row, move.col,color);
    }
}
