package controller;
//import views.MouseClick;

import com.sun.security.auth.NTUserPrincipal;
import models.Circle;
import models.Piece;
import views.YutGui;
import models.YutNoRiSet;

//import models.Circle;


import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ProcessController {
  public YutGui yutGui;
  public YutNoRiSet yutnoriSet;
  public int flag = 0;
  public int currentTurn = 0;
  public int numCanMove = 0;
  public int catchPoint = 0;
  public int chosenPiece = -1;
  //public MouseClick mClick;

  public ProcessController(YutNoRiSet set, YutGui gui) {
    yutnoriSet = set;
    yutnoriSet.setPlayerTurn(0);
    yutnoriSet.setInGameFlag(0);
    yutGui = gui;
    System.out.println("짜잔");
    System.out.println(yutnoriSet.getNumOfPiece() + "개의 피스");
    System.out.println(yutnoriSet.getNumOfPlayer() + "명의 플레이어");
  }

  public int showTurn(int t){ return (currentTurn +1) % (yutnoriSet.getNumOfPlayer()+1); }

  /*FOR TEST*/
  public void rollYutTest(int res) {
    System.out.println("Roll Yut test!");
    if (yutnoriSet.getInGameFlag() == 0) { //BEFORE YUT ROLL STATE 일 때만 윷 던지기가 동작 하도록.

      yutnoriSet.getPlayer().getPlayerResult(currentTurn).add(res);
      numCanMove++;
      if (res != 4 && res != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
        yutnoriSet.setInGameFlag(1);
      }
      System.out.println(res + "가 나왔습니다");
      if (res != 4 && res != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
        System.out.println("움직일 말을 선택하세요");
        yutnoriSet.setInGameFlag(1);
      } else{
        System.out.println("윷을 한번 더 던질 수 있습니다");
      }
      System.out.print("나온 목록 : ");
      for (int i = 0; i < yutnoriSet.getPlayer().getPlayerResult(currentTurn).size(); i++) {
        System.out.print(yutnoriSet.getPlayer().getPlayerResult(currentTurn).get(i) + ", ");
      }
      System.out.println("");
    }
  }

  /*BEFORE YUT ROLL STATE  FLAG = 0
   *윷 또는 모가 나오기 전까지는 윷던지기 버튼을 계속 누를 수 있다.
   * */
  public void rollYutProcess() {
    if (yutnoriSet.getInGameFlag() == 0) { //BEFORE YUT ROLL STATE 일 때만 윷 던지기가 동작 하도록.
      currentTurn = yutnoriSet.getPlayerTurn();
      //System.out.println("Roll Yut Flag : " + flag + "Turn :" + currentTurn);
      int result;
      result = yutnoriSet.getYutSet().rollYut();

      numCanMove++;

      yutnoriSet.getPlayer().getPlayerResult(currentTurn).add(result);

      System.out.println(result + "가 나왔습니다");
      System.out.print("나온 목록 : ");
      for (int i = 0; i < yutnoriSet.getPlayer().getPlayerResult(currentTurn).size(); i++) {
        System.out.print(yutnoriSet.getPlayer().getPlayerResult(currentTurn).get(i) + ", ");
      }
      System.out.println("");
      if (result != 4 && result != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
        System.out.println("Player " + showTurn(catchPoint) + "! 움직일 말을 선택하세요");
        yutnoriSet.setInGameFlag(1);
      } else{
        System.out.println("Player " + showTurn(catchPoint) + "한번 더 던질 수 있습니다");
      }

    } else{
      System.out.println("Player " + showTurn(catchPoint) + "윷 던지기 차례가 아닙니다");
    }
  }



    /*BEFORE SELECT PIECE STATE FLAG = 1
     * 윷을 던진 후 이동할 말을 선택하고 말을 선택하면 그 말이 어디로 갈 수 있는지 보여준다.
     * */
  public void selectOutOfBoardPieceProcess(int turn){
    if(yutnoriSet.getInGameFlag() == 1 && (turn == currentTurn)){
      if(yutnoriSet.getPlayer().getLeftNumOfPieceOfPlayer(currentTurn) > 0) {
        //System.out.println("Select Piece Flag : " + flag + " Turn :" + currentTurn + "selected Piece : " + turn);
        chosenPiece = yutnoriSet.getPlayer().getPieceIdFromOutOfBoard(currentTurn); // 눌려진 버튼으로 Piece id를 받아온다.
        yutnoriSet.showMovable(chosenPiece);
        yutnoriSet.setInGameFlag(2);
      } else {
        System.out.println("더이상 남은 말이 없습니다.");
        yutnoriSet.setInGameFlag(1);
      }
    } else if(flag == 1 && (turn != currentTurn)){
      System.out.println("Player " + showTurn(catchPoint) + "본인의 말을 선택 해주세요");
    } else {
      System.out.println("Player " + showTurn(catchPoint) + "말을 선택할 차례가 아닙니다");
    }
  }

  public void selectInTheBoardPieceProcess(int row, int col) {
    int ownerId;
    try {
      ownerId = yutnoriSet.getPlayer().getPieceByLocation(row, col).getOwnerId();
    }catch(NullPointerException e){
      //System.out.println("Not piece on circle");
      return;
    }


    if (yutnoriSet.getInGameFlag() == 1 && currentTurn == ownerId) {
      //System.out.println("Select Piece Flag : " + flag + "Turn :" + currentTurn);
      chosenPiece = yutnoriSet.getBoard().getCircleByLocation(row, col).getOccupyingPieces().get(0);
      yutnoriSet.showMovable(chosenPiece);
      yutnoriSet.setInGameFlag(2);
    } else if( flag == 1 &&
            currentTurn != (yutnoriSet.getPlayer().getPieceByPieceId(yutnoriSet.getBoard().getCircleByLocation(row, col).getOccupyingPieces().get(0)).getOwnerId())){
      System.out.println("Player " + showTurn(catchPoint) + "본인의 말을 선택 해주세요");
    }
  }

  public void decisionMaking() {
    if (numCanMove >= 1 && catchPoint == 0) { // 아직 움직일 수 있는 횟수가 남았고 잡은 말이 없다면 BEFORE SELECT PIECE STATE로 변경함
      yutnoriSet.setInGameFlag(1);
      yutnoriSet.setBoardUnchangeable();
      System.out.println("Player" + showTurn(catchPoint) + " 움직일 수 있는 횟수가 " + numCanMove + "번 남았습니다.");
    } else if (numCanMove == 0 && catchPoint == 0) { // 움직일수 있는 횟수가 없고 잡은 말이 없다면 턴을 종료하고 다음 player에게 턴을 넘김.
      yutnoriSet.setPlayerTurn(((yutnoriSet.getPlayerTurn() + 1) % yutnoriSet.getNumOfPlayer()));
      yutnoriSet.setInGameFlag(0);
      yutnoriSet.setBoardUnchangeable();
      currentTurn = yutnoriSet.getPlayerTurn();
      System.out.println("움직일수 있는 횟수 모두 소비! 다음 Player 에게 턴을 넘깁니다!");
      System.out.println("");
      System.out.println("Player" + showTurn(catchPoint) + " 윷을 던지세요! ");
    } else if (catchPoint > 0) { // 상대 말을 잡았다면 BEFORE YUT ROLL STATE로 변경하여 다시 윷을 던질 수 있게 함.
      catchPoint--;
      yutnoriSet.setInGameFlag(0);
      yutnoriSet.setBoardUnchangeable();
      System.out.println("말을 잡았습니다 윷을 한번 더 던지세요");
    }
  }

  public void multiPossibleEnd(int result) {
    try {
      System.out.println("result called " + result);
      yutnoriSet.getPlayer().getPlayerResult(currentTurn).remove((Integer) result);
      decisionMaking();
    } catch (NullPointerException e){
      System.out.println("multi possible End");
    }
  }



  /** CHOICE PIECE STATE FLAG = 2
   * 말이 선택 되었으면 ShowMovable에 의해 나온 결과에 따라 움직여 주면 된다.
   */
  public void movePieceProcess(int row, int col) {
    //System.out.println("Move  Flag : " + yutnoriSet.getInGameFlag() + " Turn :" + currentTurn);
    System.out.println("Player"+ showTurn(catchPoint) +"의 말이 움직입니다!");
    Integer resultValue;
    int numOfReachable = 0;
    boolean removeSuceed;
    if (yutnoriSet.getInGameFlag() == 2 && yutnoriSet.getBoard().getCircleByLocation(row, col).isChangeable()) {
      if (yutnoriSet.tryCatch(chosenPiece, row, col)) {
        catchPoint++;
      }

      numCanMove--;
      resultValue = yutnoriSet.getClickedResult(chosenPiece, row, col);
      yutnoriSet.move(chosenPiece, row, col);
      yutnoriSet.getMovable().clear();

      // When the piece(s) reach to the end point.
      if (row == 7 && col == 7) {
        System.out.println("Player " + showTurn(catchPoint) + "의 말이 도착했습니다!");
        for(int i : yutnoriSet.getBoard().getCircleByLocation(7,7).getOccupyingPieces()){
          yutnoriSet.getPlayer().getPieceByPieceId(i).setGone();
        }
        System.out.println(yutnoriSet.getPlayer().getWinnerPlayerId() + "가 승리!");
        System.out.println("CurrentTurn is " + currentTurn);
        if (yutnoriSet.getPlayer().getWinnerPlayerId() == currentTurn) {
          System.out.println("게임이 끝났습니다!!!!! 승자 : Player" + currentTurn);
          //종료시켜야함
          yutGui.setupExitGUI();
        }

        // call view function with currentTurn
        for(int i : yutnoriSet.getPlayer().getPlayerResult(currentTurn)){
          if(resultValue == 0){
            break;
          } else if(i >= resultValue){
            numOfReachable++;
          }
        }
        if(numOfReachable > 1) {
          System.out.println("popup called");
          yutGui.popUp(currentTurn, chosenPiece);
        }else{
          yutnoriSet.getPlayer().getPlayerResult(currentTurn).remove(resultValue);
          decisionMaking();
        }
      } else {
        removeSuceed = yutnoriSet.getPlayer().getPlayerResult(currentTurn).remove(resultValue);

        if(removeSuceed == false){
          // When first input as Do, then result value is same as Back Do.
          // So the result value is 0.
          if( resultValue == 0){
            yutnoriSet.getPlayer().getPlayerResult(currentTurn).remove((Integer)1);
          }
        }
        System.out.println("남은 목록 : ");
        for (int i = 0; i < yutnoriSet.getPlayer().getPlayerResult(currentTurn).size(); i++) {
          System.out.print(yutnoriSet.getPlayer().getPlayerResult(currentTurn).get(i) + ", ");
        }
        System.out.println("");
        decisionMaking();  
      }
      
      yutnoriSet.getBoard().getCircleByLocation(7,7).resetCircle();


    } else if (!yutnoriSet.getMovable().contains(yutnoriSet.getBoard().getCircleByLocation(row, col).getId())) {

      //System.out.println("Second click touch other thing");
      for(int i = 0; i < yutnoriSet.getMovable().size(); i++) {
        //System.out.println("Yut nori set in " + i + " with " + yutnoriSet.getMovable().get(i));
      }

      yutnoriSet.setInGameFlag(1);
      yutnoriSet.getMovable().clear();
    }

    for(int i = 0; i < yutnoriSet.getMovable().size(); i++) {
      //System.out.println("Yut nori set in " + i + " with " + yutnoriSet.getMovable().get(i));
    }
    yutnoriSet.setBoardUnchangeable();
  }

  public boolean checkEndGame() {
    boolean end = false;
    if (yutnoriSet.getPlayer().getLeftNumOfPieceOfPlayer(currentTurn) <= 0) {
      end = true;
    }
    return end;
  }
}


