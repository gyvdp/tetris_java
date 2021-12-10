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
import esi.acgt.atlj.model.tetrimino.TTetrimino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPlayer implements PlayerInterface {

  /**
   * The matrix of the board
   */
  protected Mino[][] matrix;

  /**
   * The username of the player
   */
  protected String username;

  /**
   * The stats of the actual game
   */
  protected PlayerStat stats;

  /**
   * The actual falling tetrimino
   */
  protected TetriminoInterface actualTetrimino;

  /**
   * The next tetrimino
   */
  protected TetriminoInterface nextTetrimino;

  /**
   * The holed tetrimino
   */
  protected Mino hold;

  /**
   * The Property Change support
   */
  protected PropertyChangeSupport pcs;

  /**
   * The AbstractGame constructor
   *
   * @param username the username of the player
   */
  public AbstractPlayer(String username) {
    this.username = username;
    this.actualTetrimino = new TTetrimino();
    this.nextTetrimino = null;
    this.pcs = new PropertyChangeSupport(this);
    this.matrix = new Mino[HEIGHT][WIDTH];
    this.stats = new PlayerStat(this.pcs);
  }

  /**
   * {@inheritDoc}
   */
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
        boolean notFree = !outOfBound && matrix[y][x] != null;
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
   * Setter of hold
   *
   * @param hold new mino that you hold
   */
  public synchronized void setHold(Mino hold) {
    this.hold = hold;
    this.pcs.firePropertyChange("hold", null, this.hold);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized TetriminoInterface getActualTetrimino() {
    return actualTetrimino;
  }

  /**
   * Setter of actualTetrimino
   *
   * @param actualTetrimino new actualTetrimino
   */
  public abstract void setActualTetrimino(TetriminoInterface actualTetrimino);

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized TetriminoInterface getNextTetrimino() {
    return nextTetrimino;
  }

  /**
   * Sets the next upcoming tetrimino.
   *
   * @param type Tetrimino to set.
   */
  public synchronized void setNextTetrimino(Mino type) {
    this.nextTetrimino = type != null ? Tetrimino.createTetrimino(type) : null;
    this.pcs.firePropertyChange("next", null, type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Mino[][] getMatrix() {
    Mino[][] copyBoard = new Mino[this.matrix.length - 2][];
    for (int i = 0; i < copyBoard.length; i++) {
      var y = i + 2;
      copyBoard[i] = Arrays.copyOf(this.matrix[y], this.matrix[y].length);
    }

    if (actualTetrimino != null) {
      var minos = actualTetrimino.getMinos();
      for (int i = 0; i < minos.length; ++i) {
        for (int j = 0; j < minos[i].length; ++j) {
          var x = actualTetrimino.getX() + j;
          var y = actualTetrimino.getY() + i - 2;
          if (y >= 0 && y < copyBoard.length && x >= 0 && x < copyBoard[y].length) {
            if (minos[i][j] != null) {
              copyBoard[y][x] = minos[i][j];
            }
          }
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
    pcs.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  /**
   * Fire a propertChange on the status to the view
   *
   * @param status  new Status to display
   * @param opacity opcaity of this new status
   */
  public synchronized void playerStatus(String status, double opacity) {
    this.pcs.firePropertyChange("status", status, opacity);
  }

  /**
   * Remove lines in the matrix
   *
   * @param lines The list of indexes of lines to remove
   */
  public synchronized void removeLines(List<Integer> lines) {
    var oldBoard = getMatrix();
    lines.sort(Collections.reverseOrder());
    int removed = 0;
    for (var line : lines) {
      if (line > matrix.length - 1 || line < 0) {
        throw new IllegalArgumentException("You cannot remove a line that is out of the game");
      }

      for (int i = line + removed; i >= 0; --i) {
        for (int j = 0; j < matrix[i].length; ++j) {
          matrix[i][j] = i != 0 ? matrix[i - 1][j] : null;
        }
      }
      removed++;
    }
    this.pcs.firePropertyChange("board", oldBoard, getMatrix());
  }

  /**
   * Place a tetrimino on the matrix
   *
   * @param tetrimino The tetrimino to place
   */
  public synchronized void placeTetrimino(TetriminoInterface tetrimino) {
    var oldBoard = getMatrix();
    var tMinos = tetrimino.getMinos();
    for (var i = 0; i < tMinos.length; ++i) {
      for (var j = 0; j < tMinos[i].length; ++j) {
        if (tMinos[i][j] == null) {
          continue;
        }
        var line = tetrimino.getY() + i;
        var col = tetrimino.getX() + j;

        if (!(line < 0 || col < 0) && line < matrix.length && col < matrix[line].length) {
          matrix[line][col] = tMinos[i][j];
        }
      }
    }

    List<Integer> lines = getFullLines();
    if (lines.size() != 0) {
      removeLines(lines);
      this.stats.applyAction(Action.getActionByFullLines(lines.size()));
    }

    this.pcs.firePropertyChange("board", oldBoard, getMatrix());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized PlayerStat getStats() {
    return this.stats;
  }

  /**
   * Gets all the line of the whole board
   *
   * @return All the line
   */
  protected synchronized List<Integer> getFullLines() {
    List<Integer> lines = new ArrayList<>();
    for (int i = 0; i < matrix.length; ++i) {
      boolean full = true;
      for (int j = 0; j < matrix[i].length; ++j) {
        if (matrix[i][j] == null) {
          full = false;
          break;
        }
      }

      if (full) {
        lines.add(i);
      }
    }

    return lines;
  }
}
