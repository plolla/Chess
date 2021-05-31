package chess;

import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Chess is the main class. This controls how gameplay flows.
 *
 * @author Prathik Lolla
 * @author Khush Tated
 */
public class Chess {
    /**
     * main method. This controls how gameplay will flow.
     */
    public static void main(String[] args) {

        Scanner sn = new Scanner(System.in);  // reads input from user
        Board board = new Board();
        board.setUpBoard();
        board.printBoard();
        boolean whiteMove = true;
        boolean draw = false;
        boolean whiteCheck = false;
        boolean blackCheck = false;
        while(true){
           if(whiteMove){
               System.out.println("It is white's turn");
           }
           else{
               System.out.println("It is black's turn");
           }
           boolean legal = true;
            generateAllMaps(board);
            editMaps(board);

            String line = sn.nextLine();
            String[] arr = line.split(" ");
            String input1 = arr[0];
            if(input1.equals("resign")){
                if(whiteMove){
                    System.out.println("Black wins");
                    break;
                }
                if(!whiteMove){
                    System.out.println("White wins");
                    break;
                }
            }

            if(input1.equals("draw") && draw){
                break;
            }
            if(draw && arr.length!=1){
                System.out.println("Illegal move, try again");
                continue;
            }

            String input2 = arr[1];
            String input = input1+" "+input2;
            if(arr.length == 3){
                if(arr[2].equals("draw?")){
                    draw = true;
                }
            }
            Piece piece = board.getPiece(input);

            try{

                if((whoIsPlaying(board, input).equals("White") && !whiteMove) || (whoIsPlaying(board, input).equals("Black") &&whiteMove)){
                    System.out.println("Illegal move, try again [it is not your turn]");
                    continue;
                }
                boolean whiteKingCheck = findWhiteKing(board).isKingInCheck(board);
                boolean blackKingCheck = findBlackKing(board).isKingInCheck(board);

                //If white king is in check, and player is white,
                // simulate the next move and see if you're still in check
                Board copy = Board.buildCopy(board);
                if(whiteKingCheck && whiteMove){
                    //First check if its a valid move.
                    if(piece!=null){
                        if(piece.validMove(copy, input)){
                            //Is white king still in check?
                            editMaps(copy);
                            boolean stillInCheck = findWhiteKing(copy).isKingInCheck(copy);
                            if(stillInCheck){
                                System.out.println("Illegal move, try again ");
                                legal = false;
                            }
                            else{
                                piece.validMove(board, input);
                            }
                        }
                        else {
                            System.out.println("Illegal move, try again ");
                            legal = false;
                        }
                    }
                    else{
                        System.out.println("Illegal move, try again ");
                        legal = false;
                    }
                }
                //If black king is in check and player is black
                else if(blackKingCheck && !whiteMove){
                    //First check if its a valid move.
                    if(piece!=null){
                        if(piece.validMove(copy, input)){
                            //Is white king still in check?
                            editMaps(copy);
                            boolean stillInCheck = findBlackKing(copy).isKingInCheck(copy);
                            if(stillInCheck){
                                System.out.println("Illegal move, try again, try again");
                                legal = false;
                            }
                            else{
                                piece.validMove(board, input);
                            }
                        }
                        else {
                            System.out.println("Illegal move, try again ");
                            legal = false;
                        }
                    }
                    else{
                        System.out.println("Illegal move, try again ");
                        legal = false;
                    }
                }
                //Else, it's a normal move where you're putting an opposing player in check/playing normally
                else{
                    if(piece!=null){
                        if(piece.validMove(copy, input)){
                            if((whiteMove && findWhiteKing(copy).isKingInCheck(copy)) || (!whiteMove && findBlackKing(copy).isKingInCheck(copy))){
                                System.out.println("Illegal move, try again ");
                                legal = false;
                            }
                            else piece.validMove(board, input);
                        }
                        else{
                            System.out.println("Illegal move, try again ");
                            legal = false;
                        }
                    }
                    else {
                        System.out.println("Illegal move, try again ");
                        legal = false;
                    }
                }
                if(legal) {
                    whiteMove = !whiteMove;
                }
                generateAllMaps(board);
                editMaps(board);
                findBlackKing(board).simulateKingRadius(board);
                findWhiteKing(board).simulateKingRadius(board);
                /*
                System.out.println("White map: " +findWhiteKing(board).map);
                System.out.println("Black map: "+findBlackKing(board).map);
                */

                //Find checks and checkmate
                HashMap<ArrayList<Integer>, Piece> whiteMap = findWhiteKing(board).map;
                HashMap<ArrayList<Integer>, Piece> blackMap = findBlackKing(board).map;
                 whiteCheck = whiteMap.get(getLocation(findWhiteKing(board), board)) != null;
                 blackCheck = blackMap.get(getLocation(findBlackKing(board), board)) != null;
                boolean blackCheckMate = true;
                boolean whiteCheckMate = true;
                for(ArrayList<Integer> list : whiteMap.keySet()){
                    whiteCheckMate = whiteCheckMate & whiteMap.get(list)!=null;
                }
                for(ArrayList<Integer> list : blackMap.keySet()){
                    blackCheckMate = blackCheckMate & blackMap.get(list)!=null;
                }

                if(whiteCheckMate || blackCheckMate){
                    if(whiteCheckMate){
                        for(Piece pieceInMap : whiteMap.values()){
                            whiteCheckMate = whiteCheckMate & !pieceInMap.canThisPieceDie(board);
                        }
                        if(whiteCheckMate){
                            System.out.println("Checkmate");
                            System.out.println("Black wins");
                            board.printBoard();
                            break;
                        }
                    }
                    if(blackCheckMate){
                        for(Piece pieceInMap : blackMap.values()){
                            blackCheckMate = blackCheckMate & !pieceInMap.canThisPieceDie(board);
                        }
                        if(blackCheckMate){
                            System.out.println("Checkmate");
                            System.out.println("White wins");
                            board.printBoard();
                            break;
                        }
                    }
                }


            }
            catch(Exception e){
                System.out.println("Illegal move, try again ");
            }

            board.printBoard();
            if(whiteCheck || blackCheck){
                System.out.println("Check");
            }
        }
    }
    /**
     * editMaps populates a king's hashmap to check to see if the square it's on and squares around it are able to be occupied.
     * @param board Board object where the game is being played on
     */
    private static void editMaps(Board board){
        for(int i =0; i<board.getBoard().length; i++){
            for(int j =0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j]!=null)
                    board.getBoard()[i][j].isKingInCheck(board);
            }
        }
    }
    /**
     * findBlackKing Looks for the black king on the chess board
     * @param board Board object where the game is being played on
     * @return Black King object.
     */
    private static King findBlackKing(Board board){
        for(int i =0; i<board.getBoard().length; i++){
            for(int j=0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j]!=null && board.getBoard()[i][j] instanceof King &&board.getBoard()[i][j].getColor()==false){
                    return (King)board.getBoard()[i][j];
                }
            }
        }
        return null;
    }
    /**
     * findWhiteKing Looks for the white king on the chess board
     * @param board Board object where the game is being played on
     * @return White King object.
     */
    private static King findWhiteKing(Board board){
        for(int i =0; i<board.getBoard().length; i++){
            for(int j=0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j]!=null && board.getBoard()[i][j] instanceof King &&board.getBoard()[i][j].getColor()){
                    return (King)board.getBoard()[i][j];
                }
            }
        }
        return null;
    }
    /**
     * generateAllMaps Creates initial hashmaps for king to see what surrounding squares can be occupied. editMaps() must be called consecutively.
     * @param board Board object where the game is being played on
     */
    private static void generateAllMaps(Board board){
        for(int i =0; i<board.getBoard().length; i++){
            for(int j=0; j<board.getBoard().length; j++){
                if(board.getBoard()[i][j] instanceof King){
                    King king = (King)(board.getBoard()[i][j]);
                    king.generateKeys(board);
                }
            }
        }
    }
    /**
     * getLocation Returns an ArrayList consisting of the row and column of a piece.
     * @param board Board object where the game is being played on
     * @param piece Piece you're looking to find location for.
     * @return ArrayList consisting of row, column.
     */
    private static ArrayList<Integer> getLocation(Piece piece, Board board){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i =0; i<board.getBoard().length; i++){
            for(int j =0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j] == piece){
                    list.add(i);
                    list.add(j);
                }
            }
        }
        return list;
    }
    /**
     * whoIsPlaying Looks at the inputted move and returns the player color.
     * @param board Board object where the game is being played on
     * @param move The move of the user.
     * @return String (Black/White) of who's playing
     */
    private static String whoIsPlaying(Board board, String move){
        String[] startEnd = move.split(" ");
        String start = startEnd[0];
        String end = startEnd[1];
        Pawn forMap = new Pawn("", true);
        int fileStart = forMap.columnMappings.get(start.charAt(0));
        int rankStart = forMap.rowMappings.get(Integer.parseInt(start.charAt(1)+"")) ;
        if(board.getBoard()[rankStart][fileStart]!=null && board.getBoard()[rankStart][fileStart].getColor()){
            return "White";
        }
        else if(board.getBoard()[rankStart][fileStart]!=null && !board.getBoard()[rankStart][fileStart].getColor()){
            return "Black";
        }
        else return "Error";

    }

}
