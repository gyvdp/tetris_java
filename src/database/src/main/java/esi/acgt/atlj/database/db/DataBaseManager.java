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

import esi.acgt.atlj.database.exceptions.DbException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connexion and transaction management class.
 */
public class DataBaseManager {

  /**
   * DataBase url location.
   */
  private final static String databaseUrl =
      "jdbc:sqlite:tetrisDb";
  /**
   * Connection to database.
   */
  private static Connection connection;

  /**
   * Attempts to connect to database.
   *
   * @return Successfully connection if it has connected.
   * @throws DbException If problem occurred when connection to database.
   */
  public static Connection getConnection() throws DbException {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection(databaseUrl);
      } catch (SQLException e) {
        throw new DbException("DataBase connexion is impossible" + e.getMessage());
      }
    }
    return connection;
  }

  /**
   * Start a regular transaction
   *
   * @throws DbException If start of transaction failed.
   */
  public static void startTransaction() throws DbException {
    try {
      getConnection().setAutoCommit(false);
    } catch (SQLException ex) {
      throw new DbException("Impossible to start a transaction : " + ex.getMessage());
    }
  }

  /**
   * Start a transaction with a specified level of isolation.
   *
   * @param isolationLevel Defines the isolation level of transaction.
   * @throws DbException If start of transaction failed.
   */
  public static void startTransaction(int isolationLevel) throws DbException {
    try {
      getConnection().setAutoCommit(false);

      int level = 0;
      switch (isolationLevel) {
        case 0:
          level = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
          break;
        case 1:
          level = java.sql.Connection.TRANSACTION_READ_COMMITTED;
          break;
        case 2:
          level = java.sql.Connection.TRANSACTION_REPEATABLE_READ;
          break;
        case 3:
          level = java.sql.Connection.TRANSACTION_SERIALIZABLE;
          break;
        default:
          throw new DbException("Isolation level does not exist");
      }
      getConnection().setTransactionIsolation(level);
    } catch (SQLException ex) {
      throw new DbException("Impossible to start a transaction : " + ex.getMessage());
    }
  }

  /**
   * Validates the current transaction.
   *
   * @throws DbException If current transaction cannot be validated.
   */
  public static void validateTransacation() throws DbException {
    try {
      getConnection().commit();
      getConnection().setAutoCommit(true);
    } catch (SQLException ex) {
      throw new DbException("Impossible to validate transaction : " + ex.getMessage());
    }
  }

  /**
   * Cancel the current transaction.
   *
   * @throws DbException If the current transaction cannot be canceled.
   */
  public static void cancelTransaction() throws DbException {
    try {
      getConnection().rollback();
      getConnection().setAutoCommit(true);
    } catch (SQLException ex) {
      throw new DbException("Impossible to cancel transaction : " + ex.getMessage());
    }
  }

}
