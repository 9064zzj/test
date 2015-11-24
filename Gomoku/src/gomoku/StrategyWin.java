/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class StrategyWin extends AbstractScore implements Strategy{
    @Override
    public int getValue() {
        return ScoreValue.WIN;
    }
    
    @Override
    public boolean isPresent(final Move move, final GomokuState state, int color){
        return super.nConnectDiagonal(5, state, move, color) ||
               super.nConnectHorizontal(5, state, move, color) ||
               super.nConnectVertical(5, state, move, color);
    }
}
