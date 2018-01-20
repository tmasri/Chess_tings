package chess_tings.board;

import chess_tings.Team;
import chess_tings.piece.*;
import java.util.Scanner;

public class Board {
    
    private Piece[][] board;
    private int numB, numW;
    
    public Board() {
        this.board = new Piece[8][8];
        init();
    }
    
    private void init() {
        
        this.numB = 16;
        this.numW = 16;
        
        // fill up black team
        for (int i = 0; i < board.length; i++) {
            board[1][i] = new Pawn(new Position(1,i), Team.B);
        }
        
        board[0][0] = new Rook(new Position(0,0), Team.B); // rook
        board[0][1] = new Knight(new Position(0,1), Team.B); // knight
        board[0][2] = new Bishop(new Position(0,2), Team.B); // bishop
        board[0][3] = new King(new Position(0,3), Team.B); // king
        board[0][4] = new Queen(new Position(0,4), Team.B); // queen
        board[0][5] = new Bishop(new Position(0,5), Team.B); // bishop
        board[0][6] = new Knight(new Position(0,6), Team.B); // knight
        board[0][7] = new Rook(new Position(0,7), Team.B); // rook
        
        // fill up white team 
        for (int i = 0; i < board.length; i++) {
            board[6][i] = new Pawn(new Position(6,i), Team.W);
        }
        
        board[7][0] = new Rook(new Position(7,0), Team.W); // rook
        board[7][1] = new Knight(new Position(7,1), Team.W); // knight
        board[7][2] = new Bishop(new Position(7,2), Team.W); // bishop
        board[7][3] = new King(new Position(7,3), Team.W); // king
        board[7][4] = new Queen(new Position(7,4), Team.W); // queen
        board[7][5] = new Bishop(new Position(7,5), Team.W); // bishop
        board[7][6] = new Knight(new Position(7,6), Team.W); // knight
        board[7][7] = new Rook(new Position(7,7), Team.W); // rook
        
    }
    
    public Piece getPiece(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }
    
    // checks is spot is occupied
    public boolean isOccupied(Position pos) {
        
        if (this.board[pos.getRow()][pos.getCol()] == null) return false;
        return true;
        
    }
    
    // adds a piece to the coordinates given
    public void set(int i, int j, Piece p) {
        this.board[i][j] = p;
    }
    
    public int size() {
        return board.length;
    }
    
    protected void setNum(Team t, int i) {
        if (t == Team.B) this.numB += i;
        else this.numW += i;
    }
    
    protected int getNum(Team t) {
        if (t == Team.B) return this.numB;
        return this.numW;
    }
    
    public void printBoard() {
        System.out.println("    A    B    C    D    E    F    G    H   ");
        System.out.println("-------------------------------------------");
        
        Piece p;
        for (int i = 0; i < this.board.length; i++) {
            System.out.print((i+1) + " ");
            System.out.print("|");
            for (int j = 0; j < board[i].length; j++) {
                p = this.board[i][j];
                if (this.board[i][j] == null) System.out.print("    |");
                else if (this.board[i][j].getClass().equals(Pawn.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" PN |");
                    else System.out.print(" pn |");
                }
                else if (this.board[i][j].getClass().equals(Rook.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" RK |");
                    else System.out.print(" rk |");
                }
                else if (this.board[i][j].getClass().equals(Knight.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" KT |");
                    else System.out.print(" kt |");
                }
                else if (this.board[i][j].getClass().equals(Bishop.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" BP |");
                    else System.out.print(" bp |");
                }
                else if (this.board[i][j].getClass().equals(King.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" KG |");
                    else System.out.print(" kg |");
                }
                else if (this.board[i][j].getClass().equals(Queen.class)) {
                    if (p.getTeam() == Team.B) System.out.print(" QN |");
                    else System.out.print(" qn |");
                }
            }
            System.out.println("");
            System.out.println("-------------------------------------------");
        }
        System.out.println("    a    b    c    d    e    f    g    h   ");
    }
    
}
