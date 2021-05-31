package pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Knight is a subclass of the abstract Piece class and Knight.java provides the
 * implementation needed for a knight chess piece
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */

public class Knight extends Piece {

    /**
     * Knight constructor
     *
     * @param pieceName name of the piece
     * @param isWhite determines if piece is white or not
     */
    public Knight(String pieceName, boolean isWhite) {
        super(pieceName, isWhite);
    }

    /**
     * validMove checks if the move made by a player can be made
     *
     * @param board Board object where the game is being played on
     * @param move the input of the player containing the current position of the piece as well as the end position
     * @return true if the move inputted by the used is a valid move
     */
    @Override
    public boolean validMove(Board board, String move) {
        String[] startEnd = move.split(" ");
        String start = startEnd[0];
        String end = startEnd[1];
        int startRank = rowMappings.get(Integer.parseInt(start.charAt(1)+""));
        int startFile = columnMappings.get(start.charAt(0));

        int endRank = rowMappings.get(Integer.parseInt(end.charAt(1)+""));
        int endFile = columnMappings.get(end.charAt(0));

        int file = Math.abs(endFile - startFile);
        int rank = Math.abs(endRank - startRank);

        if ( (file == 2 && rank == 1) || (file == 1 && rank == 2) ) {
            if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() != this.getColor()) {
                board.getBoard()[endRank][endFile] = null;
                board.getBoard()[startRank][startFile] = null;
                board.getBoard()[endRank][endFile] = new Knight(getPieceName(), getColor());
                return true;
            }
            if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() == this.getColor()){
                return false;
            }
            board.getBoard()[endRank][endFile] = new Knight(getPieceName(), getColor());
            board.getBoard()[startRank][startFile] = null;
            return true;
        }
        return false;
    }

    /**
     * isKingInCheck checks if the king is in check by a knight
     *
     * @param board Board object where the game is played on
     * @return true if the knight could kill a king if the king is not moved immediately
     */
    @Override
    public boolean isKingInCheck(Board board) {
        int kingRank = 0;
        int kingFile = 0;
        King king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] instanceof King && board.getBoard()[i][j].getColor() != this.getColor()) {
                    king = (King) (board.getBoard()[i][j]);
                    kingRank = i;
                    kingFile = j;
                    break;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] instanceof Knight && board.getBoard()[i][j].getColor() != king.getColor()) {
                    if (knightMovements(board, i, j)) {
                        king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * knightMovements checks all the possible movements of a knight where a king could be in check
     * @param board Board object where the game is played on
     * @param endRank rank of the knight
     * @param endFile file of knight
     * @return true if there is a knight threatening the king
     */
    // endRank = knightRank, endFile = knightFile
    public boolean knightMovements(Board board, int endRank, int endFile) {
        if (endRank+2 < 8 && endFile+1 < 8) {
            if (board.getBoard()[endRank+2][endFile+1] != null
                    && board.getBoard()[endRank+2][endFile+1] instanceof King
                    && board.getBoard()[endRank+2][endFile+1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank+2 < 8 && endFile-1 >=0) {
            if (board.getBoard()[endRank+2][endFile-1] != null
                    && board.getBoard()[endRank+2][endFile-1] instanceof King
                    && board.getBoard()[endRank+2][endFile-1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-2 >= 0 && endFile+1 < 8) {
            if (board.getBoard()[endRank-2][endFile+1] != null
                    && board.getBoard()[endRank-2][endFile+1] instanceof King
                    && board.getBoard()[endRank-2][endFile+1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-2 >= 0 && endFile-1 >= 0) {
            if (board.getBoard()[endRank-2][endFile-1] != null
                    && board.getBoard()[endRank-2][endFile-1] instanceof King
                    && board.getBoard()[endRank-2][endFile-1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        else if (endRank+1 < 8 && endFile+2 < 8) {
            if (board.getBoard()[endRank+1][endFile+2] != null
                    && board.getBoard()[endRank+1][endFile+2] instanceof King
                    && board.getBoard()[endRank+1][endFile+2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank+1 < 8 && endFile-2 >= 0) {
            if (board.getBoard()[endRank+1][endFile-2] != null
                    && board.getBoard()[endRank+1][endFile-2] instanceof King
                    && board.getBoard()[endRank+1][endFile-2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-1 >= 0 && endFile+2 < 8) {
            if (board.getBoard()[endRank-1][endFile+2] != null
                    && board.getBoard()[endRank-1][endFile+2] instanceof King
                    && board.getBoard()[endRank-1][endFile+2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-1 >= 0 && endFile-2 >= 0) {
            if (board.getBoard()[endRank-1][endFile-2] != null
                    && board.getBoard()[endRank-1][endFile-2] instanceof King
                    && board.getBoard()[endRank-1][endFile-2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        return false;
    }
    /**
     * toString gets the name of the piece
     *
     * @return name of piece
     */
    public String toString(){
        return this.getPieceName();
    }
}
