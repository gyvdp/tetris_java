package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface ViewInterface extends PropertyChangeListener {

  /**
   * Show the primary stage in the center of the screen
   */
  void show();

  /**
   * Setter of controller so that View will be interact with the Controller
   *
   * @param controller Controller to interact with
   */
  void setController(Controller controller);

  /**
   * Show a pop up with the exception message inside
   *
   * @param e excption to display
   */
  void displayError(Exception e);

  /**
   * Create and display a Stage where you put information about the server
   */
  void displayConnexion();

  /**
   * Display the board of your tetris game and opponent one
   */
  void displayBoard();

  /**
   * Get all propertyChange to update the view
   *
   * @param evt event of the propertyChange
   */
  void propertyChange(PropertyChangeEvent evt);

}
