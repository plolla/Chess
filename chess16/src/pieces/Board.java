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

import java.util.HashMap;
/**
 * Board is a class which lays out the fields/methods for a chess board.
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */
public class Board {
    /**
     * 2D Array of Piece objects
     */
    public Piece[][] board;

    /**
     * The characters in columnMappings are mapped to an integer representing the column
     */
    HashMap<Character, Integer> columnMappings;

    /**
     * The integers in rowMappings are mapped to another integer representing thw row
     */
    HashMap<Integer, Integer> rowMappings;

    /**
     * Board constructor
     */
    public Board(){
        board = new Piece[8][8];
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
    //Should we be returning the piece object or a string?
    /**
     * inRange checks if a rank and file is in the range of the board's bounds
     * @param rank the row of the 2D array
     * @param file the column of the 2D array
     * @return true if rank/file is in valid on board
    */
    public boolean inRange(int rank, int file){
        return (rank>=0 && rank<=7) && (file>=0 && file<=7);
    }

    /**
     * getBoard returns the 2D Piece array
     * @return Piece[][] array containing pieces
     */
    public Piece[][] getBoard() {
        return this.board;
    }
    /**
     * getPiece returns the piece on the board given the coordinates
     * @param move the user-inputted coordinates of a square
     * @return Piece object on the board
     */
    public Piece getPiece(String move) {
        String[] startEnd = move.split(" ");
        String start = startEnd[0];
        String end = startEnd[1];
        int file = columnMappings.get(start.charAt(0));
        int row = rowMappings.get(Integer.parseInt(start.charAt(1)+""));
        if(!inRange(row, file)) return null;
        return board[row][file];
    }



    /**
     * setUpBoard puts all the chess pieces on the board in their respective places
     * before the first player makes their move
     */
    public void setUpBoard() {
        for(int i = 0; i < 8; i++) {  // setup row of black pawns

            Piece pawn = new Pawn("bp", false);
            board[1][i] = pawn;
        }
        // setup the rest of black pieces

        // king
        board[0][4] = new King("bK", false, false);

        // queen
        board[0][3] = new Queen("bQ", false);

        // bishops
        board[0][2] = new Bishop("bB", false, 'L');;
        board[0][5] = new Bishop("bB", false, 'R');;

        // knights
        board[0][1] = new Knight("bN", false);;
        board[0][6] = new Knight("bN", false);

        // rooks
        board[0][0] = new Rook("bR", false, false);
        board[0][7] = new Rook("bR", false, false);

        for(int i = 0; i < 8; i++) {  // setup row of white pawns

            Piece pawn = new Pawn("wp", true);

            board[6][i] = pawn;
        }

        // setup the rest of white pieces

        // king
        board[7][4] = new King("wK", true, false);

        // queen
        board[7][3] = new Queen("wQ", true);

        // bishops
        board[7][2] = new Bishop("wB", true, 'L');
        board[7][5] = new Bishop("wB", true, 'R');


        // knights
        board[7][1] = new Knight("wN", true);
        board[7][6] = new Knight("wN", true);

        // rooks
        board[7][0] = new Rook("wR", true, false);
        board[7][7] = new Rook("wR", true, false);

    }

    /**
     * Prints board
     */
    public void printBoard() {

        int rowCount = 8;
        boolean shade = true;
        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    System.out.print(board[i][j]);
                }
                else if (shade) {
                    if(i % 2 != 0) {
                        System.out.print("##");
                    } else {
                        System.out.print("  ");
                    }

                }
                else {
                    if(i % 2 == 0) {
                        System.out.print("##");
                    } else {
                        System.out.print("  ");
                    }
                }
                System.out.print(" ");
                shade = !shade;

            }
            System.out.print(rowCount);
            rowCount--;
            System.out.println();
        }
        System.out.print(" a ");
        System.out.print(" b ");
        System.out.print(" c ");
        System.out.print(" d ");
        System.out.print(" e ");
        System.out.print(" f ");
        System.out.print(" g ");
        System.out.print(" h ");

        System.out.println();
        System.out.println();
    }

    /**
     * buildCopy creates a copy of the board passed in
     *
     * @param board Board object where the game is being played on
     * @return a Board object
     */
    public static Board buildCopy(Board board){

        Board copy = new Board();


        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j] == null) continue;
                if (board.getBoard()[i][j] instanceof Pawn) {
                    Piece piece =  board.getBoard()[i][j];
                    String s = piece.getColor() ? "wp" : "bp";
                    copy.getBoard()[i][j] = new Pawn(s, piece.getColor());
                }
                if (board.getBoard()[i][j] instanceof Bishop) {
                    Bishop piece =  (Bishop)board.getBoard()[i][j];
                    String s = piece.getColor() ? "wB" : "bB";
                    copy.getBoard()[i][j] = new Bishop(s, piece.getColor(), piece.getDistinct());
                }
                if (board.getBoard()[i][j] instanceof King) {
                    Piece piece =  board.getBoard()[i][j];
                    String s = piece.getColor() ? "wK" : "bK";
                    copy.getBoard()[i][j] = new King(s, piece.getColor(), false);
                }
                if (board.getBoard()[i][j] instanceof Knight) {
                    Piece piece =  board.getBoard()[i][j];
                    String s = piece.getColor() ? "wN" : "bN";
                    copy.getBoard()[i][j] = new Knight(s, piece.getColor());
                }
                if (board.getBoard()[i][j] instanceof Queen) {
                    Piece piece =  board.getBoard()[i][j];
                    String s = piece.getColor() ? "wQ" : "bQ";
                    copy.getBoard()[i][j] = new Queen(s, piece.getColor());
                }
                if (board.getBoard()[i][j] instanceof Rook) {
                    Piece piece =  board.getBoard()[i][j];
                    String s = piece.getColor() ? "wR" : "bR";
                    copy.getBoard()[i][j] = new Rook(s, piece.getColor(), false);
                }
            }
        }

        return copy;
    }

}