//{
//    public void gameProgress(models.YutNoRiSet set) {
//        int[] clickedLocation;
//        System.out.println("progress call");
//
//        resultSet = new ArrayList<Integer>(); // player가 윷을 던진 결과들을 저장
//
//        boolean endCondition;
//
//        /*BEFORE YUT ROLL STATE  FLAG = 0
//         *윷 또는 모가 나오기 전까지는 윷던지기 버튼을 계속 누를 수 있다.
//         * */
//        System.out.println("hola");
//        yutGui.yutBtn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                currentTurn = yutnoriSet.getPlayerTurn();
//                System.out.println("Roll Yut Btn pressed");
//                int result;
//                if (flag == 0) { //BEFORE YUT ROLL STATE 일 때만 윷 던지기가 동작 하도록.
//                    result = yutnoriSet.getYutSet().rollYut();
//                    numCanMove++;
//                    System.out.println(result);
//                    resultSet.add(result); //나온 결과를 순서대로 resultSet에 저장 해준다.
//                    if (result != 4 && result != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
//                        flag = 1;
//                    }
//                    System.out.println(result + "가 나왔습니다");
//                }
//            }
//        });
//
//        /*BEFORE SELECT PIECE STATE FLAG = 1
//         * 윷을 던진 후 이동할 말을 선택하고 말을 선택하면 그 말이 어디로 갈 수 있는지 보여준다.
//         * */
//        // player는 아직 판에 올려지지 않은 남은 말들 중 하나를 선택 할 수있다.
//        // 현재 턴인 플레이어의 말만 선택 할 수 있어야 한다.
//        yutGui.beginPiece[yutnoriSet.getPlayerTurn()].addMouseListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (flag == 1) { // BEFORE SELECT PIECE STATE 일 때만 동작 하도록
//                    chosenPiece = getPieceFromOutOfBoard(yutnoriSet.getPlayerTurn()); // 눌려진 버튼으로 Piece id를 받아온다.
//                    //먼저 어디로 이동 할 지 보여준다.
//                    showMovable(resultSet, chosenPiece);
//                    flag = 2;
//                }
//            }
//        });
//
//        /*second click시 해제 ? */
//
//        //player는 판 위에 올려져있는 말들 중 하나를 선택 할 수 있다.
//        yutGui..addMouseListener(new ActionListener() {
//            public void actionPerformed (ActionEvent e){
//                if(flag == 1){ // BEFORE SELECT PIECE STATE 일 때만 동작 하도록
//                    chosenPiece = getPieceFromOutOfBoard
//                    //먼저 어디로 이동 할 지 보여준다.
//                    showMovable(resultSet, chosenPicecId);
//                    flag=2;
//                }
//            }
//        });
//    }
/*CHOICE PIECE STATE FLAG = 2
 * 말이 선택 되었으면 ShowMovable에 의해 나온 결과에 따라 움직여 주면 된다.
 * */
