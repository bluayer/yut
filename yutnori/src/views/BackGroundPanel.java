package views;

import models.Yut;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


public class BackGroundPanel extends JPanel{
  private BufferedImage img;
  private int playerNumber = 2;
  private int pieceNumber = 2;

  public JButton enter = new JButton();

  public JComboBox playerNumberInput;
  public JComboBox pieceNumberInput;

  public BackGroundPanel() {

    try {
      String path = BackGroundPanel.class.getResource("").getPath();
      img = ImageIO.read(new File(path + "mainBackGround.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void setGUI() {
    JLabel initialString = new JLabel("Yut-No-Ri");

    initialString.setFont(new Font("Consolas", Font.BOLD, 40));
    initialString.setForeground(new Color(0x99191c));
    initialString.setVerticalAlignment(SwingConstants.CENTER);
    initialString.setHorizontalAlignment(SwingConstants.CENTER);

    setLayout(new BorderLayout(10, 10));
    setBorder(new EmptyBorder(100 , 100, 100, 100));

    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setLayout(new GridLayout(3, 1));
    buttonPanel.add(initialString);

    JPanel enterBtnPanel = new JPanel();
    enterBtnPanel.setOpaque(false);
    enterBtnPanel.setLayout(new BorderLayout());
    enterBtnPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    enter.setText("enter");

    Color pink = new Color(255,160,154);
    Color red = new Color(165, 8, 33);

    enter.setBackground(pink);
    enter.setForeground(Color.WHITE);
    enter.setBorder(new LineBorder(red));

    enter.setOpaque(true);
    enterBtnPanel.add(enter);

    JLabel playerInput = new JLabel("Input player number");
    JLabel pieceInput = new JLabel("Input piece number");
    buttonPanel.add(playerInput);
    buttonPanel.add(pieceInput);

    String s1[] = { "2", "3", "4" };
    String s2[] = { "2", "3", "4", "5" };

    // create checkbox
    playerNumberInput = new JComboBox(s1);
    pieceNumberInput = new JComboBox(s2);
    // add ItemListener
    playerNumberInput.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == playerNumberInput) {
          playerNumber = Integer.parseInt((playerNumberInput.getSelectedItem()).toString());
          System.out.println((playerNumberInput.getSelectedItem()).toString());
        }
        if (e.getSource() == pieceNumberInput) {
          pieceNumber = Integer.parseInt((pieceNumberInput.getSelectedItem()).toString());
          System.out.println((pieceNumberInput.getSelectedItem()).toString());
        }
      }
    });

    buttonPanel.add(playerNumberInput, BorderLayout.SOUTH);
    buttonPanel.add(pieceNumberInput, BorderLayout.SOUTH);

//    enter.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        YutGui.mainFrame.setVisible(false);
//        playerNumber = Integer.parseInt(playerNumberInput.getText());
//        pieceNumber = Integer.parseInt(pieceNumberInput.getText());
//        if (e.getSource().equals(enter)) {
//          YutGui.setupYutGUI(playerNumber, pieceNumber);
//        }
//      }
//    });


    buttonPanel.add(initialString);
    buttonPanel.add(enterBtnPanel, BorderLayout.NORTH);

    add(buttonPanel);
  }

  public BufferedImage getBackgroundImage() {
    return img;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(img, 0, 0, this);
  }

  public int getPlayerNumber(){
    return this.playerNumber;
  }
  public void setPlayerNumber(int playerNum){
    this.playerNumber=playerNum;
  }
  public int getPieceNumber(){
    return this.pieceNumber;
  }
  public void setPieceNumber(int pieceNum){
    this.pieceNumber=pieceNum;
  }
}
