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

import esi.acgt.atlj.database.db.DataBaseManager;
import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.BuisnessException;
import esi.acgt.atlj.database.exceptions.DbException;
import esi.acgt.atlj.database.exceptions.DtoException;

public class BuisnessModel implements BuisnessInterface {

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(int id) throws BuisnessException {
    try {
      DataBaseManager.startTransaction();
      User user = UserBuisnessLogic.findById(id);
      DataBaseManager.validateTransacation();
      return user;
    } catch (DbException e) {
      String error = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        error = ex.getMessage() + "\n" + e.getMessage();
      }
      throw new BuisnessException("Cannot access users\n " + error);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(String username) throws BuisnessException {
    try {
      DataBaseManager.startTransaction();
      User user = UserBuisnessLogic.findByUsername(username);
      DataBaseManager.validateTransacation();
      return user;
    } catch (DbException e) {
      String error = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        error = ex.getMessage() + "\n" + e.getMessage();
      }
      throw new BuisnessException("Cannot access users\n " + error);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int addUser(String username) throws BuisnessException {
    try {
      DataBaseManager.startTransaction();
      User user = new User(username);
      int id = UserBuisnessLogic.add(user);
      DataBaseManager.validateTransacation();
      return id;
    } catch (DbException | DtoException e) {
      String error = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        throw new BuisnessException(ex.getMessage());
      }
      throw new BuisnessException(e.getMessage());
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(User user) throws BuisnessException {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUser(User user) throws BuisnessException {

  }
}
