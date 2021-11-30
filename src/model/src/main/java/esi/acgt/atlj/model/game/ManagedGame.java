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
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;

/**
 * Game that is going to be played by the playing player.
 */
public class ManagedGame extends AbstractGame {

  private final Timer timer;
  /**
   * Tells server to lock tetrimino
   */
  Consumer<TetriminoInterface> tetriminoLock;
  /**
   * All lines that have been destroyed by game in an array to send to server
   */
  Consumer<List<Integer>> lineDestroyed;
  /**
   * Lambda expression to ask client for next piece in bag.
   */
  Runnable askNextMino;
  /**
   * Sends to server that you lost.
   */
  Runnable iLost;
  /**
   * Locked tetrimino to send to server.
   */
  Consumer<TetriminoInterface> addTetrimino;
  /**
   * Hold tetrimino to send to server
   */
  Consumer<Mino> holdMino;
  /**
   * Sends current score to the server
   */
  Consumer<Integer> setScoreServer;
  private GameStatus status;
  private TickHandler tickHandler;
  private boolean hasAlreadyHolded;

  /**
   * Establishes a new managed game
   *
   * @param username Username of player.
   */
  public ManagedGame(String username) {
    super(username);
    hasAlreadyHolded = false;
    this.status = GameStatus.NOT_STARTED;
    this.timer = new Timer(true);
    this.tickHandler = new TickHandler(this);
  }

  /**
   * Connect lambda expression from ModelClient to ask client for new mino.
   *
   * @param askNextMino Lambda expression to connect.
   */
  public synchronized void connectAskNewMino(Runnable askNextMino) {
    this.askNextMino = askNextMino;
  }


  /**
   * Connect lambda to send to server that player lost.
   */
  public synchronized void connectLost(Runnable iLost) {
    this.iLost = iLost;
  }

  /**
   * Connects lambda of locked tetrimino to client.
   *
   * @param myTT Lambda to connect
   */
  public synchronized void connectTetriminoLock(Consumer<TetriminoInterface> myTT) {
    this.tetriminoLock = myTT;
  }


  /**
   * Sends the score to the server via a Lambda
   *
   * @param setScoreServer Lambda to connect.
   */
  public synchronized void connectSendScoreServer(Consumer<Integer> setScoreServer) {
    this.setScoreServer = setScoreServer;
    this.stats.connectSendScore(setScoreServer);
  }

  /**
   * Connect lambda expression form ModelClient to indicate which mino has been held
   *
   * @param holdMino Lambda expression to connect
   */
  public synchronized void connectHoldMino(Consumer<Mino> holdMino) {
    this.holdMino = holdMino;
  }

  /**
   * Connects lambda expression to add tetrimino to server
   *
   * @param addTetrimino Lambda expression to connect
   */
  public synchronized void connectAddTetrimino(Consumer<TetriminoInterface> addTetrimino) {
    this.addTetrimino = addTetrimino;
  }

  /**
   * Game starts making tetriminos fall
   */
  public synchronized void start() {
    this.askNextMino.run();
    Mino[][] emptyBoard = new Mino[HEIGHT][WIDTH];
    this.pcs.firePropertyChange("board", emptyBoard, this.getMatrix());
    setStatus(GameStatus.TETRIMINO_FALLING);
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

    if (moved) {
      if (status == GameStatus.LOCK_DOWN) {
        setStatus(GameStatus.TETRIMINO_FALLING);
      }

      if (status == GameStatus.TETRIMINO_HARD_DROPPING) {
        stats.applyAction(Action.HARD_DROP);
      }
    }
    addTetrimino.accept(actualTetrimino);
    return moved;
  }

  public synchronized void connectLineDestroyed(Consumer<List<Integer>> lineDestroyed) {
    this.lineDestroyed = lineDestroyed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void setNextTetrimino(TetriminoInterface nextTetrimino) {
    this.nextTetrimino = nextTetrimino;
    this.pcs.firePropertyChange("next", null, this.getNextTetrimino().getType());
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
      holdMino.accept(hold);
      setStatus(GameStatus.TETRIMINO_FALLING);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void setActualTetrimino(TetriminoInterface actualTetrimino) {
    this.actualTetrimino = actualTetrimino;
    this.pcs.firePropertyChange("board", null, this.getMatrix());
  }

  /**
   * Soft drops a tetrimino.
   */
  public synchronized void softDrop() {
    setStatus(GameStatus.SOFT_DROPPING);
  }

  /**
   * Makes a tetrimino hard drop automatically locking it in place.
   */
  public synchronized void hardDrop() {
    setStatus(GameStatus.TETRIMINO_HARD_DROPPING);
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
  public synchronized GameStatus getStatus() {
    return this.status;
  }


  /**
   * Sets the status of the game
   *
   * @param status Status to set game to.
   */
  public synchronized void setStatus(GameStatus status) {
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
    tetriminoLock.accept(actualTetrimino);
    this.hasAlreadyHolded = false;
    setActualTetrimino(this.nextTetrimino);
    askNextMino.run();

    List<Integer> lines = getFullLines();
    if (lines.size() != 0) {
      removeLines(lines);
      lineDestroyed.accept(lines);
      this.stats.applyAction(Action.getActionByFullLines(lines.size()));
    }

    if (outOfBound()) {
      setActualTetrimino(null);
      setStatus(GameStatus.LOCK_OUT);
      iLost.run();
    } else {
      setStatus(GameStatus.TETRIMINO_FALLING);
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
