/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew SASSOYE, Constantin GUNDUZ, Gregory VAN DER PLUIJM, Thomas LEUTSCHER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package esi.acgt.atlj.database.business;

import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.model.game.Action;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface to interact with database.
 */
public interface BusinessInterface {

  /**
   * Gets a specific user in the database.
   *
   * @param id Id of user.
   * @return User of the given id.
   * @throws BusinessException If the query for getting the user has failed.
   */
  User getUser(int id) throws BusinessException;

  /**
   * Gets a specific user in the database.
   *
   * @param username Username of the user.
   * @return User of the given username.
   * @throws BusinessException If the query for getting the user has failed.
   */
  User getUser(String username) throws BusinessException;

  /**
   * Adds a user to the database.
   *
   * @param username Username of user to add.
   * @return Id of added user.
   * @throws BusinessException If query to create user has failed.
   */
  int addUser(String username) throws BusinessException;


  /**
   * Gets the high score of a user.
   *
   * @param user User to get high score from.
   * @return High score of user, -1 if user does not exist.
   * @throws BusinessException If query from high score failed.
   */
  int getUserHighScore(User user) throws BusinessException;


  /**
   * Gets the number of game lost by the user.
   *
   * @param user User to get number of lost games from.
   * @return Number of lost games, -1 if user does not exist.
   * @throws BusinessException If query from number of lost game failed.
   */
  int getNumberOfGamesLost(User user) throws BusinessException;

  /**
   * Gets the number of games won by the user.
   *
   * @param user User to get number of games won by.
   * @return Number of games won by the user, -1 if user does not exist.
   * @throws BusinessException If query from number of games failed.
   */
  int getNumberOfGamesWon(User user) throws BusinessException;

  /**
   * Removes user from the database.
   *
   * @param user Persistent user to remove from database.
   * @throws BusinessException If query to remove user has failed.
   */
  void removeUser(User user) throws BusinessException;

  /**
   * Selects all information from game history of user.
   *
   * @param user User to select from.
   * @return HashMap contain statistics
   */
  HashMap<String, Integer> selectAllFromUserHistory(User user) throws BusinessException;

  /**
   * Updates user in the database.
   *
   * @param user User to update in database.
   * @throws BusinessException If query for updating user has failed.
   */
  void updateUser(User user) throws BusinessException;

  void setUserHighScore(User user, int newHighScore) throws BusinessException;

  void addLostGame(User user) throws BusinessException;

  void addWonGame(User user) throws BusinessException;

  void setHighestLevel(User user, int level) throws BusinessException;

  void addBurns(User user, int increase) throws BusinessException;

  void addDestroyedLines(User user, Map<Action, Integer> actions) throws BusinessException;
}
