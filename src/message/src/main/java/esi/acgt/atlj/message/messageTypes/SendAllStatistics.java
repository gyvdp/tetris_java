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

import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.MessageType;
import java.util.HashMap;

/**
 * Sends all statistics to user.
 */
public class SendAllStatistics extends Message {

  HashMap<String, Integer> game_history;
  HashMap<String, Integer> tetrimino_history;

  /**
   * Constructor to send all statistics.
   */
  public SendAllStatistics() {
    this.messageType = MessageType.SEND_ALL_STATISTICS;
  }

  public void setGame_history(HashMap<String, Integer> game_history) {
    this.game_history = game_history;
  }

  public void setTetrimino_history(
      HashMap<String, Integer> tetrimino_history) {
    this.tetrimino_history = tetrimino_history;
  }

  /**
   * Getter for game history.
   *
   * @return Hashmap for game history.
   */
  public HashMap<String, Integer> getGame_history() {
    return game_history;
  }

  /**
   * Getter for tetrimino history.
   *
   * @return Hashmap for tetrimino history.
   */
  public HashMap<String, Integer> getTetrimino_history() {
    return tetrimino_history;
  }
}