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
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.function.Consumer;

public class ManagedGame extends Game {

  /**
   * Lambda expression to ask client for next piece in bag.
   */
  Runnable askNextMino;
  /**
   * Locked tetrimino to send to server.
   */
  Consumer<TetriminoInterface> addTetrimino;

  private GameStatus status;
  private int level;
  private final Timer timer;
  private TickHandler tickHandler;
  private boolean hasAlreadyHolded;

  public ManagedGame(String username) {
    super(username);
    hasAlreadyHolded = false;
    this.status = GameStatus.NOT_STARTED;
    this.level = 3;
    this.timer = new Timer(true);
    this.tickHandler = new TickHandler(this);
  }

  /**
   * Connect lambda expression from ModelClient to ask client for new mino.
   *
   * @param askNextMino Lambda expression to connect.
   */
  public void connectAskNewMino(Runnable askNextMino) {
    this.askNextMino = askNextMino;
  }

  public void connectAddTetrimino(Consumer<TetriminoInterface> addTetrimino) {
    this.addTetrimino = addTetrimino;
  }

  public synchronized void start() {
    setStatus(GameStatus.TETRIMINO_FALLING);
  }

  public synchronized boolean move(Direction direction) {
    if (isMoveValid(direction, generateFreeMask(6, 6, actualTetrimino.getX(), actualTetrimino.getY(), 1, 1))) {
      Mino[][] oldBoard = this.getBoard();
      this.actualTetrimino.move(direction);
      this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
      return true;
    }

    return false;
  }

  @Override
  public synchronized void setHold(Mino hold) {
    this.hold = hold;
    this.changeSupport.firePropertyChange("hold", null, this.getHold());
  }

  @Override
  public synchronized void setNextTetrimino(TetriminoInterface nextTetrimino) {
    this.nextTetrimino = nextTetrimino;
    this.changeSupport.firePropertyChange("next", null, this.getNextTetrimino());
  }

  public void hold() {
    if (!hasAlreadyHolded) {
      if (hold == null) {
        this.setHold(this.actualTetrimino.getType());
        this.setActualTetrimino(this.nextTetrimino);
      } else {
        TetriminoInterface temp = this.getHold();
        this.setHold(this.actualTetrimino.getType());
        this.setActualTetrimino(temp);
      }
      hasAlreadyHolded = true;
    }
  }

  @Override
  public synchronized void setActualTetrimino(TetriminoInterface actualTetrimino) {
    Mino[][] oldBoard = this.getBoard();
    this.actualTetrimino = actualTetrimino;
    this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
  }

  private synchronized boolean isMoveValid(Direction direction, boolean[][] mask) {
    Mino[][] minos = actualTetrimino.getMinos();
    for(int i = 0; i < minos.length; i++) {
      for (int j = 0; j < minos[i].length; j++) {
        int x = j + 1 + direction.getDeltaX();
        int y = i + 1 + direction.getDeltaY();

        if(!mask[y][x] && minos[i][j] != null) {
          return false;
        }
      }
    }

    return true;
  }

  public synchronized void softDrop() {
    this.move(Direction.DOWN);
  }

  public synchronized void hardDrop() {
    while (isMoveValid(Direction.DOWN, generateFreeMask(6, 6, actualTetrimino.getX(), actualTetrimino.getY(), 1, 1))) {
      Mino[][] oldBoard = this.getBoard();
      this.actualTetrimino.move(Direction.DOWN);
      this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
    }
  }

  public synchronized void rotate(boolean clockwise) {
    var oldBoard = this.getBoard();
    try {
      actualTetrimino.rotate(clockwise, this.generateFreeMask(4, 4, actualTetrimino.getX(),
          actualTetrimino.getY(), 0, 0));
      this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
    } catch (InvalidParameterException e) {
      //Check if there is another block or if its wall
      //If it is another block tetrimino must be placed If wall do nothing
    } catch (Exception ignored) {

    }
  }

  public synchronized void setScore(int score) {
    int oldScore = this.score;
    this.score = score;
    this.changeSupport.firePropertyChange("score", oldScore, this.score);
  }

  public synchronized void setNbLine(int nbLine) {
    int oldNbLine = this.nbLine;
    this.nbLine = nbLine;
    this.changeSupport.firePropertyChange("line", oldNbLine, this.nbLine);
  }

  public synchronized GameStatus getStatus() {
    return this.status;
  }

  public synchronized void setStatus(GameStatus status) {
    this.tickHandler.cancel();
    this.tickHandler = new TickHandler(this);
    this.status = status;

    if (status == GameStatus.TETRIMINO_FALLING) {
      timer.schedule(this.tickHandler, 0, TickHandler.tickDelay(this.level));
    }

    if (status == GameStatus.LOCK_DOWN) {
      this.timer.schedule(this.tickHandler, 500);
    }
  }

  public synchronized void lock() {
    var oldBoard = getBoard();
    var t = this.actualTetrimino;
    var tMinos = this.actualTetrimino.getMinos();
    for (var i = 0; i < tMinos.length; ++i) {
      for (var j = 0; j < tMinos[i].length; ++j) {
        if (tMinos[i][j] == null) {
          continue;
        }
        var line = t.getY() + i;
        var col = t.getX() + j;

        if (!(line < 0 || col < 0) && line < minos.length && col < minos[line].length) {
          minos[line][col] = tMinos[i][j];
        }

      }
    }
    this.hasAlreadyHolded = false;
    addTetrimino.accept(actualTetrimino);
    setActualTetrimino(this.nextTetrimino);
    askNextMino.run();
    setStatus(GameStatus.TETRIMINO_FALLING);
  }

}
