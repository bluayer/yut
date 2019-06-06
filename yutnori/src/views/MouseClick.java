package views;

import controller.ProcessController;
import models.YutNoRiSet;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class MouseClick{
  private Color clickedColor = new Color(0x41bdb5);
  private Color backgroundColor;
  public ImagePanel firstClk, secondClk;
  boolean isClicked;
  private ProcessController pc;
  private YutNoRiSet yutset;

  public MouseClick(final models.YutNoRiSet yutSet) {
    firstClk = null;
    secondClk = null;
    isClicked = false;
    backgroundColor = Color.DARK_GRAY;
    yutset = yutSet;
  }

  // Init variables
  public void initVars() {
    firstClk = null;
    secondClk = null;
  }

  public void getProcessController(ProcessController pc) {  this.pc = pc; }

  public void firstClickSetup(int row, int column) {
    System.out.println("fst click setup");
    initVars();
    // Click before start piece, so controller know who's piece selected.
    if(column == 0) {
      pc.selectOutOfBoardPieceProcess(row);
      firstClk = YutGui.beginPiece[row];
      isClicked = true;
      return;
    } else { // Click board

      // setting firstClick
      firstClk = YutGui.btn[row][column];
      firstClk.setBackground(clickedColor);
      firstClk.repaint();
      System.out.println("repaint in first clk");
      isClicked = true;
      return;
    }
  }

  public void grayRepaint() {
    for(int i = 1; i < 8; i++) {
      for (int j = 1; j < 8; j++) {
        if(yutset.getBoard().getCircleByLocation(i,j) == null){
          continue;
        }

        if (yutset.getBoard().getCircleByLocation(i, j).isChangeable()) {
          YutGui.btn[i][j].setBackground(Color.DARK_GRAY);
          YutGui.btn[i][j].repaint();
        }
      }
    }
  }

  public void secondClickSetup(int row, int column) {
    firstClk.setBackground(backgroundColor);
    firstClk.repaint();
    grayRepaint();
    pc.movePieceProcess(row, column);
    isClicked = false;
  }

  public void mouseInput(MouseEvent e) {
    // when border layout's piece click
    for(int i=0; i<YutGui.beginPiece.length; i++) {
      if (e.getSource().equals(YutGui.beginPiece[i])) {
        System.out.printf("Begin piece clicked %d\n", i);
        firstClickSetup(i, 0);
      }
    }

    if (e.getSource().equals(YutGui.yutBtn)) {
      if(pc == null) {
        System.out.println("it's null");
      }
      pc.rollYutProcess();
      System.out.println("Roll yut clicked");
    }

    for (int i=0; i<YutGui.testYutBtn.length; i++) {
      if (e.getSource().equals(YutGui.testYutBtn[i])) {
        pc.rollYutTest(i);
        System.out.printf("Test roll yut clicked %d\n", i);
      }
    }

    // when click board
    for(int i=1; i<8; i++) {
      for(int j=1; j<8; j++) {
        if(e.getSource().equals(YutGui.btn[i][j])) {
          if (!isClicked) {
            firstClickSetup(i, j);
            System.out.printf("click board %d , %d\n", i, j);
          } else {
            secondClickSetup(i, j);
            System.out.printf("Second click %d, %d \n", i, j);
            pc.movePieceProcess(i,j);
          }
        }
      }
    }
  }
}
