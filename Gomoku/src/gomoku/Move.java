/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class Move {
    public int row;
    public int col;
    public int score;
    public int COLOR;
    
    Move(int row, int col){
        this.col = col;
        this.row = row;
    }
    
    Move(int row, int col, int score){
        this.col = col;
        this.row = row;
        this.score = score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }
    
}
