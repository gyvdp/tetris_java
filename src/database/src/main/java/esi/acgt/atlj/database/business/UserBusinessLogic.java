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

import esi.acgt.atlj.database.db.UserTable;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.DbException;

/**
 * All tools needed to interact with user table.
 */
class UserBusinessLogic {

  /**
   * Adds a user into the database.
   *
   * @param user User to add into the database.
   * @return Id of user in database.
   * @throws DbException If failed to add user.
   */
  static int add(UserDto user) throws DbException {
    return UserTable.insert(user);
  }


  /**
   * Removes a user from the database
   *
   * @param user User to remove from database.
   * @throws DbException If failed to remove user.
   */
  static void remove(UserDto user) throws DbException {
    UserTable.delete(user);
  }

  /**
   * Updates a specific user in the database.
   *
   * @param user User to update in the database.
   * @throws DbException If failed to update user.
   */
  static void update(UserDto user) throws DbException {
    UserTable.update(user);
  }

  /**
   * Finds a user by id in the database.
   *
   * @param id Id to find user by.
   * @return User that match the id or null if error has occurred.
   */
  static UserDto findById(int id) {
    try {
      return UserTable.findId(id);
    } catch (Exception e) {
      System.err.println("Cannot find user \n" + e.getMessage());
    }
    return null;
  }

  /**
   * Finds a user by id in the database.
   *
   * @param username Username to find user by.
   * @return User that match the id or null if error has occurred.
   */
  static UserDto findByUsername(String username) {
    try {
      return UserTable.findUsername(username);
    } catch (Exception e) {
      System.err.println("Cannot find user \n" + e.getMessage());
    }
    return null;
  }

}
