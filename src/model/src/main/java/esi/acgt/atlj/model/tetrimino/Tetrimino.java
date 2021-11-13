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

import esi.acgt.atlj.model.board.Direction;
import java.io.Serializable;

public abstract class Tetrimino implements TetriminoInterface, Serializable {

  /**
   * x coordinate of the tetrimino on the board
   */
  protected int x;

  /**
   * y coordinate of the tetrimino on the board
   */
  protected int y;

  /**
   * minos matrix that composed
   */
  protected Mino[][] minos;
  protected Mino type;

  /**
   * Initiates default values of all Tetrimino
   */
  protected Tetrimino() {
    this.y = 0;
    this.x = 3;
  }

  public static TetriminoInterface createTetrimino(Mino hold) {
    return switch (hold) {
      case I_MINO -> new ITetrimino();
      case J_MINO -> new JTetrimino();
      case O_MINO -> new OTetrimino();
      case L_MINO -> new LTetrimino();
      case S_MINO -> new STetrimino();
      case T_MINO -> new TTetrimino();
      case Z_MINO -> new ZTetrimino();
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rotate(boolean clockwise, boolean[][] surroundingArea) {
    Mino k;
    int l = this.minos.length - 1;
    int i, j;
    for (i = 0; i < Math.floor(this.minos.length * 0.5); i++) {
      for (j = i; j < l - i; j++) {
        k = this.minos[i][j];
        this.minos[i][j] = this.minos[l - j][i];
        this.minos[l - j][i] = this.minos[l - i][l - j];
        this.minos[l - i][l - j] = this.minos[j][l - i];
        this.minos[j][l - i] = k;
      }
    }

    for (i = 0; i < minos.length; i++) {
      for (j = 0; j < minos[1].length; j++) {
        if (surroundingArea[i][j] && this.minos[i][j] != null) {
          throw new IllegalArgumentException("Turn cannot be preformed");
        }
      }
    }
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public Mino[][] getMinos() {
    return this.minos;
  }

  @Override
  public Mino getType() {
    return this.type;
  }

  @Override
  public void move(Direction direction) {
    this.x += direction.getX();
    this.y += direction.getY();
  }


  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (var line : this.minos) {
      for (var mino : line) {
        if (mino == null) {
          result.append("·");
        } else {
          result.append("x");
        }
      }
      result.append("\n");
    }

    return result.toString();
  }
}