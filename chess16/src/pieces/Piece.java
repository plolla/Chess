package pieces;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Piece is an abstract class which provides basic fields/methods for chess pieces.
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */

public abstract class Piece {
    /**
     * name of piece
     */

    private String pieceName;
    /**
     * true if white, false if black
     */

    private boolean isWhite;
    /**
     * true if the piece moved, false if it didn't
     */
    private boolean didMove;

    /**
     * The characters in columnMappings are mapped to an integer representing the column
     */
    public HashMap<Character, Integer> columnMappings;

    /**
     * The integers in rowMappings are mapped to another integer representing thw row
     */
    public HashMap<Integer, Integer> rowMappings;

    /**
     * Piece constructor
     *
     * @param pieceName name of piece
     * @param isWhite determines color of piece
     */
    public Piece(String pieceName, boolean isWhite){
        this.pieceName = pieceName;
        this.isWhite = isWhite;
        columnMappings = new HashMap<>();
        columnMappings.put('a', 0);
        columnMappings.put('b', 1);
        columnMappings.put('c', 2);
        columnMappings.put('d', 3);
        columnMappings.put('e', 4);
        columnMappings.put('f', 5);
        columnMappings.put('g', 6);
        columnMappings.put('h', 7);

        rowMappings = new HashMap<>();
        rowMappings.put(8, 0);
        rowMappings.put(7, 1);
        rowMappings.put(6, 2);
        rowMappings.put(5, 3);
        rowMappings.put(4, 4);
        rowMappings.put(3, 5);
        rowMappings.put(2, 6);
        rowMappings.put(1, 7);

    }

    /**
     * Piece constructor
     *
     * @param pieceName name of piece
     * @param isWhite determines color of piece
     * @param didMove determines if piece moved or not
     */
    public Piece(String pieceName, boolean isWhite, boolean didMove) {
        this.pieceName = pieceName;
        this.isWhite = isWhite;
        this.didMove = didMove;
        columnMappings = new HashMap<>();
        columnMappings.put('a', 0);
        columnMappings.put('b', 1);
        columnMappings.put('c', 2);
        columnMappings.put('d', 3);
        columnMappings.put('e', 4);
        columnMappings.put('f', 5);
        columnMappings.put('g', 6);
        columnMappings.put('h', 7);

        rowMappings = new HashMap<>();
        rowMappings.put(8, 0);
        rowMappings.put(7, 1);
        rowMappings.put(6, 2);
        rowMappings.put(5, 3);
        rowMappings.put(4, 4);
        rowMappings.put(3, 5);
        rowMappings.put(2, 6);
        rowMappings.put(1, 7);
    }

    /**
     * validMove checks if the move made by a player can be made
     *
     * @param board Chessboard object
     * @param move User-inputted move
     * @return true if the move can be made
     */
    public abstract boolean validMove(Board board, String move);

    /**
     *
     *
     * @param board Board object where game is played on
     * @return true if king is in check
     */
    public abstract boolean isKingInCheck(Board board);

