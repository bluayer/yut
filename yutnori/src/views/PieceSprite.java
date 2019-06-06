package views;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


public class PieceSprite {
  private String [] name = { "bPiece", "rPiece", "gPiece", "yPiece"};
  BufferedImage[] pieceList;
  BufferedImage image = null;

  public PieceSprite() {
    try {
      pieceList = new BufferedImage[4];
      pieceList = bufferedImage(name);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private BufferedImage[] bufferedImage(String[] name) throws Exception {
    BufferedImage[] list = new BufferedImage[4];
    String path = PieceSprite.class.getResource("").getPath();

    for (int i=0; i<name.length; i++) {
      try {
        File file = new File(path + name[i] + ".png");
        image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        image = ImageIO.read(file);
        list[i] = image;
      }
      catch(IOException e){
        System.out.println("Error: "+e);
      }
    }

    return list;
  }

}