//      yutGui.(highlighting 된 circle중 하나).addActionListener(new ActionListener() {
//            public void actionPerformed (ActionEvent e){
//                int[] moveLocation;
//                if(flag == 2) { // CHOICE PIECE STATE 일 때만 동작 하도록
//                    int selectedResult = 선택된서클();
//                    moveLocation = yutSet.getLocationByCircle(chosenPiece); // 이동할 수 있는 circle을 선택하면 circle을 이용해 row, col을 받아오는 getLocationByCircle 함수 필요.
//
//                    switch (checkMovingPosition(chosenPiece, moveLocation[0], moveLocation[1])) {
//                        case 1: // 아무것도 없으면
//                            break;
//                        case 2: // 본인말이 있으면 grouping
//                            // grouping 하고
//                            break;
//                        case 3: // 상대 말을 잡았으면
//                            yutSet.catchPiece(moveLocation[0], moveLocation[1]);
//                            catchPoint++;
//                            break;
//                    }
//
//                    /*arrival 조건을 판단하여 게임이 끝났는지 계속 진행되는지 판단해줌*/
//
//
//                    //게임 계속 진행된다면
//                    yutSet.move(chosenPiece, moveLocation[0], moveLocation[1]); // 선택한 circle로 piece를 이동시킴.
//                    //선택한 result를 resultSet에서 delete시켜야함.
//                    numCanMove--;
//
//                    /*Decision*/
//                    if (numCanMove >= 1 && catchPoint == 0) { // 아직 움직일 수 있는 횟수가 남았고 잡은 말이 없다면 BEFORE SELECT PIECE STATE로 변경함
//                        flag = 1;
//                    } else if (numCanMove == 0 && catchPoint == 0) { // 움직일수 있는 횟수가 없고 잡은 말이 없다면 턴을 종료하고 다음 player에게 턴을 넘김.
//                        yutSet.setPlayerTurn((yutSet.getPlayerTurn() % yutSet.getNumOfPlayer()) + 1);
//                        flag = 0;
//                    } else if (catchPoint > 0) { // 상대 말을 잡았다면 BEFORE YUT ROLL STATE로 변경하여 다시 윷을 던질 수 있게 함.
//                        catchPoint--;
//                        flag = 0;
//                    }
//                }
//            }
//        });
//    }


/* 움직이고자 하는 Circle에 뭔가 있는지 살펴본다 아무것도 없으면 1, 본인것이 있으면 2, 상대것이 있으면 3 반환*/
//    public int checkMovingPosition(int pieceId, int row, int col) {
//        int currentPlayer = yutSet.getOwnerOfPieceByPieceId(pieceId); //현재 말을 움직이는 플레이어
//
//        int ownerOfPositionedPiece = yutSet.getOwnerOfPieceByLocation(row, col); //움직이고자 하는 위치에 있는 말의 플레이어
//
//        if(ownerOfPositionedPiece == -1){ // 아무것도 없으면 1 반환
//            return 1;
//        } else if (currentPlayer == ownerOfPositionedPiece){ // 본인것이 있으면 2 반환
//            return  2;
//        } else if(currentPlayer != ownerOfPositionedPiece) { // 둘다 아니면 상대 말이 있다.
//            return 3;
//        }
//
//        return -1;
//    }
//