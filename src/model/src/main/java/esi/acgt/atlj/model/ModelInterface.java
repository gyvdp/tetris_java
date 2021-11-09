package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.BoardInterface;

public interface ModelInterface {

  /**
   * Get the usernames of the players
   *
   * @return array of usernames
   */
  String[] getUsernames();

  /**
   * Get the board of a particular player
   *
   * @param username username of the player to get the board from
   * @return the board of the asked player
   */
  BoardInterface getBoard(String username);

}