    /**
     * canThisPieceDie checks if a piece can be killed by another piece
     *
     * @param board Board object where game is played on
     * @return true if piece can be killed by another piece
     */
    public boolean canThisPieceDie(Board board) {
       int rank = 0;
       int file = 0;

       for(int i =0; i<8; i++){
           for(int j= 0; j<8; j++){
               if(board.getBoard()[i][j] == this){
                   rank = i;
                   file = j;
                   break;
               }
               if(this instanceof Bishop && board.getBoard()[i][j] instanceof Bishop) {	
                if(((Bishop) this).distinct == ((Bishop) board.getBoard()[i][j]).distinct && this.getColor() == board.getBoard()[i][j].getColor()){	
                    rank = i;	
                    file = j;	
                    break;	
                }	
             }
           }
       }

        // PAWNS AND KINGS
        if (!this.getColor()) {
            if (rank+1 < 8) {
                if (file + 1 < 8) {
                    if (board.getBoard()[rank+1][file+1] != null
                            && (board.getBoard()[rank+1][file+1] instanceof Pawn || board.getBoard()[rank+1][file+1] instanceof King)
                            && board.getBoard()[rank+1][file+1].getColor()) {
                        return true;
                    }
                }
                if (file - 1 >= 0) {
                    if (board.getBoard()[rank+1][file-1] != null
                            && (board.getBoard()[rank+1][file-1] instanceof Pawn || board.getBoard()[rank+1][file-1] instanceof King)
                            && board.getBoard()[rank+1][file+1].getColor()) {
                        return true;
                    }
                }
                if (board.getBoard()[rank+1][file] != null
                        && board.getBoard()[rank+1][file] instanceof King && board.getBoard()[rank+1][file+1].getColor()) {
                    return true;
                }
            }
        }
        else {
            if (rank-1 >= 0) {
                if (file + 1 < 8) {
                    if (board.getBoard()[rank-1][file+1] != null
                            && (board.getBoard()[rank-1][file+1] instanceof Pawn || board.getBoard()[rank+1][file+1] instanceof King)
                            && !board.getBoard()[rank+1][file+1].getColor()) {
                        return true;
                    }
                }
                if (file - 1 >= 0) {
                    if (board.getBoard()[rank-1][file-1] != null
                            && (board.getBoard()[rank-1][file-1] instanceof Pawn || board.getBoard()[rank+1][file-1] instanceof King)
                            && !board.getBoard()[rank+1][file+1].getColor()) {
                        return true;
                    }
                }
                if (board.getBoard()[rank-1][file] != null
                        && board.getBoard()[rank-1][file] instanceof King && !board.getBoard()[rank+1][file+1].getColor()) {
                    return true;
                }
            }
        }
        if (diagonalKill(board, rank, file) || straightKill(board, rank, file) || knightKill(board, rank, file)) {
            return true;
        }

        return false;
    }

