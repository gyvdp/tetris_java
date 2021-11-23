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

package esi.acgt.atlj.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase implements DataBaseInterface {

  /**
   * Sys-out for database should be in green. Color green.
   */
  private static final String GREEN = "\033[0;32m";
  /**
   * Resets sys-out color.
   */
  public static final String RESET = "\033[0m";

  /**
   * Connection to database
   */
  Connection connection;

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectToDb() {
    //todo add username to directly check for username.
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection(
          "jdbc:sqlite:/home/gregory/Documents/2020-2021/Q5/ATLIR/JAVA/atlj-project/src/server/src/main/java/esi/acgt/atlj/server/database/Tetris.db");
      System.out.println(GREEN + "Connection to database established" + RESET);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes an sql query.
   *
   * @param sql query to execute.
   */
  private void executeSql(String sql) throws SQLException {
    Statement stm = connection.createStatement();
    int numberOfRowsAffected = stm.executeUpdate(sql);
    System.out.println(GREEN + "There have been " + GREEN + numberOfRowsAffected +
        GREEN + " affected in the database" + RESET);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkUserInDb(String username) {
    String query = "SELECT * FROM Login WHERE USERNAME=?";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      if (!preparedStatement.execute()) {
        executeSql("INSERT INTO main.User(Username) VALUES (" + username + ");");
      }
    } catch (SQLException e) {
      System.err.println("Cannot find " + username + "or create new username");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void score(int score, String username) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void numberOfTetris(int nbTetris, String username) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void numberOfLinesDestroyed(int lines, String username) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void numberOfMinosPlaced(int numberOfMinos, String username) {

  }

}
