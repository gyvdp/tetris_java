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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import esi.acgt.atlj.model.tetrimino.Mino;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
    this.scene.setSpacing(15);
    this.scene.setStyle("-fx-background-color: gray");
    this.scene.setAlignment(Pos.CENTER);

    this.scene.getChildren().add(player1.scene);
    this.scene.getChildren().add(player2.scene);

    stage.setScene(new Scene(this.scene));
    this.doBindings(stage);
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int displayWidth = gd.getDisplayMode().getWidth();
    int displayHeight = gd.getDisplayMode().getHeight();
    stage.setMinHeight(displayHeight/gd.getDefaultConfiguration().getDefaultTransform().getScaleY()/2);
    stage.setMinWidth(displayWidth/gd.getDefaultConfiguration().getDefaultTransform().getScaleX()/2);
    stage.setMaxHeight(displayHeight/gd.getDefaultConfiguration().getDefaultTransform().getScaleY());
      stage.setMaxWidth(displayWidth/gd.getDefaultConfiguration().getDefaultTransform().getScaleX());
    stage.setHeight(displayHeight/gd.getDefaultConfiguration().getDefaultTransform().getScaleY()/1.5);
    stage.setWidth(displayWidth/gd.getDefaultConfiguration().getDefaultTransform().getScaleX()/1.5);


    stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (key) -> {
      controller.keyBoardInput(key.getCode());
    });
  }

  private void doBindings(Stage stage) {
    this.scene.prefHeightProperty().bind(stage.heightProperty());
    this.scene.prefWidthProperty().bind(stage.widthProperty());
  }

  public void updateBoard(Mino[][] board, int playerID) {

    if(playerID ==0){
      this.player1.updateBoard(board);
    }else{
      this.player2.updateBoard(board);
    }
  }

  public void updateScore(int newScore, int playerID) {

    if(playerID ==0){
      this.player1.updateScore(newScore);
    }else{
      this.player2.updateScore(newScore);
    }
  }

  public void updateUsername(String newUsername, int playerID) {

    if(playerID ==0){
      this.player1.updateUsername(newUsername);
    }else{
      this.player2.updateUsername(newUsername);
    }
  }

  public void updateTimer(int timer, int playerID) {

    if(playerID ==0){
      this.player1.updateTimer(timer);
    }else{
      this.player2.updateTimer(timer);
    }
  }

  public void updateLine(int line, int playerID) {
    if(playerID ==0){
      this.player1.updateLine(line);
    }else{
      this.player2.updateLine(line);
    }
  }

}
