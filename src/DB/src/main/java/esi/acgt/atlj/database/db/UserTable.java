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

public class UserTable {

  private static final String tableName = "Users";

  /**
   * Insert into user table a user
   *
   * @param user User to add.
   * @return Id of user.
   */
  public static int insertDb(User user) throws DbException {
    try {
      int num = 0;//autoincrement.
      java.sql.Connection connection = DataBaseManager.getConnection();
      java.sql.PreparedStatement insert;
      insert = connection.prepareStatement("INSERT INTO User(USERNAME) VALUE ('?');");
      insert.setString(1, user.getUsername());
      insert.executeUpdate();
      return num;
    } catch (Exception e) {
      throw new DbException(tableName + ": Impossible to add a user\n" + e.getMessage());
    }
  }

  public static void delete(User user) {

  }

  public static void update(User user) {

  }

  public static User findUsername(String username) {
    return null;
  }

  public static User findId(int id) {
    return null;
  }
}
