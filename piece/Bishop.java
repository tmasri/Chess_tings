package chess_tings.piece;

import chess_tings.Team;
import chess_tings.board.Board;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    private final static int[][] MOVES = {
                                            // \/ >
                                            {1,1}, {2,2}, {3,3}, {4,4}, {5,5}, {6,6}, {7,7},
                                            // ^ <
                                            {-1,-1}, {-2,-2}, {-3,-3}, {-4,-4}, {-5,-5}, {-6,-6}, {-7,-7},
                                            // ^ >
                                            {-1,1}, {-2,2}, {-3,3}, {-4,4}, {-5,5}, {-6,6}, {-7,7},
                                            // \/ <
                                            {1,-1}, {2,-2}, {3,-3}, {4,-4}, {5,-5}, {6,-6}, {7,-7},
                                        };
    
    private final static int value = 40;
    private final static int eval = 10;
    
    public Bishop(Position pos, Team t) {
        super(pos, t);
    }

    /*
    can move diagonally any number of moves
    it wants
    */
    @Override
    public ArrayList<Position> getMoves(Board board) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Position p;
        
        for (int i = 0; i < MOVES.length; i++) {
            if (isInBounds(getMove(i,0), getMove(i,1))) {
                p = getNewPosition(getMove(i,0), getMove(i,1));
                if (!board.isOccupied(p)) {
                    moves.add(p);
                } else {
                    if (!isTeammate(board.getPiece(p))) {
                       moves.add(p);
                       i += (6 - (i%7));
                    } else if (isTeammate(board.getPiece(p))) {
                       i += (6 - (i%7));
                    }
                }
            } else
                i += (6 - (i%7));
        }
        
        return moves;
        
    }

    @Override
    protected int getMove(int i, int j) {
        return MOVES[i][j];
    }

    @Override
    public String getType() {
        return "Bishop";
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
