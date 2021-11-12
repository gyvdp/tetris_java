package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.BoardInterface;
import esi.acgt.atlj.model.board.Direction;
import esi.acgt.atlj.model.board.ManagedBoard;
import esi.acgt.atlj.model.board.UnmanagedBoard;
import java.beans.PropertyChangeListener;

public class Model implements ModelInterface {

  protected ManagedBoard player1;
  protected UnmanagedBoard player2;

  public Model() {
    this.player1 = new ManagedBoard();
    this.player2 = new UnmanagedBoard();
  }

  @Override
  public void updateTest() {
    player1.initTetrisBoard();
  }

  @Override
  public String[] getUsernames() {
    return new String[0];
  }

  @Override
  public BoardInterface getManagedBoard(String username) {
    return null;
  }

  @Override
  public BoardInterface getUnmanagedBoard(String username) {
    return null;
  }

  public void setPlayer1(String userName) {
    this.player1.setUsername(userName);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.player1.addPropertyChangeListener(listener);
    this.player2.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.player1.removePropertyChangeListener(listener);
    this.player2.removePropertyChangeListener(listener);
  }

  @Override
  public void move(Direction direction) {
    this.player1.move(direction);
  }

  @Override
  public void hold() {
    this.player1.hold();
  }

  @Override
  public void hardDrop() {
    this.player1.hardDrop();
  }

  @Override
  public void softDrop() {
    this.player1.softDrop();
  }

}
