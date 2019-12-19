package engine_v2;

import engine.TicTacToeBoard;
import engine.Token;
import tree.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TicTacToeTree {

    private static class GenerateAllPossibleChildrenAction implements TreeItemAction<TicTacToeBoard, Integer> {

        @Override
        public Integer executeAction(TreeItem<TicTacToeBoard> treeItem) {
            TreeItemGroup<TicTacToeBoard> children = treeItem.getChildren();
            TicTacToeBoard currentBoard = treeItem.getItem();

            if(currentBoard.isWinning()) {
                return 0;
            }

//            System.out.println("Inside TreeItemAction...");
//            System.out.println("Current item = " + treeItem);
//            System.out.println("Last token added to board: " + treeItem.getItem().getLastAddedToken());

            Token tokenToPlay = currentBoard.getLastAddedToken().opposite();
            if(tokenToPlay == Token.EMPTY) {
                tokenToPlay = TicTacToeBoard.DEFAULT_PLAYER;
            }

            ArrayList<TicTacToeBoard> boards = TicTacToeBoard.generateAllPossibleBoards(currentBoard, tokenToPlay);

//            System.out.println("Children to add:\n\n" + boards + "\n\n");

            children.addItems(boards);

            return children.size();

        }
    }

    private static class PrintChildrenInfoTreeAction<T> implements TreeItemAction<T,String> {

        @Override
        public String executeAction(TreeItem<T> treeItem) {
            StringBuilder builder = new StringBuilder();
            builder.append("height = ").append(treeItem.getContainingGroup().getHeight()).append('\n');
//            builder.append("parent = ").append(treeItem.getContainingGroup().getParent()).append('\n');
            builder.append("children count = ").append(treeItem.getChildren().size()).append('\n');
            return builder.toString();
        }
    }

    /**
     * Do not use as intended, i.e. as a TreeItemAction with executeActionOrphanToRoot. The proper result is not obtained like that!
     * Maybe I need to executeActionOrphanToRoot, Maybe this class has a defect.
     * TODO: Debug this class and executeActinoOrphanToRoot from GeneralTree!
     */
    private static class AssignScoreTreeAction implements TreeItemAction<TicTacToeBoard, Double> {

        private Token token;

        public AssignScoreTreeAction(Token token) {
            this.token = token;
        }

        /**
         * Calculate's the score for each item, will also modify the score.
         * @param treeItem
         * @return
         */
        @Override
        public Double executeAction(TreeItem<TicTacToeBoard> treeItem) {
            TicTacToeBoard board = treeItem.getItem();

            double score;

            System.out.println("Calculating score for item...");

            if(board.isTie()){
                score = TicTacToeBoard.TIE_BOARD_SCORE;
                System.out.println("It's a tie!");
            } else if(board.isWinning(token)) {
                score = TicTacToeBoard.WINNING_BOARD_SCORE;
                System.out.println(token + " is winning!");
            } else if(board.isWinning(token.opposite())){
                score = TicTacToeBoard.LOSING_BOARD_SCORE;
                System.out.println(token.opposite() + " is winning!");
            } else {
                //TODO: Check the method, maybe summing is not the best idea?

                System.out.println("Summing children's scores...");

                double childrenScoresSum = 0;

                System.out.println("children count = " + treeItem.getChildren().size());

                for(TreeItem<TicTacToeBoard> children : treeItem.getChildren()) {
                    System.out.println("adding score, score = " + children.getItem().getScore());
                    childrenScoresSum += children.getItem().getScore();
                }

                System.out.println("Sum = " + childrenScoresSum);

                score = childrenScoresSum; // Check later

                // Assign max score from children.
//                score = TicTacToeBoard.LOSING_BOARD_SCORE;
//
//                for(TreeItem<TicTacToeBoard> children : treeItem.getChildren()) {
//
//                    if(score < children.getItem().getScore()) {
//                        score = children.getItem().getScore();
//                    }
//
//                }
            }


            System.out.println("Setting score to " + score);

            treeItem.getItem().setScore(score);

            return score;
        }
    }

    public static Tree<TicTacToeBoard> generateGameTree(Token startingToken) {
        Tree<TicTacToeBoard> gameTree = new Tree<>();
        for(int i = 0; i < TicTacToeBoard.BOARD_SIZE; i++) {
            for(int j = 0; j < TicTacToeBoard.BOARD_SIZE; j++) {
                TicTacToeBoard board = new TicTacToeBoard();
                board.setToken(i,j, startingToken);
//                System.out.println("Generating root board:\n" + board);
                gameTree.getRootGroup().addItem(board);
            }
        }

        gameTree.executeAction(new GenerateAllPossibleChildrenAction(), true);

        return gameTree;
    }

    public static ArrayList<Double> calculateScores(Tree<TicTacToeBoard> gameTree, Token token) {

        System.out.println("Calculating scores... token = " + token);

        // Not working, wait until fix. Maybe the problem is in executeActionOrphanToRoot from GeneralTree?
//        return gameTree.executeActionOrphanToRoot(new AssignScoreTreeAction(token));

        ArrayList<TreeLevel<TicTacToeBoard>> levels = gameTree.getLevels();

        AssignScoreTreeAction assignScoreTreeAction = new AssignScoreTreeAction(token);

        for(int i = levels.size() - 1; i >= 0; i--) {
            for(TreeItem<TicTacToeBoard> item : levels.get(i).getAllItems()) {
                assignScoreTreeAction.executeAction(item);
            }
        }

        return null;
    }

    public static Tree<TicTacToeBoard> generateGameTree(TicTacToeBoard startingPosition) {
        Tree<TicTacToeBoard> gameTree = new Tree<>();
        gameTree.getRootGroup().addItem(startingPosition);

        gameTree.executeAction(new GenerateAllPossibleChildrenAction(), true);

        return gameTree;

    }

    public static void main(String[] args) throws IOException {
        Tree<TicTacToeBoard> boardTree = new Tree<>();
        Token startingToken = Token.X;
        for(int i = 0; i < TicTacToeBoard.BOARD_SIZE; i++) {
            for(int j = 0; j < TicTacToeBoard.BOARD_SIZE; j++) {
                TicTacToeBoard board = new TicTacToeBoard();
                board.setToken(i,j, startingToken);
                System.out.println("Generating root board:\n" + board);
                boardTree.getRootGroup().addItem(board);
            }
        }

        ArrayList<Integer> result = boardTree.getRootGroup().executeAction(new GenerateAllPossibleChildrenAction(), true);
        int resultSum = 0;
        for(int i : result) {
            resultSum += i;
        }

        System.out.println("resultSum = " + resultSum);
        System.out.println("Done...");
//        System.out.println("result = " + result);
        System.out.println("item count = " + boardTree.getItemsCount());
        System.out.println("items size = " + boardTree.getAllItems().size());
        System.out.println("height = " + boardTree.getMaxHeight());
        System.out.println("winning conditions: " + boardTree.getLowestItems().size());

        System.out.println("\n\n======================\n\n");

//        File file = new File("output.txt");
//
//        PrintWriter writer = new PrintWriter(new FileWriter(file));
//
//        for(String output : boardTree.executeAction(new PrintChildrenInfoTreeAction<>(), true)){
//            writer.println(output);
//        }
//
//        writer.close();
    }

}
