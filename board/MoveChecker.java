package chess_tings.board;

import chess_tings.Team;
import chess_tings.piece.*;
import java.util.ArrayList;

public class MoveChecker {
    
    private Board board;
    private Move move;
    private ArrayList<Piece> attackingPieces;
    
    // NOTES
    // try to merge getPieces and getAllPieces
    
    public MoveChecker(Board b) {
        this.board = b;
        this.move = new Move(this.board);
        this.attackingPieces = new ArrayList();
    }
    
    /*
    Will return all the teams pieces that have at least
    one possible move
    */
    public ArrayList<Piece> getPieces(Team t) {
        
        ArrayList<Piece> pieces = new ArrayList();
        Piece p;
        Position pos;
        
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                // get piece from position i,j
                pos = new Position(i,j);
                p = this.board.getPiece(pos);
                
                // if spot has a piece from the same team
                if (p != null && p.getTeam() == t) {
                    if (getMoves(pos,t).size() > 0) {
                        // if piece has at least 1 move
                        // add it to list
                        pieces.add(p);
                    }
                }
                
            }
        }
        
        return pieces;
        
    }
    
    /*
    Returns all pieces available for the team
    It doesnt check if piece has possible moves
    So if piece is still alive it will get on the list
    */
    public ArrayList<Piece> getAllPieces(Team t) {
        
        ArrayList<Piece> pieces = new ArrayList();
        Piece p;
        Position pos;
        
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                // get piece from position i,j
                pos = new Position(i,j);
                p = this.board.getPiece(pos);
                
                // if spot has a piece from the same team
                if (p != null && p.getTeam() == t) {
                    pieces.add(p);
                }
                
            }
        }
        
        return pieces;
        
    }
    
    /*
    Returns all the possible moves for the
    selected piece
    */
    public ArrayList<Position> getMoves(Position pos, Team t) {
        
        Piece p = this.board.getPiece(pos);
        
        // find out where this is crashing and fix it
        // remove this if statement
        if (p == null) return new ArrayList();
        return p.getMoves(this.board);
        
    }
    
    /*
    Checks all possible moves for piece
    if move puts king in check it doesnt
    add it to the list
    */
    public ArrayList<Position> getPossibleMoves(Position selected, Team t) {
        
        // get selected pieces info
        Piece p = this.board.getPiece(selected);
        int row = selected.getRow();
        int col = selected.getCol();
        
        // try to get rid of this if statement
        if (p == null) return new ArrayList();
        
        ArrayList<Position> possibleMoves = p.getMoves(this.board);
        // list to hold acceptable moves
        ArrayList<Position> newMoves = new ArrayList();
        
        // checks if the pieces move can kill
        // the king
        int[] king = getKing(t);
        Piece p2;
        for (Position pos: possibleMoves) {
            p2 = this.board.getPiece(pos);
            // NOTE: MAKE SURE THAT WHEN YOU DO MOVE
            // AND UNDO MOVE, YOU UPDATE BOARD, MOVE,
            // THE PIECE YOU HAVE NOW, AND EVERYTHING
            // THAT HAS TO DO WITH THE PIECE
            if (this.move.doMove(selected, pos, t, 1)) {
                
                // if move doesnt put king in check add it to new list
                if (!isCheck(t)) {
                    newMoves.add(pos);
                }
                // undo move
                this.move.undoMove(p, p2, row, col, pos.getRow(), pos.getCol());
                p.setNewPosition(new Position(row,col));
            }
        }
        
        return newMoves;
        
    }
    
    /*
    Returns all possible moves for king
    if the move puts king in check it doesnt
    take it
    */
    public ArrayList<Position> getKingMoves(Team t) {
        
        int[] king = getKing(t);
        Position you = new Position(king[0],king[1]);
        Piece p = this.board.getPiece(you);
        
        ArrayList<Position> possibleMoves = p.getMoves(this.board);
        ArrayList<Position> newMoves = new ArrayList();
        
        Piece p2;
        int row,col;
        for (Position pos: possibleMoves) {
            p2 = this.board.getPiece(pos);
            
            if (this.move.doMove(you, pos, t, 1)) {
                row = this.board.getPiece(pos).getPosition().getRow();
                col = this.board.getPiece(pos).getPosition().getCol();
                if (!isCheck(t)) newMoves.add(pos);
                p.setNewPosition(you);
                this.move.undoMove(p,p2,king[0],king[1],row,col);
            }
            
        }
        
        return newMoves;
        
    }
    
    /*
    Checks is any opponent piece can kill the king
    and adds all attacking pieces to a list
    */
    public boolean isCheck(Team t) {
        
        int[] king = getKing(t);
        this.attackingPieces.clear();
        
        ArrayList<Piece> opponent = getKingOpponent(t,king[0],king[1]);
        Board b = this.board;
        Team t2;
        if (t == Team.B) t2 = Team.W;
        else t2 = Team.B;
        
        // add knights is can kill king
        ArrayList<Position> moves;
        for (Piece p: getPieces(t2)) {
            if (p.getType().equals("Knight")) {
                for (Position pos: getMoves(p.getPosition(),t2)) {
                    // if knights move can kill king add to list
                    if (pos.getRow() == king[0] && pos.getCol() == king[1]) {
                        this.attackingPieces.add(p);
                        break;
                    }
                }
            }
        }
        
        // check if opponent piece can kill king
        for (Piece p: opponent) {
            for (Position pos: getMoves(p.getPosition(),t2)) {
                if (pos.getRow() == king[0] && pos.getCol() == king[1]) {
                    this.attackingPieces.add(p);
                    break;
                }
            }
        }
        
        return this.attackingPieces.size() > 0;
        
    }
    
    public int kingMoveSize(Team t) {
        return getKingMoves(t).size();
    }
    
    /*
    Returns the kings position
    */
    private int[] getKing(Team t) {
        
        int[] king = new int[2];
        for (Piece p : getAllPieces(t)) {
            if (p.getType().equals("King")) {
                king[0] = p.getPosition().getRow();
                king[1] = p.getPosition().getCol();
                break;
            }
        }
        
        return king;
        
    }
    
    /*
    Goes through row, col, and diagonals of king
    and gets the first opponent piece thats there
    */
    private ArrayList<Piece> getKingOpponent(Team t, int r, int c) {
        
        ArrayList<Piece> piece = new ArrayList();
        Board b = this.board;
        Piece p;
        
        // look up
        for (int i = r; i >= 0; i--) {
            p = b.getPiece(new Position(i,c));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
        }
        
        // look down
        for (int i = r; i < b.size(); i++) {
            p = b.getPiece(new Position(i,c));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
        }
        
        // look left
        for (int i = c; i >= 0; i--) {
            p = b.getPiece(new Position(c,i));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
        }
        
        // look right
        for (int i = c; i < b.size(); i++) {
            p = b.getPiece(new Position(c,i));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
        }
        
        int row = r;
        int col = c;
        // up left
        while (row >= 0 && col >= 0) {
            p = b.getPiece(new Position(row,col));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
            row--;
            col--;
        }
        
        // up right
        row = r;
        col = c;
        while (row >= 0 && col < b.size()) {
            p = b.getPiece(new Position(row,col));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
            row--;
            col++;
        }
        
        // down left
        row = r;
        col = c;
        while (row < b.size() && col >= 0) {
            p = b.getPiece(new Position(row,col));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
            row++;
            col--;
        }
        
        // down right
        row = r;
        col = c;
        while (row < b.size() && col < b.size()) {
            p = b.getPiece(new Position(row,col));
            if (p != null && p.getTeam() != t) {
                piece.add(p);
                break;
            }
            row++;
            col++;
        }
        
        return piece;
        
    }
    
    public ArrayList<Piece> getAttackPieces() {
        return this.attackingPieces;
    }
    
}
