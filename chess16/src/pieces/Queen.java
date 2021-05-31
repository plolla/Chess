package pieces;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Queen is a subclass of the abstract Piece class and Queen.java provides the
 * implementation needed for a queen chess piece
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */

public class Queen extends Piece{

    /**
     * Queen constructor
     *
     * @param pieceName name of piece
     * @param isWhite determines if piece is white or not
     */
    public Queen(String pieceName, boolean isWhite) {
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

        if ((file == rank) || (file > 0 && rank == 0) || (file == 0 && rank > 0)) {
            if ((file == rank) && diagonalCheckPiecesInPath(board, startRank, startFile, endRank, endFile)
                    || ((file > 0 && rank == 0) || (file == 0 && rank > 0)) && straightCheckPiecesInPath(board, startRank, startFile, endRank, endFile)) {
                if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() != this.getColor()) {
                    board.getBoard()[endRank][endFile] = null;
                    board.getBoard()[startRank][startFile] = null;
                    board.getBoard()[endRank][endFile] = new Queen(getPieceName(), getColor());
                    return true;
                }
                else if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() == this.getColor()){
                    return false;
                }
                board.getBoard()[endRank][endFile] = new Queen(getPieceName(), getColor());
                board.getBoard()[startRank][startFile] = null;
                return true;
            }

        }
        return false;
    }

    /**
     * diagonalCheckPiecesInPath checks if there are any pieces in between the current position
     * and the desired position. If the queen wants to move diagonally, validMove will call this method
     *
     * @param board Board object where the game will be played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in between the current position and the desired position
     */
    public boolean diagonalCheckPiecesInPath(Board board, int startRank, int startFile, int endRank, int endFile) {
        int file = endFile - startFile;
        int rank = endRank - startRank;
        if (Math.abs(file) == Math.abs(rank)) {

            // both positive
            if (file > 0 && rank > 0) {
                for (int i = 1; i < file; i++) {
                    if (board.getBoard()[startRank + i][startFile + i] != null) {
                        return false;
                    }
                }
            }
            // positive file negative rank
            if (file > 0 && rank < 0) {
                for (int i = 1; i < file && i < Math.abs(rank); i++) {
                    if (board.getBoard()[startRank - i][startFile + i] != null) {
                        return false;
                    }
                }
            }
            // negative file positive rank
            if (file < 0 && rank > 0) {
                for (int i = 1; i < Math.abs(file) && i < rank; i++) {
                    if (board.getBoard()[startRank + i][startFile - i] != null) {
                        return false;
                    }
                }
            }
            // both negative
            if (file < 0 && rank < 0) {
                for (int i = 1; i < Math.abs(rank); i++) {
                    if (board.getBoard()[startRank - i][startFile - i] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;

    }

    /**
     * straightCheckPiecesInPath checks if there are any pieces in between the current position
     * and the desired position. If the queen wants to move straight, validMove will call this method
     *
     * @param board Board object where the game will be played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in between the current position and the desired position
     */
    public boolean straightCheckPiecesInPath(Board board, int startRank, int startFile, int endRank, int endFile) {
        int file = endFile - startFile;
        int rank = endRank - startRank;

        if (file > 0 && rank == 0) {
            for (int i = 1; i < file; i++) {
                if (board.getBoard()[startRank][startFile + i] != null) {
                    return false;
                }
            }
        }
        if (file < 0 && rank == 0) {
            for (int i = Math.abs(file)-1; i > 0; i--) {
                if (board.getBoard()[startRank][startFile - i] != null) {
                    return false;
                }
            }
        }
        if (file == 0 && rank > 0) {
            for (int i = 1; i < rank; i++) {
                if (board.getBoard()[startRank + i][startFile] != null) {
                    return false;
                }
            }
        }
        if (file == 0 && rank < 0) {
            for (int i = Math.abs(rank)-1; i > 0; i--) {
                if (board.getBoard()[startRank - i][startFile] != null) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * isKingInCheck checks if the king is in check by a queen
     *
     * @param board Board object where the game is being played on
     * @return true if the queen could kill a king if the king is not moved immediately
     */
    @Override
    public boolean isKingInCheck(Board board) {
        int rank = 0;
        int file = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] == this) {
                    rank = i;
                    file = j;
                    break;
                }
            }
        }
        boolean foundCheck = false; //bottom right to top left
        boolean foundCheck2 = false; //bottom left to top right
        boolean foundCheck3 = false; // up to down
        boolean foundCheck4 = false; // left to right
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
        // FINDING CHECK (STRAIGHT)

        if (kingRank == rank || kingFile == file) {
            //down
            if (kingRank > rank && kingFile == file) {
                for (int i = 1; rank + i <= kingRank; i++) {
                    if (board.getBoard()[rank + i][file] != null) {
                        if (board.getBoard()[rank + i][file] instanceof King && board.getBoard()[rank + i][file].getColor() != this.getColor()) {
                            foundCheck3 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //up
            else if (kingRank < rank && kingFile == file) {
                for (int i = 1; rank - i >= kingRank; i++) {
                    if (board.getBoard()[rank - i][file] != null) {
                        if (board.getBoard()[rank - i][file] instanceof King && board.getBoard()[rank - i][file].getColor() != this.getColor()) {
                            foundCheck3 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //left
            else if (kingRank == rank && kingFile > file) {
                for (int i = 1; file + i <= kingFile; i++) {
                    if (board.getBoard()[rank][file+i] != null) {
                        if (board.getBoard()[rank][file+i] instanceof King && board.getBoard()[rank][file+i].getColor() != this.getColor()) {
                            foundCheck4 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //right
            else if (kingRank == rank && kingFile < file) {
                for (int i = 1; file - i >= kingFile; i++) {
                    if (board.getBoard()[rank][file-i] != null) {
                        if (board.getBoard()[rank][file-i] instanceof King && board.getBoard()[rank][file-i].getColor() != this.getColor()) {
                            foundCheck4 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
        }
        // FINDING CHECK (DIAGONAL)
        //bottom left
        else {
            if (kingRank > rank && kingFile < file) {
                for (int i = 1; rank + i <= kingRank && file - i >= kingFile; i++) {
                    if (board.getBoard()[rank + i][file - i] != null) {
                        if (board.getBoard()[rank + i][file - i] instanceof King && board.getBoard()[rank + i][file - i].getColor() != this.getColor()) {
                            foundCheck2 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //bottom right
            else if (kingRank > rank && kingFile > file) {
                for (int i = 1; rank + i <= kingRank && file + i <= kingFile; i++) {
                    if (board.getBoard()[rank + i][file + i] != null) {
                        if (board.getBoard()[rank + i][file + i] instanceof King && board.getBoard()[rank + i][file + i].getColor() != this.getColor()) {
                            foundCheck = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //top left
            else if (kingRank < rank && kingFile < file) {
                for (int i = 1; rank - i >= kingRank && file - i >= kingFile; i++) {
                    if (board.getBoard()[rank - i][file - i] != null) {
                        if (board.getBoard()[rank - i][file - i] instanceof King && board.getBoard()[rank - i][file - i].getColor() != this.getColor()) {
                            foundCheck = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            //top right
            else if (kingRank < rank && kingFile > file) {
                for (int i = 1; rank - i >= kingRank && file + i <= kingFile; i++) {
                    if (board.getBoard()[rank - i][file + i] != null) {
                        if (board.getBoard()[rank - i][file + i] instanceof King && board.getBoard()[rank - i][file + i].getColor() != this.getColor()) {
                            foundCheck2 = true;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
        }

        if (foundCheck) {
            //System.out.println("Check");
            king.addMap(kingRank, kingFile, this);
            king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
            if (kingRank + 1 < 8 && kingFile + 1 < 8) {
                // bottom right
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank+1, kingFile+1)), this);
            }
            if (kingRank - 1 >= 0 && kingFile - 1 >= 0){
                // top left
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank-1, kingFile-1)), this);
            }
            return true;
        }
        else if (foundCheck2) {
            //System.out.println("Check");
            king.addMap(kingRank, kingFile, this);
            king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
            if (kingRank + 1 < 8 && kingFile - 1 >= 0) {
                // bottom left
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank+1, kingFile-1)), this);
            }
            if (kingRank - 1 >= 0 && kingFile + 1 >= 0){
                // top right
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank-1, kingFile+1)), this);
            }
            return true;
        }
        else if (foundCheck3) {
            //System.out.println("Check");
            king.addMap(kingRank, kingFile, this);
            king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
            if (kingRank + 1 < 8) {
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank+1, kingFile)), this);
            }
            if (kingRank - 1 >= 0) {
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank-1, kingFile)), this);
            }
            return true;
        }
        else if (foundCheck4) {
            //System.out.println("Check");
            king.addMap(kingRank, kingFile, this);
            king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
            if (kingFile + 1 < 8) {
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile+1)), this);
            }
            if (kingFile - 1 >= 0) {
                king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile-1)), this);
            }
            return true;
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
