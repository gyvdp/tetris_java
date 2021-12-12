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

import esi.acgt.atlj.database.dto.GameHistoryDto;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.DbException;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Tools needed to interact with game stats table
 */
public class GameStatsTable {

  /**
   * Table name.
   */
  private static final String tableName = "Game Stats";


  /**
   * Gets high-score of the user.
   *
   * @param id User to get high score of user.
   * @return High score of the user.
   * @throws DbException If query for getting high score from user has failed.
   */
  public static int getHighScore(int id) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement highScore;
      highScore = connection.prepareStatement(
          "SELECT high_score FROM game_stats WHERE user_id = ? LIMIT 1");
      highScore.setInt(1, id);
      ResultSet rs = highScore.executeQuery();
      return rs.getInt(1);
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get high score from the user\n" + e.getMessage());
    }
  }

  /**
   * Gets the highest level.
   *
   * @param id Id of the client.
   * @return Int of the highest level.
   * @throws DbException If query failed for getting high score from user has failed.
   */
  private static int getHighLevel(int id) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement highLevel;
      highLevel = connection.prepareStatement(
          "SELECT highest_level FROM game_stats WHERE user_id = ? LIMIT 1");
      highLevel.setInt(1, id);
      ResultSet rs = highLevel.executeQuery();
      return rs.getInt(1);
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to get high level from the user\n" + e.getMessage());
    }
  }

  /**
   * Selects all columns of the table from the user.
   *
   * @param user User to select all columns.
   * @throws DbException If query to select all has failed.
   */
  public static GameHistoryDto selectAllColumnsOfTable(UserDto user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement selectAll;
      selectAll = connection.prepareStatement(
          "SELECT high_score, nb_lost, nb_won,highest_level FROM game_stats WHERE user_id = ? LIMIT 1");
      if (user.isPersistant()) {
        selectAll.setInt(1, user.getId());
        return parseSelectAll(selectAll.executeQuery(), user.getId());
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
  private static GameHistoryDto parseSelectAll(ResultSet parsable, int id) throws DbException {
    try {
      return new GameHistoryDto(id, parsable.getInt(4), parsable.getInt(1), parsable.getInt(3),
          parsable.getInt(2)
      );
    } catch (SQLException e) {
      throw new DbException("Cannot parse select all query");
    }
  }

  /**
   * Sets the new high score of the user.
   *
   * @param id           Id to set new high score to.
   * @param newHighScore New high score to set.
   * @throws DbException If query for setting high score from user has failed.
   */
  public static void setHighScore(int id, int newHighScore) throws DbException {
    if (getHighScore(id) < newHighScore) {
      try {
        java.sql.Connection connection = DataBaseManager.getConnection();
        java.sql.PreparedStatement highScore;
        highScore = connection.prepareStatement(
            "UPDATE main.game_stats SET high_score =" + newHighScore + " WHERE user_id = ?");
        highScore.setInt(1, id);
        highScore.executeUpdate();
      } catch (Exception e) {
        throw new DbException(
            tableName + ": Impossible to set high score for the user\n" + e.getMessage());
      }
    } else {
      System.err.println(id + " " + newHighScore + " was not a highscore");
    }
  }

  /**
   * @param id    Id to set level to.
   * @param level Level to set.
   * @throws DbException If query has failed.
   */
  public static void setHighestLevel(int id, int level) throws DbException {
    if (level > getHighLevel(id)) {
      try {
        java.sql.Connection connection = DataBaseManager.getConnection();
        java.sql.PreparedStatement updateNbWonGame;
        updateNbWonGame = connection.prepareStatement(
            "UPDATE game_stats SET highest_level = ? WHERE user_id = ?");
        updateNbWonGame.setInt(1, level);
        updateNbWonGame.setInt(2, id);
        updateNbWonGame.executeUpdate();
      } catch (Exception e) {
        throw new DbException(
            tableName + ": Impossible to add won game to the user\n" + e.getMessage());
      }
    }
  }

  /**
   * Increments the number of won games.
   *
   * @param id User to increment from.
   * @throws DbException If query has failed for adding a won game.
   */
  public static void addWonGame(int id) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement updateNbWonGame;
      updateNbWonGame = connection.prepareStatement(
          "UPDATE game_stats SET nb_won = nb_won+1 WHERE user_id = ?;");
      updateNbWonGame.setInt(1, id);
      updateNbWonGame.executeUpdate();
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add won game to the user\n" + e.getMessage());
    }
  }


  /**
   * Increments the number of lost games.
   *
   * @param id User to increment from
   * @throws DbException If query has failed for adding a lost game.
   */
  public static void addLostGame(int id) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement updateNbWonGame;
      updateNbWonGame = connection.prepareStatement(
          "UPDATE game_stats SET nb_lost = nb_lost+1 WHERE user_id = ?;");
      updateNbWonGame.setInt(1, id);
      updateNbWonGame.executeUpdate();
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add lost game to the user\n" + e.getMessage());
    }
  }

}

