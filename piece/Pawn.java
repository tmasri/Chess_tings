package chess_tings.piece;

import chess_tings.board.*;
import chess_tings.Team;
import java.util.ArrayList;

public class Pawn extends Piece{

    protected final static int[][] MOVES = { 
                                            {1,0},
                                            {2,0},
                                            {-1,0},
                                            {-2,0}
                                        };
    protected final static int[][] KILL = {
                                            {1,1},
                                            {1,-1},
                                            {-1,1},
                                            {-1,-1}
                                        };
    
    private final static int value = 10;
    private final static int eval = 5;
    private boolean moved;
    
    public Pawn(Position pos, Team t) {
        super(pos,t);
        this.moved = false;
    }
    
    /*
    only moves one step ahead, if first move
    it can move 2 steps ahead, and if it has
    a kill it can only move 1 step forward diagonally
    */
    @Override
    public ArrayList<Position> getMoves(Board board) {
        
        ArrayList<Position> moves = new ArrayList<Position>();
        
        // check all the moves that are legal
        // and the moves that are not
        
        int start = 0;
        int end = 2;
        if (this.team == Team.W) {
            start = 2;
            end = 4;
        }
        
        Position p;
        for (int i = start; i < end; i++) {
            if (isInBounds(getKill(i,0), getKill(i, 1))) {
                p = getNewPosition(getKill(i,0), getKill(i,1));
                if (board.isOccupied(p) && !isTeammate(board.getPiece(p))) {
                    moves.add(p);
                }
            }
        }
        
        // check which team its on
        start = 0; // if Team.B start at 0
        end = 1; // if Team.B end at 3
        if (!this.moved) end = 2;
        if (this.team == Team.W) {
           start = 2;
           end = 3;
           if (!this.moved) end = 4;
        }
        
        boolean yes = false;
        for (int i = start; i < end; i++) {
//            if (end == 2 && i == end-1 || end == 4 && i == end-1) {
//                i--;
//                p = getNewPosition(getMove(i,0),getMove(i,1));
//                if (!board.isOccupied(p)) {
//                    yes = true;
//                }
//                i++;
//            } else yes = true;
//            if (yes) {
                if (isInBounds(getMove(i,0),getMove(i,1))) {
                    p = getNewPosition(getMove(i,0),getMove(i,1));
                    if (!board.isOccupied(p)) {
                        moves.add(p);
                    }
                }
//            }
            
        }
          
        return moves;
    }
    
    protected int getMove(int i, int j) {
        return MOVES[i][j];
    }
    
    protected int getKill(int i, int j) {
        return KILL[i][j];
    }
    
    public boolean hasMoved() {
        return this.moved;
    }
    
    public void moved() {
        this.moved = true;
    }

    @Override
    public String getType() {
        return "Pawn";
    }

    @Override
    public Position getPosition() {
        return this.position;
    }
    
    public int getValue() {
        return this.value;
    }

    @Override
    public int getEval() {
        return eval;
    }
    
}
