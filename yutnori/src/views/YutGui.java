package views;


import controller.ProcessController;
import models.YutNoRiSet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class YutGui {
  final int FRAME_WIDTH = 1000, FRAME_HEIGHT = 1000;
  public BackGroundPanel midPanel;
  public JFrame mainFrame;
  public JFrame initFrame;
  public static ImagePanel[][] btn;
  static JButton[] testYutBtn;
  public JPanel yutBoard;
  YutNoRiSet yutnoriset;
  PieceSprite pieceSprite;
  JLabel player[];
  static ImagePanel[] beginPiece;
  static JButton yutBtn;
  private UIclick clickAction;
  private ImagePanel yutResultPanel;
  private JLabel [] numberOutOfPiece;
  private JLabel playerStatus;
  private JLabel turnStatus;
  private JLabel[][] groupingNum;

  public YutGui(final models.YutNoRiSet yutSet) {
    midPanel = new BackGroundPanel();
    yutnoriset = yutSet;
    mainFrame = new JFrame("Mode Selection");
    initFrame = new JFrame("Game View");
    yutBoard= new JPanel();
    btn = new ImagePanel[8][8];
    clickAction = new UIclick(yutSet);
    yutBtn = new JButton("윷 던지기", setGIF());
    yutBtn.addMouseListener(clickAction);
    pieceSprite = new PieceSprite();
    yutResultPanel = new ImagePanel();
    testYutBtn = makeTestYutBtn();
    playerStatus = new JLabel();
    turnStatus = new JLabel();
    groupingNum = new JLabel[8][8];
  }

  public void pcBridge(ProcessController pc) { clickAction.mouseClick.getProcessController(pc); }

  public void boardRepaint() {
    //System.out.println("Board Repaint");
    for(int i = 1; i < 8; i++) {
      for (int j = 1; j < 8; j++) {

        if(yutnoriset.getBoard().getCircleByLocation(i,j) == null){
          continue;
        }
        // If piece on (i, j) board
        if(yutnoriset.getBoard().getCircleByLocation(i,j).isOccupied()) {
          // numPiece is number of piece at board
          int numPiece = yutnoriset.getBoard().getCircleByLocation(i,j).getNumOfoccupyingPieces();
          System.out.println("Num Piece is " + numPiece);
          //System.out.println(numPiece + " Piece at board at " + i + ", " + j);
          int pieceID = yutnoriset.getBoard().getCircleByLocation(i,j).getOccupyingPieces().get(0);
          int playerID = pieceID / 10;
          //System.out.println("Piece ID is " + pieceID + " and Player Id is " + playerID);
          BufferedImage[] pieceList = pieceSprite.pieceList;
          btn[i][j].setImage(pieceList[playerID]);
          groupingNum[i][j].setText(Integer.toString(numPiece));
          groupingNum[i][j].setForeground(Color.BLACK);
          groupingNum[i][j].setHorizontalTextPosition(JLabel.CENTER);
          groupingNum[i][j].setVerticalTextPosition(JLabel.CENTER);
        } else {
          groupingNum[i][j].setText("");
          btn[i][j].setImage(null);
        }

        if (yutnoriset.getBoard().getCircleByLocation(i, j).isChangeable()) {
          //System.out.println(i + ", " + j + " is " + yutnoriset.getBoard().getCircleByLocation(i, j).isChangeable());
          btn[i][j].setBackground(Color.GREEN);
        } else {
          btn[i][j].setBackground(Color.decode("#eee6c4"));
        }
        groupingNum[i][j].repaint();
        btn[i][j].repaint();
      }
    }
    for(int i = 0;i <  numberOutOfPiece.length; i++) {
      numberOutOfPiece[i].setText(Integer.toString(yutnoriset.getPlayer().getLeftNumOfPieceOfPlayer(i)));
      numberOutOfPiece[i].repaint();
    }

    if (yutnoriset.getInGameFlag() == 0) {
      playerStatus.setText("Player status : Throw yut plz");
    } else if (yutnoriset.getInGameFlag() == 1) {
      playerStatus.setText("Player status : Select piece");
    } else if (yutnoriset.getInGameFlag() == 2) {
      playerStatus.setText("Player status : Move piece");
    }
    turnStatus.setText("Turn status : Player " +yutnoriset.getPlayerTurn() + " turn");

    playerStatus.repaint();
    turnStatus.repaint();

  }

  public class ModelChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      boardRepaint();
    }
  }

  public ModelChangeListener modelListner  = new ModelChangeListener();

  private JButton[] makeTestYutBtn() {
    JButton [] test = new JButton[6];
    test[0] = new JButton("테스트 백도");
    test[1] = new JButton("테스트 도");
    test[2] = new JButton("테스트 개");
    test[3] = new JButton("테스트 걸");
    test[4] = new JButton("테스트 윷");
    test[5] = new JButton("테스트 모");

    for (int i =0; i<6; i++) {
      test[i].setPreferredSize(new Dimension(20, 20));
      test[i].addMouseListener(clickAction);
    }
    return test;
  }

  private BufferedImage getYutImage(String name) {
    BufferedImage image = null;
    File f = null;
    try{
      String path = YutGui.class.getResource("").getPath();
      f = new File(path + name + ".png"); //image file path
      image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
      image = ImageIO.read(f);
    } catch(IOException e){
      System.out.println("Error: "+e);
    }
    return image;
  }

  public void showYutResult(int res) {
    String [] filename = { "backdo", "do", "gae", "gul", "yut", "mo" };
    BufferedImage img = getYutImage(filename[res]);
    yutResultPanel.setImage(img);
    yutResultPanel.repaint();
  }

  public void setupStartUI(){
    initFrame.setSize(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
    initFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initFrame.setLayout(new BorderLayout(10, 10));
    initFrame.setLocationRelativeTo(null);

    midPanel.setGUI();

    initFrame.add(midPanel);
    initFrame.setVisible(true);
  }

  public void setupYutGUI(int playerNumber, int pieceNumber) {
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
        if (yutnoriset.getBoard().getCircleByLocation(i, j) != null) {
          if ((yutnoriset.getBoard().getCircleByLocation(i, j).isClickable())) {
            btn[i][j].setBackground(Color.decode("#eee6c4"));
            btn[i][j].setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE),
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED)
            ));
            groupingNum[i][j] = new JLabel();
            btn[i][j].add(groupingNum[i][j]);

            btn[i][j].addMouseListener(clickAction);
          }
          else {
            btn[i][j].setBackground(Color.WHITE);
          }
        }
        yutBoard.add(btn[i][j]);
        btn[i][j].repaint();
      }
    }

    JPanel statusPanels = new JPanel();
    // Fix outlook which is not compatible with name+image+number of piece.
    statusPanels.setLayout(new GridLayout(2, 0));
    statusPanels.setBorder(new EmptyBorder(0, 30, 0, 30));

    /* Left side border */
    JPanel yutButtonPanels = new JPanel();
    yutButtonPanels.setLayout(new GridLayout(0, 1));
    yutButtonPanels.setBorder(new EmptyBorder(0, 30, 0, 30));

    yutButtonPanels.add(yutBtn);
    yutButtonPanels.add(yutResultPanel);

    BufferedImage[] pieceList = pieceSprite.pieceList;

    setPiecePanel(pieceList, clickAction);
    setPlayerLabel();

    for(int i=0; i<6; i++) {
      yutButtonPanels.add(testYutBtn[i]);
    }
    numberOutOfPiece = new JLabel[playerNumber];

    JPanel selectP = new JPanel();
    selectP.setLayout(new GridLayout(playerNumber, 3));

    JPanel stateP = new JPanel();
    stateP.setLayout(new GridLayout(2 , 0));
    stateP.add(playerStatus);
    stateP.add(turnStatus);
    statusPanels.add(stateP);
    // set Player name and Piece at the side border
    for (int i=0; i< playerNumber; i++) {
      selectP.add(player[i]);
      selectP.add(beginPiece[i]);
      numberOutOfPiece[i] = new JLabel(Integer.toString(yutnoriset.getPlayer().getLeftNumOfPieceOfPlayer(i)));
      selectP.add(numberOutOfPiece[i]);
    }

    statusPanels.add(selectP);


    contentPane.add(yutButtonPanels,  BorderLayout.LINE_START);
    contentPane.add(statusPanels, BorderLayout.LINE_END);
    contentPane.add(yutBoard, BorderLayout.CENTER);
    mainFrame.setVisible(true);
  }

  private void setPlayerLabel() {
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

  private void setPiecePanel(BufferedImage[] pieceList, UIclick clickAction) {
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
}