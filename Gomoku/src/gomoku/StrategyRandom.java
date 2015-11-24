/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.Random;


public class StrategyRandom implements Strategy{
    Random generator = new Random();
    
    @Override
    public int getValue(){
        int r = generator.nextInt(3);
        if (r == 0) {
            return ScoreValue.ZERO;
        } else if (r == 1) {
            return ScoreValue.VERYLOW;
        } else {
            return ScoreValue.LOW;
        }
    }
    
    @Override
    public boolean isPresent(Move move, GomokuState model, int player) {
        return true;
    }
}
