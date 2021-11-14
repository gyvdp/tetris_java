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

import static esi.acgt.atlj.message.MessageType.NUMBER_LINES;

import esi.acgt.atlj.message.Message;

/**
 * Sets the number of lines that have been removed by a player.
 */
public class SetNbLines extends Message {

  /**
   * Number of lines that have been removed
   */
  private final int numberOfLines;

  /**
   * Constructor for set number of lines.
   *
   * @param numberOfLines Number of line that have been removed
   */
  public SetNbLines(int numberOfLines) {
    this.messageType = NUMBER_LINES;
    this.numberOfLines = numberOfLines;
  }

  /**
   * Getter for the number of lines.
   *
   * @return Number of lines that have been removed.
   */
  public int getNumberOfLines() {
    return this.numberOfLines;
  }
}
