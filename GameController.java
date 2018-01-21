package chess_tings;

import chess_tings.board.*;
import java.util.Scanner;

public class GameController {
    
    private Controller control;
    
    public GameController() {
        this.control = new Controller();
    }
    
    public void start() {
        
        Team turn = Team.W;
        boolean game = true;
        boolean ai;
        this.control.printBoard();
        Scanner in = new Scanner(System.in);
        int difficulty = getInput(in);
        
        if (difficulty == 0) ai = false;
        else ai = true;
        
        while (game) {
            
            if (turn == Team.B) {
                // human or ai
                System.out.println("Blacks turn");
                turn = Team.W;
            } else {
                // human
                System.out.println("Whites turn");
                if (!gameOver(turn)) {
                    
                } else {
                    game = false;
                }
                turn = Team.B;
                game = false;
            }
            
        }
        
    }
    
    /*
    Checks if user is in Checkmate, Stalemate, or neither
    returns true if Checkmate or Stalemate
    returns false if neither
    */
    private boolean gameOver(Team t) {
        
        System.out.println("is game over");
        if (!this.control.isCheck(t)) {
            System.out.println("not in check");
            if (this.control.isStaleMate(t)) {
                System.out.println("is stale");
                print("YOU ARE IN STALEMATE","GAME OVER");
                return true;
            }
        } else if (this.control.isCheckMate(t)) {
            System.out.println("is check");
            if (t == Team.B) System.out.println("COMPUTER IS THE WINNER");
            else System.out.println("YOU ARE THE WINNER");
            return true;
        }
        
        return false;
        
    }
    
    /*
    Gets user input to decide if user is playing against
    Human or AI
    returns 0 if playing against Human
    returns value between 2 - 5 is playing against AI
    */
    private int getInput(Scanner in) {
        
        print("Enter 1 to play against a human player");//,
//                "or 2 to play against computer");
        
        int check = 0;//in.nextInt();
        
        if (check == 2) {
            
            print("Enter a number between 2 and 5",
                    "to select your difficulty level");
            check = in.nextInt();
            while (check < 2 || check > 5) {
                print("Please enter a value between 2 - 5");
                check = in.nextInt();
            }
            return check;
            
        }
        
        return 0;
        
    }
    
    private void other() {
        
        
        Team turn = Team.W;
        boolean game = true;
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
//                String aiChoice = this.ai.getMove(this.control.getBoard(),difficulty);
//                turn = doLogic(aiChoice,turn);
            } else {
                if (!this.control.isCheck(turn)) {
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
                    print("Whites Move: ","Which piece do you want to move?");
                    t = Team.W;
                } else {
                    t = Team.B;
                }
            } else {
                print("Move not legal");
                if (t == Team.W) {
                    print("Whites Move: ","Which piece do you want to move?");
                }
            }
        } else {
            print("spot selected is empty");
            if (t == Team.W) {
                print("Whites Move: ","Which piece do you want to move?");
            }
        }
        
        return t;
        
    }
    
    private void print(String... str) {
        
        for (String s : str) {
            System.out.println(s);
        }
        
    }
    
}
