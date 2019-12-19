package engine_v2;

import engine.Token;

public class TicTacToeMove {

    private Token tokenInserted;

    private int row, column;

    public TicTacToeMove(int row, int column, Token tokenInserted) {
        this.row = row;
        this.column = column;
        this.tokenInserted = tokenInserted;
    }

    public Token getTokenInserted() {
        return tokenInserted;
    }

    public void setTokenInserted(Token tokenInserted) {
        this.tokenInserted = tokenInserted;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "{row = " + row + ", column = " + column + ", token = " + tokenInserted + "}";
    }
}
