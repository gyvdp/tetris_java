package esi.acgt.atlj.model;

public interface ModelInterface {

  /**
   * Get the usernames of the players
   *
   * @return array of usernames
   */
  String[] getUsernames();

  /**
   * Get the board of the managed board player
   *
   * @param username username of the player to get the board from
   * @return the board of the asked player
   */
  BoardInterface getManagedBoard(String username);

  /**
   * Gets the board of the unmanaged player
   * @param username
   * @return
   */
  BoardInterface getUnmanagedBoard(String username);

}
