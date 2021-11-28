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

public enum Action {
  SINGLE(100, true),
  DOUBLE(300, true),
  TRIPLE(500, true),
  TETRIS(800, true),
  SOFT_DROP(1),
  HARD_DROP(2);

  private final int score;
  private final boolean multiplyLevel;

  Action(int score) {
    this(score, false);
  }

  Action(int score, boolean multiplyLevel) {
    this.score = score;
    this.multiplyLevel = multiplyLevel;
  }

  static Action getActionByFullLines(int lines) {
    return switch (lines) {
      case 1 -> SINGLE;
      case 2 -> DOUBLE;
      case 3 -> TRIPLE;
      case 4 -> TETRIS;
      default -> null;
    };
  }

  int getScore() {
    return score;
  }

  boolean getMultiplyLevel() {
    return multiplyLevel;
  }

}
