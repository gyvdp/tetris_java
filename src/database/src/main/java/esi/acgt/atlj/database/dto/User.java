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

package esi.acgt.atlj.database.dto;

import esi.acgt.atlj.database.exceptions.DtoException;

public class User extends EntityDto<Integer> {

  private String username;


  /**
   * Constructor of persistent user.
   *
   * @param id       Id of persistent user.
   * @param username Username of persistent user.
   * @throws DtoException If user is null.
   */
  public User(Integer id, String username) throws DtoException {
    this(username);
    this.id = id;
  }

  /**
   * Constructor for non-persistent user.
   *
   * @param username Username of non-persistent user.
   * @throws DtoException If user is null.
   */
  public User(String username) throws DtoException {
    if (username == null) {
      throw new DtoException("username cannot be null");
    }
    this.username = username;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void set(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
  }

  /**
   * Getter for username of player.
   *
   * @return Username of player.
   */
  public String getUsername() {
    return username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "[User]" + "(" + getId() + ", " + getUsername() + " )";
  }
}
