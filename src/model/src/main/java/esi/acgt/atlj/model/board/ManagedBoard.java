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

import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.security.InvalidParameterException;
import java.util.Timer;

public class ManagedBoard extends Board {

  private Timer timer;

  public ManagedBoard() {
    //TODO
  }

  public void moveLeft() {
    rotate(false);
  }

  public void moveRight() {
    rotate(true);
    // TODO
  }

  public void softDrop() {
    // TODO
  }

  public void hardDrop() {
    // TODO
  }

  private void rotate(boolean clockwise) {
    try {
      actualTetrimino.rotate(clockwise, this.getSurroundingArea(actualTetrimino.getX(),
          actualTetrimino.getY()));
      this.changeSupport.firePropertyChange("player1Board", null, this.getBoard());
    } catch(InvalidParameterException e){
      //Check if there is another block or if its wall
      //If it is another block tetrimino must be placed If wall do nothing
    }
  }

  public void setScore(int score) {
    int oldScore = this.score;
    this.score = score;
    this.changeSupport.firePropertyChange("player1Score", oldScore, this.score);
  }

  public void setUsername(String username) {
    String oldValue = this.username;
    this.username = username;
    this.changeSupport.firePropertyChange("player1Name", oldValue, this.username);
  }

  public void setNbLine(int nbLine) {
    int oldNbLine = this.nbLine;
    this.nbLine = nbLine;
    this.changeSupport.firePropertyChange("player1NbLine", oldNbLine, this.nbLine);
  }

}
