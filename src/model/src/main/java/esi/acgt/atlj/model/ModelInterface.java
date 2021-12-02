package esi.acgt.atlj.model;

import esi.acgt.atlj.model.game.GameInterface;
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
  GameInterface getBoard(String username);

  /**
   * Ads a player to the game.
   *
   * @param board Game to add player to.
   */
  void addPlayer(GameInterface board);

  /**
   * Links Games (ManagedGame and Unmanaged) with their owm Listener
   *
   * @param listener all listerners
   */
  void addPropertyChangeListenerToBoards(PropertyChangeListener[] listener);

  /**
   * Remove listener from the PropertyChanger
   *
   * @param listener listener to remove.
   */
  void removePropertyChangeListener(PropertyChangeListener listener);
}
