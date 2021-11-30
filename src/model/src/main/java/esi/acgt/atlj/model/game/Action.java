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

/**
 * The Action enumeration describes actions of the player and the associated points
 */
public enum Action {
  /**
   * Single line burn
   */
  SINGLE(100, 1, true),
  /**
   * Double line burn
   */
  DOUBLE(300, 2, true),
  /**
   * TRIPLE line burn
   */
  TRIPLE(500, 3, true),
  /**
   * TETRIS line burn (4 lines)
   */
  TETRIS(800, 4, true),
  /**
   * Soft drop action
   */
  SOFT_DROP(1),
  /**
   * Hard drop action (one per line)
   */
  HARD_DROP(2);

  /**
   * Value in points of an action
   */
  private final int points;

  /**
   * Number of burns of the action
   */
  private final int burns;

  /**
   * Action to multiply by level to obtain real points
   */
  private final boolean multiplyLevel;

  /**
   * Action constructor
   *
   * @param points        number of points of the action
   * @param burns         number of burns of the action
   * @param multiplyLevel true if you need to multiply point by level
   */
  Action(int points, int burns, boolean multiplyLevel) {
    this.points = points;
    this.burns = burns;
    this.multiplyLevel = multiplyLevel;
  }

  /**
   * Action constructor
   *
   * @param points number of points of the action
   */
  Action(int points) {
    this(points, 0, false);
  }

  /**
   * Action constructor
   *
   * @param points        number of points of the action
   * @param multiplyLevel true if you need to multiply point by level
   */
  Action(int points, boolean multiplyLevel) {
    this(points, 0, multiplyLevel);
  }

  /**
   * Give the action that correspond to n line burns
   *
   * @param lines nb of lines to burn
   * @return the action associated
   */
  static Action getActionByFullLines(int lines) {
    return switch (lines) {
      case 1 -> SINGLE;
      case 2 -> DOUBLE;
      case 3 -> TRIPLE;
      case 4 -> TETRIS;
      default -> null;
    };
  }

  /**
   * Get the number of points of an action
   *
   * @return the number of points
   */
  int getPoints() {
    return points;
  }

  /**
   * Get if the action is to multiply by level
   *
   * @return true if to multiply, false if not
   */
  boolean getMultiplyLevel() {
    return multiplyLevel;
  }

  /**
   * Get the number of line burns
   *
   * @return the number of burns
   */
  int getBurns() {
    return burns;
  }

}
