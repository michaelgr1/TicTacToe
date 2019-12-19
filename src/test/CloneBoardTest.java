package test;

import engine.TicTacToeBoard;
import engine.Token;

public class CloneBoardTest {

    public static void main(String[] args) {
        Token[][] board = new Token[][]{{Token.EMPTY, Token.EMPTY, Token.EMPTY},
                {Token.EMPTY, Token.EMPTY, Token.EMPTY},
                {Token.EMPTY, Token.EMPTY, Token.EMPTY}};

        TicTacToeBoard board1 = new TicTacToeBoard(board);
        TicTacToeBoard board1Clone = new TicTacToeBoard(board1);
        board[0][0] = Token.X;
        System.out.println("board1:\n" + board1);
        System.out.println("board1Clone:\n" + board1Clone);
        board1.setToken(0,0, Token.X);
        System.out.println("board1:\n" + board1);
        System.out.println("board1Clone:\n" + board1Clone);
        board1Clone.setToken(2,2, Token.O);
        System.out.println("board1:\n" + board1);
        System.out.println("board1Clone:\n" + board1Clone);
    }

}
