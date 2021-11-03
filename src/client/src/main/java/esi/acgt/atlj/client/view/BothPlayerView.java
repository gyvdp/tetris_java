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

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BothPlayerView {

  private HBox scene;
  private PlayerTetrisFXML player1;
  private PlayerTetrisFXML player2;

  public BothPlayerView(Stage stage) {
    this.player1 = new PlayerTetrisFXML(null, stage);
    this.player2 = new PlayerTetrisFXML(null, stage);
    this.scene = new HBox();
    this.scene.setSpacing(3);
    this.scene.setStyle("-fx-background-color: black");
    this.scene.getChildren().add(player1.anchorPanePlayer);
    this.scene.getChildren().add(player2.anchorPanePlayer);
    stage.setScene(new Scene(this.scene));
  }

  public void update(Object o) {
    this.player1.update(o);
    this.player2.update(o);
  }
}
