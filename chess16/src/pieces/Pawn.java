/*
Rank: Row (side to side), numbers
File: Column (up down), letters
* A - 0
* B - 1
* C - 2
* D - 3
* E - 4
* F - 5
* G - 6
* H - 7
* */
package pieces;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pawn is a subclass of the abstract Piece class and Pawn.java provides the
 * implementation needed for a pawn chess piece
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */

public class Pawn extends Piece {
    /**
    * Checks if a pawn has moved twice from base
    * */
    boolean doubleMove = false;
    /**
     * Sees what direction a pawn has to move. If pawn is black, it moves downwards on the board (1). If pawn is white it moves upwards (-1).
     * */
    int delta = getColor() ? -1 : 1;

    /**
     * Pawn constructor
     *
     * @param pieceName the name of the piece
     * @param isWhite if the piece is white or not
     */
    public Pawn(String pieceName, boolean isWhite) {
        super(pieceName, isWhite);
    }
    /**
     * Pawn constructor
     *
     * @param pieceName the name of the piece
     * @param isWhite if the piece is white or not
     * @param doubleMove if the pawn has moved twice from base. Note that this attribute is only relevant immediately after it double moves.
     */
    public Pawn (String pieceName, boolean isWhite, boolean doubleMove){
        super(pieceName, isWhite);
        this.doubleMove = doubleMove;
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
        int fileStart = columnMappings.get(start.charAt(0));
        int rankStart = rowMappings.get(Integer.parseInt(start.charAt(1)+"")) ;
        int fileEnd = columnMappings.get(end.charAt(0));
        int rankEnd = rowMappings.get(Integer.parseInt(end.charAt(1)+""));

        /*
        System.out.println(rankStart+" "+fileStart);
        System.out.println(rankEnd+" "+fileEnd);
        */
        //Either you're in the same file and you just move up 1 rank.
        if(fileStart == fileEnd && rankStart + delta == rankEnd && board.getBoard()[rankEnd][fileEnd] == null){
            // HOW DO WE UPDATE THE BOARD WITH THE NEW POSITION OF THE PIECE?
            //CHANGE THE START FILE AND START RANK TO NULL?
            board.getBoard()[rankEnd][fileEnd] = new Pawn(getPieceName(), getColor());
            board.getBoard()[rankStart][fileStart] = null;
            if(rankEnd == 0 && getColor()) promotion(board, move); //Promotion Check
            if(rankEnd==7 && !getColor()) promotion(board, move); //Promotion Check
            return true;
        }
        //Or you move diag to kill piece of the opposite color or do enpassant
        if((fileStart==fileEnd+1 || fileStart == fileEnd-1) && rankStart + delta == rankEnd){
            //Diag Kill
            if(board.getBoard()[rankEnd][fileEnd]!=null && board.getBoard()[rankEnd][fileEnd].getColor()!=this.getColor()){
                board.getBoard()[rankEnd][fileEnd] = null;
                board.getBoard()[rankStart][fileStart] = null;
                board.getBoard()[rankEnd][fileEnd] = new Pawn(getPieceName(), getColor());
                if(rankEnd == 0 && getColor()) promotion(board, move); //Promotion Check
                if(rankEnd==7 && !getColor()) promotion(board, move); //Promotion Check
                return true;
            }
            //Ennpassant
            if(board.getBoard()[rankStart][fileEnd]!=null && board.getBoard()[rankStart][fileEnd] instanceof Pawn && board.getBoard()[rankStart][fileEnd].getColor()!=this.getColor() && ((Pawn) board.getBoard()[rankStart][fileEnd]).doubleMove){
                board.getBoard()[rankStart][fileEnd] = null;
                board.getBoard()[rankStart][fileStart] = null;
                board.getBoard()[rankEnd][fileEnd] = new Pawn(getPieceName(), getColor());
                return true;
            }
        }
        //Finally u can have a pawn from base that moves two spaces up
        if(Math.abs(rankEnd - rankStart) == 2 && fileEnd == fileStart){
            //Checks if the space in front of it is occupied
            if(board.getBoard()[rankEnd-delta][fileEnd]!=null){
                return false;
            }
            //White double check
            if(getColor() && rankStart==rowMappings.get(2) && board.getBoard()[rankEnd][fileEnd]==null){
                board.getBoard()[rankEnd][fileEnd] = new Pawn(getPieceName(), getColor(), true);
                board.getBoard()[rankStart][fileStart] = null;
                return true;
            }
            //Black double check
            if(getColor()==false && rankStart==rowMappings.get(7) && board.getBoard()[rankEnd][fileEnd]==null){
                board.getBoard()[rankEnd][fileEnd] = new Pawn(getPieceName(), getColor(), true);
                board.getBoard()[rankStart][fileStart] = null;
                return true;
            }
        }
        return false;
    }
    /**
     * promotion sees if your pawn has moved to the end of the board. If it has, it can either create a new queen (default) or a new piece of your choice.
     *
     * @param board Board object where the game is being played on
     * @param move the input of the player containing the current position of the piece as well as the end position
     */
    private void promotion(Board board, String move){
        String[] array = move.split(" ");
        String initialChar = getColor() ? "w" : "b";
        Piece promoPiece;
        if(array.length == 3){
            promoPiece = getPromoPiece(array[2]);
        }
        else{
            initialChar+="Q";
            promoPiece = new Queen(initialChar, getColor());
        }
        String start = array[0];
        String end = array[1];
        int fileStart = columnMappings.get(start.charAt(0));
        int rankStart = rowMappings.get(Integer.parseInt(start.charAt(1)+"")) ;
        int fileEnd = columnMappings.get(end.charAt(0));
        int rankEnd = rowMappings.get(Integer.parseInt(end.charAt(1)+""));
        board.getBoard()[rankEnd][fileEnd] = null;
        board.getBoard()[rankEnd][fileEnd] = promoPiece;
        return;
    }
    /**
     * isKingInCheck checks if the king is in check by a pawn
     *
     * @param board Board object where the game is being played on
     * @return true if the pawn could kill a king if the king is not moved immediately
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

        boolean foundCheck = false;
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
        if(rank+delta == kingRank && (file+1 == kingFile || file-1 == kingFile)){
            king.addMap(kingRank, kingFile, this);
            king.map.put(new ArrayList<Integer>(Arrays.asList(kingRank, kingFile)), this);
            //System.out.println("Check");
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

    /**
     * getPromoPiece returns the new piece a player wants after reaching a promotion.
     * @param string THe name of the piece a user wants
     * @return Piece object.
     */
    private Piece getPromoPiece(String string){
        String colorName = "";
        boolean color  = getColor();
        if(color) colorName+="w";
        else colorName +="b";

        if(string.equals("N")){
            return new Knight(colorName+"N", getColor());
        }
        if(string.equals("R")){
            return new Rook(colorName+"R", getColor(), true);
        }
        if(string.equals("B")){
            return new Bishop(colorName+"B", getColor(), 'P');
        }
        return new Queen(colorName+"Q", getColor());
    }
}
