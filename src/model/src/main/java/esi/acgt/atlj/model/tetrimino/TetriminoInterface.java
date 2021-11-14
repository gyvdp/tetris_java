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
package esi.acgt.atlj.model.tetrimino;

import esi.acgt.atlj.model.game.Direction;

/**
 * The TetriminoInterface
 */
public interface TetriminoInterface {

  /**
   * Get the x position
   *
   * @return the x position
   */
  int getX();

  /**
   * Get the y position
   *
   * @return the y position
   */
  int getY();

  /**
   * Get a matrix of the minos in the tetrimino
   *
   * @return the tetrimino as minos matrix
   */
  Mino[][] getMinos();

  /**
   * Get the minos wich the matrix is made of
   *
   * @return the mino contains in the matrix of minos
   */
  Mino getType();

  /**
   * Rotate the Tetrimino
   *
   * @param clockwise       true if clockwise
   * @param surroundingArea Area of surrounding blocks.
   * @throws IllegalArgumentException if non valid parameters.
   */
  void rotate(boolean clockwise, boolean[][] surroundingArea);

  /**
   * Moves the tetrimino in a direction
   *
   * @param direction Direction to move tetrimino in.
   */
  boolean move(Direction direction, boolean[][] freeMask);

  Mino[][] rotatedShape(boolean clockwise);
}
