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

package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BothPlayerView {

  private HBox scene;
  private PlayerTetrisFXML player1;
  private PlayerTetrisFXML player2;

  public BothPlayerView(Controller controller, Stage stage) {
    this.player1 = new PlayerTetrisFXML(stage);
    this.player2 = new PlayerTetrisFXML(stage);
    this.scene = new HBox();

    this.scene.setSpacing(5);
    this.scene.setStyle("-fx-background-color: black");

    this.scene.getChildren().add(player1.scene);
    this.scene.getChildren().add(player2.scene);

    stage.setScene(new Scene(this.scene));
    stage.setMinHeight(700);
    stage.setMinWidth(1150);
    stage.setHeight(700);
    stage.setWidth(1150);

    this.doBindings(stage);

    stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (key) -> {
      controller.keyBoardInput(key.getCode());
    });
  }

  private void doBindings(Stage stage) {
    this.scene.prefHeightProperty().bind(stage.heightProperty());
    this.scene.prefWidthProperty().bind(stage.widthProperty());
  }
}
