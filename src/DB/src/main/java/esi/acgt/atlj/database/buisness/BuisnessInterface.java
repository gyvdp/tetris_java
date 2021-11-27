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

package esi.acgt.atlj.database.buisness;

import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.BuisnessException;

public interface BuisnessInterface {

  /**
   * Gets a specific user in the database.
   *
   * @param id Id of user.
   * @return User of the given id.
   * @throws BuisnessException If the query for getting the user has failed.
   */
  User getUser(int id) throws BuisnessException;

  /**
   * Gets a specific user in the database.
   *
   * @param username Username of the user.
   * @return User of the given username.
   * @throws BuisnessException If the query for getting the user has failed.
   */
  User getUser(String username) throws BuisnessException;

  /**
   * Adds a user to the database.
   *
   * @param username Username of user to add.
   * @return Id of added user.
   * @throws BuisnessException If query to create user has failed.
   */
  int addUser(String username) throws BuisnessException;

  /**
   * Removes user from the database.
   *
   * @param user Persistent user to remove from database.
   * @throws BuisnessException If query to remove user has failed.
   */
  void removeUser(User user) throws BuisnessException;

  /**
   * Updates user in the database.
   *
   * @param user User to update in database.
   * @throws BuisnessException If query for updating user has failed.
   */
  void updateUser(User user) throws BuisnessException;

}
