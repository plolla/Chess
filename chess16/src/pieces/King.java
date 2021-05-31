package pieces;

import java.util.*;

/**
 * King is a subclass of the abstract Piece class and King.java provides the
 * implementation needed for a king chess piece
 *
 * @author Prathik Lolla
 * @author Khush Tated
 *
 */

public class King extends Piece {
    /**
     * hashmap that contains a list of indices the king could possibly move to on the board and if these
     * positions are mapped to a piece that means the king cannot move there
     */
    public HashMap<ArrayList<Integer>, Piece> map = new HashMap<>();

    /**
     * King constructor
     *
     * @param pieceName the name of the piece
     * @param isWhite if the piece is white or not
     * @param didMove if the king had been moved by a player
     */
    public King(String pieceName, boolean isWhite, boolean didMove) {
        super(pieceName, isWhite, didMove);
    }

    /**
     * Adds/Updates the hashmap of the king's potential moves
     *
     * @param key the row
     * @param value the column
     * @param piece Piece object
     */
    public void addMap(int key, int value, Piece piece){
        map.put(new ArrayList<Integer>(Arrays.asList(key, value)), piece);
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

        if (board.getBoard()[startRank][startFile] == null) {
            return false;
        }
        int endRank = rowMappings.get(Integer.parseInt(end.charAt(1)+""));
        int endFile = columnMappings.get(end.charAt(0));

        int file = Math.abs(endFile - startFile);
        int rank = Math.abs(endRank - startRank);


        // white castle on king's side
        if (move.trim().equals("e1 g1")){
            return whiteKingSideCastle(board, startRank, startFile, endRank, endFile);
        }
        // white castle on queen's side
        else if (move.trim().equals("e1 c1")) {
            return whiteQueenSideCastle(board, startRank, startFile, endRank, endFile);
        }
        //black castle on king's side
        else if (move.trim().equals("e8 g8")) {
            return blackKingSideCastle(board, startRank, startFile, endRank, endFile);
        }
        //black castle on queen's side
        else if (move.trim().equals("e8 c8")) {
            return blackQueenSideCastle(board, startRank, startFile, endRank, endFile);
        }
        else if ( (file == 1 && rank == 0) || (file == 0 && rank == 1) || (file == 1 && rank == 1)) {
            if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() != this.getColor()) {
                board.getBoard()[endRank][endFile] = null;
                board.getBoard()[startRank][startFile] = null;
                board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
                return true;
            }
            else if (board.getBoard()[endRank][endFile] != null && board.getBoard()[endRank][endFile].getColor() == this.getColor()){
                return false;
            }
            board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
            board.getBoard()[startRank][startFile] = null;
            return true;
        }

