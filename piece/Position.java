package chess_tings.piece;

public class Position {
    
    private int row, col;
    
    public Position(int row, int col) {
        this.row= row;
        this.col = col;
    }
    
    public boolean equals(Position pos) {
        if (this.row == pos.getRow() && this.col == pos.getCol()) return true;
        return false;
    }
    public void printPosition(){
        System.out.println("("+(row+1)+(char)(col+'A')+")");
    }
    public void setRow(int i) {
        this.row = i;
    }
    
    public void setCol(int i) {
        this.col = i;
    }
    
    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
    
}
