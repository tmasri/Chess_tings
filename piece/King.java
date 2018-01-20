package chess_tings.piece;

import chess_tings.Team;
import chess_tings.board.Board;
import java.util.ArrayList;

public class King extends Piece{

    private final static int[][] MOVES = {
                                            // DOWN
                                            {1,0}, {1,1}, {1,-1},
                                            // UP
                                            {-1,0}, {-1,1}, {-1,-1},
                                            // SIDE
                                            {0,1}, {0,-1}
                                        };
    
    private final static int value = 900;
    private final static int eval = 1;
    private boolean moved;
    
    public King(Position pos, Team t) {
        super(pos, t);
    }

    /*
    it can do the same thing as queen but can only
    move 1 step
    */
    @Override
    public ArrayList<Position> getMoves(Board board) {
        
        ArrayList<Position> moves = new ArrayList<Position>();
        Position p;
        
        for (int i = 0; i < MOVES.length; i++) {
            if (isInBounds(getMove(i,0),getMove(i,1))) {
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
        return "King";
    }
    
    public boolean hasMoved() {
        return this.moved;
    }
    
    public void moved() {
        this.moved = true;
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
