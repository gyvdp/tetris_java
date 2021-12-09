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

import esi.acgt.atlj.database.db.DataBaseManager;
import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.database.exceptions.DbException;
import esi.acgt.atlj.database.exceptions.DtoException;
import esi.acgt.atlj.model.game.Action;
import java.util.HashMap;
import java.util.Map;

/**
 * All tools needed to interact with database.
 */
public class BusinessModel implements BusinessInterface {


  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(int id) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      User user = UserBusinessLogic.findById(id);
      DataBaseManager.validateTransacation();
      return user;
    } catch (DbException e) {
      String error = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        error = ex.getMessage() + "\n" + e.getMessage();
      }
      throw new BusinessException("Cannot access users\n " + error);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(String username) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      User user = UserBusinessLogic.findByUsername(username);
      DataBaseManager.validateTransacation();
      return user;
    } catch (DbException e) {
      String error = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        error = ex.getMessage() + "\n" + e.getMessage();
      }
      throw new BusinessException("Cannot access users\n " + error);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int addUser(String username) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      User user = new User(username);
      int id = UserBusinessLogic.add(user);
      DataBaseManager.validateTransacation();
      return id;
    } catch (DbException | DtoException e) {
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        throw new BusinessException(ex.getMessage());
      }
      throw new BusinessException(e.getMessage());
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(User user) throws BusinessException {
    try {
      if (user.isPersistant()) {
        DataBaseManager.startTransaction();
        UserBusinessLogic.remove(user);
        DataBaseManager.validateTransacation();
      } else {
        throw new BusinessException("User does not exist in database");
      }
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + msg;
      } finally {
        throw new BusinessException("Impossible to remove user \n" + msg);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HashMap<String, Integer> selectAllFromGameHistory(User user) throws BusinessException {
    HashMap<String, Integer> statistics;
    try {
      DataBaseManager.startTransaction();
      statistics = GameStatsBusinessLogic.selectAll(user);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Impossible to select all \n" + msg);
      }
    }
    return statistics;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUser(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      UserBusinessLogic.update(user);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("User update is impossible \n" + msg);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getUserHighScore(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      int highScore = GameStatsBusinessLogic.getHighScore(user);
      DataBaseManager.validateTransacation();
      return highScore;
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Getting high score of user has failed \n" + msg);
      }
    }
  }

  /**
   * Tries to set a new high score to the user.
   *
   * @param user         User to set new high score to.
   * @param newHighScore New high score of the user.
   * @throws BusinessException If query to set new high score has failed
   */
  public void setUserHighScore(User user, int newHighScore) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      GameStatsBusinessLogic.setHighScore(user, newHighScore);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Setting high score of user has failed \n" + msg);
      }
    }
  }

  @Override
  public void addLostGame(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      GameStatsBusinessLogic.incrementLostGame(user);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Getting number of games won by user has failed \n" + msg);
      }
    }
  }

  @Override
  public void addWonGame(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      GameStatsBusinessLogic.incrementWonGame(user);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Getting number of games won by user has failed \n" + msg);
      }
    }
  }

  @Override
  public void setHighestLevel(User user, int level) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      GameStatsBusinessLogic.setHighestLevel(user, level);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Setting highest level has failed \n" + msg);
      }
    }
  }

  @Override
  public void addBurns(User user, int increase) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      TetriminoStatBusinessLogic.addBurns(user, increase);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Cannot increment burns \n" + msg);
      }
    }
  }

  @Override
  public void addDestroyedLines(User user, Map<Action, Integer> actions)
      throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      TetriminoStatBusinessLogic.addDestroyedLines(user, actions);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Cannot add to placed tetriminos \n" + msg);
      }
    }
  }

  @Override
  public HashMap<String, Integer> selectAllFromTetriminoHistory(User user)
      throws BusinessException {
    HashMap<String, Integer> statistics;
    try {
      DataBaseManager.startTransaction();
      statistics = TetriminoStatBusinessLogic.selectAll(user);
      DataBaseManager.validateTransacation();
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Impossible to select all \n" + msg);
      }
    }
    return statistics;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNumberOfGamesWon(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      int highScore = GameStatsBusinessLogic.getNbGamesWon(user);
      DataBaseManager.validateTransacation();
      return highScore;
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Getting number of games won by user has failed \n" + msg);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNumberOfGamesLost(User user) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      int nbLost = GameStatsBusinessLogic.getNbGamesLost(user);
      DataBaseManager.validateTransacation();
      return nbLost;
    } catch (DbException e) {
      String msg = e.getMessage();
      try {
        DataBaseManager.cancelTransaction();
      } catch (DbException ex) {
        msg = ex.getMessage() + e.getMessage();
      } finally {
        throw new BusinessException("Getting number of games lost by user has failed \n" + msg);
      }
    }
  }
}
