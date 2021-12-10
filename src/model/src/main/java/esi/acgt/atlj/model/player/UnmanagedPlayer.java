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
package esi.acgt.atlj.model.player;

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;

/**
 * Game that is going to be updated by server. Opponent.
 */
public class UnmanagedPlayer extends AbstractPlayer {

  /**
   * Initializes a unmanaged game
   */
  public UnmanagedPlayer() {
    super("search");
  }

  /**
   * Sets the current tetrimino
   *
   * @param tetrimino Tetrimino to set.
   */
  public synchronized void setActualTetrimino(TetriminoInterface tetrimino) {
    Mino[][] oldBoard = this.getMatrix();
    this.actualTetrimino = tetrimino;
    this.pcs.firePropertyChange("board", oldBoard, this.getMatrix());
  }


  /**
   * Sets the score of the player
   *
   * @param score Score to set
   */
  public synchronized void setScore(int score) {
    this.stats.setScore(score);
  }

  /**
   * Sets the username of the player.
   *
   * @param username username to set.
   */
  public synchronized void setUsername(String username) {
    this.username = username;
    this.pcs.firePropertyChange("username", null, this.username);
  }

  /**
   * Sets the number of line the players has destroyed.
   *
   * @param nbLine Number of line to set to.
   */
  public synchronized void setBurns(int nbLine) {
    this.stats.setBurns(nbLine);
  }
}
