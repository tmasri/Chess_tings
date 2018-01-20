package chess_tings.board;

import chess_tings.Team;
import chess_tings.piece.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    
    private Position selectedPiece, newSpot;
    private Move move;
    private Board board;
    private ArrayList<Position> moves;
    private Scanner in;
    
    /*
    this class connects the game to the board,
    any kind of information that the game wants
    to get from the board or the piece will be
    taken from this class
    */
    
    public Controller() {
        this.board = new Board();
        this.move = new Move(this.board);
    }
    
    public void printBoard() {
        this.board.printBoard();
    }
    
    // sets selested piece to the piece that was
    // selected by the player
    public String parsePiece(String line) {
        
        selectedPiece = new Position(0,0);
        
        this.selectedPiece.setRow(readRow(line,0));
        this.selectedPiece.setCol(readCol(line,0));
        if (this.board.getPiece(this.selectedPiece) == null)
            return "";
        
        return this.board.getPiece(this.selectedPiece).getType();
        
    }
    
    // sets position from and to
    // that the player chose
    public boolean parse(String line) {
        
        newSpot = new Position(0,0);
        if (!line.equals("")) {
            this.selectedPiece.setRow(readRow(line,0));
            this.selectedPiece.setCol(readCol(line,0));
            if (this.board.getPiece(this.selectedPiece) != null) {
                this.newSpot.setRow(readRow(line,1));
                this.newSpot.setCol(readCol(line,1));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
        
    }
    
    // calls isCheck from move
    // to check if king is in danger
    public boolean isCheck(Team t) {
        
        chooseKing(t);
        int r = this.selectedPiece.getRow();
        int c = this.selectedPiece.getCol();
        
        return this.move.isCheck(t,r,c);
        
    }
    
    /*
    checks if the game is over
    */
    public boolean isCheckMate(Team t) {
        
        chooseKing(t);
        if (this.move.kingMoveSize(this.selectedPiece, t) == 0) return true;
        return false;
        
    }
    
    /*
    checks if no piece from the team can do any move
    and whatever move king does gets it into checkmate
    */
    public boolean isStaleMate(Team t) {
        
        // if king has at least 1 move
        // but has no legal moves
        
        ArrayList<Piece> allPieces = this.move.getAllPieces(t);
        Position position;
        int numberOfMoves = 0;
        for (Piece p: allPieces) {
            if (!p.getType().equals("King")) {
                position = p.getPosition();
                numberOfMoves += this.move.getPossibleMoves(position, t).size();
            }
        }
        // check king
        chooseKing(t);
        int moves = this.move.getMoves(this.selectedPiece, t).size();
        int legalMoves = this.move.kingMoveSize(this.selectedPiece, t);
        if (numberOfMoves == 0 && moves != legalMoves) return true;
        return false;
        
    }
    
    public void getAttackPieces() {
        
        ArrayList<Piece> piece = this.move.getAttackPieces();
        int r,c;
        
        for (Piece p: piece) {
            r = p.getPosition().getRow();
            c = p.getPosition().getCol();
            System.out.print(p.getType());
            System.out.print(translate(r,c) + ", ");
        }
        System.out.println("");
        System.out.println("are attacking your king");
        
    }
    
    /*
    set selected pieces 
    */
    public void chooseKing(Team t) {
        
        selectedPiece = new Position(0,0);
        
        Piece p;
        Position pos;
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                p = this.board.getPiece(new Position(i,j));
                if (p != null) {
                    if (p.getType().equals("King") && p.getTeam() == Team.W) {
                        pos = p.getPosition();
                        this.selectedPiece.setRow(pos.getRow());
                        this.selectedPiece.setCol(pos.getCol());
                        break;
                    }
                }
            }
        }
        
    }
    
    /*
    checks if move human player wants to take
    removes the king from check
    */
    public boolean removesCheck() {
        
        // go through pieces moves
        
        Piece p = this.board.getPiece(this.selectedPiece);
        Piece them;
        int row = this.selectedPiece.getRow();
        int col = this.selectedPiece.getCol();
        ArrayList<Position> moves = p.getMoves(board);
        int[] king = this.move.getKing(Team.W);
        for (Position pos: moves) {
            // do move
            them = this.board.getPiece(pos);
            this.move.doMove(selectedPiece, pos, Team.W, 1);
            if (!this.move.isCheck(Team.W, king[0], king[1])) {
                this.move.getBoard().getPiece(pos).setNewPosition(this.selectedPiece);
                this.move.undoMove(p, them, row, col, pos.getRow(), pos.getCol());
                return true;
            }
            this.move.getBoard().getPiece(pos).setNewPosition(this.selectedPiece);
            this.move.undoMove(p, them, row, col, pos.getRow(), pos.getCol());
        }
        
        return false;
        
    }
    
    /*
    prints the possible moves to console
    */
    public void getMoves() {
        
        getArray();
        
        for (int i = 0; i < this.moves.size(); i++) {
            System.out.println(get(i));
        }
        
    }
    
    public boolean doMove(Team turn) {
        
        Piece p = this.board.getPiece(this.selectedPiece);
        int newRow = this.newSpot.getRow();
        
        int row = this.selectedPiece.getRow();
        int col = this.selectedPiece.getCol();
        if (p.getTeam() == Team.B) {
            if (newRow == this.board.size()-1) {
                
            }
        } else {
            if (newRow == 0) {
                System.out.println("You can promote your pawn!");
                System.out.print("Heres a list of pieces you");
                System.out.println(" can change it into");
                System.out.println("Queen, Bishop, Knight, Rook");
                System.out.println("Choose a piece");
                in = new Scanner(System.in);
                String line = in.nextLine();
                while (strToInt(line) == -1) {
                    line = in.nextLine();
                }
                this.move.pawnPromotion(strToInt(line), row, col, turn);
            }
        }
        boolean b = this.move.doMove(selectedPiece, newSpot,turn, 0);
        printBoard();
        return b;
    }
    
    private void getArray() {
        Piece p = this.board.getPiece(this.selectedPiece);
        if (p.getType().equals("King"))
            this.moves = this.move.getKingsMoves(this.selectedPiece,Team.W);
        else 
            this.moves = this.move.getPossibleMoves(this.selectedPiece,Team.W);
    }
    
    private String get(int i) {
        return translate(this.moves.get(i).getRow(), this.moves.get(i).getCol());
    }
    
    public Board getBoard() {
        return this.board;
    }
    
    private int strToInt(String str) {
        
        if (str.equals("Rook") || str.equals("rook")) return 0;
        if (str.equals("Knight") || str.equals("knight")) return 1;
        if (str.equals("Bishop") || str.equals("bishop")) return 2;
        if (str.equals("Queen") || str.equals("queen")) return 3;
        else return -1;
        
    }
    
    private String translate(int r, int c) {
        
        Board b = this.getBoard();
        String line = "("+(r+1);
        
        if (c == 0) return line += "A)";
        if (c == 1) return line += "B)";
        if (c == 2) return line += "C)";
        if (c == 3) return line += "D)";
        if (c == 4) return line += "E)";
        if (c == 5) return line += "F)";
        if (c == 6) return line += "G)";
        else return line += "H)";
        
    }
    
    private int readRow(String line, int type) {
        
        int i = 0;
        if (type == 1) i = 2;
        
        if (line.charAt(i) == '1') return 0;
        if (line.charAt(i) == '2') return 1;
        if (line.charAt(i) == '3') return 2;
        if (line.charAt(i) == '4') return 3;
        if (line.charAt(i) == '5') return 4;
        if (line.charAt(i) == '6') return 5;
        if (line.charAt(i) == '7') return 6;
        if (line.charAt(i) == '8') return 7;

        return 8;
    }
    
    private int readCol(String line, int type) {
        
        int i = 1;
        if (type == 1) i = 3;
        
        if (line.charAt(i) == 'a') return 0;
        if (line.charAt(i) == 'A') return 0;
        if (line.charAt(i) == 'b') return 1;
        if (line.charAt(i) == 'B') return 1;
        if (line.charAt(i) == 'C') return 2;
        if (line.charAt(i) == 'c') return 2;
        if (line.charAt(i) == 'D') return 3;
        if (line.charAt(i) == 'd') return 3;
        if (line.charAt(i) == 'E') return 4;
        if (line.charAt(i) == 'e') return 4;
        if (line.charAt(i) == 'F') return 5;
        if (line.charAt(i) == 'f') return 5;
        if (line.charAt(i) == 'G') return 6;
        if (line.charAt(i) == 'g') return 6;
        if (line.charAt(i) == 'H') return 7;
        if (line.charAt(i) == 'h') return 7;
        return 0;
        
    }
    
}
