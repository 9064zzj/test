/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public interface Strategy {
    
    boolean isPresent(Move move, GomokuState state, int color);
    
    int getValue();
}
