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

package esi.acgt.atlj.client.view.controllers;

import esi.acgt.atlj.model.tetrimino.Mino;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BoardAsideController implements Initializable {

  public final static int H = BoardMainController.H;
  public final static int L = ScoreController.W;

  @FXML
  public VBox container;
  public StackPane next;

  public TetriminoHolderController nextController;

  public StackPane hold;

  public TetriminoHolderController holdController;

  public StackPane score;
  public ScoreController scoreController;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefWidthProperty().bind(container.heightProperty().divide(H).multiply(L));
    container.prefHeightProperty().bind(container.widthProperty().divide(L).multiply(H));

    score.maxWidthProperty().bind(container.widthProperty());
    score.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(ScoreController.H));

    next.maxWidthProperty().bind(container.widthProperty().divide(L).multiply(45));
    next.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(TetriminoHolderController.H));

    hold.maxWidthProperty().bind(container.widthProperty().divide(L).multiply(45));
    hold.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(TetriminoHolderController.H));

    nextController.setType("Next");
    holdController.setType("Hold");
  }

  public void setScore(int score) {
    scoreController.setScore(score);
  }

  public void setHighScore(int highScore) {
    scoreController.setHighScore(highScore);
  }

  public void setNextTetrimino(Mino mino) {
    nextController.setTetrimino(mino);
  }

  public void setHoldTetrimino(Mino mino) {
    holdController.setTetrimino(mino);
  }
}
