package chess_tings;

import chess_tings.ai.AlphaBeta;
import chess_tings.board.*;
import java.util.Scanner;

public class GameController {
    
    private Controller control;
    private AlphaBeta ai;
    
    public GameController() {
        this.control = new Controller();
        this.ai = new AlphaBeta();
    }
    
    public void start() {
        
        Team turn = Team.W;
        boolean game = true;
        
        System.out.println("Enter difficulty level 2 - 5 (Tree Depth):");
        Scanner in = new Scanner(System.in);
        String difficulty = in.nextLine();
        String line, piece, p;
        this.control.printBoard();
        this.control.isStaleMate(turn);
        
        print("Your Turn: ","Which piece do you want to move?","");
        int i = 0;
        // starts the game
        while (game) {
            if (turn == Team.B) {
                // AIs turn
                System.out.println("AIs Move:");
                String aiChoice = this.ai.getMove(this.control.getBoard(),difficulty);
                turn = doLogic(aiChoice,turn);
            } else {
                if (this.control.isStaleMate(turn)) {
                    System.out.println("YOU ARE IN STALEMATE");
                    System.out.println("GAME OVER");
                    game = false;
                } else if (!this.control.isCheck(turn)) {
                    line = in.nextLine();
                    if (line.equals("-1")) break;
                    if (!(piece = this.control.parsePiece(line)).equals("")) {
                        p = line;
                        print("Here are the "+piece+"s possible moves:","","");
                        this.control.getMoves();
                        // if player chose a piece that doesnt mahe any possible moves
                        // ask the user to choose another piece
                        print("Type coordinates to move to","or type 0 to choose another","player: ");
                        line = in.nextLine();
                        p += line;
                        if (line.equals("-1")) break;
                        else if (line.equals("0")) {
                            print("Your Turn: ","Which piece do you want to move?","");
                        } else {
                            turn = doLogic(p,turn);
                        }
                    }
                } else if (this.control.isCheck(turn)) {
                    if (!this.control.isCheckMate(turn)) {
                        print("YOUR KING IS IN CHECK MOVE IT NOW!!","","");
                        this.control.getAttackPieces();
                        print("Which piece do you want to move?","","");
                        line = in.nextLine();
                        if (!(piece = this.control.parsePiece(line)).equals("")) {
                            if (line.equals("-1")) break;
                            p = line;
                            while (!this.control.removesCheck()) {
                                System.out.println("Please choose another piece");
                                line = in.nextLine();
                                piece = this.control.parsePiece(line);
                            }
                            print("Here are the "+piece+"s possible moves:","","");
                            this.control.getMoves();
                            System.out.println("Type coordinates to move to");
                            line = in.nextLine();
                            p += line;
                            if (line.equals("-1")) break;
                            turn = doLogic(p,turn);
                        }
                    } else {
                        if (turn == Team.B) System.out.println("COMPUTER IS THE WINNER");
                        else System.out.println("YOU ARE THE WINNER");
                        game = false;
                    }
                }
            }
        }
        
    }
    
    private Team doLogic(String command, Team t) {
     
        if (control.parse(command)) {
            if (control.doMove(t)) {
                if (t == Team.B) {
                    System.out.println("Whites Move: ");
                    System.out.println("Which piece do you want to move?");
                    t = Team.W;
                } else {
                    t = Team.B;
                }
            } else {
                System.out.println("Move not legal");
                if (t == Team.W) {
                    System.out.println("Whites Move: ");
                    System.out.println("Which piece do you want to move?");
                }
            }
        } else {
            System.out.println("spot selected is empty");
            if (t == Team.W) {
                System.out.println("Whites Move: ");
                System.out.println("Which piece do you want to move?");
            }
        }
        
        return t;
        
    }
    
    private void print(String str1, String str2, String str3) {
        
        if (!str1.equals("")) System.out.println(str1);
        if (!str2.equals("")) System.out.println(str2);
        if (!str3.equals("")) System.out.println(str3);
        
    }
    
}
