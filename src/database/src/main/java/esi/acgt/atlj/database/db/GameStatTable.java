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

package esi.acgt.atlj.database.db;

import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.DbException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;


/**
 * Tools needed to interact with game stats table
 */
public class GameStatTable {

  /**
   * Table name.
   */
  private static final String tableName = "Game Stats";

  /**
   * Gets high-score of the user.
   *
   * @param user User to get high score of user.
   * @return High score of the user.
   * @throws DbException If query for getting high score from user has failed.
   */
  public static int getHighScore(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement highScore;
      highScore = connection.prepareStatement(
          "SELECT high_score FROM game_stats WHERE user_id = ? LIMIT 1");
      if (user.isPersistant()) {
        highScore.setInt(1, user.getId());
        ResultSet rs = highScore.executeQuery();
        return rs.getInt(1);
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get high score from the user\n" + e.getMessage());
    }
    return -1;
  }

  /**
   * Selects all columns of the table from the user.
   *
   * @param user User to select all columns.
   * @throws DbException If query to select all has failed.
   */
  public static HashMap<String, Integer> selectAllColumnsOfTable(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement selectAll;
      selectAll = connection.prepareStatement(
          "SELECT high_score, nb_lost, nb_won FROM game_stats WHERE user_id = ? LIMIT 1");
      if (user.isPersistant()) {
        selectAll.setInt(1, user.getId());
        return parseSelectAll(selectAll.executeQuery());
      }

    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get high score from the user\n" + e.getMessage());
    }
    return null;
  }


  /**
   * Result set for select all to parse into list for all
   *
   * @param parsable Result set to parse.
   * @throws DbException if parsing query for select all has failed.
   */
  private static HashMap<String, Integer> parseSelectAll(ResultSet parsable) throws DbException {
    HashMap<String, Integer> statistics = new HashMap<>();
    try {
      statistics.put("SCORE", parsable.getInt(1));
      statistics.put("LOST", parsable.getInt(2));
      statistics.put("WON", parsable.getInt(3));
    } catch (SQLException e) {
      throw new DbException("Cannot parse select all query");
    }
    return statistics;
  }

  /**
   * Sets the new high score of the user.
   *
   * @param user         User to set new high score to.
   * @param newHighScore New high score to set.
   * @throws DbException If query for setting high score from user has failed.
   */
  public static void setHighScore(User user, int newHighScore) throws DbException {
    if (getHighScore(user) < newHighScore) {
      try {
        java.sql.Connection connection = DataBaseManager.getConnection();
        java.sql.PreparedStatement highScore;
        highScore = connection.prepareStatement(
            "UPDATE main.game_stats SET high_score =" + newHighScore + " WHERE user_id = ?");
        if (user.isPersistant()) {
          highScore.setInt(1, user.getId());
          highScore.executeUpdate();
        }
      } catch (Exception e) {
        throw new DbException(
            tableName + ": Impossible to set high score for the user\n" + e.getMessage());
      }
    } else {
      System.out.println(
          "[" + user.getUsername() + "]" + " " + newHighScore + " was not a high score");
    }
  }

  /**
   * @param user
   * @param level
   * @throws DbException
   */
  public static void setHighestLevel(User user, int level) throws DbException {
    //todo if (level > getLevel(user)){
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement updateNbWonGame;
      updateNbWonGame = connection.prepareStatement(
          "UPDATE game_stats SET highest_level = ? WHERE user_id = ?");
      if (user.isPersistant()) {
        updateNbWonGame.setInt(1, level);
        updateNbWonGame.setInt(2, user.getId());
        updateNbWonGame.executeUpdate();
      }

    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add won game to the user\n" + e.getMessage());
    }
  }

  /**
   * Increments the number of won games.
   *
   * @param user User to increment from.
   */
  public static void addWonGame(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement updateNbWonGame;
      updateNbWonGame = connection.prepareStatement(
          "UPDATE game_stats SET nb_won = nb_won+1 WHERE user_id = ?;");
      if (user.isPersistant()) {
        updateNbWonGame.setInt(1, user.getId());
        updateNbWonGame.executeUpdate();
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add won game to the user\n" + e.getMessage());
    }
  }

  /**
   * Gets the number of game won by the user.
   *
   * @param user User to get number of game from.
   * @return Number of games won by the user, if user does not exist in database returns -1.
   * @throws DbException If query for getting number of games won has failed.
   */
  public static int getNbGameWon(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement nbGames;
      nbGames = connection.prepareStatement(
          "SELECT nb_won FROM game_stats WHERE user_id = ? LIMIT 1");
      if (user.isPersistant()) {
        nbGames.setInt(1, user.getId());
        ResultSet rs = nbGames.executeQuery();
        return rs.getInt(1);
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get high score from the user\n" + e.getMessage());
    }
    return -1;
  }

  public static void addLostGame(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement updateNbWonGame;
      updateNbWonGame = connection.prepareStatement(
          "UPDATE game_stats SET nb_lost = nb_lost+1 WHERE user_id = ?;");
      if (user.isPersistant()) {
        updateNbWonGame.setInt(1, user.getId());
        updateNbWonGame.executeUpdate();
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add lost game to the user\n" + e.getMessage());
    }
  }


  /**
   * Gets the number of game won by the user.
   *
   * @param user User to get number of game from.
   * @return Number of games won by the user.
   * @throws DbException If query for getting number of games won has failed.
   */
  public static int getNbGameLost(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement nbLost;
      nbLost = connection.prepareStatement(
          "SELECT nb_lost FROM game_stats WHERE user_id = ? LIMIT 1");

      nbLost.setInt(1, user.getId());
      ResultSet rs = nbLost.executeQuery();
      return rs.next() ?
          rs.getInt(1) : -1;
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get number of lost games from the user\n" + e.getMessage());
    }
  }
}

