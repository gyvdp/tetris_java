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
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;

public class UnmanagedBoard extends Board {

  public UnmanagedBoard(String username) {
    super(username);
    // TODO Constructor
  }

  public void placeTetrimino(TetriminoInterface tetrimino) {
    // TODO
  }

  public void setActualTetrimino(TetriminoInterface tetrimino) {
    this.actualTetrimino = tetrimino;
    this.changeSupport.firePropertyChange("board", this.getBoard(), this.getBoard());
  }

  public void removeLine(int line) {
    // TODO
  }

  public void setNextTetrimino(TetriminoInterface tetrimino) {
    this.nextTetrimino = tetrimino;
    this.changeSupport.firePropertyChange("next", this.nextTetrimino, this.nextTetrimino);
  }

  public void setScore(int score) {
    this.score = score;
    this.changeSupport.firePropertyChange("score", this.score, this.score);
  }

  public void setUsername(String username) {
    this.username = username;
    this.changeSupport.firePropertyChange("username", this.username, this.username);
  }

  public void setNbLine(int nbLine) {
    this.nbLine = nbLine;
    this.changeSupport.firePropertyChange("line", this.nbLine, this.nbLine);
  }

  @Override
  public void setHold(Mino hold) {
    this.hold = hold;
    this.changeSupport.firePropertyChange("hold", this.hold, this.hold);
  }


}