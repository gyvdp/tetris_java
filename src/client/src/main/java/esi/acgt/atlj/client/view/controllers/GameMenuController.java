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

import esi.acgt.atlj.client.controller.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.MapProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameMenuController implements Initializable {

  private String username;
  private Controller controller;

  @FXML
  public Label usernameValue;
  @FXML
  public Label singleValue;
  @FXML
  public Label doubleValue;
  @FXML
  public Label tripleValue;
  @FXML
  public Label tetrisValue;
  @FXML
  public Label burnsValue;
  @FXML
  public Label levelValue;
  @FXML
  public Label wonValue;
  @FXML
  public Label lostValue;
  @FXML
  public Label highScoreValue;
  @FXML
  public Label winningPercentValue;

  public void setRootController(Controller controller) {
    this.controller = controller;
  }

  public void setUsername(String username) {
    this.username = username;
    this.usernameValue.setText(username);
  }

  public void bindStats(MapProperty<String, Integer> stats) {
    highScoreValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("SCORE", 0)), stats));

    wonValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("WON", 0)), stats));

    lostValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("LOST", 0)), stats));

    winningPercentValue.textProperty().bind(Bindings.createStringBinding(() -> {
      var won = stats.getOrDefault("WON", 0);
      var lost = stats.getOrDefault("LOST", 0);
      if (won + lost == 0) {
        return "N/A";
      }
      return String.format("%.2f%%", (double) won / (won + lost) * 100);
    }, stats));

    singleValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("SINGLE", 0)), stats));

    doubleValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("DOUBLE", 0)), stats));

    tripleValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("TRIPLE", 0)), stats));

    tetrisValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("TETRIS", 0)), stats));

    burnsValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("BURN", 0)), stats));

    levelValue.textProperty().bind(
        Bindings.createStringBinding(() -> String.valueOf(stats.getOrDefault("LEVEL", 0)), stats));
  }

  /**
   * Action when you click on the disconnect button
   */
  public void disconnect() {
    this.controller.stop();
  }

  /**
   * Action when you click on the Play button
   */
  public void play() {
    this.controller.startMultiplayerGame(this.username);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.usernameValue.setText(this.usernameValue.getText() + this.username);
  }
}
