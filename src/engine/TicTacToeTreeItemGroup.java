package engine;

import engine.TicTacToeTreeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class TicTacToeTreeItemGroup implements Iterable<TicTacToeTreeItem>{

    private ArrayList<TicTacToeTreeItem> items;

    private TicTacToeTreeItem parent;

    private TicTacToeTree containingTree;

    // Distance from root.
    private int height;

    public TicTacToeTreeItemGroup(TicTacToeTreeItem parent, TicTacToeTree containingTree) {
        items = new ArrayList<>();
        this.parent = parent;
        this.containingTree = containingTree;

        if(parent == null) { // i.e. this is root
            height = 0;
        } else {
            height = parent.getContainingGroup().getHeight() + 1;
        }

    }

    public void updateHeight() {
        if(parent == null) { // i.e. this is root
            height = 0;
        } else {
            height = parent.getContainingGroup().getHeight() + 1;
        }

        for(TicTacToeTreeItem child : items) {
            if(child.hasChildren()) {
                child.getChildren().updateHeight();
            }
        }
    }

    public TicTacToeTreeItem getParent() {
        return parent;
    }

    public TicTacToeTreeItem getItem(int index) {
        return items.get(index);
    }

    public void addItem(TicTacToeBoard item) {
        TicTacToeTreeItem ti = new TicTacToeTreeItem(item, this);
        items.add(ti);
    }

    public boolean removeItem(TicTacToeBoard item) {
        TicTacToeTreeItem treeItem = null;

        for(TicTacToeTreeItem ti : items) {
            if(ti.getItem().equals(item)) {
                treeItem = ti;
            }
        }

        try {

            if (treeItem.hasChildren()) {
                treeItem.getChildren().removeAll();
            }

            items.remove(treeItem);

        }catch(NullPointerException ex) {
            return false;
        }

        return true;
    }

    public void removeAll() {
        ArrayList<TicTacToeTreeItem> temp = new ArrayList<>(items);

        System.out.println("Removing all:");

        System.out.println(temp);

        for(TicTacToeTreeItem treeItem : temp) {
            removeItem(treeItem.getItem());
        }
    }

    public void addItems(Collection<TicTacToeBoard> items) {
        for(TicTacToeBoard item : items) {
            addItem(item);
        }
    }

    public int getHeight() {
        return height;
    }

    public TicTacToeTree getContainingTree() {
        return containingTree;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(TicTacToeTreeItem item : items) {
            output.append(item).append(" ");
        }

        output.append("{h=").append(height).append("}").append('\n');

        return output.toString();
    }

    @Override
    public Iterator<TicTacToeTreeItem> iterator() {
        return new Iterator<TicTacToeTreeItem>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public TicTacToeTreeItem next() {
                return items.get(index++);
            }
        };
    }
}
