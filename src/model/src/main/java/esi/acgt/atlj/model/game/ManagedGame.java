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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;

/**
 * Game that is going to be played by the playing player.
 */
public class ManagedGame extends AbstractGame {

  /**
   * All lines that have been destroyed by game in an array to send to server
   */
  Consumer<ArrayList<Integer>> lineDestroyed;
  /**
   * Lambda expression to ask client for next piece in bag.
   */
  Runnable askNextMino;
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
  private int level;
  private final Timer timer;
  private TickHandler tickHandler;
  private boolean hasAlreadyHolded;

  /**
   * Establishes a new managed game
   *
   * @param username Username of playe .
   */
  public ManagedGame(String username) {
    super(username);
    hasAlreadyHolded = false;
    this.status = GameStatus.NOT_STARTED;
    this.level = 1;
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

  /**
   * Sends the score to the server via a Lambda
   *
   * @param setScoreServer Lambda to connect.
   */
  public void connectSendScoreServer(Consumer<Integer> setScoreServer) {
    this.setScoreServer = setScoreServer;
  }

  /**
   * Connect lambda expression form ModelClient to indicate which mino has been held
   *
   * @param holdMino Lambda expression to connect
   */
  public void connectHoldMino(Consumer<Mino> holdMino) {
    this.holdMino = holdMino;
  }

  /**
   * Connects lambda expression to add tetrimino to server
   *
   * @param addTetrimino Laùbda expression to connect
   */
  public void connectAddTetrimino(Consumer<TetriminoInterface> addTetrimino) {
    this.addTetrimino = addTetrimino;
  }

  /**
   * Game starts making tetriminos fall
   */
  public synchronized void start() {
    setStatus(GameStatus.TETRIMINO_FALLING);
  }

  /**
   * Moves a tetrimino in the direction.
   *
   * @param direction Direction in wich to move
   * @return True if tetrimino is able to move
   */
  public synchronized boolean move(Direction direction) {
    Mino[][] oldBoard = this.getBoard();
    boolean moved = this.actualTetrimino.move(direction,
        generateFreeMask(6, 6, actualTetrimino.getX(), actualTetrimino.getY(), 1, 1));
    this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
    if (moved) {
      if (status == GameStatus.LOCK_DOWN) {
        setStatus(GameStatus.TETRIMINO_FALLING);
      }

      if (status == GameStatus.TETRIMINO_HARD_DROPPING) {
        increaseScore(2);
      }
    }
    return moved;
  }

  public void connectLineDestroyed(Consumer<ArrayList<Integer>> lineDestroyed) {
    this.lineDestroyed = lineDestroyed;
  }

  @Override
  public synchronized void setHold(Mino hold) {
    this.hold = hold;
    holdMino.accept(hold);
    this.changeSupport.firePropertyChange("hold", null, this.getHold());
  }

  @Override
  public synchronized void setNextTetrimino(TetriminoInterface nextTetrimino) {
    this.nextTetrimino = nextTetrimino;
    this.changeSupport.firePropertyChange("next", null, this.getNextTetrimino());
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
    }
    setStatus(GameStatus.TETRIMINO_FALLING);
  }

  @Override
  public synchronized void setActualTetrimino(TetriminoInterface actualTetrimino) {
    Mino[][] oldBoard = this.getBoard();
    this.actualTetrimino = actualTetrimino;
    this.changeSupport.firePropertyChange("board", oldBoard, this.getBoard());
  }

  /**
   * Soft drops a tetrimino.
   */
  public synchronized void softDrop() {
    this.move(Direction.DOWN);
    increaseScore(1);
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

  /**
   * Sets the score of the current player
   *
   * @param score Score to set to.
   */
  public synchronized void setScore(int score) {
    int oldScore = this.score;
    this.score = score;
    this.changeSupport.firePropertyChange("score", oldScore, this.score);
  }

  public synchronized void increaseScore(int increment) {
    setScore(score + increment);
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

    if (status == GameStatus.TETRIMINO_FALLING) {
      timer.schedule(this.tickHandler, 0, TickHandler.tickDelay(this.level));
    }

    if (status == GameStatus.LOCK_DOWN) {
      this.timer.schedule(this.tickHandler, 500);
    }

    if (status == GameStatus.TETRIMINO_HARD_DROPPING) {
      this.timer.schedule(this.tickHandler, 0, 1);
    }
  }

  /**
   * Locks a tetrimino making it unable to move.
   */
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
    setActualTetrimino(this.nextTetrimino);
    askNextMino.run();
    setStatus(GameStatus.TETRIMINO_FALLING);
  }

  /**
   * Update that the game has finished
   *
   * @param winnerName Name of the winner
   * @param reason     Reason from player to have won, sometimes it's just skill :}
   */
  public synchronized void fireEndGame(String winnerName, String reason) {
    this.changeSupport.firePropertyChange("winner", winnerName, reason);
  }

  /**
   * @return
   */
  private synchronized List<Integer> getFullLines() {
    List<Integer> lines = new ArrayList<>();
    for (int i = 0; i < minos.length; ++i) {
      boolean full = true;
      for (int j = 0; j < minos[i].length; ++j) {
        if (minos[i][j] == null) {
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
