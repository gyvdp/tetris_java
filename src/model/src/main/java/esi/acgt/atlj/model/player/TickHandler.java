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

import java.util.TimerTask;

public class TickHandler extends TimerTask {

  private final ManagedPlayer managedBoard;

  public TickHandler(ManagedPlayer managedBoard) {
    this.managedBoard = managedBoard;
  }

  public static long tickDelay(int level) {
    return (int) ((Math.pow(0.8 - ((level - 1) * 0.007), level - 1)) * 1000);
  }

  @Override
  public synchronized void run() {
    switch (managedBoard.getStatus()) {
      case TETRIMINO_FALLING -> {
        if (!managedBoard.move(Direction.DOWN)) {
          managedBoard.setStatus(PlayerStatus.LOCK_DOWN);
        } else {
          managedBoard.setStatus(PlayerStatus.TETRIMINO_FALLING);
        }
      }
      case TETRIMINO_HARD_DROPPING -> {
        if (!managedBoard.move(Direction.DOWN)) {
          managedBoard.lock();
        } else {
          managedBoard.setStatus(PlayerStatus.TETRIMINO_HARD_DROPPING);
        }
      }
      case LOCK_DOWN -> managedBoard.lock();
      case ROTATING_CLOCKWISE -> {
        if (managedBoard.rotate(true)) {
          managedBoard.setStatus(PlayerStatus.TETRIMINO_FALLING);
        }
      }
      case ROTATING_ANTI_CLOCKWISE -> managedBoard.rotate(false);
      case SOFT_DROPPING -> {
        if (managedBoard.move(Direction.DOWN)) {
          managedBoard.getStats().increaseScore(1);
          managedBoard.setStatus(PlayerStatus.TETRIMINO_FALLING);
        } else {
          managedBoard.setStatus(PlayerStatus.LOCK_DOWN);
        }

      }
    }
  }
}
