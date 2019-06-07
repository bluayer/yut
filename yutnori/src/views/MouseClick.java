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
    initVars();
    // Click before start piece, so controller know who's piece selected.
    if(column == 0) {
      pc.selectOutOfBoardPieceProcess(row);
      firstClk = YutGui.beginPiece[row];
      isClicked = true;
      return;
    } else { // Click board
      pc.selectInTheBoardPieceProcess(row, column);
      // setting firstClick
      firstClk = YutGui.btn[row][column];
      firstClk.setBackground(clickedColor);
      firstClk.repaint();
      //System.out.println("repaint in first clk");
      isClicked = true;
      return;
    }
  }

  public void secondClickSetup(int row, int column) {
    firstClk.setBackground(backgroundColor);
    firstClk.repaint();
    if (!yutset.getMovable().contains(yutset.getBoard().getCircleByLocation(row, column).getId())) {
      initVars();
    }
    pc.movePieceProcess(row, column);

    isClicked = false;
  }

  public void mouseInput(MouseEvent e) {
    // when border layout's piece click
    for(int i=0; i<YutGui.beginPiece.length; i++) {
      if (e.getSource().equals(YutGui.beginPiece[i])) {
        System.out.printf("Begin piece clicked %d\n\n", i);
        firstClickSetup(i, 0);
      }
    }

    if (e.getSource().equals(YutGui.yutBtn)) {
      pc.rollYutProcess();
      //System.out.println("Roll yut clicked");
    }

    for (int i=0; i<YutGui.testYutBtn.length; i++) {
      if (e.getSource().equals(YutGui.testYutBtn[i])) {
        pc.rollYutTest(i);
        // System.out.printf("Test roll yut clicked %d\n", i);
      }
    }

    // when click board
    for(int i=1; i<8; i++) {
      for(int j=1; j<8; j++) {
        if(e.getSource().equals(YutGui.btn[i][j])) {
          if (!isClicked) {
            System.out.printf("First click %d , %d\n\n", i, j);
            firstClickSetup(i, j);
          } else {
            System.out.printf("Second click %d, %d \n\n", i, j);
            secondClickSetup(i, j);
          }
        }
      }
    }
  }
}
