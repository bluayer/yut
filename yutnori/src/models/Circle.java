package models;

import java.util.ArrayList;

public class Circle extends ClickableGameObject {

  private boolean occupied;
  // Holding id of pieces which occupying the circle
  private ArrayList<Integer> occupyingPieces;
  private int numOfoccupyingPieces;


  Circle(int circleId, int row, int column){
    setId(circleId);
    setLocation(row,column);
    this.occupyingPieces = new ArrayList<Integer>();
    this.setClickable();
    occupied = false;
    numOfoccupyingPieces = 0;
  }

  // related with occupying propertys
  public boolean isOccupied(){
    return occupied;
  }
  public void setOccupied(){
    occupied = true;
  }
  public void resetOccupied(){
    occupied = false;
  }
  // manage occupying pieces
  public ArrayList<Integer> getOccupyingPieces(){
    return occupyingPieces;
  }
  public void addOccupyingPieces(int pieceId){
    occupyingPieces.add(pieceId);
    numOfoccupyingPieces++;
  }
  public void clearOccupyingPieces(){
    occupyingPieces.clear();
  }
  // manage number of occupying pieces.
  public int getNumOfoccupyingPieces(){
    return numOfoccupyingPieces;
  }
  public void resetNumOfoccupyingPieces(){
    numOfoccupyingPieces = 0;
  }
  public void resetCircle(){
    resetOccupied();
    resetNumOfoccupyingPieces();
    clearOccupyingPieces();
  }

}
