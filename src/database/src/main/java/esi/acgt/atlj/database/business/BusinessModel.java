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
import esi.acgt.atlj.database.dto.GameHistoryDto;
import esi.acgt.atlj.database.dto.TetriminoDto;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.database.exceptions.DbException;
import esi.acgt.atlj.database.exceptions.DtoException;

/**
 * All tools needed to interact with database.
 */
public class BusinessModel implements BusinessInterface {


  /**
   * {@inheritDoc}
   */
  @Override
  public UserDto getUser(String username) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      UserDto user = UserBusinessLogic.findByUsername(username);
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
      UserDto user = new UserDto(username);
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
  public GameHistoryDto getGameStatEntity(UserDto user) throws BusinessException {
    GameHistoryDto dto;
    try {
      DataBaseManager.startTransaction();
      dto = GameStatsBusinessLogic.getGameHistory(user);
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
    return dto;
  }

  @Override
  public TetriminoDto getTetriminoEntity(UserDto user) throws BusinessException {
    TetriminoDto dto;
    try {
      DataBaseManager.startTransaction();
      dto = TetriminoStatsBusinessLogic.getTetrimino(user);
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
    return dto;
  }

  @Override
  public void setGameStatEntity(GameHistoryDto gameStatEntity)
      throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      GameStatsBusinessLogic.setGameHistory(gameStatEntity);
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
  }

  @Override
  public void setTetriminoEntity(TetriminoDto tetriminoEntity) throws BusinessException {
    try {
      DataBaseManager.startTransaction();
      TetriminoStatsBusinessLogic.setTetrimino(tetriminoEntity);
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getUserHighScore(UserDto user) throws BusinessException {
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

}