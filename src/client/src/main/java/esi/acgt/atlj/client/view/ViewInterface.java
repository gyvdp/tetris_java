package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface ViewInterface extends PropertyChangeListener {

  public void show();

  public void setController(Controller controller);

  public void displayError(Exception e);

  public void displayConnexion();

  public void displayBoard();

  public void updateBoard(Mino[][] board, int playerID);

  public void updateScore(int newScore, int playerID);

  public void updateUsername(String newUsername, int playerID);

  public void updateTimer(int timer, int playerID);

  public void updateLine(int line, int playerID);

  public void propertyChange(PropertyChangeEvent evt);

}