        return false;
    }

    /**
     * whiteKingSideCastle checks if both the white king and the white king's side rook haven't moved and if
     * a castle between them is possible. If it is the method will complete the move.
     *
     *
     * @param board Board object where the game is being played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in the path
     */
    public boolean whiteKingSideCastle(Board board, int startRank, int startFile, int endRank, int endFile) {
        //white king's side
        if (board.getBoard()[startRank][startFile].didItMove() || board.getBoard()[startRank][startFile+3].didItMove()) {
            return false;
        }
        if (board.getBoard()[startRank][startFile].getColor()
                && board.getBoard()[startRank][startFile+3].getPieceName().equals("wR") ) {
            for (int i = 1; startFile + i < 7; i++) {
                if (board.getBoard()[startRank][startFile + i] != null) {
                    return false;
                }
            }
            board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
            board.getBoard()[startRank][startFile] = null;
            board.getBoard()[endRank][endFile - 1] = new Rook("wR", true, true);
            board.getBoard()[7][7] = null;
        }

        return true;
    }
    /**
     * blackKingSideCastle checks if both the black king and the black king's side rook haven't moved and if
     * a castle between them is possible. If it is the method will complete the move.
     *
     *
     * @param board Board object where the game is being played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in the path
     */
    public boolean blackKingSideCastle(Board board, int startRank, int startFile, int endRank, int endFile) {
        //black king's side
        if (board.getBoard()[startRank][startFile].didItMove() || board.getBoard()[startRank][startFile+3].didItMove()) {
            return false;
        }
        if (!board.getBoard()[startRank][startFile].getColor()
                && board.getBoard()[startRank][startFile+3].getPieceName().equals("bR")) {

            for (int i = 1; startFile + i < 7; i++) {
                if (board.getBoard()[startRank][startFile + i] != null) {
                    return false;
                }
            }
            board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
            board.getBoard()[startRank][startFile] = null;
            board.getBoard()[endRank][endFile - 1] = new Rook("bR", false, true);
            board.getBoard()[0][7] = null;
        }

        return true;
    }
    /**
     * whiteQueenSideCastle checks if both the white king and the white queen's side rook haven't moved and if
     * a castle between them is possible. If it is the method will complete the move.
     *
     *
     * @param board Board object where the game is being played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in the path
     */
    public boolean whiteQueenSideCastle(Board board, int startRank, int startFile, int endRank, int endFile) {
        //white queen's side
        if (board.getBoard()[startRank][startFile].didItMove() || board.getBoard()[startRank][startFile-4].didItMove()) {
            return false;
        }
        if (board.getBoard()[startRank][startFile].getColor()
                && board.getBoard()[startRank][startFile-4].getPieceName().equals("wR")) {
            for (int i = endFile-1; i > 0; i--) {
                if (board.getBoard()[startRank][startFile - i] != null) {
                    return false;
                }
            }
        }
        board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
        board.getBoard()[startRank][startFile] = null;

        board.getBoard()[endRank][endFile + 1] = new Rook("wR", true, true);
        board.getBoard()[7][0] = null;
        return true;

    }
    /**
     * blackQueenSideCastle checks if both the black king and the black queen's side rook haven't moved and if
     * a castle between them is possible. If it is the method will complete the move.
     *
     *
     * @param board Board object where the game is being played on
     * @param startRank the row of the current position of the piece
     * @param startFile the column of the current position of the piece
     * @param endRank the row of the desired position
     * @param endFile the column of the desired position
     * @return true if there are no pieces in the path
     */
    public boolean blackQueenSideCastle(Board board, int startRank, int startFile, int endRank, int endFile) {
        //white queen's side
        if (board.getBoard()[startRank][startFile].didItMove() || board.getBoard()[startRank][startFile-4].didItMove()) {
            return false;
        }
        if (board.getBoard()[startRank][startFile].getColor()
                && board.getBoard()[startRank][startFile-4].getPieceName().equals("bR")) {
            for (int i = endFile-1; i > 0; i--) {
                if (board.getBoard()[startRank][startFile - i] != null) {
                    return false;
                }
            }
        }
        board.getBoard()[endRank][endFile] = new King(getPieceName(), getColor(), true);
        board.getBoard()[startRank][startFile] = null;

        board.getBoard()[endRank][endFile + 1] = new Rook("bR", false, true);
        board.getBoard()[0][0] = null;
        return true;

    }
    /**
     * generateKeys() creates the initial hashmap of possibilities where king could move.
     * @param board Board object where the game is being played on
     */
    public void generateKeys(Board board) { //MAKE SURE WE CALL THIS METHOD FOR BOTH KINGS WHEN GENERATING THE BOARD TO START OFF WITH?
        map = new HashMap<>();
        int initialRow = -1;
        int initialCol= -1;
        for(int i =0; i<board.getBoard().length; i++){ //Locate king coordinates
            for(int j = 0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j] instanceof King && board.getBoard()[i][j].getColor() == getColor()){
                    initialRow = i;
                    initialCol = j;
                }
            }
        }
        //Add everything possible to hashmap.
        int[] step1 = new int[]{initialRow, initialCol};
        map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol)), null);
        try{
            if(board.getBoard()[initialRow+1][initialCol]==null || board.getBoard()[initialRow+1][initialCol].getColor()!=this.getColor() )
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol]==null || board.getBoard()[initialRow-1][initialCol].getColor()!=this.getColor())
                map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow][initialCol+1]==null || board.getBoard()[initialRow][initialCol+1].getColor()!=this.getColor())
                map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol+1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow][initialCol-1]==null || board.getBoard()[initialRow][initialCol-1].getColor()!=this.getColor())
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow+1][initialCol+1]==null || board.getBoard()[initialRow+1][initialCol+1].getColor()!=this.getColor())
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol+1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol-1]==null || board.getBoard()[initialRow-1][initialCol-1].getColor()!=this.getColor())
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow+1][initialCol-1]==null || board.getBoard()[initialRow+1][initialCol-1].getColor()!=this.getColor())
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol+1]==null || board.getBoard()[initialRow-1][initialCol+1].getColor()!=this.getColor())
            map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol+1)), null);

        }
        catch (Exception e){}
        return;
    }
    /**
     * isKingINCheck returns whether this king is in check
     *
     * @param board Board object where the game is being played on
     * @return True if this king is in check.
     */
    @Override
    public boolean isKingInCheck(Board board) {
        for(ArrayList<Integer> key : map.keySet()){
            if(map.get(key)!=null) return true;
        }
        return false;
    }
    /**
     * simulateKingRadius essentially moves the king to a square on the board. The purpose of this method is to see when a king is in check, if it is able to move to other squares.
     * This method will check whether surrounding squares are valid. If not, the square's coordinates will be mapped to the opposing player's piece that can kill it.
     *
     * @param board Board object where the game is being played on
     */
    public void simulateKingRadius(Board board){
        int rank =0;
        int file =0;
        //Locate where the current king is
        for(int i =0; i<board.getBoard().length; i++){
            for(int j =0 ;j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j] == this){
                    rank = i;
                    file = j;
                }
            }
        }
        Set<ArrayList<Integer>> set = map.keySet();

        for(ArrayList<Integer> s : set){
            //Something is already checking it so don't worry about it.
            if(map.get(s)!=null) continue;
            int row = s.get(0);
            int col = s.get(1);
            Board copy = Board.buildCopy(board);
            King king = getColor() ? findWhiteKing(copy) : findBlackKing(copy);
            copy.getBoard()[rank][file] = null;
            copy.getBoard()[row][col] = king;
            king.generateKeys(copy, king);
            king.editMaps(copy);
            map.put(s, king.map.get(s));

        }

    }
    /**
     * generateKeys() creates the initial hashmap of possibilities where king could move.
     * This is different in that it also takes a refernece to the King object you're looking at.
     * @param board Board object where the game is being played on
     * @param king King object.
     */
    public void generateKeys(Board board, King king) { //MAKE SURE WE CALL THIS METHOD FOR BOTH KINGS WHEN GENERATING THE BOARD TO START OFF WITH?
        king.map = new HashMap<>();
        int initialRow = -1;
        int initialCol= -1;
        for(int i =0; i<board.getBoard().length; i++){ //Locate king coordinates
            for(int j = 0; j<board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j] instanceof King && board.getBoard()[i][j].getColor() == getColor()){
                    initialRow = i;
                    initialCol = j;
                }
            }
        }
        //Add everything possible to hashmap.
        int[] step1 = new int[]{initialRow, initialCol};
        king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol)), null);
        try{
            if(board.getBoard()[initialRow+1][initialCol]==null || board.getBoard()[initialRow+1][initialCol].getColor()!=this.getColor() )
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol]==null || board.getBoard()[initialRow-1][initialCol].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow][initialCol+1]==null || board.getBoard()[initialRow][initialCol+1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol+1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow][initialCol-1]==null || board.getBoard()[initialRow][initialCol-1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow+1][initialCol+1]==null || board.getBoard()[initialRow+1][initialCol+1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol+1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol-1]==null || board.getBoard()[initialRow-1][initialCol-1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow+1][initialCol-1]==null || board.getBoard()[initialRow+1][initialCol-1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow+1, initialCol-1)), null);

        }
        catch (Exception e){}
        try{
            if(board.getBoard()[initialRow-1][initialCol+1]==null || board.getBoard()[initialRow-1][initialCol+1].getColor()!=this.getColor())
                king.map.put(new ArrayList<Integer>(Arrays.asList(initialRow-1, initialCol+1)), null);

        }
        catch (Exception e){}
        return;
    }
    /**
     * editMaps populates a king's hashmap to check to see if the square it's on and squares around it are able to be occupied.
     * @param board Board object where the game is being played on
     */
    private void editMaps(Board board){
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
    private King findBlackKing(Board board){
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
    private King findWhiteKing(Board board){
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
     * toString gets the name of the piece
     *
     * @return name of piece
     */
    public String toString(){
        return this.getPieceName();
    }
}
