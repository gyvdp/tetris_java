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

import esi.acgt.atlj.model.tetrimino.ITetrimino;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.OTetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractGame implements GameInterface, Serializable {

  protected Mino[][] minos;
  protected int score;
  protected String username;
  protected int nbLine;
  protected Mino hold;
  protected TetriminoInterface actualTetrimino;
  protected TetriminoInterface nextTetrimino;
  protected PropertyChangeSupport changeSupport;

  public AbstractGame(String username) {
    this.username = username;
    this.score = 0;
    this.nbLine = 0;
    this.actualTetrimino = new OTetrimino();
    this.nextTetrimino = new ITetrimino();
    this.changeSupport = new PropertyChangeSupport(this);
    minos = new Mino[HEIGHT][WIDTH];
  }

  @Override
  public synchronized boolean[][] generateFreeMask(int height, int width, int xStart, int yStart,
      int xMargin, int yMargin) {
    if (width < 1) {
      throw new IllegalArgumentException("The width of the area can be null");
    }

    if (height < 1) {
      throw new IllegalArgumentException("The height of the area can be null");
    }

    boolean[][] mask = new boolean[height][width];
    for (int i = 0; i < mask.length; i++) {
      for (int j = 0; j < mask[i].length; j++) {
        int x = xStart - xMargin + j;
        int y = yStart - yMargin + i;

        boolean outOfBound = x < 0 || y < 0 || x > WIDTH - 1 || y > HEIGHT - 1;
        boolean notFree = !outOfBound && minos[y][x] != null;
        if (outOfBound || notFree) {
          mask[i][j] = false;
          continue;
        }
        mask[i][j] = true;
      }
    }

    return mask;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized int getScore() {
    return score;
  }

  /**
   * Setter of Score
   *
   * @param score new score
   */
  public abstract void setScore(int score);

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized String getUsername() {
    return username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Mino getHold() {
    return this.hold;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized TetriminoInterface getActualTetrimino() {
    return actualTetrimino;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized TetriminoInterface getNextTetrimino() {
    return nextTetrimino;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Mino[][] getBoard() {
    Mino[][] copyBoard = new Mino[this.minos.length][];
    for (int i = 0; i < this.minos.length; i++) {
      copyBoard[i] = Arrays.copyOf(this.minos[i], this.minos[i].length);
    }
    for (int x = 0; x < this.actualTetrimino.getMinos().length; x++) {
      for (int y = 0; y < this.actualTetrimino.getMinos()[x].length; y++) {
        if ((this.actualTetrimino.getMinos()[x][y] != null
            && x + this.actualTetrimino.getY() >= 0)
            && (x + this.actualTetrimino.getY() < this.minos.length)
            && (y + this.actualTetrimino.getX() >= 0)
            && (y + this.actualTetrimino.getX() < this.minos[x].length)) {
          copyBoard[x + this.actualTetrimino.getY()][y
              + this.actualTetrimino.getX()] = this.actualTetrimino.getMinos()[x][y];
        }
      }
    }
    return copyBoard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Sets the number of lines player has destroyed
   *
   * @param nbLine Number of lines to set
   */
  public synchronized void setNbLine(int nbLine) {
    int oldNbLine = this.nbLine;
    this.nbLine = nbLine;
    this.changeSupport.firePropertyChange("line", oldNbLine, this.nbLine);
  }

  /**
   * Setter of hold
   *
   * @param hold new mino that you hold
   */
  public abstract void setHold(Mino hold);

  /**
   * Setter of nextTetrimino
   *
   * @param nextTetrimino new nextTetrimino
   */
  public abstract void setNextTetrimino(TetriminoInterface nextTetrimino);

  /**
   * Setter of actualTetrimino
   *
   * @param actualTetrimino new actualTetrimino
   */
  public abstract void setActualTetrimino(TetriminoInterface actualTetrimino);

  /**
   * Fire a propertChange on the status to the view
   *
   * @param status  new Status to display
   * @param opacity opcaity of this new status
   */
  public void playerStatus(String status, double opacity) {
    this.changeSupport.firePropertyChange("status", status, opacity);
  }

  public synchronized void removeLines(List<Integer> lines) {
    var oldBoard = getBoard();
    lines.sort(Collections.reverseOrder());
    int removed = 0;
    for (var line : lines) {
      if (line > minos.length - 1 || line < 0) {
        throw new IllegalArgumentException("You cannot remove a line that is out of the game");
      }

      for (int i = line + removed; i >= 0; --i) {
        for (int j = 0; j < minos[i].length; ++j) {
          minos[i][j] = i != 0 ? minos[i - 1][j] : null;
        }
      }
      removed++;
    }

    this.changeSupport.firePropertyChange("board", oldBoard, getBoard());
  }
}
