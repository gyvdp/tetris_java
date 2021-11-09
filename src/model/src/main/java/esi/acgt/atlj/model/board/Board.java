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

public abstract class Board implements BoardInterface {

  protected Mino[][] minos;
  protected int score;
  protected String username;
  protected TetriminoInterface hold;
  protected TetriminoInterface actualTetrimino;
  protected TetriminoInterface nextTetrimino;

  @Override
  public void initTetrisBoard() {
    minos=new Mino[HEIGHT][WIDTH];
  }

  @Override
  public boolean[][] getSurroundingArea(int x, int y) {
    return new boolean[0][]; //todo
  }

  @Override
  public int getScore() {
    return score;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public TetriminoInterface getHold() {
    return hold;
  }

  @Override
  public TetriminoInterface getActualTetrimino() {
    return actualTetrimino;
  }

  @Override
  public TetriminoInterface getNextTetrimino() {
    return nextTetrimino;
  }

  @Override
  public Mino[][] getBoard() {
    return minos;
  }
}
