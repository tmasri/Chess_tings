package chess_tings.board;

import chess_tings.Team;
import chess_tings.piece.*;
import java.util.ArrayList;

public class Move {

    private Board board;
    private ArrayList<Piece> attackingPieces;

    public Move(Board board) {
        this.board = board;
        this.attackingPieces = new ArrayList();
    }
    
    /*
    this class is connected to board and
    piece, it updates the board and the pieces
    */

    /*
    gets all the pieces of the team that have at least
    1 move
    */
    public ArrayList<Piece> getPieces(Team t) {

        ArrayList<Piece> pieces = new ArrayList();
        Piece p;
        Position pos;
        // go through all the pieces for the team
        // and only return pieces that have at least 1 move
        int count = 0;
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                pos = new Position(i, j);
                p = this.board.getPiece(pos);
                // checks if piece is in team
                if (p != null && p.getTeam() == t) {
                    if (getMoves(pos, t).size() > 0) {
                        pieces.add(p);
                    }
                    count++;
                    if (count == this.board.getNum(t)) {
                        break;
                    }
                }
            }
        }

        return pieces;

    }
    
    /*
    helps with check and check mate so that if move
    puts the king in dager it wont take it
    */
    public ArrayList<Piece> getAllPieces(Team t) {

        ArrayList<Piece> pieces = new ArrayList();
        Piece p;
        Position pos;
        // go through all the pieces for the team
        // and only return pieces that have at least 1 move
        int count = 0;
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                pos = new Position(i, j);
                p = this.board.getPiece(pos);
                // checks if piece is in team
                if (p != null && p.getTeam() == t) {
                    pieces.add(p);
                    count++;
                    if (count == this.board.getNum(t)) {
                        break;
                    }
                }
            }
        }

        return pieces;

    }

    /*
    returns all possible moves for the piece
    */
    public ArrayList<Position> getMoves(Position selected, Team t) {
        
        Piece p = this.board.getPiece(selected);
        
        if (p == null) {
            return new ArrayList<Position>();
        }
        
        
        return p.getMoves(board);

    }
    
    /*
    accounts for isCheck so if contains a move
    that can put the king in danger it doesnt
    concider it
    */
    public ArrayList<Position> getPossibleMoves(Position selected, Team t) {
        
        Piece p = this.board.getPiece(selected);
        Piece p2;
        Position posP = p.getPosition();
        int posR = posP.getRow();
        int posC = posP.getCol();

        if (p == null) {
            return new ArrayList<Position>();
        }

        // get all possible moves for piece
        ArrayList<Position> possibleMoves = p.getMoves(board);
        ArrayList<Position> newMoves = new ArrayList<Position>();

        // check if any of the moves has the kings position
        int[] king = getKing(t);

        for (Position pos: possibleMoves) {
            // move the piece
            p2 = this.board.getPiece(pos);
            if (doMove(posP,pos,t,1)) {

                // if move doesnt put you in check add to new list
                if (!isCheck(t,king[0],king[1])) {
                    newMoves.add(pos);
                }
                // undo move
                undoMove(p,p2,posR,posC,pos.getRow(),pos.getCol());
                p.setNewPosition(new Position(posR,posC));
            }
        }

        return newMoves;

    }
    
    /*
    gets all the possible legal moves that the
    king can take without getting killed
    */
    public ArrayList<Position> getKingsMoves(Position selected, Team t) {
        
        Piece p = this.board.getPiece(selected);
        Position posP = p.getPosition();
        int posR = posP.getRow();
        int posC = posP.getCol();
        
        ArrayList<Position> possibleMoves = p.getMoves(board);
        ArrayList<Position> newMoves = new ArrayList<Position>();
        
        Piece p2;
        int newRow, newCol;
        for (Position pos: possibleMoves) {
            p2 = this.board.getPiece(pos);
            // do move
            if (doMove(selected,pos,t,1)) {
                newRow = this.board.getPiece(pos).getPosition().getRow();
                newCol = this.board.getPiece(pos).getPosition().getCol();
                if (!isCheck(t,newRow,newCol)) {
                    newMoves.add(pos);
                }
                p.setNewPosition(selected);
                undoMove(p,p2,posR,posC,pos.getRow(),pos.getCol());
            }
        }
        
        return newMoves;
        
    }

    /*
    moves the piece on the board, updates
    board and piece after move is done
    */
    public boolean doMove(Position selectedPos, Position newPos, Team t, int player) {

        Piece p = this.board.getPiece(selectedPos);
        Piece p2;
        boolean check = false;
        
        int sR = selectedPos.getRow();
        int sC = selectedPos.getCol();
        int nR = newPos.getRow();
        int nC = newPos.getCol();
        ArrayList<Position> moves = new ArrayList<Position>();

        if (p != null) {
            moves = p.getMoves(board);
        }
        for (int i = 0; i < moves.size(); i++) {
            // checks if valid move
            if (moves.get(i).equals(new Position(nR, nC))) {
                p2 = this.board.getPiece(new Position(nR, nC));
                if (p2 != null && p2.getTeam() != t) {
                    if (t == Team.W) {
                        this.board.setNum(Team.B, -1);
                    } else {
                        this.board.setNum(Team.W, -1);
                    }
                }
                
                p.setNewPosition(new Position(nR, nC));
                if (p.getType().equals("Pawn") && !p.hasMoved() && player == 0) p.moved();
                
                
                this.board.set(newPos.getRow(), newPos.getCol(), this.board.getPiece((new Position(sR, sC))));
                this.board.set((new Position(sR, sC)).getRow(), (new Position(sR, sC)).getCol(), null);
                check = true;
                break;
            }
        }

        return check;

    }
    
    /*
    undoes the move on the board
    */
    public void undoMove(Piece you, Piece them, int prevR, int prevC, int curR, int curC) {
        
        this.board.set(prevR, prevC, you);
        if (them != null) {
            int themR = them.getPosition().getRow();
            int themC = them.getPosition().getCol();
            this.board.set(themR, themC, them);
        } else {
            this.board.set(curR, curC, null);
        }
        
    }
    
    /*
    checks if there are any pieces that can kill the board
    at this boards instance
    */
    public boolean isCheck(Team t, int r, int c) {
        
        this.attackingPieces.clear();
        ArrayList<Piece> opponent = getKingOpponent(t,r,c);
        Board b = this.board;
        Team t2;
        if (t == Team.B) t2 = Team.W;
        else t2 = Team.B;
        
        // dont check this
        System.out.println("opponent size = " + opponent.size());
        for (Piece o: opponent) {
            System.out.println("Piece is "+o.getType());
        }
        System.out.println("");
        // dont check this
        
        ArrayList<Position> moves;
        for (Piece p: getPieces(t2)) {
            if (p.getType().equals("Knight")) {
                for (Position pos: getMoves(p.getPosition(),t2)) {
                    if (pos.getRow() == r && pos.getCol() == c) {
                        this.attackingPieces.add(p);
                        break;
                    }
                }
            }
        }
        
        for (Piece p: opponent) {
            // get all possible moves for the piece
            // check if piece can attack king
            for (Position pos: getMoves(p.getPosition(),t2)) {
                if (pos.getRow() == r && pos.getCol() == c) {
                    this.attackingPieces.add(p);
                    break;
                }
            }
        }
        
        if (this.attackingPieces.size() != 0) return true;
        return false;
        
    }
    
    /*
    returns the number of moves the king can do
    */
    public int kingMoveSize(Position selected, Team t) {
        return getKingsMoves(selected,t).size();
    }
    
    /*
    gets the row and column indecies of the king
    */
    public int[] getKing(Team t) {
        
        
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
    looks at vertical, hotizontal, and diagonal moves
    and gets the opponents pieces that are in those
    lines
    */
    private ArrayList<Piece> getKingOpponent(Team t, int r, int c) {
        
        ArrayList<Piece> piece = new ArrayList<Piece>();
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
    
    /*
    if pawn reaches the end of the board it can change to
    one of the pieces bellow
    */
    public void pawnPromotion(int i, int r, int c, Team t) {
        
        switch (i) {
            case 0:
                this.board.set(r, c, new Rook(new Position(r,c),t));
                break;
            
            case 1:
                this.board.set(r, c, new Knight(new Position(r,c),t));
                break;
                
            case 2:
                this.board.set(r, c, new Bishop(new Position(r,c),t));
                break;
                
            case 3:
                this.board.set(r, c, new Queen(new Position(r,c),t));
                break;
                
        }
        
    }
    
    /*
    gets the pieces that can attack the king
    */
    public ArrayList<Piece> getAttackPieces() {
        return this.attackingPieces;
    }

    public void printBoard() {
        this.board.printBoard();
    }

    public Board getBoard() {
        return this.board;
    }

}
