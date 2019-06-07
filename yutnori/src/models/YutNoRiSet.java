/**
 * @filename YutNoRiSet.java
 *
 */

package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;

public class YutNoRiSet {
  private Player player;
  private Board board;
  private YutSet yutSet;
  private RuleTable ruleTable;

  int numOfPlayer;
  int numOfPiece;
  ArrayList<Integer> movable;

  private int playerTurn;
  private int inGameFlag;
  private PropertyChangeSupport observable;

  // Set board, ruleTable and yutSet, the setPlayer must be called to start the game.
  public YutNoRiSet(){
    board = new Board();
    yutSet = new YutSet();
    ruleTable = new RuleTable();
    observable = new PropertyChangeSupport(this);
    movable = new ArrayList<Integer>();
  }

  // Set board, yutSet, player and ruleTable with the parameters.
  public YutNoRiSet(int numOfPlayer, int numOfPiece){
    player = new Player(numOfPlayer, numOfPiece);
    board = new Board();
    yutSet = new YutSet();
    ruleTable = new RuleTable();
    this.numOfPlayer = numOfPlayer;
    this.numOfPiece = numOfPiece;
    observable = new PropertyChangeSupport(this);
  }

  // Make instance of Player with the parameters.
  public void setPlayer(int numberOfPlayer, int numberOfPiece){
    player = new Player(numberOfPlayer, numberOfPiece);
    this.numOfPlayer = numberOfPlayer;
    this.numOfPiece = numberOfPiece;
  }

  public void addObserver(PropertyChangeListener observer){
    this.observable.addPropertyChangeListener(observer);
  }

  public int getNumOfPlayer(){
    return numOfPlayer;
  }
  public int getNumOfPiece (){
    return numOfPiece;
  }

  public Player getPlayer(){
    return player;
  }

  public Board getBoard(){
    return board;
  }

  public YutSet getYutSet(){
    return yutSet;
  }

  public int getPlayerTurn(){
    return playerTurn;
  }

  public void setPlayerTurn(int turn){
    this.playerTurn = turn;
    observable.firePropertyChange("player change", false, true);
  }

  public RuleTable getRuleTable() {
    return ruleTable;
  }

  // Check the next circle to move.
  // Return true if catch succeed, else return false.
  public boolean tryCatch(int pieceId, int row, int column){
    Piece targetPiece = player.getPieceByPieceId(pieceId);

    // Next circle.
    Circle nextCircle = board.getCircleByLocation(row,column);

    // Check if the next circle occupied.
    if(nextCircle.isOccupied()){
      Piece occupyingPiece =
        player.getPieceByPieceId(nextCircle.getOccupyingPieces().get(0));
      // Check if the owner of that piece is different.
      // if different, reset that circle and return true to represent catch succeed.
      if(occupyingPiece.getOwnerId() != targetPiece.getOwnerId()){
        occupyingPiece.resetPieceToOrigin();
        nextCircle.clearOccupyingPieces();
        return true;
      }
    }
    return false;
  }

  // Get movable circle from point of piece.
  // Contain 2 ArrayList<Integer> type.
  // First element is circle ids could go.
  // Second element is result which mapped with circle ids of first element.
  ArrayList<ArrayList<Integer>> getMovableCircleIds(int pieceId, ArrayList<Integer> result){
    ArrayList<ArrayList<Integer>> movableCircle = new ArrayList<>();

    // Get player Piece and get the circle id of that location to use rule table.
    Piece targetPiece = player.getPieceByPieceId(pieceId);
    Circle currentCircle =
      board.getCircleByLocation(targetPiece.getRow(), targetPiece.getColumn());

    for(int i : result){
      // Circle ids for next move
      ArrayList<Integer> ids = new ArrayList<>();
      // Mapping the result with the circle id.
      ArrayList<Integer> results = new ArrayList<>();

      // Get movable circle ids which is array to represent two way.
      int[] nextMovableCircleIds = ruleTable.getNextMoveCircleIds(currentCircle.getId(), i);

      ids.add(nextMovableCircleIds[0]);
      results.add(i);

      // If the second value of nextMovableCircleIds is -1, the piece could go only one way.
      if(nextMovableCircleIds[1] != -1){
        ids.add(nextMovableCircleIds[1]);
        results.add(i);
      }
      movableCircle.add(ids);
      movableCircle.add(result);
    }

    return movableCircle;
  }

