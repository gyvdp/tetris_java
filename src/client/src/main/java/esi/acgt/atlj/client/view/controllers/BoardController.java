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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardController implements Initializable, PropertyChangeListener {

  public final static int HEIGHT = BoardMainController.HEIGHT;

  public final static int WIDTH = BoardMainController.WIDTH + BoardAsideController.WIDTH;

  @FXML
  public HBox container;

  @FXML
  public VBox boardMain;

  @FXML
  public BoardMainController boardMainController;

  public VBox aside;

  @FXML
  public BoardAsideController asideController;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefWidthProperty().bind(container.heightProperty().divide(HEIGHT).multiply(WIDTH));
    container.prefHeightProperty().bind(container.widthProperty().divide(WIDTH).multiply(HEIGHT));

    boardMain.maxWidthProperty()
        .bind(container.widthProperty().divide(WIDTH).multiply(BoardMainController.WIDTH));

    aside.maxWidthProperty()
        .bind(container.widthProperty().divide(WIDTH).multiply(BoardAsideController.WIDTH));
  }

  public void setMatrix(Mino[][] minos) {
    boardMainController.setMatrix(minos);
  }

  public void setUsername(String username) {
    boardMainController.setUsername(username);
  }

  public void setLines(int i) {
    boardMainController.setLines(i);
  }

  public void setScore(int score) {
    asideController.setScore(score);
  }

  public void setHighScore(int highScore) {
    asideController.setHighScore(highScore);
  }

  public void setNextTetrimino(Mino mino) {
    asideController.setNextTetrimino(mino);
  }

  public void setHoldTetrimino(Mino mino) {
    asideController.setHoldTetrimino(mino);
  }

  /**
   * PropertyChange that will change the view on base of what he received
   *
   * @param evt evt that has been fired
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> {
      switch (evt.getPropertyName()) {
        case "board" -> setMatrix((Mino[][]) evt.getNewValue());
        case "BURNS" -> setLines((int) evt.getNewValue());
        case "SCORE" -> setScore((int) evt.getNewValue());
        case "HIGH_SCORE" -> setHighScore((int) evt.getNewValue());
        case "username" -> setUsername(evt.getNewValue().toString());
        case "hold" -> setHoldTetrimino(((Mino) evt.getNewValue()));
        case "next" -> setNextTetrimino((Mino) evt.getNewValue());
        //case "fire" -> //function in view(with value)
      }
    });
  }

}
