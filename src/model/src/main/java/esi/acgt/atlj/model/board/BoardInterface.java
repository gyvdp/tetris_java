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

package esi.acgt.atlj.model.board;

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;

public interface BoardInterface {

  /**
   * The width of the board
   */
  int WIDTH = 10;

  /**
   * The height of the board
   */
  int HEIGHT = 22;

  /**
   * Get the matrix of the board (including falling piece)
   *
   * @return the matrix of minos
   */
  Mino[][] getBoard();

  /**
   * Get the actual score of the player
   *
   * @return the score
   */
  int getScore();

  /**
   * Get the username of the player
   *
   * @return the username of the player
   */
  String getUsername();

  /**
   * Get the held piece
   *
   * @return the held piece or null if there is no piece
   */
  TetriminoInterface getHold();

  /**
   * Get the actual falling tetrimino
   *
   * @return the falling tetrimino
   */
  TetriminoInterface getActualTetrimino();

  /**
   * Get the next tetrimino of the player
   *
   * @return the next tetrimino
   */
  TetriminoInterface getNextTetrimino();
}
