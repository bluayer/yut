package controller;
//import views.MouseClick;
import models.Circle;
import models.Piece;
import views.YutGui;
import models.YutNoRiSet;

//import models.Circle;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        yutGui = gui;
        System.out.println("짜잔");
        System.out.println(yutnoriSet.getNumOfPiece() + "개의 피스");
        System.out.println(yutnoriSet.getNumOfPlayer() + "명의 플레이어");
    }

    /*FOR TEST*/
    public void rollYutTest(int res){
        System.out.println("Roll Yut test!");
        if (flag == 0) { //BEFORE YUT ROLL STATE 일 때만 윷 던지기가 동작 하도록.
            currentTurn = yutnoriSet.getPlayerTurn();
            numCanMove++;
            yutnoriSet.getPlayer().getPlayerResult(currentTurn).add(res);
            if (res != 4 && res != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
                flag = 1;
            }
            System.out.println(res + "가 나왔습니다");
            System.out.print("player의 결과 : ");
            for(int i=0;i<yutnoriSet.getPlayer().getPlayerResult(currentTurn).size(); i++){
                System.out.print(yutnoriSet.getPlayer().getPlayerResult(currentTurn).get(i)+", ");
            }
            System.out.println("");
        }
    }

    /*BEFORE YUT ROLL STATE  FLAG = 0
     *윷 또는 모가 나오기 전까지는 윷던지기 버튼을 계속 누를 수 있다.
     * */
    public void rollYutProcess(){
        System.out.println("Roll Yut Btn pressed");
        if (flag == 0) { //BEFORE YUT ROLL STATE 일 때만 윷 던지기가 동작 하도록.
            currentTurn = yutnoriSet.getPlayerTurn();
            int result;
            result = yutnoriSet.getYutSet().rollYut();
            numCanMove++;
            System.out.println(result); //나온 결과를 순서대로 resultSet에 저장 해준다.
            yutnoriSet.getPlayer().getPlayerResult(currentTurn).add(result);
            if (result != 4 && result != 5) { // 윷이나 모가 아니라면 befoer yut roll state에서 빠져나와 BEFORE SELECT PIECE STATE 로 들어가게 된다.
                flag = 1;
            }
            System.out.println(result + "가 나왔습니다");
        }
    }


    /*BEFORE SELECT PIECE STATE FLAG = 1
     * 윷을 던진 후 이동할 말을 선택하고 말을 선택하면 그 말이 어디로 갈 수 있는지 보여준다.
     * */
    public void selectOutOfBoardPieceProcess(){
        if(flag == 1){
            chosenPiece = yutnoriSet.getPlayer().getPieceFromOutOfBoard(yutnoriSet.getPlayerTurn()); // 눌려진 버튼으로 Piece id를 받아온다.
            yutnoriSet.showMovable(chosenPiece);
            flag = 2;
        }
    }

    public void selectInTheBoardPieceProcess(int row, int col){
        if(flag == 1){
            chosenPiece = yutnoriSet.getBoard().getCircleByLocation(row,col).getOccupyingPieces().get(0);
            yutnoriSet.showMovable(chosenPiece);
            flag = 2;
        }
    }

     /*CHOICE PIECE STATE FLAG = 2
     * 말이 선택 되었으면 ShowMovable에 의해 나온 결과에 따라 움직여 주면 된다.
     * */
    public void movePieceProcess(int row, int col){
        if(flag == 2){
            int[] moveLocation;
            if(yutnoriSet.tryCatch(chosenPiece, row, col)){
                catchPoint++;
            }
            yutnoriSet.getPlayer().getPlayerResult(currentTurn).remove(yutnoriSet.getClickedResult(chosenPiece, row,col));
            yutnoriSet.move(chosenPiece, row, col);
            numCanMove--;

            if(yutnoriSet.getPlayer().getWinnerPlayerId() != -1){
                //종료시켜야함
            }

            /*Decision*/
            if (numCanMove >= 1 && catchPoint == 0) { // 아직 움직일 수 있는 횟수가 남았고 잡은 말이 없다면 BEFORE SELECT PIECE STATE로 변경함
                flag = 1;
            } else if (numCanMove == 0 && catchPoint == 0) { // 움직일수 있는 횟수가 없고 잡은 말이 없다면 턴을 종료하고 다음 player에게 턴을 넘김.
                yutnoriSet.setPlayerTurn((yutnoriSet.getPlayerTurn() % yutnoriSet.getNumOfPlayer()) + 1);
                flag = 0;
            } else if (catchPoint > 0) { // 상대 말을 잡았다면 BEFORE YUT ROLL STATE로 변경하여 다시 윷을 던질 수 있게 함.
                catchPoint--;
                flag = 0;
            }
        }
    }

    public boolean checkEndGame(){
        boolean end = false;
        if(yutnoriSet.getPlayer().getLeftNumOfPieceOfPlayer(currentTurn)<=0) {end = true;}
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