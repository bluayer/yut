package models;

import java.util.ArrayList;
public class Board {

  // Board size
  public final int BOARDSIZE = 29;

  // managing Circles
  private ArrayList<Circle> boards;

  Board(){
    int id = 0;
    boards = new ArrayList<Circle>(0);


    boards.add(new Circle(id++, 7, 7));
    boards.add(new Circle(id++, 6, 7));
    boards.add(new Circle(id++, 5, 7));
    boards.add(new Circle(id++, 3, 7));
    boards.add(new Circle(id++, 2, 7));
    boards.add(new Circle(id++, 1, 7));

    boards.add(new Circle(id++, 1, 6));
    boards.add(new Circle(id++, 1, 5));
    boards.add(new Circle(id++, 1, 3));
    boards.add(new Circle(id++, 1, 2));
    boards.add(new Circle(id++, 1, 1));

    boards.add(new Circle(id++, 2, 1));
    boards.add(new Circle(id++, 3, 1));
    boards.add(new Circle(id++, 5, 1));
    boards.add(new Circle(id++, 6, 1));
    boards.add(new Circle(id++, 7, 1));

    boards.add(new Circle(id++, 7, 2));
    boards.add(new Circle(id++, 7, 3));
    boards.add(new Circle(id++, 7, 5));
    boards.add(new Circle(id++, 7, 6));

    boards.add(new Circle(id++, 6, 6));
    boards.add(new Circle(id++, 5, 5));
    boards.add(new Circle(id++, 4, 4));
    boards.add(new Circle(id++, 3, 3));
    boards.add(new Circle(id++, 2, 2));

    boards.add(new Circle(id++, 2, 6));
    boards.add(new Circle(id++, 3, 5));
    boards.add(new Circle(id++, 5, 3));
    boards.add(new Circle(id++, 6, 2));
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


}
