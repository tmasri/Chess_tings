package chess_tings.piece;

import chess_tings.Team;
import chess_tings.board.Board;
import java.util.ArrayList;

public class Rook extends Piece{

    private final static int[][] MOVES = {
                                            {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0},
                                            {-1,0}, {-2,0}, {-3,0}, {-4,0}, {-5,0}, {-6,0}, {-7,0},
                                            {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6}, {0,7},
                                            {0,-1}, {0,-2}, {0,-3}, {0,-4}, {0,-5}, {0,-6}, {0,-7}
                                        };
    
    private final static int value = 60;
    private final static int eval = 10;
    private boolean moved;
    
    public Rook(Position pos, Team t) {
        super(pos, t);
        this.moved = false;
    }

    /*
    rook does the same thing that queen does
    but only checks horizontal and vertical
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
    
    public boolean hasMoved() {
        return this.moved;
    }
    
    public void moved() {
        this.moved = true;
    }

    @Override
    public String getType() {
        return "Rook";
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
    public int getEval() {
        return eval;
    }
    
}
