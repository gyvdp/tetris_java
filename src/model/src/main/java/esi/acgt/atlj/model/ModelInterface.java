package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.BoardInterface;
import esi.acgt.atlj.model.board.Direction;
import java.beans.PropertyChangeListener;

public interface ModelInterface {

  /**
   * Get the usernames of the players
   *
   * @return array of usernames
   */
  String[] getUsernames();

  void updateTest();

  /**
   * Get the board of a particular Player Get the board of the managed board player
   *
   * @param username username of the Player to get the board from
   * @return the board of the asked Player
   */
  BoardInterface getManagedBoard(String username);

  /**
   * Gets the board of the unmanaged player.
   *
   * @param username Name of player.
   * @return the unmanaged board.
   */
  BoardInterface getUnmanagedBoard(String username);

  void addPropertyChangeListener(PropertyChangeListener listener);

  void setPlayer1(String name);

  void removePropertyChangeListener(PropertyChangeListener listener);

  void move(Direction direction);
}
