package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.Direction;
import esi.acgt.atlj.model.board.ManagedBoard;
import esi.acgt.atlj.model.board.UnmanagedBoard;
import java.beans.PropertyChangeListener;

public class ClientModel extends Model {

  ManagedBoard player1;
  UnmanagedBoard player2;

  public ClientModel() {
    super();
  }

  public void start() {
    this.player1.start();
  }

  public void initManagedBoard(String username) {
    player1 = new ManagedBoard(username);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.player1.addPropertyChangeListener(listener);
    //this.player2.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.player1.removePropertyChangeListener(listener);
    //this.player2.removePropertyChangeListener(listener);
  }

  public void move(Direction direction) {
    this.player1.move(direction);
  }

  public void hold() {
    this.player1.hold();
  }

  public void hardDrop() {
    this.player1.hardDrop();
  }

  public void softDrop() {
    this.player1.softDrop();
  }

  public void rotate(boolean clockwise) {
    player1.rotate(clockwise);
  }
}
