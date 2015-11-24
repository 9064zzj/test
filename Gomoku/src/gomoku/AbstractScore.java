/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public abstract class AbstractScore {
    private boolean nConnect(int n, GomokuState state, Move move,int color, BoardState Bs){  //check if there are n connecting pieces
        if(n<2 || n>5 || state.getPiece(move)!=state.NONE){    //unable to check state because invalid number or Can not play on this position
            return false;
        }
        
        int count = 0;
        boolean findConnect = false;
        
        for(int i=-n; i<=n; i++){
            if(Bs.getBoardState(i, state, move)==color){  //
                count++;
            }
            else if(i==0){ //the planned piece, count as connecting
                count++;
            }
            else if(count!=n){   //current color is not wantted, clear the count
                count=0;
            }
            //finished
            if(count==n){  //find the nConnectNumber
                findConnect=true;
                break;
            }
        }
        return findConnect;
    }
    
    /*
    private boolean nConnectWithHoles(int n, int holes, GomokuState state, Move move, int color, BoardState Bs) {
        if(n<2 || n>5 || holes<0 || holes>2 || state.getPiece(move)!=state.NONE){  //unable to check state because invalid number  hole can only be 0~2
          return false;
        }
        
        int countHoles =0;
        int count=0;
        boolean findConnect = false;
        
        for(int i=-n; i<=n; i++){
            if(Bs.getBoardState(i, state, move)==color){  //
                count++;
            }
            else if(i==0){
                count++;
            }
            else if(Bs.getBoardState(i, state, move)==state.NONE){
                countHoles++;
            }
            else if(count!=n){ // meet white piece, clear count
                countHoles = 0;
                count = 0;
            }
            //out of condition
            if(countHoles>holes){  //if too many holes, clear count
                countHoles = 0;
                count=0;
            }
            else if(countHoles+count == n){
                findConnect=true;
                break;
            }   
        }
        return findConnect;
     }
    */
    public boolean nConnectHorizontal(int n, GomokuState state, Move move, int color) {

        return nConnect(n, state, move, color, new BoardState() {
            @Override
            public int getBoardState(int counter, GomokuState state, Move move) {
                return state.getPiece(new Move(move.row,move.col+counter));     //we only check row here
            }
        });
    }
    
    public boolean nConnectVertical(int n, GomokuState state, Move move, int color) {

        return nConnect(n, state, move, color, new BoardState() {
            @Override
            public int getBoardState(int counter, GomokuState state, Move move) {
                return state.getPiece(new Move(move.row+counter,move.col));    //only check col here
            }
        });
    }
    
    public boolean nConnectDiagonal(int n, GomokuState state, Move move, int color) {

        BoardState TopLefttoBottomRight = new BoardState(){
            public int getBoardState(int counter, GomokuState state, Move move){
                return state.getPiece(new Move(move.row+counter,move.col+counter)); 
            }
        };
        
        BoardState BottomLefttoTopRight = new BoardState(){
            public int getBoardState(int counter, GomokuState state, Move move){
                return state.getPiece(new Move(move.row+counter,move.col-counter)); 
            }
        };
        
        return nConnect(n, state, move, color,TopLefttoBottomRight) || nConnect(n, state, move, color,BottomLefttoTopRight);
    }
    
    
    public boolean nConnectTLBRDiagonal(int n, GomokuState state, Move move, int color){
        return nConnect(n, state, move, color,new BoardState(){
            public int getBoardState(int counter, GomokuState state, Move move){
                return state.getPiece(new Move(move.row+counter,move.col+counter)); 
            }
        });
    }
    
    public boolean nConnectBLTRDiagonal(int n, GomokuState state, Move move, int color){
        return nConnect(n, state, move, color,new BoardState(){
            public int getBoardState(int counter, GomokuState state, Move move){
                return state.getPiece(new Move(move.row+counter,move.col-counter)); 
            }
        });
    }
    
    
//    public boolean nConncetedHorizontalWithHoles(int n, int holes, GomokuState state, Move move, int color) {
//       return nConnectWithHoles(n, holes, state, move, color, new BoardState() {
//
//           @Override
//           public int getBoardState(int counter, GomokuState state, Move move) {
//               return state.getPiece(new Move(move.row,move.col+counter));
//           }
//       });
//   }
    
//    public boolean nConncetedVerticalWithHoles(int n, int holes, GomokuState state, Move move, int color){
//        return nConnectWithHoles(n, holes, state, move, color, new BoardState() {
//
//           @Override
//           public int getBoardState(int counter, GomokuState state, Move move) {
//               return state.getPiece(new Move(move.row+counter,move.col));
//           }
//       });
//    }
    
//    public boolean nConncetedDiagonalWithHoles(int n, int holes, GomokuState state, Move move, int color){
//        BoardState TopLefttoBottomRight = new BoardState(){
//            public int getBoardState(int counter, GomokuState state, Move move){
//                return state.getPiece(new Move(move.row+counter,move.col+counter)); 
//            }
//        };
//        
//        BoardState BottomLefttoTopRight = new BoardState(){
//            public int getBoardState(int counter, GomokuState state, Move move){
//                return state.getPiece(new Move(move.row+counter,move.col-counter)); 
//            }
//        };
//        
//        return nConnectWithHoles(n, holes,state, move, color, TopLefttoBottomRight) || nConnectWithHoles(n, holes,state, move, color,BottomLefttoTopRight);
//    }
    
    private interface BoardState{
        int getBoardState(int counter, GomokuState state, Move move);
    }
}
