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

/**
 * Database object to represent a game history entry.
 */
public class GameHistoryDto extends EntityDto<Integer> {

  int highestLevel;
  int highScore;
  int nbWon;
  int nbLost;

  /**
   * Constructor of persistent entry.
   *
   * @param id           Id of user
   * @param highestLevel Highest level user has ever reached.
   * @param highScore    Highest score user has ever reached.
   * @param nbLost       Number of games player has lost.
   * @param nbWon        Number of games player has won
   */
  public GameHistoryDto(Integer id, int highestLevel, int highScore, int nbLost, int nbWon) {
    this(highestLevel, highScore, nbLost, nbWon);
    this.id = id;
  }

  /**
   * Constructor of non-persistent entry.
   *
   * @param highestLevel Highest level user has ever reached.
   * @param highScore    Highest score user has ever reached.
   * @param nbLost       Number of games player has lost.
   * @param nbWon        Number of games player has won
   */
  public GameHistoryDto(int highestLevel, int highScore, int nbLost, int nbWon) {
    this.highestLevel = highestLevel;
    this.highScore = highScore;
    this.nbLost = nbLost;
    this.nbWon = nbWon;
  }

  public int getHighestLevel() {
    return highestLevel;
  }

  public int getHighScore() {
    return highScore;
  }

  public int getNbWon() {
    return nbWon;
  }


  /**
   * Getter for number of games lost.
   *
   * @return Number of games lost.
   */
  public int getNbLost() {
    return nbLost;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return " [User]" + "(" + this.id + ") [ " + highestLevel + ", " + highScore + ", " + nbLost
        + ", " + nbWon + " ]";
  }
}
