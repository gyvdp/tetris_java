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

/**
 * Interface to communicate with database.
 */
public interface DataBaseInterface {

  /**
   * Connects to tetris database.
   */
  void connectToDb();

  /**
   * Creates a new username is the database.
   *
   * @param username Username of player.
   */
  void checkUserInDb(String username);

  /**
   * Sets the score in the database of the user. If high-score becomes new high-score.
   *
   * @param score    Score to set .
   * @param username Username to set score to.
   */
  void score(int score, String username);

  /**
   * Adds to the number of tetrises in the database of the user.
   *
   * @param nbTetris Number of tetrises to add.
   * @param username Username to add tertises too.
   */
  void numberOfTetris(int nbTetris, String username);

  /**
   * Adds to the number of lines destroyed in the database of the user.
   *
   * @param lines    Number of lines destroyed to add.
   * @param username Username to add number to.
   */
  void numberOfLinesDestroyed(int lines, String username);

  /**
   * Adds to the number of minos placed in the database of the user.
   *
   * @param numberOfMinos Number of minos to add.
   * @param username      Username to add number to.
   */
  void numberOfMinosPlaced(int numberOfMinos, String username);
}

