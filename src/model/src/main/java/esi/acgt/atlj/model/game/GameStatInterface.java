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

import java.util.Map;

/**
 * The GameStatInterface describes the possible statistics you can get in a game.
 */
public interface GameStatInterface {

  /**
   * Get the high-score of the player (include actual game score)
   *
   * @return the player high-score
   */
  int getHighScore();

  /**
   * Get the actual game score
   *
   * @return the score
   */
  int getScore();

  /**
   * Get the number of burns in this game
   *
   * @return nb lines burnt
   */
  int getBurns();

  /**
   * Get the actual level of the game
   *
   * @return the level
   */
  int getLevel();

  /**
   * Get the count of all actions of the game
   *
   * @return a map with the count of each action
   */
  Map<Action, Integer> getActionCount();
}