    /**
     * diagonalKill checks if a piece can be killed by a queen or a bishop
     *
     * @param board Board object where the game is played on
     * @param rank rank of piece
     * @param file file of piece
     * @return true if the piece can be killed by a diagonal move
     */
    public boolean diagonalKill(Board board, int rank, int file) {
        for (int i = 1; rank-i >= 0 && file-i >= 0; i++) {
            if (board.getBoard()[rank-i][file-i] != null) {
                if ((board.getBoard()[rank-i][file-i] instanceof Bishop || board.getBoard()[rank-i][file-i] instanceof Queen)
                        && board.getBoard()[rank-i][file-i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }

            }
        }
        for (int i = 1; rank+i < 8 && file-i >= 0; i++) {
            if (board.getBoard()[rank+i][file-i] != null) {
                if ((board.getBoard()[rank+i][file-i] instanceof Bishop || board.getBoard()[rank+i][file-i] instanceof Queen)
                        && board.getBoard()[rank+i][file-i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }

            }
        }
        for (int i = 1; rank-i >= 0 && file+i < 8; i++) {
            if (board.getBoard()[rank-i][file+i] != null) {
                if ((board.getBoard()[rank-i][file+i] instanceof Bishop || board.getBoard()[rank-i][file+i] instanceof Queen)
                        && board.getBoard()[rank-i][file+i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }

            }
        }
        for (int i = 1; rank+i < 8 && file+i < 8; i++) {
            if (board.getBoard()[rank+i][file+i] != null) {
                if ((board.getBoard()[rank+i][file+i] instanceof Bishop || board.getBoard()[rank+i][file+i] instanceof Queen)
                        && board.getBoard()[rank+i][file+i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }

            }
        }
        return false;
    }

    /**
     * straightKill checks if a piece can be killed by a queen or a rook
     * @param board Board object where the game is played on
     * @param rank rank of piece
     * @param file file of piece
     * @return true if the piece can be killed by a queen or a rook
     */
    public boolean straightKill(Board board, int rank, int file) {
        for (int i = 1; rank+i < 8; i++) {
            if (board.getBoard()[rank+i][file] != null) {
                if ((board.getBoard()[rank+i][file] instanceof Rook || board.getBoard()[rank+i][file] instanceof Queen)
                        && board.getBoard()[rank+i][file].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }
            }

        }
        for (int i = 1; rank-i >= 0; i++) {
            if (board.getBoard()[rank-i][file] != null) {
                if ((board.getBoard()[rank-i][file] instanceof Rook || board.getBoard()[rank-i][file] instanceof Queen)
                        && board.getBoard()[rank-i][file].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }
            }

        }
        for (int i = 1; file+i < 8; i++) {
            if (board.getBoard()[rank][file+i] != null) {
                if ((board.getBoard()[rank][file+i] instanceof Rook || board.getBoard()[rank][file+i] instanceof Queen)
                        && board.getBoard()[rank][file+i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }
            }

        }
        for (int i = 1; file-i >= 0; i++) {
            if (board.getBoard()[rank][file-i] != null) {
                if ((board.getBoard()[rank][file-i] instanceof Rook || board.getBoard()[rank][file-i] instanceof Queen)
                        && board.getBoard()[rank][file-i].getColor() != board.getBoard()[rank][file].getColor()) {
                    return true;
                }
                else {
                    break;
                }
            }

        }

        return false;
    }

    /**
     * knightKill checks to see whether a piece could be killed by a knight
     * @param board Board object where the game is played on
     * @param endRank rank of piece
     * @param endFile file of piece
     * @return true if piece can be killed by a knight
     */
    public boolean knightKill(Board board, int endRank, int endFile) {
        if (endRank+2 < 8 && endFile+1 < 8) {
            if (board.getBoard()[endRank+2][endFile+1] != null
                    && board.getBoard()[endRank+2][endFile+1] instanceof Knight
                    && board.getBoard()[endRank+2][endFile+1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank+2 < 8 && endFile-1 >=0) {
            if (board.getBoard()[endRank+2][endFile-1] != null
                    && board.getBoard()[endRank+2][endFile-1] instanceof Knight
                    && board.getBoard()[endRank+2][endFile-1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-2 >= 0 && endFile+1 < 8) {
            if (board.getBoard()[endRank-2][endFile+1] != null
                    && board.getBoard()[endRank-2][endFile+1] instanceof Knight
                    && board.getBoard()[endRank-2][endFile+1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-2 >= 0 && endFile-1 >= 0) {
            if (board.getBoard()[endRank-2][endFile-1] != null
                    && board.getBoard()[endRank-2][endFile-1] instanceof Knight
                    && board.getBoard()[endRank-2][endFile-1].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        else if (endRank+1 < 8 && endFile+2 < 8) {
            if (board.getBoard()[endRank+1][endFile+2] != null
                    && board.getBoard()[endRank+1][endFile+2] instanceof Knight
                    && board.getBoard()[endRank+1][endFile+2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank+1 < 8 && endFile-2 >= 0) {
            if (board.getBoard()[endRank+1][endFile-2] != null
                    && board.getBoard()[endRank+1][endFile-2] instanceof Knight
                    && board.getBoard()[endRank+1][endFile-2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-1 >= 0 && endFile+2 < 8) {
            if (board.getBoard()[endRank-1][endFile+2] != null
                    && board.getBoard()[endRank-1][endFile+2] instanceof Knight
                    && board.getBoard()[endRank-1][endFile+2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        if (endRank-1 >= 0 && endFile-2 >= 0) {
            if (board.getBoard()[endRank-1][endFile-2] != null
                    && board.getBoard()[endRank-1][endFile-2] instanceof Knight
                    && board.getBoard()[endRank-1][endFile-2].getColor() != board.getBoard()[endRank][endFile].getColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * getPieceName gets name of piece
     *
     * @return name of piece
     */
    public String getPieceName() {
        return pieceName;
    }

    /**
     * getColor gets the color of the piece
     *
     * @return true if piece is white, false if its black
     */
    public boolean getColor(){
        //if true, it's white. otherwise its black.
        return isWhite;
    }

    /**
     * didItMove checks if piece moved
     *
     * @return true if piece moved, false if it didn't
     */
    public boolean didItMove() {
        return didMove;
    }

}
