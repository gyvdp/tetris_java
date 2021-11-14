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
package esi.acgt.atlj.model.game;

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;

/**
 * Game that is going to be updated by server. Opponent.
 */
public class UnmanagedGame extends AbstractGame {

  /**
   * Initializes a unmanaged game
   */
  public UnmanagedGame() {
    super(null);
  }

  /**
   * Places a tetrimino
   *
   * @param tetrimino Tetrimino to place
   */
  public void placeTetrimino(TetriminoInterface tetrimino) {
    //TODO a faire en static via le lock.
    var oldBoard = getBoard();
    var tMinos = tetrimino.getMinos();
    for (var i = 0; i < tMinos.length; ++i) {
      for (var j = 0; j < tMinos[i].length; ++j) {
        if (tMinos[i][j] == null) {
          continue;
        }
        var line = tetrimino.getY() + i;
        var col = tetrimino.getX() + j;

        if (line < minos.length && col < minos[line].length) {
          minos[line][col] = tMinos[i][j];
        }
      }
    }
    this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
  }

  /**
   * Sets the current tetrimino
   *
   * @param tetrimino Tetrimino to set.
   */
  public void setActualTetrimino(TetriminoInterface tetrimino) {
    Mino[][] oldBoard = this.getBoard();
    this.actualTetrimino = tetrimino;
    this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
  }

  /**
   * Sets the next upcoming tetrimino.
   *
   * @param tetrimino Tetrimino to set.
   */
  public void setNextTetrimino(TetriminoInterface tetrimino) {
    this.nextTetrimino = tetrimino;
    this.changeSupport.firePropertyChange("next", null, this.getNextTetrimino());
  }

  /**
   * Sets the score of the player
   *
   * @param score Score to set
   */
  public void setScore(int score) {
    this.score = score;
    this.changeSupport.firePropertyChange("score", null, this.score);
  }

  /**
   * Sets the username of the player.
   *
   * @param username username to set.
   */
  public void setUsername(String username) {
    this.username = username;
    this.changeSupport.firePropertyChange("username", null, this.username);
  }

  /**
   * Sets the number of line the players has destroyed.
   *
   * @param nbLine Number of line to set to.
   */
  public void setNbLine(int nbLine) {
    System.out.println("I have to update my lines");
    this.nbLine += nbLine;
    this.changeSupport.firePropertyChange("line", null, this.nbLine);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHold(Mino hold) {
    this.hold = hold;
    this.changeSupport.firePropertyChange("hold", null, this.hold);
  }


}
