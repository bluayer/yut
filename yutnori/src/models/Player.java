package models;

import java.util.ArrayList;

public class Player {
  // Using two dimension ArrayList to hold players and player's pieces info
  private ArrayList<ArrayList<Piece>> players;
  private int pieceNumber;
  private int playerNumber;

  private ArrayList<ArrayList<Integer>> playerResult;

  Player(int playerNumber, int pieceNumber){
    this.playerNumber = playerNumber;
    this.pieceNumber = pieceNumber;
    players = new ArrayList<ArrayList<Piece>>();
    playerResult = new ArrayList<ArrayList<Integer>>();

    for(int i = 0; i < playerNumber; i++){
      ArrayList<Piece> pieces = new ArrayList<Piece>();
      for(int j = 0; j < pieceNumber; j++){
        // piece id is combination of row and column.
        pieces.add(new Piece(0,0,i,i*10+j));
      }
      players.add(pieces);

      ArrayList<Integer> playerR = new ArrayList<Integer>();
      playerResult.add(playerR);
    }
  }

  // get Piece by piece id
  public Piece getPieceByPieceId(int pieceId){
    if(pieceId/10 >= playerNumber && pieceId%10 >= pieceNumber){
      return null;
    }

    return players.get(pieceId/10).get(pieceId%10);
  }
  // get Piece by location of piece
  public Piece getPieceByLocation(int row, int column){
    for(int i = 0; i < playerNumber; i++){
      for( Piece j : players.get(i)){
        if(j.getRow() == row && j.getColumn() == column){
          return j;
        }
      }
    }
    return null;
  }
  // get one player's pieces.
  public ArrayList<Piece> getPlayerPieces(int playerId){
    return players.get(playerId);
  }


  public int getLeftNumOfPieceOfPlayer(int playerId){
    try{
      int numOfLeftPieces = 0;
      for(Piece i : getPlayerPieces(playerId)){
        if(!i.isGone() && i.isOutOfBoard()){
          numOfLeftPieces++;
        }
      }
      //System.out.println("left piece" + numOfLeftPieces);
      return numOfLeftPieces;
    } catch (NullPointerException e){
      /* error handle */
    }
    return -1;
  }


  // return player id who's pieces are all get to the finish line
  public int getWinnerPlayerId(){
    for(int i = 0; i < playerNumber; i++) {
      int numOfGonePiece = 0;
      for (Piece j : players.get(i)) {
        if (!j.isGone()) {
          numOfGonePiece++;
        }
      }
      if(numOfGonePiece == 0){
        return i;
      }
    }
    return -1;
  }

  public int getNumOfPiecesOutOfBoard(int playerId){
    int numOfPieceLocateInOutOfBoard = 0;
    for(Piece i : getPlayerPieces(playerId)){
      if(i.isOutOfBoard()){
        numOfPieceLocateInOutOfBoard++;
      }
    }
    return numOfPieceLocateInOutOfBoard;
  }

  public int getPieceIdFromOutOfBoard(int playerId){
    for(Piece i : getPlayerPieces(playerId)){
      if(i.isOutOfBoard()){
        if(!i.isGone())
          return i.getId();
      }
    }
    return -1;
  }

  public ArrayList<Integer> getPlayerResult(int playerId) {
    return playerResult.get(playerId);
  }
}
