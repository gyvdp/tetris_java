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
   * Get the board of a particular Player
   *
   * @param username username of the Player to get the board from
   * @return the board of the asked Player
   */
  BoardInterface getBoard(String username);

}
