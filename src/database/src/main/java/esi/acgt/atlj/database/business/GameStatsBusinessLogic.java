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

import esi.acgt.atlj.database.db.GameStatsTable;
import esi.acgt.atlj.database.dto.UserDto;
import java.util.HashMap;

class GameStatsBusinessLogic {


  /**
   * Gets the high score of the given user.
   *
   * @param user User to get high score from.
   * @return High score of the user.
   */
  static int getHighScore(UserDto user) {
    try {
      return GameStatsTable.getHighScore(user);
    } catch (Exception e) {
      System.err.println("Cannot get high score from database \n" + e.getMessage());
    }
    return -1;
  }

  /**
   * @param user
   * @param level
   */
  static void setHighestLevel(UserDto user, int level) {
    try {
      GameStatsTable.setHighestLevel(user, level);
    } catch (Exception e) {
      System.err.println("Cannot set highest level in database \n" + e.getMessage());
    }
  }

  /**
   * Sets the high score of the given user.
   *
   * @param user User to set high score from.
   */
  static void setHighScore(UserDto user, int newHighScore) {
    try {
      GameStatsTable.setHighScore(user, newHighScore);
    } catch (Exception e) {
      System.err.println("Cannot set high score \n" + e.getMessage());
    }
  }

  /**
   * Gets the number of games won of the given user.
   *
   * @param user User to get number of games won.
   * @return Number of games won by the user.
   */
  static int getNbGamesWon(UserDto user) {
    try {
      return GameStatsTable.getNbGameWon(user);
    } catch (Exception e) {
      System.err.println("Cannot get number of games won \n" + e.getMessage());
    }
    return -1;
  }

  /**
   * Increments the number of games won of a user.
   *
   * @param user User to modify
   */
  static void incrementWonGame(UserDto user) {
    try {
      GameStatsTable.addWonGame(user);
    } catch (Exception e) {
      System.err.println("Cannot add to the number of games won \n" + e.getMessage());
    }
  }

  /**
   * Selects all columns in table of specific user.
   *
   * @param user User to select all from.
   */
  static HashMap<String, Integer> selectAll(UserDto user) {
    try {
      return GameStatsTable.selectAllColumnsOfTable(user);
    } catch (Exception e) {
      System.err.println(
          "Cannot select all of columns of game_history from user" + "[" + user + "]");
    }
    return null;
  }

  /**
   * Increments the number of game lost by a user.
   *
   * @param user User to modify
   */
  static void incrementLostGame(UserDto user) {
    try {
      GameStatsTable.addLostGame(user);
    } catch (Exception e) {
      System.err.println("Cannot add to the number of games lost \n" + e.getMessage());
    }
  }

  /**
   * Gets the number of games won of the given user.
   *
   * @param user User to get number of games won.
   * @return Number of games won by the user.
   */
  static int getNbGamesLost(UserDto user) {
    try {
      return GameStatsTable.getNbGameLost(user);
    } catch (Exception e) {
      System.err.println("Cannot get number of games won \n" + e.getMessage());
    }
    return -1;
  }

}
