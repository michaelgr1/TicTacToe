package engine;

public enum Token{

    X, O, EMPTY;

    public Token opposite() {
        switch (this) {
            case O:
                return X;
            case X:
                return O;
            default:
                return EMPTY;
        }
    }

    public Token copy() {
        switch (this) {
            case O:
                return Token.O;
            case X:
                return Token.X;
            default:
                return Token.EMPTY;
        }
    }

    @Override
    public String toString() {
        if(this == EMPTY) {
            return "_";
        } else {
            return super.toString();
        }
    }
}
