package views;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class UIclick implements MouseListener{
  MouseClick mouseClick;

  public UIclick(final models.YutNoRiSet yutSet) {
    mouseClick = new MouseClick(yutSet);
  }
  @Override
  public void mouseClicked(MouseEvent e) {
    mouseClick.mouseInput(e);
  }
  @Override
  public void mousePressed(MouseEvent e) {
  }
  @Override
  public void mouseReleased(MouseEvent e) {
  }
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  @Override
  public void mouseExited(MouseEvent e) {
  }
}
