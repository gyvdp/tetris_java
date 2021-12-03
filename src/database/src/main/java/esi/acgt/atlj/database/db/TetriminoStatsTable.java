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
import esi.acgt.atlj.model.game.Action;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class TetriminoStatsTable {

  /**
   * Name of table
   */
  private final static String tableName = "Tetrimino Stats";

  /**
   * @param user
   * @param actions
   * @throws DbException
   */
  public static void addDestroyedLines(User user, Map<Action, Integer> actions)
      throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement addActions;
      addActions = connection.prepareStatement(
          "UPDATE tetrimino_stats "
              + "SET single = single + ?,"
              + "doubles = doubles + ?,"
              + "triple = triple + ?,"
              + "tetris = tetris + ?,"
              + "soft_drop = soft_drop + ?,"
              + "hard_drop = hard_drop + ?"
              + "WHERE user_id = ?;");
      if (user.isPersistant()) {
        parseAction(addActions, actions);
        addActions.setInt(7, user.getId());
        addActions.executeUpdate();
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add destroyed lines to the user\n" + e.getMessage());
    }
  }

  /**
   * Parses actions from client statistics.
   *
   * @param statement Statements to add action to.
   * @param actions   Actions to parse.
   * @throws DbException If query has failed.
   */
  private static void parseAction(PreparedStatement statement, Map<Action, Integer> actions)
      throws DbException {
    try {
      statement.setInt(1, actions.getOrDefault(Action.SINGLE, 0));
      statement.setInt(2, actions.getOrDefault(Action.DOUBLE, 0));
      statement.setInt(3, actions.getOrDefault(Action.TRIPLE, 0));
      statement.setInt(4, actions.getOrDefault(Action.TETRIS, 0));
      statement.setInt(5, actions.getOrDefault(Action.SOFT_DROP, 0));
      statement.setInt(6, actions.getOrDefault(Action.HARD_DROP, 0));
    } catch (SQLException e) {
      throw new DbException(
          tableName + ": Impossible to add destroyed lines to the user\n" + e.getMessage());
    }
  }

  /**
   * @param user     Adds number of burns to the user
   * @param increase Numbers to increase total number of burns to the user.
   * @throws DbException If query to add number of burns has failed.
   */
  public static void addNbBurns(User user, int increase) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement addBurns;
      addBurns = connection.prepareStatement(
          "UPDATE main.tetrimino_stats SET total_burns = total_burns + ? WHERE user_id = ?;");
      if (user.isPersistant()) {
        addBurns.setInt(1, increase);
        addBurns.setInt(2, user.getId());
        addBurns.executeUpdate();
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add nb of burns to the user\n" + e.getMessage());
    }
  }

  /**
   * Selects all columns of database from user.
   *
   * @param user User to select all columns from.
   * @throws DbException If query to select all has failed.
   */
  public static void selectAll(User user) throws DbException {

  }

}
