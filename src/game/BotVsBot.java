package game;

import engine.TicTacToeBoard;
import engine.Token;
import engine_v2.BotMethod;
import engine_v2.TicTacToeBot;
import engine_v2.TicTacToeMove;

import java.util.Scanner;

public class BotVsBot {

    public static void main(String[] args) {
        TicTacToeBoard currentBoard = new TicTacToeBoard();

        while(!currentBoard.isWinning() && !currentBoard.isTie()) {
            Token tokenToPlay = currentBoard.getLastAddedToken().opposite();
            if(tokenToPlay == Token.EMPTY) {
                tokenToPlay = Token.X;
            }

            System.out.println("Current board:");
            System.out.println("==================\n");
            System.out.println(currentBoard);
            System.out.println("==================\n");

            TicTacToeMove moveToPlay = null;

            if(tokenToPlay == Token.X) {
                System.out.println("X, it's your turn...");
                moveToPlay = TicTacToeBot.getOptimalMove(currentBoard, BotMethod.ITEM_SCORE);
            } else {
                System.out.println("O, it's your turn...");
                moveToPlay = TicTacToeBot.getOptimalMove(currentBoard, BotMethod.ITEM_SCORE);
            }

            currentBoard.executeMove(moveToPlay);
        }

        System.out.println("Current board:");
        System.out.println("==================\n");
        System.out.println(currentBoard);
        System.out.println("==================\n");

        if(currentBoard.isWinning(Token.X)) {
            System.out.println("===============\n\n");
            System.out.println("X has won!");
            System.out.println("===============\n\n");
        } else if(currentBoard.isWinning(Token.O)){
            System.out.println("===============\n\n");
            System.out.println("O has won!");
            System.out.println("===============\n\n");
        } else {
            System.out.println("It's a tie!");
        }
    }

}
