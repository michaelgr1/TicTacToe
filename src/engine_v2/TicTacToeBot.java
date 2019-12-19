package engine_v2;

import engine.TicTacToeBoard;
import engine.Token;
import tree.Tree;
import tree.TreeItem;
import tree.TreeItemGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TicTacToeBot {

    private static double calculateWinningPercentage(ArrayList<TreeItem<TicTacToeBoard>> items, Token winningToken) {

        if(items.isEmpty()) {
            return 0;
        }

        int winningsCount = 0;
        for (TreeItem<TicTacToeBoard> item : items) {
            if(item.getItem().isWinning(winningToken)) {
                ++winningsCount;
            }
        }

        double height = items.get(0).getContainingGroup().getHeight();
        double maxHeight = items.get(0).getContainingGroup().getContainingTree().getMaxHeight();

        System.out.println("height = " + height + ", maxHeight = " + maxHeight);

        // Winning percentage multiplied by height / maxHeight. The closer the item is, the less chance of winning?!

//        return (((double) winningsCount / (double) items.size()) * 100) * (height / maxHeight);
        return (((double) winningsCount / (double) items.size()) * 100);
    }

    private static double calculateLosingPercentage(ArrayList<TreeItem<TicTacToeBoard>> items, Token token) {
        int losingCount = 0;
        for(TreeItem<TicTacToeBoard> item : items) {
            if(item.getItem().isWinning(token.opposite())) {
                ++losingCount;
            }
        }

        return ((double) losingCount / (double) items.size()) * 100;
    }

    public static TicTacToeMove getOptimalMove(TicTacToeBoard currentBoard, BotMethod botMethod) {

//        if(currentBoard.isEmpty()) {
//            throw new IllegalArgumentException("Please provide a board that is not empty!");
//        }

        Token lastAddedToken = currentBoard.getLastAddedToken();
        if (lastAddedToken == Token.EMPTY) {
            lastAddedToken = Token.O; // So token.opposite will yield X.
        }


        Tree<TicTacToeBoard> gameTree = TicTacToeTree.generateGameTree(currentBoard);

        if(botMethod == BotMethod.ITEM_SCORE) {
            TicTacToeTree.calculateScores(gameTree, lastAddedToken.opposite());
        }

        if(gameTree.getRootGroup().size() != 1 || !gameTree.getRootGroup().getItem(0).hasChildren()) {
            System.err.println("Fatal error");
            return null;
        }

        TicTacToeMove bestMove = null;

        TicTacToeBoard bestBoard = null;

        TreeItemGroup<TicTacToeBoard> children = gameTree.getRootGroup().getItem(0).getChildren();

        if(botMethod == BotMethod.HIGHEST_CHANCE_OF_WINNING || botMethod == BotMethod.LOWEST_CHANCE_OF_LOSING) {

            ArrayList<Double> winningPercentagePerChildren = new ArrayList<>();
            ArrayList<Double> losingPercentagePerChildren = new ArrayList<>();

            for (TreeItem<TicTacToeBoard> child : children) {
                Tree<TicTacToeBoard> subTree = new Tree<>();
                subTree.setRootGroup(new TreeItemGroup<>(null, subTree, child));

                if(botMethod == BotMethod.HIGHEST_CHANCE_OF_WINNING) {
                    winningPercentagePerChildren.add(calculateWinningPercentage(subTree.getLowestItems(), lastAddedToken.opposite()));
                } else {
                    losingPercentagePerChildren.add(calculateLosingPercentage(subTree.getLowestItems(), lastAddedToken.opposite()));
                }
            }


            if(botMethod == BotMethod.HIGHEST_CHANCE_OF_WINNING) {
                double bestChance = Collections.max(winningPercentagePerChildren);

                //Best board based on winning chance
                bestBoard = children.getItem(winningPercentagePerChildren.indexOf(bestChance)).getItem();
                //TODO: Add random in case of several equal chances.
            } else {
                double lowestLosingChance = Collections.min(losingPercentagePerChildren);

                //Best board based on not losing
                bestBoard = children.getItem(losingPercentagePerChildren.indexOf(lowestLosingChance)).getItem();
                //TODO: Add random in case of several equal chances.
            }




        } else if(botMethod == BotMethod.ITEM_SCORE) {
            double maxScore = children.getItem(0).getItem().getScore();

            for(TreeItem<TicTacToeBoard> child : children) {
                if(maxScore < child.getItem().getScore()) {
                    maxScore = child.getItem().getScore();
                }
            }

            System.out.println("Max score = " + maxScore);

            for(int i = 0; i < children.size(); i++) {
                if(children.getItem(i).getItem().getScore() == maxScore) {
                    bestBoard = children.getItem(i).getItem();
                }
            }

        }

//        System.out.println("Best move:\n" + bestBoard);

//        System.out.println("Lowest losing chance move:\n" + bestBoard);

        TicTacToeMove moveToPlay = new TicTacToeMove(0,0, Token.EMPTY);

        for(int i = 0; i < TicTacToeBoard.BOARD_SIZE; ++i) {
            for(int j = 0; j < TicTacToeBoard.BOARD_SIZE; ++j) {
                if(currentBoard.getToken(i,j) != bestBoard.getToken(i,j)) {
                    moveToPlay.setRow(i);
                    moveToPlay.setColumn(j);
                    moveToPlay.setTokenInserted(bestBoard.getToken(i,j));
                }
            }
        }

        return moveToPlay;

    }

    public static void main(String[] args) {
        System.out.println("Bot test:");
        TicTacToeBoard board = new TicTacToeBoard();
        board.setToken(0,0, Token.X);
//        board.setToken(1,1, Token.O);
//        board.setToken(0,2, Token.X);

        TicTacToeMove moveToPlay = getOptimalMove(board, BotMethod.HIGHEST_CHANCE_OF_WINNING);
        System.out.println("Move to play = " + moveToPlay);
    }

}
