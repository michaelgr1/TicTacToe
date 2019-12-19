package engine;

import engine_v2.TicTacToeMove;

import java.util.ArrayList;
import java.util.Arrays;

public class TicTacToeBoard {

    public static final double WINNING_BOARD_SCORE = 1;

    public static final double TIE_BOARD_SCORE = 0.5;

    public static final double LOSING_BOARD_SCORE = -1;

    // Changing the size will cause a logic error!
    public static final int BOARD_SIZE = 3;

    public static final Token DEFAULT_PLAYER = Token.X;

    /**
     * This is the score of the current board. It is calculated based on the game tree and therefore doesn't get it's value from this class.
     */
    private double score;

    private Token[][] board;

    private Token lastAddedToken = Token.EMPTY;

    /**
     * Initializes an empty board
     */
    public TicTacToeBoard() {
        this.board = new Token[BOARD_SIZE][BOARD_SIZE];

        for(int i = 0; i < board.length; ++i) {
            for(int j = 0; j < board[i].length; ++j) {
                this.board[i][j] = Token.EMPTY;
            }
        }
    }

    /**
     * Initialize based on board param.
     * @param board -
     */
    public TicTacToeBoard(Token[][] board) {

        //Make sure that board length is BOARD_SIZE x BOARD_SIZE

        if(board.length != BOARD_SIZE) {
            throw new IllegalArgumentException("Illegal size of board[][]");
        } else {
            for (Token[] tokens : board) {
                if (tokens.length != BOARD_SIZE) {
                    throw new IllegalArgumentException("Illegal size of board[][]");
                }
            }
        }

        this.board = new Token[BOARD_SIZE][BOARD_SIZE];

        for(int i = 0; i < board.length; ++i) {
            for(int j = 0; j < board[i].length; ++j) {
                this.board[i][j] = board[i][j].copy();
            }
        }
    }

    public TicTacToeBoard(TicTacToeBoard other) {
        this(other.board);
        this.lastAddedToken = other.lastAddedToken.copy();
    }

    /**
     * \
     * Get the token in row,column
     * @param row - X value for the desired token
     * @param column - Y value for the desired token
     * @return - The Token in (row,column)
     */
    public Token getToken(int row, int column) {
        if(row >= BOARD_SIZE || row < 0 || column >= BOARD_SIZE || column < 0) {
            throw new IllegalArgumentException("Illegal values for row or column.");
        }

        return board[row][column];
    }

    public boolean setToken(int row, int column, Token newValue) {
        if(row >= BOARD_SIZE || row < 0 || column >= BOARD_SIZE || column < 0) {
            throw new IllegalArgumentException("Illegal values for row or column.");
        }

        Token temp  = board[row][column];
        this.board[row][column] = newValue;

        lastAddedToken = newValue;

//        System.out.println("Setting token, lastAddedToken = " + lastAddedToken);

        return !temp.equals(newValue);
    }

    public boolean executeMove(TicTacToeMove move) {
        if(isMoveValid(move)) {
//            System.out.println("Move is valid, executing....");
            return setToken(move.getRow(), move.getColumn(), move.getTokenInserted());
        } else {
//            System.out.println("Move is not valid...");
            return false;
        }
    }

    public boolean isMoveValid(TicTacToeMove move) {
        return isMoveValid(move.getRow(), move.getColumn());
    }

    public boolean isMoveValid(int row, int column) {
        if(row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE) {
            return board[row][column] == Token.EMPTY;
        } else {
            throw new IllegalArgumentException("Invalid value for row or column!");
        }
    }

    public Token getLastAddedToken() {
        return lastAddedToken;
    }

    public boolean isFull() {
        for(Token[] tokens : board) {
            for(Token token : tokens) {
                if(token == Token.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isEmpty() {
        for(Token[] tokens : board) {
            for(Token token : tokens) {
                if(token != Token.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if token is winning. Will only work when BOARD_SIZE = 3 !!
     * @param token - The desired winner
     * @return - true if token is winning, false otherwise.
     */
    public boolean isWinning(Token token) {
        if(token == Token.EMPTY) {
            throw new IllegalArgumentException("param token can't be empty");
        }

        // Check for column (n, n)
        if(board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if(board[0][0] == token) {
                return true;
            }
        }

        // Check for column (2,0) (1,1) (0,2)
        if(board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
            if(board[2][0] == token) {
                return true;
            }
        }


        for(int i = 0; i < board.length; ++i) {
            // Check for a win in row (i, 0)
            if(board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if(board[i][0] == token) {
                    return true;
                }
            }

            // Check for a win in column (0, i)
            if(board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if(board[0][i] == token) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isWinning() {
        return isWinning(Token.X) || isWinning(Token.O);
    }

    public boolean isTie() {
        return !isWinning(Token.X) && !isWinning(Token.O) && isFull();
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder boardBuilder = new StringBuilder();

        for (Token[] tokens : board) {
            for (int j = 0; j < tokens.length; j++) {
                if (j == 0 || j == 1) {
                    boardBuilder.append(' ').append(tokens[j]).append(' ').append('|');
                } else {
                    boardBuilder.append(' ').append(tokens[j]).append(' ');
                }
            }
            boardBuilder.append('\n');
        }

        return boardBuilder.toString();
    }


    public static ArrayList<TicTacToeBoard> generateAllPossibleBoards(TicTacToeBoard startingPoint, Token tokenToAdd) {
        if(startingPoint.isFull()) {
            return new ArrayList<TicTacToeBoard>();
        }

//        System.out.println("Generating all possible boards:");

        ArrayList<TicTacToeBoard> possibleBoards = new ArrayList<>();

        for(int i = 0; i < startingPoint.board.length; ++i) {
            for(int j = 0; j < startingPoint.board[i].length; j++) {
                if(startingPoint.board[i][j] == Token.EMPTY) {
//                    System.out.println("Found an empty place, i = " + i + ", j = " + j);
                    TicTacToeBoard board = new TicTacToeBoard(startingPoint);
                    board.setToken(i,j, tokenToAdd);
//                    System.out.println("Newly generated board = " + board);
                    possibleBoards.add(board);
                }
            }
        }

        return possibleBoards;
    }

}
