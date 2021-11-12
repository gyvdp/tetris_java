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

import esi.acgt.atlj.model.tetrimino.Mino;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ManagedBoard extends Board {

  private Timer timer;

  public ManagedBoard() {
    //TODO
  }

  public boolean move(Direction direction) {
    if (isMoveValid(direction)) {
      Mino[][] oldBoard = this.getBoard();
      this.actualTetrimino.move(direction);
      this.changeSupport.firePropertyChange("player1Board", oldBoard, this.getBoard());
      return true;
    }
    return false;
  }

  private boolean isMoveValid(Direction direction) {
    switch (direction) {
      case UP -> {
        if (this.actualTetrimino.getY() <= 0) {
          for (var mino : this.actualTetrimino.getMinos()[Math.abs(this.actualTetrimino.getY())]) {
            if (mino != null) {
              return false;
            }
          }
        }
      }
      case RIGHT -> {
        //colision mur droit
        if (this.actualTetrimino.getX()
            >= this.minos[0].length - this.actualTetrimino.getMinos().length) {
          for (var mino : this.actualTetrimino.getMinos()) {
            if (mino[this.minos[0].length - this.actualTetrimino.getX() - 1] != null) {
              return false;
            }
          }
        }
        // colision piece à droite
        //    if (this.actualTetrimino.getX() > this.minos[0].length - this.actualTetrimino.getMinos().length) {
        for (int i = 0; i < this.actualTetrimino.getMinos().length; i++) {
          for (int j = 0; j < this.actualTetrimino.getMinos()[i].length; j++) {
            if (this.actualTetrimino.getMinos()[i][j] != null
                && this.minos[this.actualTetrimino.getY() + i][this.actualTetrimino.getX() + j + 1]
                != null) {
              return false;
            }
          }
        }
      }
      case DOWN -> {
        if ((this.actualTetrimino.getY() + this.actualTetrimino.getMinos().length)
            > this.minos.length) {
          for (var mino : this.actualTetrimino.getMinos()[this.minos.length
              - this.actualTetrimino.getY() - 1]) {
            if (mino != null) {
              return false;
            }
          }
        }

        for (int i = 0; i < this.actualTetrimino.getMinos().length; i++) {
          for (int j = 0; j < this.actualTetrimino.getMinos()[i].length; j++) {
            if (this.actualTetrimino.getMinos()[i][j] != null
                &&
                this.minos[this.actualTetrimino.getY() + i + 1][this.actualTetrimino.getX() + j]
                    != null) {
              return false;
            }
          }
        }
      }
      case LEFT -> {
        if (this.actualTetrimino.getX() <= 0) {
          for (var mino : this.actualTetrimino.getMinos()) {
            System.out.println(Math.abs(this.actualTetrimino.getX() + 1));
            if (mino[Math.abs(this.actualTetrimino.getX())] != null) {
              return false;
            }
          }
        }
        for (int i = 0; i < this.actualTetrimino.getMinos().length; i++) {
          for (int j = 0; j < this.actualTetrimino.getMinos()[i].length; j++) {
            if (this.actualTetrimino.getMinos()[i][j] != null
                && this.minos[this.actualTetrimino.getY() + i][this.actualTetrimino.getX() + j - 1]
                != null) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  public void softDrop() {
    //Todo Gestion reset timer
    this.move(Direction.DOWN);
  }

  public void hardDrop() {
    // TODO
    while (isMoveValid(Direction.DOWN)) {
      Mino[][] oldBoard = this.getBoard();
      this.actualTetrimino.move(Direction.DOWN);
      //    try {
      //      TimeUnit.MILLISECONDS.sleep(100);
      //    } catch (InterruptedException e) {
      //      e.printStackTrace();
      //    }
      this.changeSupport.firePropertyChange("player1Board", oldBoard, this.getBoard());
    }
  }

  private void rotate(boolean clockwise) {
    var oldBoard = this.getBoard();
    try {
      actualTetrimino.rotate(clockwise, this.getSurroundingArea(actualTetrimino.getX(),
          actualTetrimino.getY()));
      this.changeSupport.firePropertyChange("player1Board", oldBoard, this.getBoard());
    } catch (InvalidParameterException e) {
      //Check if there is another block or if its wall
      //If it is another block tetrimino must be placed If wall do nothing
    }
  }

  public void setScore(int score) {
    int oldScore = this.score;
    this.score = score;
    this.changeSupport.firePropertyChange("player1Score", oldScore, this.score);
  }

  public void setUsername(String username) {
    String oldValue = this.username;
    this.username = username;
    this.changeSupport.firePropertyChange("player1Name", oldValue, this.username);
  }

  public void setNbLine(int nbLine) {
    int oldNbLine = this.nbLine;
    this.nbLine = nbLine;
    this.changeSupport.firePropertyChange("player1NbLine", oldNbLine, this.nbLine);
  }

}
