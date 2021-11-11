package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.BoardInterface;
import java.beans.PropertyChangeListener;

public interface ModelInterface {

  /**
   * Get the usernames of the players
   *
   * @return array of usernames
   */
  String[] getUsernames();

  /**
   * Get the board of a particular Player Get the board of the managed board player
   *
   * @param username username of the Player to get the board from
   * @return the board of the asked Player
   */
  BoardInterface getManagedBoard(String username);

  /**
   * Gets the board of the unmanaged player
   *
   * @param username
   * @return
   */
  BoardInterface getUnmanagedBoard(String username);

  void addPropertyChangeListener(PropertyChangeListener listener);

  void setPlayer1(String name);

  void removePropertyChangeListener(PropertyChangeListener listener);
}
