package chess_tings.ai;

import chess_tings.Team;
import chess_tings.board.*;
import chess_tings.piece.Piece;
import chess_tings.piece.Position;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class AlphaBeta {
    
    private Move move;
    private int DEPTH;
    private int index;
    private int boardCount;
    
    public AlphaBeta() {
    }
    
    public String getMove(Board b, String difficulty) {
        
        this.DEPTH = Integer.parseInt(difficulty);
        this.move = new Move(b);
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        boardCount = 0;
        
        int val = max(0,b,alpha,beta);
        System.out.println("board count = "+boardCount);
        if (val != Integer.MIN_VALUE) {

            // get piece to move and position to move to
            int pieceIndex = 0;
            int moveIndex = 0;
            Piece p;
            ArrayList<Piece> pieces = getPieces(Team.B);
            ArrayList<Position> moves;
            int ind = 0;
            for (int i = 0; i < pieces.size(); i++) {
                p = pieces.get(i);
                moves = this.move.getPossibleMoves(p.getPosition(), Team.B);
                for (int j = 0; j < moves.size(); j++) {
                    ind++;
                    if (ind == this.index) {
                        pieceIndex = i;
                        moveIndex = j;
                        break;
                    }
                }
            }

            pieces = getPieces(Team.B);
            p = pieces.get(pieceIndex);
            moves = this.move.getPossibleMoves(p.getPosition(), Team.B);
            Position pos = moves.get(moveIndex);

            return translate(p.getPosition(),pos);
        } else {
            return "";
        }
        
    }
    
    /*
    will check the best board that the AI can take
    */
    public int max(int d, Board board, int a, int b) {
        
        ArrayList<Piece> pieces = getPieces(Team.B);
        ArrayList<Integer> evaluation = new ArrayList<Integer>();
        
        // variables for undo
        int row, col, row2, col2;
        Piece them;
        
        ArrayList<Position> moves;
        Position pos;
        Team t = Team.B;
        board = this.move.getBoard();
        // if it got to the last node
        if (d == DEPTH) {
            // checks the leaf nodes
            for (Piece p:pieces) {
                // get everything for undo
                pos = p.getPosition();
                row = pos.getRow(); // get current row value
                col = pos.getCol(); // get current col value

                moves = this.move.getPossibleMoves(pos, t);
                // if alpha >= beta and alpha isnt -infinity and beta isnt
                // +infinity then it prunes
                if (a >= b && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    for (int i = 0; i < moves.size(); i++) {
                        row2 = moves.get(i).getRow();
                        col2 = moves.get(i).getCol();
                        them = board.getPiece(new Position(row2,col2));
                        // do move
                        if (this.move.doMove(pos,moves.get(i), t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(getEval(t));
                            // undo move
                            p.setNewPosition(moves.get(i));
                            board = undo(p,row,col,them,moves.get(i),board,t);
                        }

                    }
                }
                Collections.sort(evaluation);
                if (evaluation.size() == 0) a = 0;
                else a = evaluation.get(evaluation.size() - 1);
            }
            
            return a;
            
        } else if (d > 0 && d < DEPTH) {
            d++;
            
            // checks any level between leaf and root
            for (Piece p: pieces) {
                // get all variables for undo
                pos = p.getPosition();
                row = pos.getRow();
                col = pos.getCol();

                if (a >= b && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    moves = this.move.getPossibleMoves(pos, t);
                    for (Position position: moves) {
                        row2 = position.getRow();
                        col2 = position.getCol();
                        them = board.getPiece(position);

                        // do move
                        if (this.move.doMove(pos, position, t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(min(d,board, a, b));
                            // undo move
                            board = undo(p,row,col,them,position,board,t);
                        }

                    }
                }
                Collections.sort(evaluation);
                if (evaluation.size() == 0) a = 0;
                else a = evaluation.get(evaluation.size() - 1);
            }
            
            return a;
        } else {
            d++;
            for (Piece p: pieces) {
                // get all variables for undo
                pos = p.getPosition();
                row = pos.getRow();
                col = pos.getCol();
                
                moves = this.move.getPossibleMoves(pos, t);
                int[] king = getKing(t);

                if (a >= b && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    for (Position position: moves) {
                        row2 = position.getRow();
                        col2 = position.getCol();
                        them = board.getPiece(position);

                        // do move
                        if (this.move.doMove(pos,position, t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(min(d,board, a, b));
                            // undo move
                            board = undo(p,row,col,them,position,board,t);
                        }

                    }
                }
                getBest(evaluation);
                if (evaluation.size() == 0) a = 0;
                else a = evaluation.get(evaluation.size() - 1);
            }
            
            return a;
        }
        
    }
    
    public int min(int d, Board board, int a, int b) {
        
        ArrayList<Piece> pieces = getPieces(Team.W);
        ArrayList<Integer> evaluation = new ArrayList<Integer>();
        
        /*
        gets the worst possible move that human
        player can take
        */
        
        // variables for undo
        int row, col, row2, col2;
        Piece them;
        
        ArrayList<Position> moves;
        Position pos;
        Team t = Team.W;
        board = this.move.getBoard();
        // if it got to the last node
        if (d == DEPTH) {
            // runs for leaf nodes
            for (Piece p:pieces) {
                // get everything for undo
                pos = p.getPosition();
                row = pos.getRow(); // get current row value
                col = pos.getCol(); // get current col value

                // if beta is less than alpha and alpha and beta
                // arent infinity and -infinity then prune
                if (b <= a && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    moves = this.move.getPossibleMoves(pos, t);
                    for (int i = 0; i < moves.size(); i++) {
                        row2 = moves.get(i).getRow();
                        col2 = moves.get(i).getCol();
                        them = board.getPiece(new Position(row2,col2));
                        // do move
                        if (this.move.doMove(pos,moves.get(i), t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(getEval(t));
                            // undo move
                            p.setNewPosition(moves.get(i));
                            board = undo(p,row,col,them,moves.get(i),board,t);
                        }

                    }
                }
                Collections.sort(evaluation);
                if (evaluation.size() == 0) b = 0;
                else b = evaluation.get(0);
            }
            
            return b;
            
        } else if (d > 0 && d < DEPTH) {
            d++;
            
            /*
            checks all the levels between root and leaf nodes
            */
            
            for (Piece p: pieces) {
                // get all variables for undo
                pos = p.getPosition();
                row = pos.getRow();
                col = pos.getCol();

                if (b <= a && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    moves = this.move.getPossibleMoves(pos, t);
                    for (Position position: moves) {
                        row2 = position.getRow();
                        col2 = position.getCol();
                        them = board.getPiece(position);

                        // do move
                        if (this.move.doMove(pos, position, t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(max(d,board, a, b));
                            // undo move
                            board = undo(p,row,col,them,position,board,t);
                        }

                    }
                }
                if (evaluation.size() == 0) b = 0;
                else b = evaluation.get(0);
            }
            
            return b;
        } else {
            d++;
            for (Piece p: pieces) {
                // get all variables for undo
                pos = p.getPosition();
                row = pos.getRow();
                col = pos.getCol();
                
                if (b <= a && a != Integer.MIN_VALUE && a != Integer.MAX_VALUE) {
                } else {
                    moves = this.move.getPossibleMoves(pos, t);
                    for (Position position: moves) {
                        row2 = position.getRow();
                        col2 = position.getCol();
                        them = board.getPiece(position);

                        // do move
                        if (this.move.doMove(pos,position, t, 1)) {
                            // evaluate new board
                            boardCount++;
                            evaluation.add(max(d,board, a, b));
                            // undo move
                            board = undo(p,row,col,them,position,board,t);
                        }

                    }
                }
                getWorst(evaluation);
                if (evaluation.size() == 0) b = 0;
                else b = evaluation.get(0);
            }
            
            return b;
        }
        
    }
    
    // gets the best board
    private void getBest(ArrayList<Integer> eval) {
        
        int bestValue = Integer.MIN_VALUE;
        for (int i = 0; i < eval.size(); i++) {
            if (bestValue < eval.get(i)) {
                bestValue = eval.get(i);
                this.index = i;
            }
        }
        
    }
    
    // gets worst board
    private void getWorst(ArrayList<Integer> eval) {
        
        int bestValue = Integer.MAX_VALUE;
        for (int i = 0; i < eval.size(); i++) {
            if (bestValue > eval.get(i)) {
                bestValue = eval.get(i);
                this.index = i;
            }
        }
        
    }
    
    // undoes the board
    private Board undo(Piece you, int curR, int curC, Piece them, Position next, Board board, Team t) {
        
        int nextR = next.getRow();
        int nextC = next.getCol();
        
        you.setNewPosition(new Position(curR,curC));
        
        // place them back on board
        this.move.undoMove(you, them, curR, curC, nextR, nextC);
        
        return this.move.getBoard();
        
    }
    
    // evaluates the board
    private int getEval(Team t) {
        
        ArrayList<Piece> pieces = this.move.getAllPieces(t);
        int eval1 = 0;
        
        // do evaluation for AI
        for (Piece p:pieces) {
            eval1 += p.getValue();
        }
        
        ArrayList<Position> moves;
        int eval;
        for (Piece p: this.move.getAllPieces(t)) {
            moves = this.move.getPossibleMoves(p.getPosition(), t);
            eval = p.getEval();
            if (p.getType().equals("Pawn")) {
                for (Position pos: moves) {
                    eval1 += (eval + (pos.getRow()*2));
                }
            } else {
                eval1 += (eval * moves.size());
            }
        }
        
        pieces.clear();
        if (t == Team.W) pieces = this.move.getAllPieces(Team.B);
        else pieces = getPieces(Team.W);
        
        // do evaluation for Human
        int eval2 = 0;
        for (Piece p:pieces) {
            eval2 += p.getValue();
        }
        
        for (Piece p: this.move.getAllPieces(t)) {
            moves = this.move.getPossibleMoves(p.getPosition(), t);
            eval = p.getEval();
            if (p.getType().equals("Pawn")) {
                for (Position pos: moves) {
                    eval2 += (eval + (pos.getRow()*3));
                }
            } else {
                eval2 += (eval * moves.size());
            }
        }
        
        return eval1 -= eval2;
        
    }
    
    // gets pieces of team from move
    private ArrayList<Piece> getPieces(Team t) {
        
        return this.move.getPieces(t);
        
    }
    
    // gets coordinates of king
    private int[] getKing(Team t) {
        
        ArrayList<Piece> pieces = this.move.getAllPieces(t);
        int[] king = new int[2];
        
        for (Piece p: pieces) {
            if (p.getType().equals("King")) {
                king[0] = p.getPosition().getRow();
                king[1] = p.getPosition().getCol();
                break;
            }
        }
        
        return king;
        
    }
    
    private String translate(Position p, Position pos) {
        
        int fromR = p.getRow();
        int fromC = p.getCol();
        int toR = pos.getRow();
        int toC = pos.getCol();
        Board b = this.move.getBoard();
        String str = "" + (fromR + 1);
        
        if (fromC == 0) str += "A";
        if (fromC == 1) str += "B";
        if (fromC == 2) str += "C";
        if (fromC == 3) str += "D";
        if (fromC == 4) str += "E";
        if (fromC == 5) str += "F";
        if (fromC == 6) str += "G";
        if (fromC == 7) str += "H";
        
        str += "" + (toR + 1);
        
        if (toC == 0) return str + "A";
        if (toC == 1) return str + "B";
        if (toC == 2) return str + "C";
        if (toC == 3) return str + "D";
        if (toC == 4) return str + "E";
        if (toC == 5) return str + "F";
        if (toC == 6) return str + "G";
        if (toC == 7) return str + "H";
        
        return "";
        
    }
    
}
