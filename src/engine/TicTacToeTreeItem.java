package engine;

public class TicTacToeTreeItem {

    protected TicTacToeTreeItemGroup containingGroup;

    protected TicTacToeTreeItemGroup children;

    protected TicTacToeBoard item;

    public TicTacToeTreeItem(TicTacToeBoard item, TicTacToeTreeItemGroup containingGroup) {
        this.item = item;
        this.containingGroup = containingGroup;
    }


    public boolean generateAllPossibleChildren(Token tokenToAdd) {
        if(getItem().isWinning()) {
            return false;
        } else { // Generate possibilities only if no one has won yet!
            for(TicTacToeBoard possibility : TicTacToeBoard.generateAllPossibleBoards(getItem(), tokenToAdd)) {
                getChildren().addItem(possibility);
                ((TicTacToeTreeItem)getChildren().getItem(getChildren().size() - 1)).generateAllPossibleChildren(tokenToAdd.opposite());
            }
        }

        return true;
    }

    public TicTacToeBoard getItem() {
        return item;
    }

    public void setItem(TicTacToeBoard item) {
        this.item = item;
    }

    public void remove() {
        containingGroup.removeItem(getItem());
    }

    public TicTacToeTreeItemGroup getContainingGroup() {
        return containingGroup;
    }

    public TicTacToeTreeItemGroup getChildren() {
        if(children == null) {
            children = new TicTacToeTreeItemGroup(this, getContainingGroup().getContainingTree());
        }
        return children;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    @Override
    public String toString() {
        return "(" + item.toString() + ", h = " + getContainingGroup().getHeight() + ")";
    }

}

