/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class StrategyBlock extends AbstractScore implements Strategy{
    @Override
    public int getValue(){
        return ScoreValue.BLOCK;
    }
    
    @Override
    public boolean isPresent(final Move move, final GomokuState state, int color){
        int oppenont;
        if(color == GomokuState.BLACK)
            oppenont = GomokuState.WHITE;
        else
            oppenont = GomokuState.BLACK;
        //5 ? 4
        return  super.nConnectHorizontal(4, state, move, oppenont) ||
                super.nConnectVertical(4, state, move, oppenont) ||
                super.nConnectDiagonal(4, state, move, oppenont) ||
        	super.nConnectHorizontal(5, state, move, oppenont) ||
                super.nConnectVertical(5, state, move, oppenont) ||
                super.nConnectDiagonal(5, state, move, oppenont);
        
    }
}
