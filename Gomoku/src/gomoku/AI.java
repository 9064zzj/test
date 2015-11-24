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

public class AI {
    private int color;
    private List<Strategy> StrategyBase;
    
    
    public AI(int color){
        this.color = color;
        StrategyBase = new ArrayList<>();
        StrategyBase.add(new StrategyWin());
        StrategyBase.add(new StrategyBlock());
        StrategyBase.add(new StrategyPositionMiddle());
        StrategyBase.add(new StrategyRandom());
        StrategyBase.add(new StrategyConnectPiece());
    }
    
    public Move makeMove(GomokuState state){
        return BestMove(state);
    }
    
    public Move BestMove(GomokuState state){
        Tree tree = new Tree(state,color,1 ,StrategyBase);
        int maxNode = 0;
        Move maxMove = tree.treeBody.get(1).get(0).move;
        for (int i = 0; i < tree.treeBody.get(1).size(); i++) {
            if (tree.treeBody.get(1).get(i).move.score > maxMove.score) { //CHANGED TO MAX
                maxMove = tree.treeBody.get(1).get(i).move;
                maxNode = i;
            }
        }
        
//        System.out.println("CHILDREN");
//        for (int i = 0; i < tree.getLevel(1).get(minNode).getChildren().size(); i++) {
//        	System.out.println(tree.getLevel(1).get(minNode).getChildren().get(i).move.toString());
//    	}
        
        return maxMove;
    }
    
}

