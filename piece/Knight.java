package chess_tings.piece;

import chess_tings.Team;
import chess_tings.board.Board;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private final static int[][] MOVES = {
                                            {1,2},
                                            {1,-2},
                                            {-1,2},
                                            {-1,-2},
                                            {2,1},
                                            {2,-1},
                                            {-2,1},
                                            {-2,-1}
                                        };
    
    private final static int value = 40;
    private final static int eval = 15;
    
    public Knight(Position pos, Team t) {
        super(pos, t);
    }

    /*
    moves 3 steps in 1 direction and 1 in another
    */
    @Override
    public ArrayList<Position> getMoves(Board board) {
        
        ArrayList<Position> moves = new ArrayList<Position>();
        Position p;
        
        for (int i = 0; i < MOVES.length; i++) {
            if (isInBounds(getMove(i,0), getMove(i,1))) {
                p = getNewPosition(getMove(i,0),getMove(i,1));
                if (!board.isOccupied(p)) {
                    moves.add(p);
                } else {
                    if (!isTeammate(board.getPiece(p)))
                        moves.add(p);
                }
            }
        }
        
        return moves;
        
    }

    @Override
    protected int getMove(int i, int j) {
        return MOVES[i][j];
    }

    @Override
    public String getType() {
        return "Knight";
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public boolean hasMoved() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moved() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getEval() {
        return eval;
    }
    
}
