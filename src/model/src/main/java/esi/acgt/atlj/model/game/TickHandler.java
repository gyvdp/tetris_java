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

import java.util.TimerTask;

public class TickHandler extends TimerTask {

  private final ManagedGame managedBoard;

  public TickHandler(ManagedGame managedBoard) {
    this.managedBoard = managedBoard;
  }

  @Override
  public synchronized void run() {
    switch (managedBoard.getStatus()) {
      case TETRIMINO_FALLING -> {
        if (!managedBoard.move(Direction.DOWN)) {
          managedBoard.setStatus(GameStatus.LOCK_DOWN);
        }
      }
      case TETRIMINO_HARD_DROPPING -> {
        if (!managedBoard.move(Direction.DOWN)) {
          managedBoard.lock();
        }
      }
      case LOCK_DOWN -> managedBoard.lock();
    }
  }

  public static long tickDelay(int level) {
    return (int) ((Math.pow(0.8 - ((level - 1) * 0.007), level - 1)) * 1000);
  }
}
