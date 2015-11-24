/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class GomokuState {
    public static final int NONE=0;
    public static final int BLACK=-1;
    public static final int WHITE=1;    ///human alwalys white
    public static final int ERROR=-65535;
    
    private int size;
    private int board[][];
    private enum direction{Diag, Hori, Vert};
    private int LastMoveRow;
    private int LastMoveCol;
    
    
    GomokuState(int size){
        this.size=size;
        board = new int[size][size];
    }
    
    public void setPiece(int row, int col, int color){
        board[row][col] = color;
    }
    
    public int getPiece(Move move){
        int row = move.row;
        int col = move.col;
        if(row<0 || col<0 || row>size-1 || col>size-1){
            return ERROR;
        }
        else{
            return board[row][col];
        }
    }
    
    boolean playPiece(int row, int col, int color){
        if(board[row][col]==NONE){
            board[row][col]=color;
            LastMoveRow=row;
            LastMoveCol=col;
            return true;
        }
        else
            return false;
    }
    
    int getWinner(){
//        System.out.println("LastMoveRow:"+LastMoveRow);
//        System.out.println("LastMoveCol:"+LastMoveCol);
//        System.out.println();
        
        
        int Diag = CheckLine(LastMoveRow,LastMoveCol,direction.Diag);
        int Hori = CheckLine(LastMoveRow,LastMoveCol,direction.Hori);
        int Vert = CheckLine(LastMoveRow,LastMoveCol,direction.Vert);
        
//        System.out.println("Diag Check:"+Diag);
//        System.out.println("Hori Check:"+Hori);
//        System.out.println("Vert Check:"+Vert);
//        System.out.println();
        
       if(Diag!=NONE)
           return Diag;
       else if(Hori!=NONE)
           return Hori;
       else if(Vert!=NONE)
           return Vert;
       else
           return NONE;
    }
    
    int CheckLine(int row, int col, direction dir){
        int Color = board[row][col];
        int DirX,DirY;
        int r,c;
        
        //System.out.println("Move Color:"+Color);
        //System.out.println("Move Color:"+dir);
        
        if(Color==NONE){
            //System.out.println("Error here");
            return NONE;
        }
        else{
            
            if(dir.equals(direction.Diag)){
                DirX=1;
                DirY=1;
            }
            else if(dir.equals(direction.Hori)){
                DirX=1;
                DirY=0;
            }
            else{
                DirX=0;
                DirY=1;
            }
        
            int pieces = 1;
            r = row + DirX;  // Look at square in specified direction.
            c = col + DirY;
        
            while(r >= 0 && r < size && c >= 0 && c < size && board[r][c]==Color){
                pieces++;
                r+=DirX;
                c+=DirY;
            }
            
            r = row - DirX;   // look in the opposite direction.
            c = col - DirY;
            while(r >= 0 && r < size && c >= 0 && c < size && board[r][c]==Color){
                pieces++;
                r-=DirX;
                c-=DirY;
                
            }
//            System.out.println("Pieces:"+pieces);
            if(pieces>4)
                return Color;
            else
                return NONE;
        }
    }
    
    public Move getLastMove(){
        return new Move(LastMoveRow,LastMoveCol);
    }
}
