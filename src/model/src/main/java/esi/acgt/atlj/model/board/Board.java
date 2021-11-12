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

package esi.acgt.atlj.model.board;

import esi.acgt.atlj.model.tetrimino.ITetrimino;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.OTetrimino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Arrays;

public abstract class Board implements BoardInterface, Serializable {

  protected Mino[][] minos;
  protected int score;
  protected String username;
  protected int nbLine;
  protected Mino hold;
  protected TetriminoInterface actualTetrimino;
  protected TetriminoInterface nextTetrimino;
  protected PropertyChangeSupport changeSupport;

  public Board(String username) {
    this.username = username;
    this.score = 0;
    this.nbLine = 0;
    this.actualTetrimino = new OTetrimino();
    this.nextTetrimino = new ITetrimino();
    this.changeSupport = new PropertyChangeSupport(this);
    minos = new Mino[HEIGHT][WIDTH];
  }

  @Override
  public synchronized void initTetrisBoard() {
    var oldBoard = new Mino[HEIGHT][WIDTH];;
    this.changeSupport.firePropertyChange("player1Board", oldBoard, this.getBoard());
    this.changeSupport.firePropertyChange("player1Next", null, this.getNextTetrimino());
  }

  @Override
  public synchronized boolean[][] getSurroundingArea(int x, int y) {
    boolean[][] surroundingArea = new boolean[4][4];
    for (int i = x; i <= x + 4; i++) {
      for (int j = y; j <= y + 4; j++) {
        //this.minos[i][j]=
      }
    }
    return surroundingArea;
  }

  @Override
  public synchronized int getScore() {
    return score;
  }

  public abstract void setScore(int score);

  @Override
  public synchronized String getUsername() {
    return username;
  }

  public abstract void setUsername(String username);

  @Override
  public synchronized TetriminoInterface getHold() {
    return Tetrimino.createTetrimino(this.hold);
  }

  @Override
  public synchronized TetriminoInterface getActualTetrimino() {
    return actualTetrimino;
  }

  @Override
  public synchronized TetriminoInterface getNextTetrimino() {
    return nextTetrimino;
  }

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
      // 015332266
    }
    return copyBoard;
  }

  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.addPropertyChangeListener(listener);
  }

  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.removePropertyChangeListener(listener);
  }

  public abstract void setNbLine(int nbLine);

  public abstract void setHold(Mino hold);

  public abstract void setNextTetrimino(TetriminoInterface nextTetrimino);

  public abstract void setActualTetrimino(TetriminoInterface actualTetrimino);
}
