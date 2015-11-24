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

public class Tree {
    public Node root;
    public List<List<Node>> treeBody;
    public int depth;
    public List<Strategy> StrategyBase;
    
    public Tree(GomokuState state, int color, int depth, List<Strategy> StrategyBase){
        this.depth = depth;
        this.StrategyBase = StrategyBase;
        root = new Node(state.getLastMove(),state);
        root.color =color;
        root.switchColor(); //first step is made by human player
        //initial the 2 level tree
        treeBody = new ArrayList<List<Node>>();
        for(int i=0; i<3; i++){     //max depth of tree is 3 here
            treeBody.add(new ArrayList<Node>());
        }
        treeBody.get(0).add(root); //add root to 0 level
        
        for(int i=0; i<depth; i++){
            List<Node> curlevel = treeBody.get(i);   ///for child generate
            List<Node> nextlevel = treeBody.get(i+1);
            for(Node node : curlevel){
                nextlevel.addAll(generateChildren(node));
            }
        }
    }
    
//    public List<Node> getLevel(int level){
//        return treeBody.get(level);
//    }
    
    public ArrayList<Node> generateChildren(Node current){
        ArrayList<Node> children = new ArrayList<Node>();
        if(!current.Leaf){
            for(int y =0; y<19; y++){
                for(int x=0; x<19; x++){
                    if(check(new Move(y,x),current.state)){
                        GomokuState temp = cloneBoard(current.state);
                        Node child = new Node(new Move(x,y),temp);
                        child.parent = current;
                        child.color = current.color;
                        child.switchColor();
                        child.calcScore(StrategyBase);
                        child.executeMove();
                        current.children.add(child);
                        children.add(child);
                    }  
                }
            }
        }
        return children;
    }
    
    public boolean check(Move move, GomokuState state){
        int row = move.row;
        int col = move.col;
        boolean OK=false;
        
        for(int i=-1; i<1; i++){
            for(int j=-1; j<1; j++){
                int r = row + i;
                int c = col + j;
                if(!(i == 0 && j == 0) && r >= 0 && r < 19 && c >= 0 && c < 19 && (state.getPiece(new Move(r,c)) != GomokuState.NONE)) //no piece played away from existence piece
                     OK = true;
            }
        }
        
        
        return OK || state.getPiece(move) == GomokuState.NONE;
    }
    
    public GomokuState cloneBoard(GomokuState state) {
        GomokuState clone = new GomokuState(19);
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j <19; j++) {
                clone.setPiece(i, j, state.getPiece(new Move(i,j)));
            }
        }
        return clone;
    }
    
    
}
