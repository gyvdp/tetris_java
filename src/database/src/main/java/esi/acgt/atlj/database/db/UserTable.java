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
import esi.acgt.atlj.database.exceptions.DtoException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserTable {

  private static final String tableName = "Users";

  /**
   * Insert into user table a user
   *
   * @param user User to add.
   * @return unique id of user.
   * @throws DbException If failed to add player.
   */
  public static int insert(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement insert;
      insert = connection.prepareStatement("INSERT INTO user(username) VALUES (?);");
      insert.setString(1, user.getUsername());
      insert.executeUpdate();
      insert = connection.prepareStatement("INSERT INTO game_stats(user_id) VALUES (?);");
      insert.setInt(1, findUsername(user.getUsername()).getId());
      insert.executeUpdate();
      return findUsername(user.getUsername()).getId();
    } catch (Exception e) {
      throw new DbException(tableName + ": Impossible to add a user\n" + e.getMessage());
    }
  }

  /**
   * Deletes from user table a user.
   *
   * @param user User to delete
   * @throws DbException If failed to delete player
   */
  public static void delete(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement alter;
      alter = connection.prepareStatement("DELETE FROM user WHERE username = ?");
      alter.setString(1, user.getUsername());
      alter.executeUpdate();
    } catch (Exception e) {
      throw new DbException(tableName + ": Impossible to delete the user\n" + e.getMessage());
    }
  }

  /**
   * Updates a user with the given user.
   *
   * @param user User to update in database.
   * @throws DbException If failed to update user.
   */
  public static void update(User user) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement update;
      update = connection.prepareStatement("UPDATE user SET "
          + "username=?"
          + "WHERE id=?;");
      update.setString(1, user.getUsername());
      update.setInt(2, user.getId());
      update.executeUpdate();
    } catch (Exception e) {
      throw new DbException(tableName + ": Impossible to update the user\n" + e.getMessage());
    }
  }

  private static ResultSet select(Object selector) throws DbException {
    try {
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement select = null;
      if (selector instanceof String username) {
        select = connection.prepareStatement("SELECT id, username FROM user WHERE username = ?");
        select.setString(1, username);
      }
      if (selector instanceof Integer id) {
        select = connection.prepareStatement("SELECT id, username FROM user WHERE id = ?");
        select.setInt(1, id);
      }
      if (select != null) {
        return select.executeQuery();
      }
    } catch (Exception e) {
      throw new DbException(tableName + ": Impossible to delete the user\n" + e.getMessage());
    }
    return null;
  }


  public static User findUsername(String username) throws DbException, SQLException, DtoException {
    ResultSet rs = select(username);
    if (rs.next()) {
      return new User(rs.getInt(1), rs.getString(2));
    }
    return null;
  }

  public static User findId(int id) throws DbException, SQLException, DtoException {
    ResultSet rs = select(id);
    if (rs.next()) {
      return new User(rs.getInt("id"), rs.getString("username"));
    }
    return null;
  }
}
