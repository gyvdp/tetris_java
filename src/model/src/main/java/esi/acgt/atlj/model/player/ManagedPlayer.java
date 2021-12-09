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
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Game that is going to be played by the playing player.
 */
public class ManagedPlayer extends AbstractPlayer {

  private final Timer timer;

  private PlayerStatus status;
  private TickHandler tickHandler;
  private boolean hasAlreadyHolded;

  /**
   * Establishes a new managed game
   *
   * @param username Username of player.
   */
  public ManagedPlayer(String username) {
    super(username);
    hasAlreadyHolded = false;
    this.status = PlayerStatus.NOT_STARTED;
    this.timer = new Timer(true);
    this.tickHandler = new TickHandler(this);
  }

  /**
   * Game starts making tetriminos fall
   */
  public synchronized void start() {
    // todo : this.askNextMino.run();
    Mino[][] emptyBoard = new Mino[HEIGHT][WIDTH];
    this.pcs.firePropertyChange("board", emptyBoard, this.getMatrix());
    setStatus(PlayerStatus.TETRIMINO_FALLING);
  }

  /**
   * Moves a tetrimino in the direction.
   *
   * @param direction Direction in wich to move
   * @return True if tetrimino is able to move
   */
  public synchronized boolean move(Direction direction) {
    Mino[][] oldBoard = this.getMatrix();

    boolean moved = this.actualTetrimino.move(direction,
        generateFreeMask(6, 6, actualTetrimino.getX(), actualTetrimino.getY(), 1, 1));

    this.pcs.firePropertyChange("board", oldBoard, this.getMatrix());
    this.pcs.firePropertyChange("ACTUAL", null, this.actualTetrimino);

    if (moved) {
      if (status == PlayerStatus.LOCK_DOWN) {
        setStatus(PlayerStatus.TETRIMINO_FALLING);
      }

      if (status == PlayerStatus.TETRIMINO_HARD_DROPPING) {
        stats.applyAction(Action.HARD_DROP);
      }
    }
    return moved;
  }

  /**
   * Adds a tetrimino to the hold case
   */
  public synchronized void hold() {
    if (!hasAlreadyHolded) {
      if (hold == null) {
        this.setHold(this.actualTetrimino.getType());
        this.setActualTetrimino(this.nextTetrimino);
      } else {
        TetriminoInterface temp = Tetrimino.createTetrimino(this.getHold());
        this.setHold(this.actualTetrimino.getType());
        this.setActualTetrimino(temp);
      }
      hasAlreadyHolded = true;
      setStatus(PlayerStatus.TETRIMINO_FALLING);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void setActualTetrimino(TetriminoInterface actualTetrimino) {
    this.actualTetrimino = actualTetrimino;
    this.pcs.firePropertyChange("ACTUAL", null, this.actualTetrimino);
    this.pcs.firePropertyChange("board", null, this.getMatrix());
  }

  /**
   * Soft drops a tetrimino.
   */
  public synchronized void softDrop() {
    setStatus(PlayerStatus.SOFT_DROPPING);
  }

  /**
   * Makes a tetrimino hard drop automatically locking it in place.
   */
  public synchronized void hardDrop() {
    setStatus(PlayerStatus.TETRIMINO_HARD_DROPPING);
  }

  /**
   * Rotates a tetrimino clockwise or counter-clockwise
   *
   * @param clockwise True if tetrimino should rotate clockwise.
   */
  public synchronized boolean rotate(boolean clockwise) {
    var oldBoard = getMatrix();
    boolean rotated = actualTetrimino.rotate(clockwise,
        this.generateFreeMask(4, 4, actualTetrimino.getX(),
            actualTetrimino.getY(), 0, 0));
    if (rotated) {
      this.pcs.firePropertyChange("board", oldBoard, getMatrix());
    }
    return rotated;
  }

  /**
   * Gets the status of the game
   *
   * @return Current status of the game
   */
  public synchronized PlayerStatus getStatus() {
    return this.status;
  }


  /**
   * Sets the status of the game
   *
   * @param status Status to set game to.
   */
  public synchronized void setStatus(PlayerStatus status) {
    this.tickHandler.cancel();
    this.tickHandler = new TickHandler(this);
    this.status = status;

    switch (status) {
      case TETRIMINO_FALLING -> {
        timer.schedule(this.tickHandler, TickHandler.tickDelay(this.stats.getLevel()));
        this.playerStatus("", 0);
      }
      case LOCK_DOWN -> {
        this.timer.schedule(this.tickHandler, 500);
        this.playerStatus("LOCK DOWN", 0.2);
      }
      case TETRIMINO_HARD_DROPPING,
          ROTATING_CLOCKWISE,
          ROTATING_ANTI_CLOCKWISE,
          SOFT_DROPPING -> this.timer.schedule(this.tickHandler, 1);
      case LOCK_OUT -> this.playerStatus("LOCK OUT", 0.9);
    }
  }

  /**
   * Locks a tetrimino making it unable to move.
   */
  public synchronized void lock() {
    placeTetrimino(this.actualTetrimino);
    this.pcs.firePropertyChange("PLACE_TETRIMINO", null, this.actualTetrimino);
    this.hasAlreadyHolded = false;
    setActualTetrimino(this.nextTetrimino);
    setNextTetrimino(null);

    List<Integer> lines = getFullLines();
    if (lines.size() != 0) {
      removeLines(lines);
      this.stats.applyAction(Action.getActionByFullLines(lines.size()));
    }

    if (outOfBound()) {
      setActualTetrimino(null);
      setStatus(PlayerStatus.LOCK_OUT);
    } else {
      setStatus(PlayerStatus.TETRIMINO_FALLING);
    }

  }

  /**
   * Gets all the line of the whole board
   *
   * @return All the line
   */
  private synchronized List<Integer> getFullLines() {
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

  private synchronized boolean outOfBound() {
    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < matrix[i].length; ++j) {
        if (matrix[i][j] != null) {
          return true;
        }
      }
    }

    return false;
  }
}
