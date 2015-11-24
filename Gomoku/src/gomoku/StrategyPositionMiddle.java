/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class StrategyPositionMiddle implements Strategy {
    int value = 0;
    
    @Override
    public int getValue(){
        int FinalValue = value;
        value=0; //reset
        return FinalValue;
    }
    
    @Override
    public boolean isPresent(final Move move, final GomokuState state, int color){
        if ((move.row >= 7 && move.row <= 11) && (move.col >= 7 && move.col <= 11)) {
            value = ScoreValue.MEDIUM;
        } else if ((move.row >= 5 && move.row <= 13) && (move.col >= 5 && move.col <= 13)) {
            value = ScoreValue.LOW;
        } else if ((move.row >= 3 && move.row <= 15) && (move.col >= 3 && move.col <= 15)) {
            value = ScoreValue.VERYLOW;
        } else {
            value = ScoreValue.ZERO;
        }
        return true;
    }
    
//    private void calculateValue(final Move move, GomokuState state){
//        if ((move.row >= 7 && move.row <= 11) && (move.col >= 7 && move.col <= 11)) {
//            value = ScoreValue.MEDIUM;
//        } else if ((move.row >= 5 && move.row <= 13) && (move.col >= 5 && move.col <= 13)) {
//            value = ScoreValue.LOW;
//        } else if ((move.row >= 3 && move.row <= 15) && (move.col >= 3 && move.col <= 15)) {
//            value = ScoreValue.VERYLOW;
//        } else {
//            value = ScoreValue.ZERO;
//        }
//    }
}
