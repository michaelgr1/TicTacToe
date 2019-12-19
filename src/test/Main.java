package test;

import engine.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Creating new tic tac toe game tree...");
        TicTacToeTree ticTacToeTree = new TicTacToeTree();

        System.out.println("ticTacToeTree height = " + ticTacToeTree.getMaxHeight());
        System.out.println("ticTacToeTree items size = " + ticTacToeTree.getAllItems().size());
        System.out.println("ticTacToeTree item count = " + ticTacToeTree.getItemsCount());
//        System.out.println("items:\n" + ticTacToeTree);
        System.out.println("============= DONE ================");
    }

}
