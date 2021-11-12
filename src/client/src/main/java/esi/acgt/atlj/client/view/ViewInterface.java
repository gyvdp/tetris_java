package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface ViewInterface extends PropertyChangeListener {

  void show();

  void setController(Controller controller);

  void displayError(Exception e);

  void displayConnexion();

  void displayBoard();

  void updateBoard(Mino[][] oldBoard, Mino[][] newBoard, int playerID);

  void updateScore(int newScore, int playerID);

  void updateUsername(String newUsername, int playerID);

  void updateTimer(int timer, int playerID);

  void updateLine(int line, int playerID);

  void updateHold(TetriminoInterface hold, int playerID);

  void propertyChange(PropertyChangeEvent evt);

}