  public int getClickedResult(int pieceId, int row, int column){
    Piece targetPiece = player.getPieceByPieceId(pieceId);
    Circle currentCircle;
    if(player.getPieceByPieceId(pieceId).isOutOfBoard()){
      currentCircle = board.getCircleByLocation(7, 7);
    }else {
      currentCircle = board.getCircleByLocation(targetPiece.getRow(), targetPiece.getColumn());
    }
    int nextCicleId = board.getCircleByLocation(row, column).getId();
    for(int i = 0; i < 6; i++){
      int[] getResult = ruleTable.nextMoveTable[currentCircle.getId()][i];
      if(getResult[0] == nextCicleId  || getResult[1]  == nextCicleId) {
        return i;
      }
    }
    return -1;
  }

  public void showMovable(int pieceId){
    // Get player Piece and get the circle id of that location to use rule table.
    Piece targetPiece = player.getPieceByPieceId(pieceId);
    Circle currentCircle;
    System.out.println("Piece is"  + pieceId);
    if(targetPiece.isOutOfBoard()) {
      currentCircle = board.getCircleByLocation(7, 7);
    } else {
      currentCircle = board.getCircleByLocation(targetPiece.getRow(), targetPiece.getColumn());
    }
    // Change circle state changeable.
    System.out.println("showMovablecalled! current Circle is " + currentCircle.getId());
    for(int i : player.getPlayerResult(targetPiece.getOwnerId())){
      int[] nextMovableCircleIds = ruleTable.getNextMoveCircleIds(currentCircle.getId(), i);

      board.getCircleByCircleId(nextMovableCircleIds[0]).setChangeable();
      board.getCircleByCircleId(nextMovableCircleIds[0]).getId();
      movable.add(nextMovableCircleIds[0]);
      // If this is two way circle
      if(nextMovableCircleIds[1] != -1){
        board.getCircleByCircleId(nextMovableCircleIds[1]).setChangeable();
        movable.add(nextMovableCircleIds[1]);
      }
    }

    observable.firePropertyChange("movable",false, true);
  }

  public ArrayList<Integer> getMovable() { return movable; }


  public void move(int pieceId, int row, int column){
    for(int i = 0; i < player.getPlayerResult(playerTurn).size(); i++){
      System.out.println(playerTurn + " " + player.getPlayerResult(playerTurn).get(i));
    }
    Piece targetPiece = player.getPieceByPieceId(pieceId);
    Circle lastCircle;
    if(targetPiece.isOutOfBoard()) {
      lastCircle = board.getCircleByLocation(7, 7);
      lastCircle.addOccupyingPieces(targetPiece.getId());
    } else {
      lastCircle = board.getCircleByLocation(targetPiece.getRow(), targetPiece.getColumn());
    }
    Circle nextCircle = board.getCircleByLocation(row,column);

    for(int i : lastCircle.getOccupyingPieces()){
      Piece movePiece = player.getPieceByPieceId(i);
      movePiece.setLocation(nextCircle.getRow(), nextCircle.getColumn());
      nextCircle.addOccupyingPieces(i);
    }
    targetPiece.setOutOfBoard(false);
    nextCircle.setOccupied();
    lastCircle.resetCircle();
    // board.setAllUnChangeable();
    // observable.firePropertyChange("move", true, false);
  }

  public int getInGameFlag(){
    return inGameFlag;
  }

  public void setInGameFlag(){
    inGameFlag = (inGameFlag+1)%3;
    observable.firePropertyChange("turn change", false, true);
  }
}