package engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TicTacToeTree {


    protected TicTacToeTreeItemGroup rootGroup;

    protected int itemsCount = 0;


    public TicTacToeTree() {
        rootGroup = new TicTacToeTreeItemGroup(null, this);
        System.out.println("Generating a tree, x is starting...!");
        generateTree(Token.X);
    }

    public TicTacToeTree(TicTacToeTreeItemGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public void generateTree(Token startingToken) {
        if(startingToken == Token.EMPTY) {
            throw new IllegalArgumentException("starting token can't be EMPTY! (i.e. have the value Token.EMPTY)");
        }

        for(int i = 0; i < TicTacToeBoard.BOARD_SIZE; i++) {
            for(int j = 0; j < TicTacToeBoard.BOARD_SIZE; j++) {
                TicTacToeBoard board = new TicTacToeBoard();
                board.setToken(i,j, startingToken);
                this.getRootGroup().addItem(board); // Add base elements
                System.out.println("Base element added:\n" + board);
            }
        }

        /*
         This loop will cause a recursive reaction that will create an entire tic tac toe game tree.
         Each children will generate all possibilities and then make his children do the same!
         */

        for(TicTacToeTreeItem rootChildren : this.getRootGroup()) {
            rootChildren.generateAllPossibleChildren(startingToken.opposite());
        }
    }

    public void setRootGroup(TicTacToeTreeItemGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public TicTacToeTreeItemGroup getRootGroup() {
        return rootGroup;
    }

    public void updateHeight() {
        rootGroup.updateHeight();
    }

    public boolean hasBranches() {
        for(TicTacToeTreeItem item : rootGroup) {
            if(item.hasChildren()) {
                return true;
            }
        }

        return false;
    }

    public int getMaxHeight() {
        ArrayList<Integer> heights = new ArrayList<>();
        heights.add(rootGroup.getHeight());
        for(TicTacToeTreeItem item : rootGroup) {
            if(item.hasChildren()) {
                TicTacToeTree subTree = new TicTacToeTree(item.getChildren());
                heights.add(subTree.getMaxHeight());
            }
        }

        return Collections.max(heights);
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public ArrayList<TicTacToeTreeItem> getAllItems() {
        ArrayList<TicTacToeTreeItem> items = new ArrayList<>();
        rootGroup.forEach(items::add);

        for(TicTacToeTreeItem item : rootGroup) {
            if(item.hasChildren()) {
                TicTacToeTree subTree = new TicTacToeTree(item.getChildren());
                items.addAll(subTree.getAllItems());
            }
        }

        return items;
    }

    public ArrayList<TicTacToeTreeItem> getLowestItems() {
        ArrayList<TicTacToeTreeItem> items = new ArrayList<>();
        for(TicTacToeTreeItem treeItem : getAllItems()) {
            if(!treeItem.hasChildren()) {
                items.add(treeItem);
            }
        }

        items.sort(Comparator.comparingInt(t -> t.getContainingGroup().getHeight()));
        Collections.reverse(items);

        return items;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        ArrayList<ArrayList<TicTacToeTreeItem>> itemsByLevels = new ArrayList<>();

        for(int i = 0; i <= getMaxHeight(); i++) {
            itemsByLevels.add(new ArrayList<>());
        }

        for(TicTacToeTreeItem item : getAllItems()) {
            itemsByLevels.get(item.getContainingGroup().getHeight()).add(item);
        }

        for(int i = 0; i < itemsByLevels.size(); i++) {
            System.out.print("L" + i + ": {");
            System.out.println(itemsByLevels.get(i) + "}");
        }

        return output.toString();
    }

}
