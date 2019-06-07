package controller;

//import views.MouseClick;
import models.YutNoRiSet;
import views.YutGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import  views.BackGroundPanel;


public class GameController {
    public static int playerNumber;
    public static int pieceNumber;
    public static ProcessController processController;
    public static void main(String[] args) throws IOException {
        final models.YutNoRiSet yutSet = new YutNoRiSet();
        YutGui yutgui = new YutGui(yutSet);
        yutSet.addObserver(yutgui.modelListner);

        yutgui.setupStartUI();

        BackGroundPanel midP =yutgui.midPanel;

        yutgui.midPanel.enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if (Integer.parseInt(midP.playerNumberInput.getText()) >= 2 && Integer.parseInt(midP.playerNumberInput.getText()) <= 4
                      && Integer.parseInt(midP.pieceNumberInput.getText()) >= 2 && Integer.parseInt(midP.pieceNumberInput.getText()) <= 5) {
                yutgui.initFrame.setVisible(false);

                if (e.getSource().equals(midP.enter)) {
                  midP.setPlayerNumber(Integer.parseInt(midP.playerNumberInput.getText()));
                  midP.setPieceNumber(Integer.parseInt(midP.pieceNumberInput.getText()));
                  yutSet.setPlayer(midP.getPlayerNumber(), midP.getPieceNumber());
                  yutgui.setupYutGUI(midP.getPlayerNumber(), midP.getPieceNumber());
                  yutSet.setPlayerTurn(0);
                  processController = new ProcessController(yutSet, yutgui);
                  yutgui.pcBridge(processController);
                  System.out.println("controller create");
                }
              } else {
                System.out.println("2~4명의 플레이어 수, 2~5개의 피스 수를 입력하세요.");
              }
            }
        });


        return;
    }
}

