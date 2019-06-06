package models;

import java.util.ArrayList;
public class Board {

  // Board size
  public final int BOARDSIZE = 29;

  // Reachable direction
  final int NORMALBOARD = 1;
  final int TWOWAYBOARD = 2;

  // managing Circles
  private ArrayList<Circle> boards;

  Board(){
    boards = new ArrayList<Circle>(0);

    // Set circles location along to map.
    for(int i = 2; i < 7; i++){
      if(i==4) continue;
      boards.add(new Circle(NORMALBOARD, i, 7, 1, 0));
      boards.add(new Circle(NORMALBOARD, i, 1, -1, 0));
      boards.add(new Circle(NORMALBOARD, 7, i, 0, 1));
      boards.add(new Circle(NORMALBOARD, 1, i, 0, -1));
    }

    // Four corner.
    // Right bottom.
    boards.add(new Circle(NORMALBOARD, 1, 1, 1, 0));
    // Left bottom.
    boards.add(new Circle(NORMALBOARD, 1, 7, 0, -1));
    // Left top.
    boards.add(new Circle(TWOWAYBOARD, 7, 7, -1, 0));
    // Left top to go down diagonal.
    boards.get(18).addNextRow(-1);
    boards.get(18).addNextColumn(-1);
    // Right top
    boards.add(new Circle(TWOWAYBOARD, 7, 1, 0, 1));
    // Right top to go down diagonal.
    boards.get(19).addNextRow(1);
    boards.get(19).addNextColumn(1);

    // Set 9 circles in the middle of the board.
    // Circles from left top to right bottom.
    for(int i = 0; i < 5; i++){
      int id = NORMALBOARD;
      if(i==2)
        id = TWOWAYBOARD;
      boards.add(new Circle(id, i+2, i+2,-1,-1));
    }
    // Circles from right top to left bottom.
    for(int i = 0; i < 5; i++){
      if(i==2) continue;
      boards.add(new Circle(NORMALBOARD, 6-i, i+2,-1,-1));
    }
    // Circle right in the middle.
    boards.get(22).addNextRow(1);
    boards.get(22).addNextColumn(1);

  }

  // get Circle by location
  public Circle getCircleByLocation(int row, int column){
    for(Circle i : boards){
      if(i.getRow() == row && i.getColumn() == column)
        return i;
    }
    return null;
  }

  public Circle getCircleByCircleId(int circleId){
    return boards.get(circleId);
  }

  public void showMovable(ArrayList<Integer> movableCircleIds){
    for(int i : movableCircleIds){
      getCircleByCircleId(i).setChangeable();
    }
  }
}
