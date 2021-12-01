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

public class TetriminoStatsTable {

  /**
   * Name of table
   */
  private final static String tableName = "Tetrimino Stats";

  /**
   * @param user
   * @param increase
   * @throws DbException
   */
  public static void addNbPlacedTetriminos(User user, int increase) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement addTetriminos;
      addTetriminos = connection.prepareStatement(
          "UPDATE tetrimino_stats SET total_tetriminos_placed = total_tetriminos_placed + ? WHERE user_id = ?;");
      if (user.isPersistant()) {
        addTetriminos.setInt(1, increase);
        addTetriminos.setInt(2, user.getId());
        addTetriminos.executeUpdate();
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add won game to the user\n" + e.getMessage());
    }
  }

  /**
   * @param user
   * @param increase
   * @throws DbException
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

}
