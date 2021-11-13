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
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * View Item that contains both player's boards
 */
public class BothPlayerView {

  private final HBox scene;
  private final PlayerTetrisFXML player1;
  private final PlayerTetrisFXML player2;

  public PropertyChangeListener getPlayer1() {
    return player1;
  }

  public PropertyChangeListener getPlayer2() {
    return player2;
  }

  /**
   * Constructor of BothPlayerView
   *
   * @param controller Controller to interact with
   * @param stage      stage that this scene will use
   */
  public BothPlayerView(Controller controller, Stage stage) {
    this.scene = new HBox();
    this.setSizeStage(stage);
    this.doBindings(stage);
    stage.setScene(new Scene(this.scene));

    this.player1 = new PlayerTetrisFXML(scene);
    this.player2 = new PlayerTetrisFXML(scene);

    this.scene.getChildren().add(player1.scene);
    this.scene.getChildren().add(player2.scene);

    this.scene.setSpacing(15);
    this.scene.setPadding(new Insets(10));
    this.scene.setStyle("-fx-background-color: gray");
    this.scene.setAlignment(Pos.CENTER);

    stage.getScene()
        .addEventFilter(KeyEvent.KEY_PRESSED, (key) -> controller.keyBoardInput(key.getCode()));
  }

  /**
   * Set the new size of the Stage with Screen informations
   *
   * @param stage stage that we resize
   */
  private void setSizeStage(Stage stage) {
    double width = Screen.getPrimary().getBounds().getWidth();
    double height = Screen.getPrimary().getBounds().getHeight();
    stage.setMinWidth(width * 0.5);
    stage.setMinHeight(height * 0.5);
    stage.setMaxWidth(width);
    stage.setMaxHeight(height);
    stage.setWidth(width * 0.75);
    stage.setHeight(height * 0.75);
  }

  /**
   * Bind every element with stage property
   *
   * @param stage stage that we bind with
   */
  private void doBindings(Stage stage) {
    this.scene.prefHeightProperty().bind(stage.heightProperty());
    this.scene.prefWidthProperty().bind(stage.widthProperty());
  }
}
