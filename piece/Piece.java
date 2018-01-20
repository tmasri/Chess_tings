package chess_tings.piece;

import chess_tings.board.*;
import chess_tings.Team;
import java.util.ArrayList;

public abstract class Piece {
    
    protected Position position;
    protected Team team;
    
    public Piece(Position pos, Team t) {
        this.position = pos;
        this.team = t;
    }
    
    public Piece(Position pos) {
        this.position = pos;
    }
    
    public void printPosition(){
        this.position.printPosition();
    }
    protected boolean isTeammate(Piece p) {
        if (p.getTeam() == this.team) return true;
        return false;
    }
    
    public void setNewPosition(Position p) {
        this.position.setRow(p.getRow());
        this.position.setCol(p.getCol());
    }
    
    public Team getTeam() {
        return this.team;
    }
    
    public abstract ArrayList<Position> getMoves(Board board);
    protected abstract int getMove(int i, int j);
    public abstract String getType();
    public abstract Position getPosition();
    public abstract int getValue();
    public abstract boolean hasMoved();
    public abstract void moved();
    public abstract int getEval();
    
    protected boolean isInBounds(int row, int col) {
        Position p = this.position;
        if (p.getRow() + row >= 0 && p.getRow() + row <= 7)
            if (p.getCol() + col >= 0 && p.getCol() + col <= 7) return true;
        return false;
    }
    
    protected Position getNewPosition(int row, int col) {
        Position p = new Position(0,0);
        // set new position
        p.setRow(this.position.getRow() + row);
        p.setCol(this.position.getCol() + col);
        
        return p;
        
    }
    
}
