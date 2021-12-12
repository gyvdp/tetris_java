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

package esi.acgt.atlj.message.messageTypes;

import esi.acgt.atlj.message.StatisticMessage;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Sends all statistics to user.
 */
public class SendAllStatistics extends StatisticMessage {

  HashMap<String, Integer> game_history;
  HashMap<String, Integer> tetrimino_history;

  /**
   * Constructor to send all statistics.
   */
  public SendAllStatistics() {
    super();
  }

  /**
   * Sets the whole table entry of a specific user.
   *
   * @param game_history Game entry to set.
   */
  public void setGame_history(HashMap<String, Integer> game_history) {
    this.game_history = game_history;
  }

  /**
   * Sets the whole table entry of a specific user.
   *
   * @param tetrimino_history Tetrimino entry to set.
   */
  public void setTetrimino_history(
      HashMap<String, Integer> tetrimino_history) {
    this.tetrimino_history = tetrimino_history;
  }

  public void execute(Consumer<HashMap<String, Integer>> setStats) {
    game_history.putAll(tetrimino_history);
    setStats.accept(game_history);
  }
}
