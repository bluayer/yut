package views;


import models.YutNoRiSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class YutGui {
  final int FRAME_WIDTH = 1000, FRAME_HEIGHT = 1000;
  public BackGroundPanel midPanel;
  public JFrame mainFrame;
  static ImagePanel btn[][];
  public JPanel yutBoard;
  YutNoRiSet yutnoriset;
  PieceSprite pieceSprite;
  static JLabel player[];
  static ImagePanel beginPiece[];
  public JButton yutBtn;
  private UIclick clickAction;

  // private static UIclick clickBridge;

  public YutGui(final models.YutNoRiSet yutSet) {
    midPanel = new BackGroundPanel();
    yutnoriset = yutSet;
    mainFrame = new JFrame("Mode Selection");
    yutBoard= new JPanel();
    btn = new ImagePanel[8][8];
    yutBtn = new JButton("윷 던지기", setGIF());
    clickAction = new UIclick();
    pieceSprite = new PieceSprite();
  }

  private static void setPlayerLabel() {
    JLabel player1 = new JLabel("Player 1");
    JLabel player2 = new JLabel("Player 2");
    JLabel player3 = new JLabel("Player 3");
    JLabel player4 = new JLabel("Player 4");
    player = new JLabel[] { player1, player2, player3, player4};
  }

  private ImageIcon setGIF() {
    ImageIcon ii = new ImageIcon(getClass().getResource("loadyut.gif"));

    // resize image in button
    ii.setImage(ii.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

    return ii;
  }

  private static void setPiecePanel(BufferedImage[] pieceList, UIclick clickAction) {
    ImagePanel piece1 = new ImagePanel();
    ImagePanel piece2 = new ImagePanel();
    ImagePanel piece3 = new ImagePanel();
    ImagePanel piece4 = new ImagePanel();

    piece1.setImage(pieceList[0]);
    piece2.setImage(pieceList[1]);
    piece3.setImage(pieceList[2]);
    piece4.setImage(pieceList[3]);

    piece1.addMouseListener(clickAction);
    piece2.addMouseListener(clickAction);
    piece3.addMouseListener(clickAction);
    piece4.addMouseListener(clickAction);

    beginPiece = new ImagePanel[] { piece1, piece2, piece3, piece4};
  }

  public void setupStartUI(){
    mainFrame.setSize(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setLayout(new BorderLayout(10, 10));
    mainFrame.setLocationRelativeTo(null);

    midPanel.setGUI();

    mainFrame.add(midPanel);

    mainFrame.setVisible(true);
  }

  public void setupYutGUI(int playerNumber, int pieceNumber) {
    mainFrame = new JFrame("Game View");
    mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setLocationRelativeTo(null);

    Container contentPane = mainFrame.getContentPane();
    contentPane.setLayout(new BorderLayout(10, 10));


    // set board
    yutBoard.setLayout(new GridLayout(7, 7));
    yutBoard.setBackground(Color.WHITE);
    yutBoard.setBorder(new EmptyBorder(30, 30, 30, 30));

    for(int i = 1; i < 8; i++) {
      for(int j = 1; j < 8; j++) {
        btn[i][j] = new ImagePanel();
        btn[i][j].setOpaque(true);
        // btn[i][j].addMouseListener(clickBridge);
        if (yutnoriset.getBoard().getCircleByLocation(i, j) != null) {
          if ((yutnoriset.getBoard().getCircleByLocation(i, j).isClickable())) {
            if (yutnoriset.getBoard().getCircleByLocation(i, j).isChangeable()) {
              btn[i][j].setBackground(Color.GREEN);
            } else {
              btn[i][j].setBackground(Color.DARK_GRAY);
            }
          } else {
            btn[i][j].setBackground(Color.WHITE);
          }
        }
        btn[i][j].addMouseListener(clickAction);
        yutBoard.add(btn[i][j]);
        btn[i][j].repaint();
      }
    }

    JPanel statusPanels = new JPanel();
    // Fix outlook which is not compatible with name+image+number of piece.
    statusPanels.setLayout(new GridLayout(playerNumber, 3));
    statusPanels.setBorder(new EmptyBorder(0, 30, 0, 30));

    /* Right side border */
    JPanel yutButtonPanels = new JPanel();
    yutButtonPanels.setBorder(new EmptyBorder(0, 30, 0, 30));

    yutButtonPanels.add(yutBtn);

    // It' for yut result image
    ImagePanel yutResultPanel = new ImagePanel();

    BufferedImage[] pieceList = pieceSprite.pieceList;
    setPiecePanel(pieceList, clickAction);
    setPlayerLabel();
    // set Player name and Piece at the side border
    for (int i=0; i< playerNumber; i++) {
      statusPanels.add(player[i]);
      statusPanels.add(beginPiece[i]);
      // Add number if player's left piece

      statusPanels.add(new JLabel(Integer.toString(yutnoriset.getPlayer().getLeftNumOfPieceOfPlayer(i))));
      // beginPiece[i].addMouseListener(clickBridge);
    }

    contentPane.add(yutButtonPanels,  BorderLayout.LINE_START);
    contentPane.add(statusPanels, BorderLayout.LINE_END);
    contentPane.add(yutBoard, BorderLayout.CENTER);
    mainFrame.setVisible(true);

  }
}