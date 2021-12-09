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

import esi.acgt.atlj.database.dto.TetriminoDto;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.DbException;
import esi.acgt.atlj.model.player.Action;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TetriminoStatsTable {

  /**
   * Name of table
   */
  private final static String tableName = "Tetrimino Stat";

  public static void setTetrimino(TetriminoDto entry) throws DbException {
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
              + "hard_drop = hard_drop + ?,"
              + "total_burns = total_burns + ?"
              + "WHERE user_id = ?;");
      if (entry.getSingle() != 0) {
        addActions.setInt(1, entry.getSingle());
      }
      if (entry.getDoubles() != 0) {
        addActions.setInt(2, entry.getDoubles());
      }
      if (entry.getTriple() != 0) {
        addActions.setInt(3, entry.getTriple());
      }
      if (entry.getTetris() != 0) {
        addActions.setInt(4, entry.getTetris());
      }
      if (entry.getSoftdrop() != 0) {
        addActions.setInt(5, entry.getSoftdrop());
      }
      if (entry.getHarddrop() != 0) {
        addActions.setInt(6, entry.getHarddrop());
      }
      if (entry.getBurns() != 0) {
        addActions.setInt(6, entry.getBurns());
      }
      addActions.setInt(8, entry.getId());
      addActions.executeUpdate();
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add destroyed lines to the user\n" + e.getMessage());
    }
  }


  /**
   * Selects all columns of database from user.
   *
   * @param user User to select all columns from.
   * @throws DbException If query to select all has failed.
   */
  public static TetriminoDto selectAll(UserDto user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement selectAll;
      selectAll = connection.prepareStatement(
          "SELECT total_burns, single, doubles, triple, tetris, soft_drop, hard_drop FROM tetrimino_stats WHERE user_id = ? LIMIT 1");
      if (user.isPersistant()) {
        selectAll.setInt(1, user.getId());
        return parseSelectAll(selectAll.executeQuery(), user.getId());
      }
    } catch (Exception e) {
      throw new DbException(
          tableName + ": Impossible to add nb of burns to the user\n" + e.getMessage());
    }
    return null;
  }

  public static TetriminoDto parseSelectAll(ResultSet parsable, int id) throws DbException {
    try {
      return new TetriminoDto(id, parsable.getInt(2), parsable.getInt(3), parsable.getInt(4),
          parsable.getInt(5), parsable.getInt(6), parsable.getInt(7), parsable.getInt(1));
    } catch (SQLException e) {
      throw new DbException("Cannot parse select all query");
    }
  }
}
